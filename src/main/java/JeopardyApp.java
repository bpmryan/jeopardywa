// package jeopardywa.src.main.java;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JeopardyApp {
    @RequestMapping("/")
	public String index() {
		return "index.html";
	}
}
