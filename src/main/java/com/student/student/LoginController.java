package com.student.student;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model,
                        HttpSession session) {

        System.out.println("LOGIN HIT 🔥");

        if(username.equals("Vamsi") && password.equals("Vamsi@3265")) {

            // 🔥 VERY IMPORTANT (for filter)
            session.setAttribute("user", username);

            return "redirect:/dashboard"; // always redirect
        } else {
            model.addAttribute("errorMessage", "Invalid Username or Password");
            return "login";
        }
    }

    // 🔥 LOGOUT METHOD (ADD THIS ALSO)
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate(); // clear session
        return "redirect:/login";
    }
}