package app.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

		if(principal != null) {
			userService.findByMail(principal.getName()).ifPresent(u -> currentUser = u);
			model.addAttribute("logged", true);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
		} else {
			model.addAttribute("logged", false);
		}
	}

    @GetMapping("/game/{id}")
	public String gameProduct(Model model, @PathVariable long id) {
		try{
			Game game = gameService.findById(id).orElseThrow();
            model.addAttribute("game", game);
			model.addAttribute("averageStars", game.averageStars());
			model.addAttribute("haveStars", game.getReviews().size() > 0);
			model.addAttribute("starNumber", game.getReviews().size());
			model.addAttribute("ratings", game.getStarDistributionInt());
			if (currentUser != null){
				model.addAttribute("inCart", currentUser.getCart().contains(game));
				model.addAttribute("isBought", purchaseService.purchasedGamesByUser(currentUser).contains(game));
				model.addAttribute("isReviewed", reviewService.reviewedByUser(currentUser, game));
			}
            return "product-info";
		}catch(Exception e){
			return "redirect:/error";
		}
	}

    @GetMapping("/{id}/imageTitle")
	public ResponseEntity<Resource> downloadImageProfile(@PathVariable long id) throws SQLException {
		Optional<Game> game = gameService.findById(id);
		if (game.isPresent() && game.get().getTitleImage() != null) {
			Resource file = new InputStreamResource(game.get().getTitleImageFile().getBinaryStream());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").contentLength(game.get().getTitleImageFile().length()).body(file);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

    @GetMapping("/{id}/gameplayImage/{index}")
	public ResponseEntity<Resource> downloadGameplayImages(@PathVariable long id, @PathVariable int index) throws SQLException {
		Optional<Game> game = gameService.findById(id);
		if (game.isPresent() && game.get().getGameplayImages() != null) {
			Resource file = new InputStreamResource(game.get().getGameplayImagesFiles().get(index - 1).getBinaryStream());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").contentLength(game.get().getGameplayImagesFiles().get(index - 1).length()).body(file);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/{userId}/reviewGame/{id}")
	public String reviewGame(Model model, @PathVariable long id, @PathVariable long userId, String comment, int reviewRate) {
		try{
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
		}catch(Exception e){
			return "redirect:/error";
		}
	}
}

