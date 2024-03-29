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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

    @Operation(summary = "Get more reviews of a game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "More reviews", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Review.class)) }),
            @ApiResponse(responseCode = "404", description = "Game not found", content = @Content) })
    @GetMapping("/more/{id}/{page}")
    public Page<Review> getMoreReviews(@Parameter(description = "Page number") @PathVariable int page,
            @Parameter(description = "Game id") @PathVariable long id) {
        // Before returning a page it confirms that there are more left
        Game game = gameService.findById(id).orElseThrow();
        if (page <= (int) Math.ceil(reviewService.countByGame(game) / 6)) {
            return reviewService.findByGame(game, PageRequest.of(page, 6));
        }
        return null;
    }

    @Operation(summary = "Add a review to a game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review added", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Review.class)) }),
            @ApiResponse(responseCode = "404", description = "Game not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
    @PostMapping("/{gameId}/{userId}")
    public ResponseEntity<Review> addReview(@Parameter(description = "Id of the game") @PathVariable long gameId,
            @Parameter(description = "Id of the user") @PathVariable long userId,
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

    @Operation(summary = "Delete a review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review deleted", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Review.class)) }),
            @ApiResponse(responseCode = "404", description = "Review not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Review> deleteReview(@Parameter(description = "Id of the review") @PathVariable long reviewId,
            HttpServletRequest request) {
        try {
            User user = userService.findByMail(request.getUserPrincipal().getName()).orElseThrow();
            return reviewService.deleteReview(reviewId, user);
        } catch (Exception e) {
            return null;
        }
    }

    @Operation(summary = "Get a review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Review.class)) }),
            @ApiResponse(responseCode = "404", description = "Review not found", content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReview(@Parameter(description = "Id of the user") @PathVariable long id) {
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
