package com.student.student;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model) {

        System.out.println("LOGIN HIT 🔥");

        if(username.equals("Vamsi") && password.equals("Vamsi@3265")) {
            return "dashboard";
        } else {
            model.addAttribute("errorMessage", "Invalid Username or Password");
            return "login";
        }
    }
}