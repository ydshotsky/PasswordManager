package com.passwordManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PasswordManagerApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(PasswordManagerApplication.class);
        springApplication.addListeners(event ->
                System.out.println("SPRING_DATASOURCE_URL=" + System.getenv("SPRING_DATABASE_URL")));
        springApplication.addListeners(event ->
                System.out.println("SPRING_DATASOURCE_USERNAME=" + System.getenv("SPRING_DATASOURCE_USERNAME")));

        springApplication.run(args);
    }
}
