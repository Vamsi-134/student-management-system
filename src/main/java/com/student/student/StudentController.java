package com.student.student;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

@Controller
public class StudentController {

	@PostMapping("/addStudent")
	public String addStudent(
	        @RequestParam int id,
	        @RequestParam String name,
	        @RequestParam int age,
	        @RequestParam String course,
	        @RequestParam double marks,
	        org.springframework.ui.Model model) {

	    try {

	        Connection con = DBConnection.getConnection();

	        String sql = "INSERT INTO student VALUES(?,?,?,?,?)";

	        PreparedStatement ps = con.prepareStatement(sql);

	        ps.setInt(1, id);
	        ps.setString(2, name);
	        ps.setInt(3, age);
	        ps.setString(4, course);
	        ps.setDouble(5, marks);

	        int result = ps.executeUpdate();

	        if(result > 0){
	            model.addAttribute("success", "Student added successfully!");
	        } else {
	            model.addAttribute("error", "Failed to add student");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("error", "Something went wrong");
	    }

	    return "addStudent"; // 🔥 stay on same page
	}
	@GetMapping("/viewStudents")
	public String viewStudents(org.springframework.ui.Model model) {

	    java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();

	    try {
	        Connection con = DBConnection.getConnection();

	        String sql = "SELECT * FROM student";
	        java.sql.PreparedStatement ps = con.prepareStatement(sql);

	        java.sql.ResultSet rs = ps.executeQuery();

	        while(rs.next()){
	            java.util.Map<String, Object> row = new java.util.HashMap<>();

	            row.put("id", rs.getInt("id"));
	            row.put("name", rs.getString("name"));
	            row.put("age", rs.getInt("age"));
	            row.put("course", rs.getString("course"));
	            row.put("marks", rs.getDouble("marks"));

	            list.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    model.addAttribute("students", list);

	    return "viewStudents";
	}
	@GetMapping("/ranking")
	public String ranking(Model model){

	    List<Map<String,Object>> list = new ArrayList<>();

	    try{
	        Connection con = DBConnection.getConnection();
	        Statement st = con.createStatement();

	        ResultSet rs = st.executeQuery("SELECT * FROM student ORDER BY marks DESC");

	        while(rs.next()){
	            Map<String,Object> row = new HashMap<>();

	            row.put("name", rs.getString("name"));
	            row.put("marks", rs.getDouble("marks"));

	            list.add(row);
	        }

	    }catch(Exception e){
	        e.printStackTrace();
	    }

	    model.addAttribute("students", list);

	    return "ranking"; // ⚠️ THIS MUST MATCH FILE NAME
	}
	@GetMapping("/manageStudents")
	public String manageStudents(org.springframework.ui.Model model){

	    java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();

	    try{
	        java.sql.Connection con = DBConnection.getConnection();
	        java.sql.Statement st = con.createStatement();

	        java.sql.ResultSet rs = st.executeQuery("SELECT * FROM student");

	        while(rs.next()){
	            java.util.Map<String, Object> row = new java.util.HashMap<>();

	            row.put("id", rs.getInt("id"));
	            row.put("name", rs.getString("name"));
	            row.put("marks", rs.getDouble("marks"));

	            list.add(row);
	        }

	    }catch(Exception e){
	        e.printStackTrace();
	    }

	    model.addAttribute("students", list);

	    return "manageStudents";
	}
	@GetMapping("/deleteStudent")
	public String deleteStudent(@RequestParam int id){

	    try{
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement("DELETE FROM student WHERE id=?");

	        ps.setInt(1, id);
	        ps.executeUpdate();

	    }catch(Exception e){
	        e.printStackTrace();
	    }

	    return "redirect:/manageStudents";
	}
	@GetMapping("/editStudent")
	public String editStudent(@RequestParam int id, Model model){

	    try{
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement("SELECT * FROM student WHERE id=?");

	        ps.setInt(1, id);
	        ResultSet rs = ps.executeQuery();

	        if(rs.next()){
	            model.addAttribute("id", rs.getInt("id"));
	            model.addAttribute("name", rs.getString("name"));
	            model.addAttribute("marks", rs.getDouble("marks"));
	        }

	    }catch(Exception e){
	        e.printStackTrace();
	    }

	    return "updateStudent";
	}
	@PostMapping("/updateStudent")
	public String updateStudent(@RequestParam int id,
	                           @RequestParam double marks){

	    try{
	        Connection con = DBConnection.getConnection();
	        PreparedStatement ps = con.prepareStatement("UPDATE student SET marks=? WHERE id=?");

	        ps.setDouble(1, marks);
	        ps.setInt(2, id);

	        ps.executeUpdate();

	    }catch(Exception e){
	        e.printStackTrace();
	    }

	    return "redirect:/manageStudents";
	}
	@GetMapping("/search")
	public String search(@RequestParam("keyword") String keyword, Model model){

	    List<Map<String, Object>> list = new ArrayList<>();

	    try{
	        Connection con = DBConnection.getConnection();

	        PreparedStatement ps = con.prepareStatement(
	            "SELECT * FROM student WHERE name LIKE ?"
	        );

	        ps.setString(1, "%" + keyword + "%");

	        ResultSet rs = ps.executeQuery();

	        while(rs.next()){
	            Map<String, Object> row = new HashMap<>();

	            row.put("id", rs.getInt("id"));
	            row.put("name", rs.getString("name"));
	            row.put("age", rs.getInt("age"));
	            row.put("course", rs.getString("course"));
	            row.put("marks", rs.getDouble("marks"));

	            list.add(row);
	        }

	    }catch(Exception e){
	        e.printStackTrace();
	    }

	    model.addAttribute("students", list);

	    return "viewStudents"; // reuse same page
	}
	
}