package app.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.model.User;
import app.service.UserService;

@Controller
public class LoginWebController {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	User currentUser;

	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest request) {

		Principal principal = request.getUserPrincipal();
		
		if(principal != null) {
			userService.findByMail(principal.getName()).ifPresent(u -> currentUser = u);
			model.addAttribute("logged", true);
			model.addAttribute("userName", currentUser.getName());
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
		} else {
			model.addAttribute("logged", false);
		}
	}

	@GetMapping("/login")
	public String login(Model model, @RequestParam Optional<String> error) {
		error.ifPresent(e -> model.addAttribute("error", true));
		return "login";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@PostMapping("/register")
	public String registerProcess(Model model, User user) throws IOException {
		if (!userService.existMail(user.getMail())) {
			Resource image = new ClassPathResource("/static/images/avatar.png");
			user.setProfilePirctureFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
			user.setProfilePircture("/static/images/avatar.png");
			user.setEncodedPassword(passwordEncoder.encode(user.getEncodedPassword()));
			userService.save(user);
			return "redirect:/login";
		} else{
				model.addAttribute("error", true);
				return "register";
			}
	}
}