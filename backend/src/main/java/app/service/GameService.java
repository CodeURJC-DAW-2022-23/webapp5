package app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.model.Game;
import app.repository.GameRepository;

@Service
public class GameService {

	@Autowired
	private GameRepository games;

	public void save(Game game) {
		games.save(game);
	}

	public List<Game> findAll() {
		return games.findAll();
	}

	public List<Game> findByName(String category) {
		return games.findByCategory(category);
	}

	public Optional<Game> findById(long id) {
		return games.findById(id);
	}

	public void deleteById(long id) {
		games.deleteById(id);
	}
}
