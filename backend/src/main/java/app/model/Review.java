package app.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Review {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Game game;

    private LocalDate date = LocalDate.now();
    
    @ElementCollection(fetch=FetchType.LAZY)
    List<Integer> stars = new ArrayList<>();

    @ElementCollection(fetch=FetchType.LAZY)
    List<Integer> noStars = new ArrayList<>();

    private int rating;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    public Review() {

    }

    public Review(User user, Game game, int rating, String comment) {
        this.user = user;
        this.game = game;
        this.rating = rating;
        setRating(rating);
        this.comment = comment;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        for (int i = 1; i <= rating; i++) {
            stars.add(1);
        }
        for (int i = 1; i <= 5 - rating; i++) {
            noStars.add(1);
        }
        this.rating = rating;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
