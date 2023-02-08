package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.model.Game;
import jakarta.annotation.PostConstruct;

@Service
public class SampleDataService {

	@Autowired
	private GameService games;
	
	@PostConstruct
	public void init() {

		Game g = new Game("God of War", "Buen Juego", "Windows 10");
		Game j = new Game("Gfasdfd", "Buen Juego", "Windows 10");
		games.save(g);
		games.save(j);
	}
}
