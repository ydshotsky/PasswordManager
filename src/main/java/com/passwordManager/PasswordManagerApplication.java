package com.passwordManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PasswordManagerApplication{
    public static void main(String[] args) {
        SpringApplication.run(PasswordManagerApplication.class, args);
    }
    @PostConstruct
public void logEnv() {
    System.out.println("SPRING_DATASOURCE_URL=" + System.getenv("SPRING_DATASOURCE_URL"));
    System.out.println("SPRING_DATASOURCE_USERNAME=" + System.getenv("SPRING_DATASOURCE_USERNAME"));
    System.out.println("SPRING_DATASOURCE_PASSWORD=" + System.getenv("SPRING_DATASOURCE_PASSWORD"));
}

}
