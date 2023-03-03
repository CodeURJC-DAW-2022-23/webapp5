package app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.model.Game;
import app.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMail(String mail);

    Optional<User> findByName(String name);

    List<String> findRolesByName(String name);

    @Query("SELECT u.cart FROM User u WHERE u.id = :userId")
    Page<Game> findGamesInCartByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(*) FROM User u JOIN u.cart c WHERE u.id = :userId")
    int countGamesInCartByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = "DELETE FROM user_cart WHERE cart_id = :gameId", nativeQuery = true)
    void deleteFromUserCartByGameId(@Param("gameId") Long gameId);

    @Query(value = "SELECT u.* FROM user u INNER JOIN user_cart uc ON u.id = uc.user_id WHERE uc.cart_id = :gameId", nativeQuery = true)
    List<User> findUsersByGameInCart(@Param("gameId") Long gameId);
}