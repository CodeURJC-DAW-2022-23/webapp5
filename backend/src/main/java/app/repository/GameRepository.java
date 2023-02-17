package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByCategory(String category);

    @Query("SELECT g FROM Game g WHERE LOWER(g.name) LIKE %:name% AND g.category = :category")
    List<Game> findByCategoryAndName(String category, String name);

    @Query("SELECT g FROM Game g WHERE LOWER(g.name) LIKE %:name%")
    List<Game> findByName(String name);
}

