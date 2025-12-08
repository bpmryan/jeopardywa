/*
 * DemoApplication.java
 * Purpose: Spring Boot application entry point. Starts the application and
 * exposes a simple mapping for the root path to return the `index.html`
 * static resource.
 *
 * Key methods:
 *  - main(String[]): boots the Spring application
 *  - index(): returns the root `index.html` page
 */
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @RequestMapping("/")
    public String index() {
        return "index.html";
    }
}
