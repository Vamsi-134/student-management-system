package com.student.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private DataSource dataSource;

    // =========================
    // 🔥 GRADE METHOD (ADD THIS)
    // =========================
    private String getGrade(double p){
        if(p >= 9) return "A+";
        else if(p >= 8) return "A";
        else if(p >= 7) return "B+";
        else if(p >= 6) return "B";
        else return "C";
    }

    // =========================
    // 🔥 OPEN ADD RESULT PAGE
    // =========================
    @GetMapping("/addStudentResult")
    public String openAddResultPage() {
        return "addStudentResult";
    }

    // =========================
    // 🔥 VIEW STUDENTS
    // =========================
    @GetMapping("/viewStudents")
    public String viewStudents(Model model) {

        List<Student> list = new ArrayList<>();

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM student");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Student s = new Student();

                s.setId(rs.getString("id"));
                s.setName(rs.getString("name"));

                if (rs.getDate("dob") != null)
                    s.setDob(rs.getDate("dob").toLocalDate());

                s.setCourse(rs.getString("course"));

                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("students", list);
        return "viewStudents";
    }

    // =========================
    // 🔥 ADD STUDENT
    // =========================
    @PostMapping("/addStudent")
    public String addStudent(
            @RequestParam String id,
            @RequestParam String name,
            @RequestParam String dob,
            @RequestParam String course,
            Model model) {

        try {
            Connection con = dataSource.getConnection();

            String sql = "INSERT INTO student(id,name,dob,course) VALUES(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, id);
            ps.setString(2, name);
            ps.setDate(3, Date.valueOf(dob));
            ps.setString(4, course);

            ps.executeUpdate();

            model.addAttribute("success", "Student Added Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error adding student!");
        }

        return "addStudent";
    }

    // =========================
    // 🔥 DELETE
    // =========================
    @GetMapping("/deleteStudent")
    public String deleteStudent(@RequestParam String id) {

        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM student WHERE id=?"
            );

            ps.setString(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/viewStudents";
    }

    // =========================
    // 🔥 SEARCH
    // =========================
    @GetMapping("/searchStudents")
    public String searchStudents(@RequestParam String keyword, Model model) {

        List<Student> list = new ArrayList<>();

        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM student WHERE name LIKE ?"
            );

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Student s = new Student();

                s.setId(rs.getString("id"));
                s.setName(rs.getString("name"));

                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("students", list);
        return "viewStudents";
    }

    // =========================
    // 🔥 OPEN SEM PAGE
    // =========================
    @GetMapping("/viewStudentResult")
    public String openSemesterPage() {
        return "selectSemester";
    }

    // =========================
    // 🔥 FILTER BY SEM
    // =========================
    @GetMapping("/resultsBySem")
    public String resultsBySem(@RequestParam String sem, Model model) {

        List<StudentResult> list = new ArrayList<>();

        try {
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM student_result WHERE semester=?"
            );

            ps.setString(1, sem);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                StudentResult s = new StudentResult();

                s.setRegNo(rs.getString("reg_no"));
                s.setName(rs.getString("name"));
                s.setSemester(rs.getString("semester"));

                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("students", list);
        return "resultsList";
    }

    // =========================
    // 🔥 FINAL RESULT PAGE + GRADE
    // =========================
    @GetMapping("/results")
    public String viewResult(@RequestParam("id") String id,
                             @RequestParam("sem") String sem,
                             Model model) {

        try {
            Connection con = dataSource.getConnection();

            String sql = "SELECT * FROM student_result WHERE reg_no=? AND semester=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, id);
            ps.setString(2, sem);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                StudentResult s = new StudentResult();

                s.setRegNo(rs.getString("reg_no"));
                s.setName(rs.getString("name"));
                s.setProgram(rs.getString("program"));
                s.setSemester(rs.getString("semester"));

                s.setSub0(rs.getDouble("sub0_point"));
                s.setSub1(rs.getDouble("sub1_point"));
                s.setSub2(rs.getDouble("sub2_point"));
                s.setSub3(rs.getDouble("sub3_point"));
                s.setSub4(rs.getDouble("sub4_point"));
                s.setSub5(rs.getDouble("sub5_point"));
                s.setSub6(rs.getDouble("sub6_point"));

                s.setCgpa(rs.getDouble("cgpa"));

                model.addAttribute("result", s);

                // 🔥 ADD GRADES
                model.addAttribute("g0", getGrade(s.getSub0()));
                model.addAttribute("g1", getGrade(s.getSub1()));
                model.addAttribute("g2", getGrade(s.getSub2()));
                model.addAttribute("g3", getGrade(s.getSub3()));
                model.addAttribute("g4", getGrade(s.getSub4()));
                model.addAttribute("g5", getGrade(s.getSub5()));
                model.addAttribute("g6", getGrade(s.getSub6()));

            } else {
                model.addAttribute("error", "No result found");
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error loading result");
        }

        return "result";
    }

    // =========================
    // 🔥 SAVE RESULT
    // =========================
    @PostMapping("/saveResult")
    public String saveResult(
            @RequestParam String regNo,
            @RequestParam String name,
            @RequestParam String fatherName,
            @RequestParam String motherName,
            @RequestParam String semester,
            @RequestParam String program,

            @RequestParam String sub0,
            @RequestParam String sub1,
            @RequestParam String sub2,
            @RequestParam String sub3,
            @RequestParam String sub4,
            @RequestParam String sub5,
            @RequestParam String sub6
    ) {

        try {
            Connection con = dataSource.getConnection();

            double p0 = Double.parseDouble(sub0);
            double p1 = Double.parseDouble(sub1);
            double p2 = Double.parseDouble(sub2);
            double p3 = Double.parseDouble(sub3);
            double p4 = Double.parseDouble(sub4);
            double p5 = Double.parseDouble(sub5);
            double p6 = Double.parseDouble(sub6);

            double cgpa =
                    (p0*3 + p1*4 + p2*4 + p3*4 + p4*4 + p5*2 + p6*2) /
                    (3+4+4+4+4+2+2);

            String sql = "INSERT INTO student_result(" +
                    "reg_no,name,father_name,mother_name,semester,program," +
                    "sub0_point,sub1_point,sub2_point,sub3_point,sub4_point,sub5_point,sub6_point,cgpa" +
                    ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, regNo);
            ps.setString(2, name);
            ps.setString(3, fatherName);
            ps.setString(4, motherName);
            ps.setString(5, semester);
            ps.setString(6, program);

            ps.setDouble(7, p0);
            ps.setDouble(8, p1);
            ps.setDouble(9, p2);
            ps.setDouble(10, p3);
            ps.setDouble(11, p4);
            ps.setDouble(12, p5);
            ps.setDouble(13, p6);
            ps.setDouble(14, cgpa);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/dashboard";
    }
}