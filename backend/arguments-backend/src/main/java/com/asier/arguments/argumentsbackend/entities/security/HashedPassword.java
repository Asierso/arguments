package com.asier.arguments.argumentsbackend.entities.security;

import org.mindrot.jbcrypt.BCrypt;

import lombok.Data;

@Data
public class HashedPassword {
    private String hashedPassword;
    public HashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, hashedPassword);
    }
    public static HashedPassword hashPassword(String password) {
        return new HashedPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
    }
}
