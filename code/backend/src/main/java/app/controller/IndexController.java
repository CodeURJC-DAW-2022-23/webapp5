package app.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.data.domain.PageRequest;

import app.service.GameService;

import java.security.Principal;

import app.model.User;
import app.service.UserService;

@Controller
public class IndexController {
	@Autowired
	private GameService gameService;
	@Autowired
	private UserService userService;

	User currentUser;

	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest request) {

		Principal principal = request.getUserPrincipal();

		if (principal != null) {
			userService.findByMail(principal.getName()).ifPresent(u -> currentUser = u);
			model.addAttribute("logged", true);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("emptyCart", currentUser.getCart().isEmpty());
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
			model.addAttribute("userCart",
					userService.findGamesInCartByUserId(currentUser.getId(), PageRequest.of(0, 3)));
			model.addAttribute("moreGamesInCart", currentUser.getCart().size() > 3);
		} else {
			model.addAttribute("logged", false);
		}
		model.addAttribute("carrouselGames", gameService.recomendationGames(currentUser, 3));
	}

	@GetMapping("/")
	public String showBooks(Model model) {
		model.addAttribute("allGames", gameService.findGames(PageRequest.of(0, 6)));
		model.addAttribute("popularGames", gameService.findRecomendNoReg(5));
		return "index";
	}

	@GetMapping("/controlPanel")
	public String controlPanel(Model model) {
		model.addAttribute("allGames", gameService.findGames(PageRequest.of(0, 6)));
		return "controlPanel";
	}
}