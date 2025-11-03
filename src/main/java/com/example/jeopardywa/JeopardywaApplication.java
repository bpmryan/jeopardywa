package com.example.jeopardywa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@SpringBootApplication
@Controller
public class JeopardywaApplication {
	@RequestMapping("/")
	public String readIndex() {
		String getIndex = getIndex();
		return getIndex();
	}
	

	// public static void main(String[] args) {
	// 	SpringApplication.run(JeopardywaApplication.class, args);
	// }

	private String getIndex() {
		return "/src/main/resources/frontend/index.html";
	}
}
