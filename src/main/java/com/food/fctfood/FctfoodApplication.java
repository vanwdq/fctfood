package com.food.fctfood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

@ComponentScan(basePackages = {"com.food.fctfood"})
@SpringBootApplication
@CrossOrigin
@EnableScheduling
public class FctfoodApplication {

    public static void main(String[] args) {
        SpringApplication.run(FctfoodApplication.class, args);
    }
}
