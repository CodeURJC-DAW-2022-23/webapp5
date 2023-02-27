package app.model.modelRest;

import java.util.List;

import app.model.Game;
import app.model.Review;

public class GameDetails {
    Game game;
    List<Review> reviews;


    public GameDetails() {
    }


    public GameDetails(Game game, List<Review> reviews) {
        this.game = game;
        this.reviews = reviews;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
    
}
