package app.controller.restController;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.model.Game;
import app.model.User;
import app.model.modelRest.UserProfile;
import app.service.GameService;
import app.service.PurchaseService;
import app.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private PurchaseService purchaseService;

	@Autowired
	private GameService gameService;

	@GetMapping("/me")
	public ResponseEntity<UserProfile> profile(HttpServletRequest request) {
		Optional<User> currentUser = userService.findByMail(request.getUserPrincipal().getName());
		if (currentUser.isPresent()) {
			User user = currentUser.get();
			List<Game> games = purchaseService.purchasedGamesByUser(user);
			UserProfile userProfile = new UserProfile(user, games);
			return new ResponseEntity<>(userProfile, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/recomended")
	public ResponseEntity<List<Game>> recomendations(HttpServletRequest request, @RequestParam int numberOfGames) {
		try {
			User user = userService.findByMail(request.getUserPrincipal().getName()).orElseThrow();
			return new ResponseEntity<>(gameService.recomendationGames(user, numberOfGames), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(gameService.recomendationGames(null, numberOfGames), HttpStatus.OK);
		}
	}

	@GetMapping("/{id}/imageProfile")
	public ResponseEntity<Resource> downloadImageProfile(@PathVariable long id) throws SQLException {
		return userService.downloadImageProfile(id);
	}

	// Register users
	@PostMapping("/")
	public ResponseEntity<User> register(@RequestBody User user) throws IOException {
		ResponseEntity<User> response = userService.register(user);
		if (response.getStatusCode() == HttpStatus.CREATED) {
			URI location = fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
			return ResponseEntity.created(location).body(user);
		} else {
			return response;
		}
	}
}