package app.controller.passed;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.model.Game;
import app.service.GameService;
import app.service.UserService;
import app.service.ReviewService;
import app.model.Review;
import app.model.User;

@RestController
@RequestMapping("/api/ajax")
public class AjaxRestController {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;
    
    @GetMapping("/moreIndexGames/{page}")
	public List<Game> getMoreIndexGames(@PathVariable int page) {
		// Before returning a page it confirms that there are more left
		if (page <= (int) Math.ceil(gameService.countGames()/6)) {
			return gameService.findGames(PageRequest.of(page,6));
		}
		return null;
	}

    @GetMapping("/moreReviews/{id}/{page}")
	public List<Review> getMoreReviews(@PathVariable int page, @PathVariable long id) {
		// Before returning a page it confirms that there are more left
        Game game = gameService.findById(id).orElseThrow();
			if (page <= (int) Math.ceil(reviewService.countByGame(game)/6)) {
				return reviewService.findByGame(game, PageRequest.of(page,6));
            }
            return null;
	}

    @GetMapping("/moreFoundGames/{page}")
	public List<Game> getMoreFoundGames(@PathVariable int page, String category, String name) {
		// Before returning a page it confirms that there are more left
		if (name.equals("null")){
			name = null;
		}
		if (category.equals("null")){
			category = null;
		}
		if (page <= (int) Math.ceil(gameService.countByCategoryAndName(name, category)/6)) {
			return gameService.findByCategoryAndName(name, category, PageRequest.of(page,6));
		}
		return null;
	}

    @GetMapping("/moreCartGames/{page}/{userId}")
	public List<Game> getMoreCartGames(@PathVariable int page, HttpServletRequest request, @PathVariable long userId) {
		// Before returning a page it confirms that there are more left
		User user = userService.findByMail(request.getUserPrincipal().getName()).orElseThrow();
		User requestUser = userService.findById(userId).orElseThrow();
		if (!user.getId().equals(requestUser.getId())){
			return null;
		}
		if (page <= (int) Math.ceil(userService.countGamesInCartByUserId(user.getId())/3)) {
			return userService.findGamesInCartByUserId(user.getId(), PageRequest.of(page,3));
		}
		return null;
	}
}
