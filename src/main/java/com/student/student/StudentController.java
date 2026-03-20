package com.student.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private DataSource dataSource;

    @PostMapping("/addStudent")
    public String addStudent(
            @RequestParam int id,
            @RequestParam String name,
            @RequestParam int age,
            @RequestParam String course,
            @RequestParam double marks,
            org.springframework.ui.Model model) {

        Connection con = null;
        PreparedStatement ps = null;

        try {
            // 🔥 Step 1: Get connection
            con = dataSource.getConnection();

            // ✅ Safety check
            if (con == null) {
                model.addAttribute("error", "Database connection failed");
                return "addStudent";
            }

            // 🔥 Step 2: SQL
            String sql = "INSERT INTO student(id,name,age,course,marks) VALUES(?,?,?,?,?)";
            ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setInt(3, age);
            ps.setString(4, course);
            ps.setDouble(5, marks);

            int result = ps.executeUpdate();

            // 🔥 Step 3: Result
            if (result > 0) {
                model.addAttribute("success", "Student added successfully!");
            } else {
                model.addAttribute("error", "Insert failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Something went wrong");
        } finally {
            // 🔥 Step 4: Close resources
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "addStudent";
    }
    @GetMapping("/viewStudents")
    public String viewStudents(org.springframework.ui.Model model) {

        List<Student> list = new ArrayList<>();

        try {
            Connection con = dataSource.getConnection();

            String sql = "SELECT * FROM student";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setAge(rs.getInt("age"));
                s.setCourse(rs.getString("course"));
                s.setMarks(rs.getDouble("marks"));

                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("students", list);

        return "viewStudents";
    }
    @GetMapping("/ranking")
    public String ranking(org.springframework.ui.Model model) {

        List<Student> list = new ArrayList<>();

        try {
            Connection con = dataSource.getConnection();

            // 🔥 IMPORTANT (sorting for ranking)
            String sql = "SELECT * FROM student ORDER BY marks DESC";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setAge(rs.getInt("age"));
                s.setCourse(rs.getString("course"));
                s.setMarks(rs.getDouble("marks"));

                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("students", list);

        return "ranking"; // HTML file name
    }
    @GetMapping("/manageStudents")
    public String manageStudents(org.springframework.ui.Model model) {

        List<Student> list = new ArrayList<>();

        try {
            Connection con = dataSource.getConnection();

            String sql = "SELECT * FROM student";
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setAge(rs.getInt("age"));
                s.setCourse(rs.getString("course"));
                s.setMarks(rs.getDouble("marks"));

                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("students", list);

        return "manageStudents"; // HTML file name
    }
    @GetMapping("/editStudent")
    public String editStudent(@RequestParam int id, Model model) {

        Student s = new Student();

        try {
            Connection con = dataSource.getConnection();

            String sql = "SELECT * FROM student WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setAge(rs.getInt("age"));
                s.setCourse(rs.getString("course"));
                s.setMarks(rs.getDouble("marks"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("student", s);

        return "updateStudent";
    }
    @PostMapping("/updateStudent")
    public String updateStudent(
            @RequestParam int id,
            @RequestParam double marks,
            org.springframework.ui.Model model) {

        try {
            Connection con = dataSource.getConnection();

            String sql = "UPDATE student SET marks=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setDouble(1, marks);
            ps.setInt(2, id);

            int result = ps.executeUpdate();

            if (result > 0) {
                model.addAttribute("success", "Marks updated successfully!");
            } else {
                model.addAttribute("error", "Update failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Something went wrong");
        }

        return "updateStudent";
    }
}