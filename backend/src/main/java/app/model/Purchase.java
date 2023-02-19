package app.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Purchase {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @ManyToMany
    private List<Game> games = new ArrayList<>();

    @ManyToOne
    private User user;

    private LocalDate date = LocalDate.now();

    public Purchase(){

    }

    public Purchase(List<Game> games, User user) {
        super();
        for (Game game : games) {
            this.games.add(game);
        }
        this.user = user;
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Game> getGames() {
        return this.games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
