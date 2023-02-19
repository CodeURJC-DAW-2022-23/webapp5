package app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.model.Game;
import app.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMail(String mail);
    Optional<User> findByName(String name);
    List<String> findRolesByName(String name);

    @Query("SELECT u.cart FROM User u WHERE u.id = :userId")
    List<Game> findGamesInCartByUserId(@Param("userId") Long userId, Pageable pageable);
    @Query("SELECT COUNT(*) FROM User u JOIN u.cart c WHERE u.id = :userId")
    int countGamesInCartByUserId(@Param("userId") Long userId);
}