package app.controller;


import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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


	@Autowired
	private UserService userService;

	User currentUser;
	Game currentGame;

	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest request) {

		Principal principal = request.getUserPrincipal();

		if(principal != null) {
			userService.findByMail(principal.getName()).ifPresent(u -> currentUser = u);
			model.addAttribute("logged", true);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
		} else {
			model.addAttribute("logged", false);
		}
	}

	@GetMapping("/newGame")
	public String newGame() {
		return "newGame";
	}

	@PostMapping("/newGame")
	public String newgameProcess(Model model, Game game, MultipartFile imageField, List<MultipartFile> imageFields) throws IOException, SQLException{
		updateImageGame(game, imageField);
		updateGameplayImages(game, imageFields);
		gameService.save(game);
		return "redirect:/";
	}

	@GetMapping("/editGame/{id}")
	public String editProfile(Model model, @PathVariable long id) {
		Game game = gameService.findById(id).orElseThrow();
		if (game.getId().equals(currentGame.getId())) {
			model.addAttribute("game", game);
			return "editGame";
		}
		return "redirect:/error";
	}

	@PostMapping("/editGame")
	public String editGameProcess(Model model, Game game, MultipartFile imageField, List<MultipartFile> imageFields) throws IOException, SQLException {
		updateImageGame(currentGame, imageField);
		updateGameplayImages(currentGame, imageFields);
		currentGame.editGame(game);
		return "redirect:/game/" + currentGame.getId();
	}

	private void updateGameplayImages(Game game, List<MultipartFile> imageFields) {
		if (imageFields != null) {
			game.setGameplayImagesFiles(imageFields.stream().map(file -> {
				try {
					return BlobProxy.generateProxy(file.getInputStream(), file.getSize());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}).collect(Collectors.toList()));
			game.setGameplayImages(imageFields.stream().map(file -> "").collect(Collectors.toList()));
		}else{
			Game dbGame = gameService.findById(game.getId()).orElseThrow();
			game.setGameplayImagesFiles( dbGame.getGameplayImagesFiles());			
		}
	}

	private void updateImageGame(Game game, MultipartFile imageField) throws IOException, SQLException {
		if (!imageField.isEmpty()) {
			game.setTitleImageFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
			game.setTitleImage("");
		} else {
			Game dbGame = gameService.findById(game.getId()).orElseThrow();
			game.setTitleImageFile(dbGame.getTitleImageFile());
		}
	}
}
