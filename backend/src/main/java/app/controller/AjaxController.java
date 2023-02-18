package app.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import app.model.Game;
import app.service.GameService;
import app.service.ReviewService;
import app.service.UserService;
import app.model.User;

@Controller
public class AjaxController {

    @Autowired
    private GameService gameService;

    @Autowired
    private ReviewService reviewService;

	@Autowired
	private UserService userService;

	User currentUser;

	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest request) {

		Principal principal = request.getUserPrincipal();

		if(principal != null) {
			userService.findByMail(principal.getName()).ifPresent(u -> currentUser = u);
			model.addAttribute("logged", true);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
		} else {
			model.addAttribute("logged", false);
		}
	}

    @GetMapping("/moreReviews/{id}/{page}")
	public String getMoreReviews(Model model, @PathVariable int page, @PathVariable long id) {
		// Before returning a page it confirms that there are more left
        Game game = gameService.findById(id).orElseThrow();
		if (page <= (int) Math.ceil(reviewService.countByGame(game)/6)) {
			model.addAttribute("foundReviews", reviewService.findByGame(game, PageRequest.of(page,6)));
			return "moreReviews";
		}
		return null;
	}

	@GetMapping("/moreFoundGames/{page}")
	public String getMoreFoundGames(Model model, @PathVariable int page, String category, String name) {
		// Before returning a page it confirms that there are more left
		if (name.equals("null")){
			name = null;
		}
		if (category.equals("null")){
			category = null;
		}
		if (page <= (int) Math.ceil(gameService.countByCategoryAndName(name, category)/6)) {
			model.addAttribute("foundGames", gameService.findByCategoryAndName(name, category, PageRequest.of(page,6)));
			return "moreGames";
		}
		return null;
	}
	
	@GetMapping("/moreIndexGames/{page}")
	public String getMoreIndexGames(Model model, @PathVariable int page) {
		// Before returning a page it confirms that there are more left
		if (page <= (int) Math.ceil(gameService.countGames()/6)) {
			model.addAttribute("foundGames", gameService.findGames(PageRequest.of(page,6)));
			return "indexGames";
		}
		return null;
	}

	@GetMapping("/moreCartGames/{id}/{page}")
	public String getMoreCartGames(Model model, @PathVariable int page, @PathVariable long id) {
		// Before returning a page it confirms that there are more left
		User user = userService.findById(id).orElseThrow();
		if (!user.getId().equals(currentUser.getId())){
			return null;
		}
		if (page <= (int) Math.ceil(userService.countGamesInCartByUserId(id)/3)) {
			model.addAttribute("foundGames", userService.findGamesInCartByUserId(id, PageRequest.of(page,3)));
			return "cartGames";
		}
		return null;
	}
}