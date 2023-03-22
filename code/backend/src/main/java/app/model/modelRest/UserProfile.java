package app.model.modelRest;

import app.model.User;
import app.model.Game;

import java.util.List;


public class UserProfile {
    private User user;
    private List<Game> games;

    public UserProfile(){
        super();
    }

    public UserProfile(User user, List<Game> games){
        this.user = user;
        this.games = games;
    }


    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Game> getGames() {
        return this.games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

}

