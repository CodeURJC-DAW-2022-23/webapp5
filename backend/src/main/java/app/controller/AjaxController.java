package app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import app.model.Game;
import app.service.GameService;
import app.service.ReviewService;

@Controller
public class AjaxController {

    @Autowired
    private GameService gameService;

    @Autowired
    private ReviewService reviewService;

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
            List<Game> findByGame = gameService.findByCategoryAndName(name, category, PageRequest.of(page,6));
			model.addAttribute("foundGames", findByGame);
			return "moreGames";
		}
		return null;
	}
}