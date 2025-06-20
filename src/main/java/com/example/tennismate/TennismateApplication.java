package com.example.tennismate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TennismateApplication {

    public static void main(String[] args) {
        SpringApplication.run(TennismateApplication.class, args);
    }

}
