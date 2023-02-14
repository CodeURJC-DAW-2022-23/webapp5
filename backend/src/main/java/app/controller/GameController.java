package app.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import app.model.Game;
import app.model.User;
import app.service.GameService;
import app.service.UserService;

@Controller
public class GameController {
	
	@Autowired
	private UserService userService;

    @Autowired
    private GameService gameService;
	
	
	User currentUser;

	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest request) {

		Principal principal = request.getUserPrincipal();

		if(principal != null) {
			userService.findByMail(principal.getName()).ifPresent(u -> currentUser = u);
			model.addAttribute("logged", true);
			model.addAttribute("user", currentUser);
			List<Game> cart = currentUser.getCart();
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
		} else {
			model.addAttribute("logged", false);
		}
	}

    @GetMapping("/game/{id}")
	public String gameProduct(Model model, @PathVariable long id) {
		try{
			Game game = gameService.findById(id).orElseThrow();
            model.addAttribute("game", game);
			if (currentUser != null){
				model.addAttribute("inCart", currentUser.getCart().contains(game));
			}
            return "product-info";
		}catch(Exception e){
			return "redirect:/error";
		}
	}

    @GetMapping("/{id}/imageTitle")
	public ResponseEntity<Resource> downloadImageProfile(@PathVariable long id) throws SQLException {
		Optional<Game> game = gameService.findById(id);
		if (game.isPresent() && game.get().getTitleImage() != null) {
			Resource file = new InputStreamResource(game.get().getTitleImageFile().getBinaryStream());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").contentLength(game.get().getTitleImageFile().length()).body(file);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

    @GetMapping("/{id}/gameplayImage/{index}")
	public ResponseEntity<Resource> downloadGameplayImages(@PathVariable long id, @PathVariable int index) throws SQLException {
		Optional<Game> game = gameService.findById(id);
		if (game.isPresent() && game.get().getGameplayImages() != null) {
			Resource file = new InputStreamResource(game.get().getGameplayImagesFiles().get(index - 1).getBinaryStream());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").contentLength(game.get().getGameplayImagesFiles().get(index - 1).length()).body(file);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}

