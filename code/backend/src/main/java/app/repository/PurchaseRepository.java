package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.model.Game;
import app.model.Purchase;
import app.model.User;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByUser(User user);

    @Query("SELECT DISTINCT g FROM Purchase p JOIN p.games g WHERE p.user = :user")
    List<Game> findGamesByUser(@Param("user") User user);

    @Query("SELECT COUNT(g) > 0 FROM Purchase p JOIN p.games g WHERE p.user = :user AND g.id = :gameId")
    boolean hasUserBoughtGame(@Param("user") User user, @Param("gameId") Long gameId);
}
