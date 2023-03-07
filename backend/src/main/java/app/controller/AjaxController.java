package app.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import app.model.Game;
import app.model.Review;
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

		if (principal != null) {
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
		Page<Review> moreReviews = reviewService.getReviews(page, id, currentUser);
		if (moreReviews == null) {
			return null;
		} else {
			model.addAttribute("foundReviews", moreReviews);
			return "moreReviews";
		}
	}

	@GetMapping("/moreFoundGames/{page}")
	public String getMoreFoundGames(Model model, @PathVariable int page, String category, String name) {
		Page<Game> moreGames = gameService.getSearchGames(page, name, category);
		if (moreGames == null) {
			return null;
		} else {
			model.addAttribute("foundGames", moreGames);
			return "moreGames";
		}
	}

	@GetMapping("/moreIndexGames/{page}")
	public String getMoreIndexGames(Model model, @PathVariable int page) {
		// Before returning a page it confirms that there are more left
		Page<Game> moreGames = gameService.getMoreIndexGames(page);
		if (moreGames == null) {
			return null;
		} else {
			model.addAttribute("foundGames", moreGames);
			return "indexGames";
		}
	}

	@GetMapping("/moreControlGames/{page}")
	public String getControlIndexGames(Model model, @PathVariable int page) {
		// Before returning a page it confirms that there are more left
		Page<Game> moreGames = gameService.getMoreIndexGames(page);
		if (moreGames == null) {
			return null;
		} else {
			model.addAttribute("foundGames", moreGames);
			return "controlGames";
		}
	}

	@GetMapping("/moreCartGames/{id}/{page}")
	public String getMoreCartGames(Model model, @PathVariable int page, @PathVariable long id) {
		// Before returning a page it confirms that there are more left
		Page<Game> moreGames = userService.getMoreCartGames(id, page, currentUser);
		if (moreGames == null) {
			return null;
		} else {
			model.addAttribute("foundGames", moreGames);
			return "cartGames";
		}
	}

	@GetMapping("/moreCheckGames/{id}/{page}")
	public String getMoreCheckGames(Model model, @PathVariable int page, @PathVariable long id) {
		// Before returning a page it confirms that there are more left
		Page<Game> moreGames = userService.getMoreCartGames(id, page, currentUser);
		if (moreGames == null) {
			return null;
		} else {
			model.addAttribute("foundGames", moreGames);
			return "checkGames";
		}
	}
}