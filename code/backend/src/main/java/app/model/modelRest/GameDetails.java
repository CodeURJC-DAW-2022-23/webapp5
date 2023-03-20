package app.model.modelRest;

import org.springframework.data.domain.Page;

import app.model.Game;
import app.model.Review;

public class GameDetails {
    Game game;
    Page<Review> reviews;


    public GameDetails() {
        super();
    }


    public GameDetails(Game game, Page<Review> reviews) {
        this.game = game;
        this.reviews = reviews;
    }

}
