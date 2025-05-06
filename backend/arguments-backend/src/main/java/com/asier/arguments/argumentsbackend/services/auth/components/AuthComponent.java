package com.asier.arguments.argumentsbackend.services.auth.components;

import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class AuthComponent {
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
    public String getAuthSubject(String token){
        try {
            return Jwts.parserBuilder().setSigningKey(authKey).build().parseClaimsJws(token).getBody().getSubject();
        } catch(MalformedJwtException | SignatureException e){
            return null;
        }
    }
    public String getClientKey(){
        return Base64.getEncoder().encodeToString(clientKey.getEncoded());
    }
}
