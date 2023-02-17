package app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

	public List<Game> findByCategoryAndName(String name, String category, Pageable pageable) {
		if (name == null )
			name = "";
		if (category == null || category.isEmpty())
			return games.findByName(name, pageable);
		return games.findByCategoryAndName(name, category, pageable);
	}

	public int countByCategoryAndName(String name, String category) {
		if (name == null )
			name = "";
		if (category == null || category.isEmpty())
			return games.countByName(name);
		return games.countByCategoryAndName(name, category);
	}
}
