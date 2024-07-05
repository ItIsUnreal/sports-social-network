package com.vlad.sportsnetwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SportsNetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(SportsNetworkApplication.class, args);
    }

}
