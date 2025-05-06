package com.asier.arguments.arguments_backend.endpoints;

import com.asier.arguments.argumentsbackend.entities.user.User;
import com.asier.arguments.argumentsbackend.entities.user.UserCredentials;
import com.asier.arguments.argumentsbackend.entities.commons.ServiceResponse;
import com.asier.arguments.argumentsbackend.entities.user.UserCreatorDto;
import com.asier.arguments.argumentsbackend.services.auth.components.AuthComponent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.util.List;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserCreations {
    private final int port = 8088;
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void testCreateUser() throws JsonProcessingException {
        String uri = "http://localhost:" + port + "/api/v1/users?clientToken=" + new AuthComponent().getClientKey();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UserCreatorDto user = UserCreatorDto.builder()
                .user(User.builder().username("userprueba").firstname("Prueba").lastname("PruebaApellido").build())
                .credentials(UserCredentials.builder().username("userprueba").password("1234").build()).build();

        ObjectMapper map = new ObjectMapper();

        HttpEntity<String> entity = new HttpEntity<>(map.writeValueAsString(user), headers);


        ResponseEntity<ServiceResponse> response = restTemplate.exchange(uri, HttpMethod.POST, entity, ServiceResponse.class);

        Assert.assertEquals(200,response.getStatusCode().value());
    }

    @Test
    public void testSelectUserByName() throws JsonProcessingException {
        String uri = "http://localhost:" + port + "/api/v1/users/username/userprueba?clientToken=" + new AuthComponent().getClientKey();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper map = new ObjectMapper();

        HttpEntity<String> entity = new HttpEntity<>(headers);


        ResponseEntity<ServiceResponse> response = restTemplate.exchange(uri, HttpMethod.GET, entity, ServiceResponse.class);

        Assert.assertEquals(200,response.getStatusCode().value());
        log.info("testGetAllUsers: {}", (response.getBody().getResult()).toString());
    }

    @Test
    public void testSelectUserById() throws JsonProcessingException {
        String uri = "http://localhost:" + port + "/api/v1/users/id/67db6959ab53504efcc64bbd?clientToken=" + new AuthComponent().getClientKey();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper map = new ObjectMapper();

        HttpEntity<String> entity = new HttpEntity<>(headers);


        ResponseEntity<ServiceResponse> response = restTemplate.exchange(uri, HttpMethod.GET, entity, ServiceResponse.class);

        Assert.assertEquals(200,response.getStatusCode().value());
        log.info("testGetAllUsers: {}", (response.getBody().getResult()).toString());
    }

    @Test
    public void testCreateUserUnauthorized() throws JsonProcessingException {
        String uri = "http://localhost:" + port + "/api/v1/users";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UserCreatorDto user = UserCreatorDto.builder()
                .user(User.builder().username("userprueba").firstname("Prueba").lastname("PruebaApellido").build())
                .credentials(UserCredentials.builder().username("userprueba").password("1234").build()).build();

        ObjectMapper map = new ObjectMapper();

        HttpEntity<String> entity = new HttpEntity<>(map.writeValueAsString(user), headers);


        ResponseEntity<ServiceResponse> response = restTemplate.exchange(uri, HttpMethod.POST, entity, ServiceResponse.class);

        Assert.assertEquals(401,response.getStatusCode().value());
    }

    @Test
    public void testGetAllUsers(){
        String uri = "http://localhost:" + port + "/api/v1/users/all?clientToken=" + new AuthComponent().getClientKey();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ServiceResponse> response = restTemplate.exchange(uri, HttpMethod.GET, entity, ServiceResponse.class);

        Assert.assertEquals(200,response.getStatusCode().value());
        log.info("testGetAllUsers: {}", ((List<User>) response.getBody().getResult()).toString());
    }

}
