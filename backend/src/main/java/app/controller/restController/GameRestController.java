package app.controller.restController;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.model.Game;
import app.model.User;
import app.model.Review;
import app.model.modelRest.GameDetails;
import app.service.PurchaseService;
import app.service.ReviewService;
import app.service.UserService;
import app.service.GameService;

@RestController
@RequestMapping("/api/games")
public class GameRestController {

    @Autowired
    private GameService gameService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private PurchaseService purchaseService;


    
    @GetMapping("/")
    public ResponseEntity<List<Game>> getGames() {
		// Before returning a page it confirms that there are more left
        if (gameService.countGames() > 0) {
            return new ResponseEntity<>(gameService.findGames(PageRequest.of(0,6)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
	}

    @GetMapping("/{id}")
    public ResponseEntity<GameDetails> gameDetails(@PathVariable long id) {
		Optional<Game> opGame = gameService.findById(id);
        if (opGame.isPresent()) {
            Game game = opGame.get();
            GameDetails gameDetails = new GameDetails(game, reviewService.findByGame(game, PageRequest.of(0 ,6)));
            return new ResponseEntity<>(gameDetails, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
	}

    @PostMapping("/review/{gameId}/{userId}")
    public ResponseEntity<Review> addReview(@PathVariable long gameId, @PathVariable long userId, HttpServletRequest request, String comment, int reviewRate) {
        Optional<Game> opGame = gameService.findById(gameId);
        Optional<User> opUser = userService.findById(userId);
        if (opGame.isPresent() && opUser.isPresent()) {
            Optional<User> currentUser = userService.findByMail(request.getUserPrincipal().getName());
            Game game = opGame.get();
            User user = opUser.get();
            if (!currentUser.isPresent()) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            if (!user.getId().equals(currentUser.get().getId()) | !purchaseService.hasUserBoughtGame(user, gameId) | reviewService.reviewedByUser(user, game)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            Review review = new Review(user, game, reviewRate, comment);
			game.addReview(review);
			gameService.save(game);
            return new ResponseEntity<>(review, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/review/{reviewId}/{userId}")
    public ResponseEntity<Review> deleteReview(@PathVariable long reviewId, @PathVariable long userId, HttpServletRequest request) {
        Optional<Review> opReview = reviewService.findById(reviewId);
        Optional<User> opUser = userService.findById(userId);
        if (opReview.isPresent() && opUser.isPresent()) {
            Optional<User> currentUser = userService.findByMail(request.getUserPrincipal().getName());
            Review review = opReview.get();
            User user = opUser.get();
            if (!currentUser.isPresent()) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            if (!user.getId().equals(currentUser.get().getId()) | !user.getId().equals(review.getUser().getId())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            Game game = review.getGame();
            game.deleteReview(review);
			gameService.save(game);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/coverImage")
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
		if (game.isPresent() && game.get().getGameplayImages() != null && index > 0 && index <= game.get().getGameplayImages().size()) {
			Resource file = new InputStreamResource(game.get().getGameplayImagesFiles().get(index - 1).getBinaryStream());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").contentLength(game.get().getGameplayImagesFiles().get(index - 1).length()).body(file);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
