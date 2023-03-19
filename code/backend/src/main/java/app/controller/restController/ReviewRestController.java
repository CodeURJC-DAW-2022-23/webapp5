package app.controller.restController;

import app.service.ReviewService;
import app.service.UserService;
import app.service.GameService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
