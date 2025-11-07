// package jeopardywa.src.main.java;

import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JeopardyApp {
	public static void main(String[] args) {
		SpringApplication.run(JeopardyApp.class, args);
	}
    @RequestMapping("/")
	public String index() {
		return "index.html";
	}
}
