package app.controller;


import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import app.model.Game;
import app.model.User;
import app.service.GameService;
import app.service.UserService;

@Controller
public class AddGameController {

	@Autowired
	private GameService gameService;

	@GetMapping("/newGame")
	public String newGame() {
		System.out.println("Entrando en NewGame");
		return "admin";
	}

	@PostMapping("/newGame")
	public String newgameProcess(Model model, Game game, MultipartFile titleImageFile, List<MultipartFile> gameplayImagesFiles) throws IOException {
		game.setTitleImageFile(BlobProxy.generateProxy(titleImageFile.getInputStream(), titleImageFile.getSize()));
		game.setTitleImage(titleImageFile.getOriginalFilename());
		game.setGameplayImagesFiles(gameplayImagesFiles.stream().map(file -> {
			try {
				return BlobProxy.generateProxy(file.getInputStream(), file.getSize());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList()));
		game.setGameplayImages(gameplayImagesFiles.stream().map(file -> file.getOriginalFilename()).collect(Collectors.toList()));
		gameService.save(game);
		return "redirect:/index";
	}
}
