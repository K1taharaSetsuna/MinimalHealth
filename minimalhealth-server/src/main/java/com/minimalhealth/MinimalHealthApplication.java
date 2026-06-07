package com.minimalhealth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MinimalHealthApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinimalHealthApplication.class, args);
    }
}
