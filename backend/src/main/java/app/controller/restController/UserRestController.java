package app.controller.restController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.core.io.InputStreamResource;
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

import app.Email.EmailDetails;
import app.Email.EmailServiceImpl;
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
	private PasswordEncoder passwordEncoder;

	@Autowired
    private EmailServiceImpl emailService;

	@Autowired
	private GameService gameService;

	@GetMapping("/me")
	public ResponseEntity<UserProfile> profile(HttpServletRequest request) {
		Optional<User> userPrincipal = userService.findByMail(request.getUserPrincipal().getName());
		if (userPrincipal.isPresent()) {
			User user = userPrincipal.get();
			List<Game> games = purchaseService.purchasedGamesByUser(user);
			UserProfile userProfile = new UserProfile(user, games);
			return new ResponseEntity<>(userProfile, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/recomended")
	public ResponseEntity<List<Game>> recomendations(HttpServletRequest request, @RequestParam int numberOfGames) {
		if (request.getUserPrincipal() == null){
			return new ResponseEntity<>(gameService.findRecomendnoreg(numberOfGames), HttpStatus.OK);
		}
		Optional<User> userPrincipal = userService.findByMail(request.getUserPrincipal().getName());
		if (userPrincipal.isPresent()) {
			User user = userPrincipal.get();
			if ( request.isUserInRole("ADMIN") || purchaseService.purchasedGamesByUser(user).isEmpty()){
				return new ResponseEntity<>(gameService.findRecomendnoreg(numberOfGames), HttpStatus.OK);
			}
			String category = gameService.findRecomendCategory(user.getId());
			List<Game> games = gameService.findRecomendbyCategory(category,user.getId(), numberOfGames);
			if(games.isEmpty()){
				games.addAll(gameService.findRecomendnoreg(numberOfGames));
			}else if (games.size() < numberOfGames){
				games.addAll(gameService.findRecomendnoreg(numberOfGames-games.size()));
			}
			return new ResponseEntity<>(games, HttpStatus.OK);
		}else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


    @GetMapping("/{id}/imageProfile")
	public ResponseEntity<Object> downloadImageProfile(@PathVariable long id) throws SQLException {
		Optional<User> user = userService.findById(id);
		if (user.isPresent() && user.get().getProfilePirctureFile() != null) {
			Resource file = new InputStreamResource(user.get().getProfilePirctureFile().getBinaryStream());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").contentLength(user.get().getProfilePirctureFile().length()).body(file);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	//Register users
	@PostMapping("/")
	public ResponseEntity<Object> register(@RequestBody User user) throws IOException {
		if (!userService.existMail(user.getMail())) {
			Resource image = new ClassPathResource("/static/images/avatar.png");
			user.setProfilePirctureFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
			user.setProfilePircture("/static/images/avatar.png");
			user.setEncodedPassword(passwordEncoder.encode(user.getEncodedPassword()));
			user.setRoles("USER");
			if (user.getAboutMe() == null){
				user.setAboutMe("");
			}
			userService.save(user);
			return new ResponseEntity<>(user, HttpStatus.CREATED);
		} else{
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
	}

	@GetMapping("/{userId}/moreCartGames/{page}")
	public List<Game> getMoreCartGames(@PathVariable int page, HttpServletRequest request, @PathVariable long userId) {
		// Before returning a page it confirms that there are more left
		User user = userService.findByMail(request.getUserPrincipal().getName()).orElseThrow();
		User requestUser = userService.findById(userId).orElseThrow();
		if (!user.getId().equals(requestUser.getId())){
			return null;
		}
		if (page <= (int) Math.ceil(userService.countGamesInCartByUserId(user.getId())/3)) {
			return userService.findGamesInCartByUserId(user.getId(), PageRequest.of(page,3));
		}
		return null;
	}

	@GetMapping("/{userId}/cart")
    public ResponseEntity<List<Game>> cart(HttpServletRequest request, @PathVariable long userId) {
        Optional<User> userPrincipal = userService.findByMail(request.getUserPrincipal().getName());
        if (userPrincipal.isPresent()) {
            User user = userPrincipal.get();
            User requestUser = userService.findById(userId).orElseThrow();
			if (!user.equals(requestUser)){
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
            return new ResponseEntity<>(userService.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("{userId}/cart/{id}")
    public ResponseEntity<Object> addCart(HttpServletRequest request, @PathVariable long id, @PathVariable long userId) {
        Optional<User> userPrincipal = userService.findByMail(request.getUserPrincipal().getName());
        if (userPrincipal.isPresent()) {
            User user = userPrincipal.get();
            User requestUser = userService.findById(userId).orElseThrow();
			if (!user.equals(requestUser)){
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
            Optional<Game> opGame = gameService.findById(id);
            if (!opGame.isPresent()) {
                return new ResponseEntity<>(userService.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
                        HttpStatus.NOT_FOUND);
            }
            Game game = opGame.get();
            if (user.getCart().contains(game) || game.getDeleted()
                    || purchaseService.hasUserBoughtGame(user, game.getId())) {
                return new ResponseEntity<>(userService.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
                        HttpStatus.FORBIDDEN);
            }
            user.addGameToCart(game);
            userService.save(user);
            return new ResponseEntity<>(userService.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{userId}/cart/{id}")
    public ResponseEntity<Object> deleteCart(HttpServletRequest request, @PathVariable long id, @PathVariable long userId) {
        Optional<User> userPrincipal = userService.findByMail(request.getUserPrincipal().getName());
        if (userPrincipal.isPresent()) {
            User user = userPrincipal.get();
            User requestUser = userService.findById(userId).orElseThrow();
			if (!user.equals(requestUser)){
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
            Optional<Game> opGame = gameService.findById(id);
            if (!opGame.isPresent()) {
                return new ResponseEntity<>(userService.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
                        HttpStatus.NOT_FOUND);
            }
            Game game = opGame.get();
            if (!user.getCart().contains(game)) {
                return new ResponseEntity<>(userService.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
                        HttpStatus.NOT_FOUND);
            }
            user.removeGameFromCart(game);
            userService.save(user);
            return new ResponseEntity<>(userService.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<Object> checkoutProcess(HttpServletRequest request, String billing_street,
            String billing_apartment, String billing_city, String billing_country, String billing_postcode,
            String billing_phone, @PathVariable long userId) {
        Optional<User> userPrincipal = userService.findByMail(request.getUserPrincipal().getName());
        if (userPrincipal.isPresent()) {
            User user = userPrincipal.get();
            User requestUser = userService.findById(userId).orElseThrow();
			if (!user.equals(requestUser)){
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
            if (user.getCart().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            if (user.getBillingInformation().equals("")) {
                String billingInfo = billing_street + " " +
                        billing_apartment + ", " + billing_city + billing_country + ", " + billing_postcode + ", "
                        + billing_phone;
                user.setBillingInformation(billingInfo);
            }
            Purchase purchase = new Purchase(user.getCart(), user);
            purchaseService.save(purchase);
            user.purchase();
            userService.save(user);
            EmailDetails emailDetails = new EmailDetails(user.getMail(), "Thank you for your purchase!");
            emailDetails.generatePurchaseMessage(purchase, user);
            emailService.sendSimpleMail(emailDetails);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

	@PutMapping("/{userId}")
	public ResponseEntity<Object> editProfile(@PathVariable long userId, User newUser, HttpServletRequest request, MultipartFile imageFile) throws IOException, SQLException {
		Optional<User> userPrincipal = userService.findByMail(request.getUserPrincipal().getName());
		if (userPrincipal.isPresent()){
			User user = userPrincipal.get();
			User requestUser = userService.findById(userId).orElseThrow();
			if (!user.equals(requestUser)){
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			updateImageProfile(user, imageFile);
			if (newUser.getAboutMe() != null){
				user.setAboutMe(newUser.getAboutMe());
			}
			user.setName(newUser.getName());
			user.setLastName(newUser.getLastName());
			if (newUser.getEncodedPassword() != null){
				user.setEncodedPassword(passwordEncoder.encode(newUser.getEncodedPassword()));
			}
			userService.save(user);
			return new ResponseEntity<>(user, HttpStatus.OK);
	}else{
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}

private void updateImageProfile(User user, MultipartFile imageField) throws IOException, SQLException {
	if (imageField!=null && !imageField.isEmpty()) {
		user.setProfilePirctureFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
	} else {
		User dbUser = userService.findById(user.getId()).orElseThrow();
		if (dbUser.getProfilePircture() != null) {
			user.setProfilePirctureFile(BlobProxy.generateProxy(dbUser.getProfilePirctureFile().getBinaryStream(), dbUser.getProfilePirctureFile().length()));
		}
	}
}


}
