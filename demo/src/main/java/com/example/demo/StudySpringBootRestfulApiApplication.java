package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
public class StudySpringBootRestfulApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudySpringBootRestfulApiApplication.class, args);
    }

}
