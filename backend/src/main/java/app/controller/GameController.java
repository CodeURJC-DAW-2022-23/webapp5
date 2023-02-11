package app.controller;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import app.service.UserService;

import java.security.Principal;


@Controller
public class GameController {

	@Autowired
	private UserService userService;

	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest request) {

		Principal principal = request.getUserPrincipal();


		if(principal != null) {
			System.out.println("Logged in as: " + principal.getName());
			model.addAttribute("logged", true);
			model.addAttribute("userName", principal.getName());
		} else {
			model.addAttribute("logged", false);
		}
	}

	@GetMapping("/")
	public String showBooks(Model model) {

		return "index";
	}

	@GetMapping("/search")
	public String search(Model model) {

		return "search";
	}

	@GetMapping("/newGame")
	public String newGame(Model model) {

		return "admin";
	}

	@GetMapping("/cart")
	public String cart(Model model) {

		return "cart";
	}

	@GetMapping("/checkout")
	public String checkout(Model model) {

		return "checkout";
	}

	@GetMapping("/product")
	public String product(Model model) {

		return "product-info";
	}

	@GetMapping("/profile")
	public String profile(Model model) {
		return "user-profile";
	}

}
