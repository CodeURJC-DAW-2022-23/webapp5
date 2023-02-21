package app.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT g FROM Game g WHERE g.category = :category AND g.deleted = false")
    List<Game> findByCategory(String category);

    @Query("SELECT g FROM Game g WHERE LOWER(g.name) LIKE %:name% AND g.category = :category AND g.deleted = false")
    List<Game> findByCategoryAndName(String name, String category, Pageable pageable);

    @Query("SELECT g FROM Game g WHERE LOWER(g.name) LIKE %:name% AND g.deleted = false")
    List<Game> findByName(String name, Pageable pageable);

    @Query("SELECT COUNT(g) FROM Game g WHERE LOWER(g.name) LIKE %:name% AND g.category = :category AND g.deleted = false")
    int countByCategoryAndName(String name, String category);

    @Query("SELECT COUNT(g) FROM Game g WHERE LOWER(g.name) LIKE %:name% AND g.deleted = false")
    int countByName(String name);

    int countById(long id);

    @Query("SELECT g FROM Game g WHERE g.deleted = false")
    List<Game> findGames(Pageable pageable);
}


