package com.asier.arguments.argumentsbackend.filters;

import com.asier.arguments.argumentsbackend.services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@WebFilter("/api/v1/auth/*")
public class AuthTokenFilter implements Filter {
    @Autowired
    private AuthService authService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendError(servletResponse);
            return;
        }

        String token = authHeader.substring(7);
        String username = authService.getAuthSubject(token);

        if (username != null) {
            request.setAttribute("username", username);
            filterChain.doFilter(request, servletResponse);
        } else {
            sendError(servletResponse);
        }
    }

    private void sendError(ServletResponse response){
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(401);
        httpResponse.setContentType("application/json");
    }
}
