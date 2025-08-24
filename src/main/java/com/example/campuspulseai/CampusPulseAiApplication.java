package com.example.campuspulseai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CampusPulseAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusPulseAiApplication.class, args);
    }

}
