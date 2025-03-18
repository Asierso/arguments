package com.asier.arguments.argumentsbackend.services;

import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class AuthService {
    private final Key authKey = Keys.hmacShaKeyFor(PropertiesUtils.getProperties(ResourceLocator.ARGUMENTS).getProperty("arguments.api.authSecret").getBytes());
    private final Key clientKey = Keys.hmacShaKeyFor(PropertiesUtils.getProperties(ResourceLocator.ARGUMENTS).getProperty("arguments.api.clientSecret").getBytes());
    public String generateAuthToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .signWith(authKey)
                .compact();
    }
    public boolean validateClientToken(String token){
        return token.equals(Base64.getEncoder().encodeToString(clientKey.getEncoded()));
    }
    public boolean validateAuthToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(authKey).build().parseClaimsJws(token);
            return true;
        }catch (Exception ignore){
            return false;
        }
    }
    public String getAuthSubject(String token){
        return Jwts.parserBuilder().setSigningKey(authKey).build().parseClaimsJws(token).getBody().getSubject();
    }
    public String getClientKey(){
        return Base64.getEncoder().encodeToString(clientKey.getEncoded());
    }
}
