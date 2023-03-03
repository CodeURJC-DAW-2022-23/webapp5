package app.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
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

		if (currentUser == null || request.isUserInRole("ADMIN") || purchaseService.purchasedGamesByUser(currentUser).isEmpty()) {
			model.addAttribute("relatedGames", gameService.findRecomendnoreg(4));
		}else{
			String category = gameService.findRecomendCategory(currentUser.getId());
			List<Game> games = gameService.findRecomendbyCategory(category,currentUser.getId(),4);
			if(games.isEmpty()){
				games.addAll(gameService.findRecomendnoreg(4));
			}else if (games.size() < 4){
				games.addAll(gameService.findRecomendnoreg(4-games.size()));
			}
			model.addAttribute("relatedGames", games);
		}
	}

	@GetMapping("/game/{id}")
	public String gameProduct(Model model, @PathVariable long id) {
		try {
			Game game = gameService.findById(id).orElseThrow();
			model.addAttribute("popularGames", gameService.findRecomendnoreg(5));
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
		Optional<Game> game = gameService.findById(id);
		if (game.isPresent() && game.get().getTitleImage() != null) {
			Resource file = new InputStreamResource(game.get().getTitleImageFile().getBinaryStream());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					.contentLength(game.get().getTitleImageFile().length()).body(file);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/{id}/gameplayImage/{index}")
	public ResponseEntity<Resource> downloadGameplayImages(@PathVariable long id, @PathVariable int index)
			throws SQLException {
		Optional<Game> game = gameService.findById(id);
		if (game.isPresent() && game.get().getGameplayImages() != null) {
			Resource file = new InputStreamResource(
					game.get().getGameplayImagesFiles().get(index - 1).getBinaryStream());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					.contentLength(game.get().getGameplayImagesFiles().get(index - 1).length()).body(file);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/{userId}/reviewGame/{id}")
	public String reviewGame(Model model, @PathVariable long id, @PathVariable long userId,
			@RequestParam String comment, @RequestParam int reviewRate) {
		try {
			Game game = gameService.findById(id).orElseThrow();
			User user = userService.findById(userId).orElseThrow();
			if (!user.getId().equals(currentUser.getId())) {
				throw new Exception();
			}
			if (reviewService.reviewedByUser(user, game)) {
				throw new Exception();
			}
			Review review = new Review(user, game, reviewRate, comment);
			game.addReview(review);
			gameService.save(game);
			return "redirect:/game/{id}";
		} catch (Exception e) {
			return "redirect:/error";
		}
	}

	@GetMapping("/deleteReview/{id}")
	public String deleteReview(Model model, @PathVariable long id) {
		try {
			Review review = reviewService.findById(id).orElseThrow();
			if (!review.getUser().getId().equals(currentUser.getId()) && !currentUser.getRoles().contains("ADMIN")) {
				throw new Exception();
			}
			Game game = review.getGame();
			game.deleteReview(review);
			gameService.save(game);
			return "redirect:/game/" + game.getId();
		} catch (Exception e) {
			return "redirect:/error";
		}
	}
}
