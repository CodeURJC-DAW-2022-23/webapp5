package app.controller.restController;

import java.net.URI;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import app.model.Game;
import app.model.User;
import app.model.Review;
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

    @GetMapping("/more/{id}/{page}")
    public Page<Review> getMoreReviews(@PathVariable int page, @PathVariable long id) {
        // Before returning a page it confirms that there are more left
        Game game = gameService.findById(id).orElseThrow();
        if (page <= (int) Math.ceil(reviewService.countByGame(game) / 6)) {
            return reviewService.findByGame(game, PageRequest.of(page, 6));
        }
        return null;
    }

    @PostMapping("/{gameId}/{userId}")
    public ResponseEntity<Review> addReview(@PathVariable long gameId, @PathVariable long userId,
            HttpServletRequest request, String comment, int reviewRate) {
        try {
            User user = userService.findByMail(request.getUserPrincipal().getName()).orElseThrow();
            ResponseEntity<Review> addReview = reviewService.addReview(gameId, user, userId, comment, reviewRate);
            if (addReview.getStatusCode() == HttpStatus.CREATED) {
                Review review = addReview.getBody();
                Game game = review.getGame();
                int index = fromCurrentRequest().toUriString().indexOf("/api/reviews");
                String value = fromCurrentRequest().toUriString().substring(0, index) + "/api/reviews/"
                        + game.getReviews().get(game.getReviews().size() - 1).getId();
                URI location = URI.create(value);
                return ResponseEntity.created(location).body(review);
            } else {
                return addReview;
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Review> deleteReview(@PathVariable long reviewId,
            HttpServletRequest request) {
        try {
            User user = userService.findByMail(request.getUserPrincipal().getName()).orElseThrow();
            return reviewService.deleteReview(reviewId, user);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReview(@PathVariable long id) {
        // Before returning a page it confirms that there are more left
        Optional<Review> opReview = reviewService.findById(id);
        if (opReview.isPresent()) {
            return new ResponseEntity<>(opReview.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/review/{userId}/{gameId}")
    public ResponseEntity<Boolean> getReview(@PathVariable long userId, @PathVariable long gameId) {
        // Before returning a page it confirms that there are more left
        Optional<User> opUser = userService.findById(userId);
        Optional<Game> opGame = gameService.findById(gameId);
        try{
            Boolean opReview = reviewService.reviewedByUser(opUser.get(), opGame.get());
            return new ResponseEntity<>(opReview, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
