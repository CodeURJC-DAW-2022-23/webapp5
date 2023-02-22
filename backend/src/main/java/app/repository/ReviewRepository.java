package app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.model.Game;
import app.model.Review;
import app.model.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserAndGame(User user, Game game);
    @Query("SELECT r FROM Review r WHERE r.game = :game AND r.user <> :user")
    List<Review> findByGameAndNotUser(Game game, User user, Pageable pageable);
    List<Review> findByGame(Game game, Pageable pageable);
    public int countByGame(Game game);
    @Query("SELECT COUNT(r) FROM Review r WHERE r.game = :game AND r.user <> :user")
    public int countByGameAndNotUser(Game game, User user);
}