// package jeopardywa.src.main.java;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JeopardyApp {
    @RequestMapping("../src/main/resources/frontend/index.html")
	public String readIndex() {
		return "/src/main/resources/frontend/index.html";
	}
}
