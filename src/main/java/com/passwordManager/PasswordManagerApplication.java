package com.passwordManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PasswordManagerApplication{
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PasswordManagerApplication.class);

        app.addListeners(event ->
                System.out.println("SPRING_DATASOURCE_URL=" + System.getenv("SPRING_DATASOURCE_URL"))
        );
        app.run(args);
    }
}
