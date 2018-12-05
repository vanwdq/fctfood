package com.fct;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;


@EnableScheduling
@ComponentScan(basePackages = {"com.fct"})
@EnableCaching
@CrossOrigin
/*@SpringBootApplication(exclude = PageHelperAutoConfiguration.class)*/
@SpringBootApplication
public class FctApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(FctApplication.class, args);
    }
}
