package app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Game;
import app.model.Review;
import app.model.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserAndGame(User user, Game game);
    List<Review> findByGame(Game game, Pageable pageable);
    public int countByGame(Game game);
}