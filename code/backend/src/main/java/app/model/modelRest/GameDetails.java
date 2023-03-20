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

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Page<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(Page<Review> reviews) {
        this.reviews = reviews;
    }
    
}
