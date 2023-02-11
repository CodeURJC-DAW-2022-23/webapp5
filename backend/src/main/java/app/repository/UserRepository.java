package app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMail(String mail);
    Optional<User> findByName(String name);
    Optional<User> findById(long id);
    List<String> findRolesByName(String name);
}