package com.asier.arguments.argumentsbackend.filters;

import com.asier.arguments.argumentsbackend.entities.dtos.ServiceResponse;
import com.asier.arguments.argumentsbackend.services.AuthService;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This web filter applies to all API calls. All calls should be launched on authorized clients
 * The token is calculated when server is started using properties secret
 */
@Slf4j
@WebFilter(urlPatterns = "/api/*")
public class ClientTokenFilter implements Filter {
    @Autowired
    private AuthService authService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //Check if the client is genuine
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        if(authService.validateClientToken(req.getParameter("clientToken"))){
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            //Define headers
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setStatus(401);
            httpResponse.setContentType("application/json");

            //Define body
            ObjectMapper map = new ObjectMapper();
            try {
                String msg = map.writeValueAsString(ServiceResponse.builder().status("401").result(PropertiesUtils.getProperties(ResourceLocator.STATUS).getProperty("status.unauthorizedClient")).build());
                httpResponse.getWriter().write(msg);
            } catch (IOException e) {
                log.error("Error processing errorSender: {}",e.getMessage());
            }
        }
    }
}
