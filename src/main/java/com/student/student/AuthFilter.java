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

        HttpSession session = request.getSession(false);

        String path = request.getRequestURI();

        // 🔥 Allow these without login
        if (path.equals("/login") || path.equals("/") || path.contains("css") || path.contains("js")) {
            chain.doFilter(req, res);
            return;
        }

        // 🔥 Check login
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/login");
            return;
        }

        chain.doFilter(req, res);
    }
}