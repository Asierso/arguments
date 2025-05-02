package com.asier.arguments.argumentsbackend.filters;

import com.asier.arguments.argumentsbackend.entities.commons.ValidAuthTokens;
import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import com.asier.arguments.argumentsbackend.services.auth.AuthService;
import com.asier.arguments.argumentsbackend.services.auth.ValidAuthsTokenService;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * This web filter applies to all API calls that require user authentication.
 * The token is calculated when server is started using properties secret
 */
@Slf4j
@WebFilter("/api/v1/auth/*")
public class AuthTokenFilter implements Filter {
    @Autowired
    private AuthService authService;
    @Autowired
    private ValidAuthsTokenService authTokenService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        //Get auth HTTP header
        String authHeader = request.getHeader("Authorization");

        //Check if authorization mode is with Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendError(servletResponse);
            return;
        }

        //Get user auth and username. Username is contained in auth token
        String token = authHeader.substring(7);
        String username = authService.getAuthSubject(token);

        if (username != null) {
            //Save username in request attributes
            request.setAttribute("username", username.toLowerCase());
            request.setAttribute("authToken",token);

            //Check if the auth token is valid. Valid auth tokens are registered in auths collection
            if(!authTokenService.exists(ValidAuthTokens.builder().token(token).build())){
                sendError(servletResponse);
                return;
            }

            //Continue with the endpoint call
            filterChain.doFilter(request, servletResponse);
        } else {
            sendError(servletResponse);
        }
    }

    private void sendError(ServletResponse response) {
        //Define headers
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(401);
        httpResponse.setContentType("application/json");

        //Builds body error
        ObjectMapper map = new ObjectMapper();
        try {
            String msg = map.writeValueAsString(ServiceResponse.builder().status("401").result(PropertiesUtils.getProperties(ResourceLocator.STATUS).getProperty("status.unauthorizedAuth")).build());
            httpResponse.getWriter().write(msg);
        } catch (IOException e) {
            log.error("Error processing errorSender: {}",e.getMessage());
        }

    }
}
