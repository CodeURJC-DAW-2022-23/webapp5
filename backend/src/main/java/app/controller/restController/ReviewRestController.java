package app.controller.restController;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.model.Game;
import app.model.User;
import app.model.Review;
import app.service.PurchaseService;
import app.service.ReviewService;
import app.service.UserService;
import app.service.GameService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewRestController {

    @Autowired
    private GameService gameService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private PurchaseService purchaseService;
    
    @GetMapping("/more/{id}/{page}")
	public List<Review> getMoreReviews(@PathVariable int page, @PathVariable long id) {
		// Before returning a page it confirms that there are more left
        Game game = gameService.findById(id).orElseThrow();
			if (page <= (int) Math.ceil(reviewService.countByGame(game)/6)) {
				return reviewService.findByGame(game, PageRequest.of(page,6));
            }
            return null;
	}

    @PostMapping("/{gameId}/{userId}")
    public ResponseEntity<Review> addReview(@PathVariable long gameId, @PathVariable long userId,
            HttpServletRequest request, String comment, int reviewRate) {
        Optional<Game> opGame = gameService.findById(gameId);
        Optional<User> opUser = userService.findById(userId);
        if (opGame.isPresent() && opUser.isPresent()) {
            Optional<User> currentUser = userService.findByMail(request.getUserPrincipal().getName());
            Game game = opGame.get();
            User user = opUser.get();
            if (!currentUser.isPresent()) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            if (!user.getId().equals(currentUser.get().getId()) | !purchaseService.hasUserBoughtGame(user, gameId)
                    | reviewService.reviewedByUser(user, game)) {
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

    @DeleteMapping("/{reviewId}/{userId}")
    public ResponseEntity<Review> deleteReview(@PathVariable long reviewId, @PathVariable long userId,
            HttpServletRequest request) {
        Optional<Review> opReview = reviewService.findById(reviewId);
        Optional<User> opUser = userService.findById(userId);
        if (opReview.isPresent() && opUser.isPresent()) {
            Optional<User> currentUser = userService.findByMail(request.getUserPrincipal().getName());
            Review review = opReview.get();
            User user = opUser.get();
            if (!currentUser.isPresent()) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            if (!user.getId().equals(currentUser.get().getId()) | !user.getRoles().contains("ADMIN")) {
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
}
