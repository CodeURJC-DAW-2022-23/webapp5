package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

}

