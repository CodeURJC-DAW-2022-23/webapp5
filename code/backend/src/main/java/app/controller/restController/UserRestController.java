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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

	@Operation(summary = "Get information about the current user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the user", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfile.class)) }),
			@ApiResponse(responseCode = "404", description = "User not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
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

	@Operation(summary = "Get recomendation tothe current user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found games recomendations", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfile.class)) }),
			@ApiResponse(responseCode = "404", description = "Not found games recomendation", content = @Content) })
	@GetMapping("/recomended")
	public ResponseEntity<List<Game>> recomendations(HttpServletRequest request, @RequestParam int numberOfGames) {
		try {
			User user = userService.findByMail(request.getUserPrincipal().getName()).orElseThrow();
			return new ResponseEntity<>(gameService.recomendationGames(user, numberOfGames), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(gameService.recomendationGames(null, numberOfGames), HttpStatus.OK);
		}
	}

	@Operation(summary = "Get the image profile of the user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the image profile", content = {
					@Content(mediaType = "image/png") }),
			@ApiResponse(responseCode = "404", description = "image profile not found", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/{id}/imageProfile")
	public ResponseEntity<Resource> downloadImageProfile(
			@Parameter(description = "id of the current user") @PathVariable long id) throws SQLException {
		return userService.downloadImageProfile(id);
	}

	@Operation(summary = "Register a new user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "User created", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	// Register users
	@PostMapping("/")
	public ResponseEntity<User> register(@RequestParam String name, @RequestParam String lastName,
			@RequestParam String mail, @RequestParam String password, @RequestParam String aboutMe) throws IOException {
		User user = new User(name, lastName, mail, password, aboutMe);
		ResponseEntity<User> response = userService.register(user);
		if (response.getStatusCode() == HttpStatus.CREATED) {
			URI location = fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
			return ResponseEntity.created(location).body(user);
		} else {
			return response;
		}
	}

	@Operation(summary = "Get more cart games of the user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found more games in the cart", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Game.class)) }),
			@ApiResponse(responseCode = "404", description = "Games not found in the cart", content = @Content) })
	@GetMapping("/{userId}/moreCartGames/{page}")
	public Page<Game> getMoreCartGames(@Parameter(description = "page of the games") @PathVariable int page,
			HttpServletRequest request,
			@Parameter(description = "id of the current user") @PathVariable long userId) {
		try {
			User user = userService.findByMail(request.getUserPrincipal().getName()).orElseThrow();
			return userService.getMoreCartGames(userId, page, user);
		} catch (Exception e) {
			return null;
		}
	}

	@Operation(summary = "Get user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the user", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(HttpServletRequest request, @Parameter(description = "id of the user") @PathVariable long id) {
		// Before returning a page it confirms that there are more left
		Optional<User> opUser = userService.findById(id);
		if (opUser.isPresent()) {
			if (request.getUserPrincipal() != null) {
				try {
					User user = userService.findByMail(request.getUserPrincipal().getName()).orElseThrow();
					if (user.getId() != id && !user.getRoles().contains("ADMIN")) {
						return new ResponseEntity<>(HttpStatus.FORBIDDEN);
					}else{
						return new ResponseEntity<>(opUser.get(), HttpStatus.OK);
					}
				} catch (Exception e) {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
				}
			}else{
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Get the cart of the user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the cart", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfile.class)) }),
			@ApiResponse(responseCode = "404", description = "Cart not found", content = @Content) })
	@GetMapping("/{userId}/cart")
	public ResponseEntity<Page<Game>> cart(HttpServletRequest request,
			@Parameter(description = "id of the current user") @PathVariable long userId) {
		try {
			User currentUser = userService.findByMail(request.getUserPrincipal().getName()).get();
			return userService.cart(currentUser, userId);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@Operation(summary = "Add a game to the cart of the user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Game added to the cart", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfile.class)) }),
			@ApiResponse(responseCode = "404", description = "Game not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PostMapping("{userId}/cart/{id}")
	public ResponseEntity<Object> addCart(HttpServletRequest request,
			@Parameter(description = "id of the game") @PathVariable long id,
			@Parameter(description = "id of the current user") @PathVariable long userId) {
		try {
			User currentUser = userService.findByMail(request.getUserPrincipal().getName()).orElseThrow();
			return userService.addCart(currentUser, id, userId);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

	}

	@Operation(summary = "Delete a game to the cart of the user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Game deleted to the cart", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfile.class)) }),
			@ApiResponse(responseCode = "404", description = "Game not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@DeleteMapping("/{userId}/cart/{id}")
	public ResponseEntity<Object> deleteCart(HttpServletRequest request,
			@Parameter(description = "id of the game") @PathVariable long id,
			@Parameter(description = "id of the current user") @PathVariable long userId) {
		try {
			User currentUser = userService.findByMail(request.getUserPrincipal().getName()).orElseThrow();
			return userService.deleteCart(currentUser, id, userId);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@Operation(summary = "make a purchase")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "the purchase has been successfully completed", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfile.class)) }),
			@ApiResponse(responseCode = "404", description = "Game not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PostMapping("/{userId}/checkout")
	public ResponseEntity<Purchase> checkoutProcess(HttpServletRequest request, String billing_street,
			String billing_apartment, String billing_city, String billing_country, String billing_postcode,
			String billing_phone, @Parameter(description = "id of the current user") @PathVariable long userId) {
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

	@Operation(summary = "Get the purchase of the user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the purchase", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfile.class)) }),
			@ApiResponse(responseCode = "404", description = "Purchase not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@GetMapping("/{userId}/purchase/{purchaseId}")
	public ResponseEntity<Purchase> purchase(HttpServletRequest request,
			@Parameter(description = "id of the current user") @PathVariable long userId,
			@Parameter(description = "id of the purchase") @PathVariable long purchaseId) {
		try {
			User currentUser = userService.findByMail(request.getUserPrincipal().getName()).orElseThrow();
			return purchaseService.getPurchase(currentUser, userId, purchaseId);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@Operation(summary = "Update the profile of the user")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Profile updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserProfile.class)) }),
			@ApiResponse(responseCode = "404", description = "Profile not found ", content = @Content),
			@ApiResponse(responseCode = "403", description = "forbiden o dont have permissions", content = @Content) })
	@PutMapping("/{userId}")
	public ResponseEntity<Object> editProfile(
			@Parameter(description = "id of the current user") @PathVariable long userId, User newUser,
			HttpServletRequest request,
			MultipartFile imageFile) throws IOException, SQLException {
		try {
			User currentUser = userService.findByMail(request.getUserPrincipal().getName()).orElseThrow();
			return userService.editProfile(userId, newUser, currentUser, imageFile);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}
}
