package com.student.student;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();
        HttpSession session = request.getSession(false);

        System.out.println("FILTER HIT: " + path); // 🔥 DEBUG

        // 🔓 allow login + static files
        if (path.equals("/") || path.equals("/login") ||
            path.contains("css") || path.contains("js") || path.contains("images")) {

            chain.doFilter(req, res);
            return;
        }

        // 🔒 block if not logged in
        if (session == null || session.getAttribute("user") == null) {
            System.out.println("BLOCKED 🔒");
            response.sendRedirect("/login");
            return;
        }

        chain.doFilter(req, res);
    }
}