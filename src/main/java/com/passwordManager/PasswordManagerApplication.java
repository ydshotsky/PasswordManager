package com.passwordManager;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PasswordManagerApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(PasswordManagerApplication.class);
        springApplication.addListeners(event ->
                System.out.println("SPRING_DATASOURCE_URL=" + System.getenv("SPRING_DATABASE_URL")));
        springApplication.addListeners(event ->
                System.out.println("SPRING_DATASOURCE_USERNAME=" + System.getenv("SPRING_DATABASE_USERNAME")));

        springApplication.run(args);
    }
    @PostConstruct
    public void logDatasource() {
        System.out.println("ENV URL: " + System.getenv("SPRING_DATASOURCE_URL"));
        System.out.println("ENV USER: " + System.getenv("SPRING_DATASOURCE_USERNAME"));
        System.out.println("ENV PASS: " + System.getenv("SPRING_DATASOURCE_PASSWORD"));
    }

}
