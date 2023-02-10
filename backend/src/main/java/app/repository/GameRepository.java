package app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByCategory(String category);
}

