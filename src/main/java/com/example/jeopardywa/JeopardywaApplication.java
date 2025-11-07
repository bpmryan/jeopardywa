package com.example.jeopardywa;

import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class JeopardywaApplication {
	public static void main(String[] args) {
		SpringApplication.run(JeopardywaApplication.class, args);
	}
    @RequestMapping("/")
	public String index() {
		return "index.html";
	}
}
