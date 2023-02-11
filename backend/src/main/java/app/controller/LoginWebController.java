package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginWebController {
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping("/loginerror")
	public String loginerror() {
		return "loginerror";
	}
}