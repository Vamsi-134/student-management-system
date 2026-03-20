package com.student.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

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
}