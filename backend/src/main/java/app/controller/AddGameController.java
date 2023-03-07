package app.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.PageRequest;

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

	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest request) {

		Principal principal = request.getUserPrincipal();

		if (principal != null) {
			userService.findByMail(principal.getName()).ifPresent(u -> currentUser = u);
			model.addAttribute("logged", true);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("emptyCart", currentUser.getCart().isEmpty());
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
			model.addAttribute("userCart",
					userService.findGamesInCartByUserId(currentUser.getId(), PageRequest.of(0, 3)));
			model.addAttribute("moreGamesInCart", currentUser.getCart().size() > 3);
		} else {
			model.addAttribute("logged", false);
		}
	}

	@GetMapping("/newGame")
	public String newGame() {
		return "newGame";
	}

	@PostMapping("/newGame")
	public String newGameProcess(Model model, Game game, @RequestParam MultipartFile imageField,
			@RequestParam List<MultipartFile> imageFields) throws IOException, SQLException {
		gameService.saveNewGame(game, imageField, imageFields);
		return "redirect:/";
	}

	@GetMapping("/editGame/{id}")
	public String editGame(Model model, @PathVariable long id) {
		Game currentGame = gameService.findById(id).orElseThrow();
		model.addAttribute("currentGame", currentGame);
		return "editGame";
	}

	@GetMapping("/deleteGame/{id}")
	public String deleteGame(Model model, @PathVariable long id) {
		gameService.deleteById(id);
		return "redirect:/controlPanel";
	}

	@PostMapping("/editGame/{id}")
	public String editGameProcess(Model model, Game game, @RequestParam MultipartFile imageField,
			@RequestParam List<MultipartFile> imageFields, @PathVariable long id) throws IOException, SQLException {
		gameService.editGame(id, game, imageField, imageFields);
		return "redirect:/game/" + id;
	}
}
