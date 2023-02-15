package app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Game;
import app.model.Review;
import app.model.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserAndGame(User user, Game game);
}