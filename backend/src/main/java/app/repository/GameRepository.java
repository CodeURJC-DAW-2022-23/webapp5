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

    @Query(
        value = "SELECT game.* FROM game INNER JOIN review ON game.id = review.game_id GROUP BY game.id ORDER BY (total_rating/COUNT(review.id)) DESC LIMIT ?1",
        nativeQuery = true)
        List<Game> findRecomendnoreg(Integer num);
    @Query(
        value ="SELECT game.category FROM game INNER JOIN purchase_games ON game.id = purchase_games.games_id INNER JOIN purchase ON purchase_games.purchase_id = purchase.id WHERE purchase.user_id = ?1 GROUP BY game.category ORDER BY COUNT(*) DESC LIMIT 1",
        nativeQuery = true)
        String findRecomendCategory(Long id);
        
    @Query(
        value ="SELECT game.* FROM game WHERE game.category = ?1 AND NOT game.id IN (SELECT game.id FROM game INNER JOIN purchase_games ON game.id = purchase_games.games_id INNER JOIN purchase ON purchase_games.purchase_id = purchase.id WHERE purchase.user_id = ?2) LIMIT ?3",
        nativeQuery = true)
        List<Game>  findRecomendbyCategory(String category,Long id,Integer num);
}