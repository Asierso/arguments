package com.asier.arguments.argumentsbackend.services.auth;

import com.asier.arguments.argumentsbackend.entities.user.UserSync;

public interface UserSyncService {
    void update(String username);
    UserSync select(String username);
}
