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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import app.model.Game;
import app.model.Purchase;
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

	@GetMapping("/{userId}/moreCartGames/{page}")
	public Page<Game> getMoreCartGames(@PathVariable int page, HttpServletRequest request, @PathVariable long userId) {
		try {
			User user = userService.findByMail(request.getUserPrincipal().getName()).orElseThrow();
			return userService.getMoreCartGames(userId, page, user);
		} catch (Exception e) {
			return null;
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(@PathVariable long id) {
		// Before returning a page it confirms that there are more left
		Optional<User> opUser = userService.findById(id);
		if (opUser.isPresent()) {
			return new ResponseEntity<>(opUser.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/{userId}/cart")
	public ResponseEntity<Page<Game>> cart(HttpServletRequest request, @PathVariable long userId) {
		try {
			User currentUser = userService.findByMail(request.getUserPrincipal().getName()).get();
			return userService.cart(currentUser, userId);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@PostMapping("{userId}/cart/{id}")
	public ResponseEntity<Object> addCart(HttpServletRequest request, @PathVariable long id,
			@PathVariable long userId) {
		try {
			User currentUser = userService.findByMail(request.getUserPrincipal().getName()).orElseThrow();
			return userService.addCart(currentUser, id, userId);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

	}

	@DeleteMapping("/{userId}/cart/{id}")
	public ResponseEntity<Object> deleteCart(HttpServletRequest request, @PathVariable long id,
			@PathVariable long userId) {
		try {
			User currentUser = userService.findByMail(request.getUserPrincipal().getName()).orElseThrow();
			return userService.deleteCart(currentUser, id, userId);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@PostMapping("/{userId}/checkout")
	public ResponseEntity<Purchase> checkoutProcess(HttpServletRequest request, String billing_street,
			String billing_apartment, String billing_city, String billing_country, String billing_postcode,
			String billing_phone, @PathVariable long userId) {
		try {
			User user = userService.findByMail(request.getUserPrincipal().getName()).orElseThrow();
			ResponseEntity<Purchase> doPurchase = purchaseService.checkoutProcess(user, billing_street,
					billing_apartment, billing_city, billing_country, billing_postcode, billing_phone, userId);
			if (doPurchase.getStatusCode() == HttpStatus.CREATED) {
				Purchase purchase = doPurchase.getBody();
				int index = fromCurrentRequest().toUriString().indexOf("/api/users");
				String value = fromCurrentRequest().toUriString().substring(0, index) + "/api/users/" + userId
						+ "/purchase/" + purchase.getId();
				URI location = URI.create(value);
				return ResponseEntity.created(location).body(purchase);
			} else {
				return doPurchase;
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/{userId}/purchase/{purchaseId}")
	public ResponseEntity<Purchase> purchase(HttpServletRequest request, @PathVariable long userId,
			@PathVariable long purchaseId) {
		try {
			User currentUser = userService.findByMail(request.getUserPrincipal().getName()).orElseThrow();
			return purchaseService.getPurchase(currentUser, userId, purchaseId);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@PutMapping("/{userId}")
	public ResponseEntity<Object> editProfile(@PathVariable long userId, User newUser, HttpServletRequest request,
			MultipartFile imageFile) throws IOException, SQLException {
		try {
			User currentUser = userService.findByMail(request.getUserPrincipal().getName()).orElseThrow();
			return userService.editProfile(userId, newUser, currentUser, imageFile);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}
}
