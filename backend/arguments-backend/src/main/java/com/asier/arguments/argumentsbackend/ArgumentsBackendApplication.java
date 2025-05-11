package com.asier.arguments.argumentsbackend;

import com.asier.arguments.argumentsbackend.services.auth.components.AuthComponent;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ArgumentsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArgumentsBackendApplication.class, args);

		//Get client token
		String token = new AuthComponent().getClientKey();
        log.info("Client token: {}", token);

		//Write client token into file "token txt"
		try(BufferedWriter bw = new BufferedWriter(new FileWriter("token.txt"))){
			bw.write(token);
		}catch(IOException e){
			log.error("Error writing token to file", e);
		}
	}

}
