package app.model;

import java.time.LocalDate;
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
	private long id;

    @ManyToMany
    private List<Game> games;

    @ManyToOne
    private User user;

    private LocalDate date = LocalDate.now();

    public Purchase(){

    }

    public Purchase(List<Game> games, User user) {
        this.games = games;
        this.user = user;
    }


    public long getId() {
        return this.id;
    }

    public void setId(long id) {
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
