package com.asier.arguments.argumentsbackend.filters;

import com.asier.arguments.argumentsbackend.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This web filter applies to all API calls. All calls should be launched on authorized clients
 * The token is calculated when server is started using properties secret
 */
@WebFilter(urlPatterns = "/api/*")
public class ClientTokenFilter implements Filter {
    @Autowired
    private AuthService authService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        if(authService.validateClientToken(req.getParameter("clientToken"))){
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setStatus(401);
            httpResponse.setContentType("application/json");
        }
    }
}
