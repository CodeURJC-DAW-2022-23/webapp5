package app.controller;

import java.security.Principal;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.model.Game;
import app.model.User;
import app.service.GameService;
import app.service.UserService;
import app.service.PurchaseService;
import app.service.ReviewService;
import app.model.Review;
import java.util.Optional;

@Controller
public class GameController {

	@Autowired
	private UserService userService;

	@Autowired
	private GameService gameService;

	@Autowired
	private PurchaseService purchaseService;

	@Autowired
	private ReviewService reviewService;

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
		gameService.recomendationGames(currentUser, 3);
	}

	@GetMapping("/game/{id}")
	public String gameProduct(Model model, @PathVariable long id) {
		try {
			Game game = gameService.findById(id).orElseThrow();
			model.addAttribute("popularGames", gameService.findRecomendNoReg(5));
			model.addAttribute("game", game);
			model.addAttribute("averageStars", game.averageStars());
			model.addAttribute("haveStars", game.getReviews().size() > 0);
			model.addAttribute("starNumber", game.getReviews().size());
			model.addAttribute("ratings", game.getStarDistributionInt());

			if (currentUser != null) {
				model.addAttribute("inCart", currentUser.getCart().contains(game));
				model.addAttribute("isBought", purchaseService.purchasedGamesByUser(currentUser).contains(game));
				model.addAttribute("isReviewed", reviewService.reviewedByUser(currentUser, game));
				model.addAttribute("userReview", reviewService.findByUserInGame(currentUser, game));
				model.addAttribute("thisReviews",
						reviewService.findByGameAndNotUser(game, currentUser, PageRequest.of(0, 6)));
			} else {
				model.addAttribute("thisReviews", reviewService.findByGame(game, PageRequest.of(0, 6)));
			}
			return "product-info";
		} catch (Exception e) {
			return "redirect:/error";
		}
	}

	@GetMapping("/{id}/imageTitle")
	public ResponseEntity<Resource> downloadImageProfile(@PathVariable long id) throws SQLException {
		return gameService.downloadImageProfile(id);
	}

	@GetMapping("/{id}/gameplayImage/{index}")
	public ResponseEntity<Resource> downloadGameplayImages(@PathVariable long id, @PathVariable int index)
			throws SQLException {
		return gameService.downloadGameplayImages(id, index);
	}

	@PostMapping("/{userId}/reviewGame/{id}")
	public String reviewGame(Model model, @PathVariable long id, @PathVariable long userId,
			@RequestParam String comment, @RequestParam int reviewRate) {
		ResponseEntity<Review> addReview = reviewService.addReview(id, currentUser, userId, comment, reviewRate);
		if (addReview.getStatusCode().is2xxSuccessful()) {
			long gameId = gameService.findById(id).get().getId();
			return "redirect:/game/" + gameId;
		} else {
			return "redirect:/error";
		}
	}

	@GetMapping("/deleteReview/{id}")
	public String deleteReview(Model model, @PathVariable long id) {
		long gameId;
		Optional<Review> opRev = reviewService.findById(id);
		if (opRev.isPresent()) {
			gameId = opRev.get().getGame().getId();
		} else {
			return "redirect:/error";
		}
		ResponseEntity<Review> deleteReview = reviewService.deleteReview(id, currentUser);
		if (deleteReview.getStatusCode().is2xxSuccessful()) {
			return "redirect:/game/" + gameId;
		} else {
			return "redirect:/error";
		}
	}
}
