package com.everycampus.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.everycampus")
@EnableJpaRepositories(basePackages = "com.everycampus.repository")  // ← 이거 추가!
@EntityScan(basePackages = "com.everycampus.entity")  

public class EverycampusApplication {

    public static void main(String[] args) {
        SpringApplication.run(EverycampusApplication.class, args);
    }
}
