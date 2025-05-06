package com.asier.arguments.argumentsbackend;

import com.asier.arguments.argumentsbackend.services.auth.components.AuthComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ArgumentsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArgumentsBackendApplication.class, args);
        log.info("Client token: {}", new AuthComponent().getClientKey());
	}

}
