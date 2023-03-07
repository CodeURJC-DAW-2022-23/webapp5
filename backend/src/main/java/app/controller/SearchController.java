package app.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import app.model.Game;
import app.model.User;
import app.service.GameService;
import app.service.UserService;

@Controller
public class SearchController {

	@Autowired
	private UserService userService;

	@Autowired
	private GameService gameService;

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
	}

	@RequestMapping("/search")
	public String search(Model model, String name, String category) {
		if (name == null)
			name = "";
		Page<Game> gamesFound = gameService.getSearchGames(0, name, category);
		model.addAttribute("games", gamesFound);
		model.addAttribute("found", gamesFound.getNumberOfElements() > 0);
		model.addAttribute("lastSearch", name);
		return "search";
	}

}