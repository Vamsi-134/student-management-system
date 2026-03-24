package com.student.student;

import java.time.LocalDate;

public class Student {

    private String id;   // ✅ changed
    private String name;
    private LocalDate dob;   // ✅ new
    private String course;
    private double marks;
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public double getMarks() { return marks; }
    public void setMarks(double marks) { this.marks = marks; }
}