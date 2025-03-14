package com.asier.arguments.argumentsbackend.entities.dtos;

import com.asier.arguments.argumentsbackend.entities.User;
import com.asier.arguments.argumentsbackend.entities.UserCredentials;
import com.asier.arguments.argumentsbackend.utils.validation.Mandatory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreatorDto {
    @Mandatory
    private User user;
    @Mandatory
    private UserCredentials credentials;
}
