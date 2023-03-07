package app.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import app.model.Game;
import app.model.User;
import app.repository.GameRepository;
import app.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository users;

	@Autowired
	private PurchaseService purchaseService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private GameRepository games;

	public void save(User user) {
		users.save(user);
	}

	public Optional<User> findByMail(String mail) {
		return users.findByMail(mail);
	}

	public Optional<User> findById(long id) {
		Optional<User> findById = users.findById(id);
		return findById;
	}

	public boolean existMail(String name) {
		Optional<User> user = findByMail(name);
		return user.isPresent();
	}

	public ResponseEntity<Resource> downloadImageProfile(long id) throws SQLException {
		Optional<User> user = users.findById(id);
		if (user.isPresent() && user.get().getProfilePirctureFile() != null) {
			Resource file = new InputStreamResource(user.get().getProfilePirctureFile().getBinaryStream());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					.contentLength(user.get().getProfilePirctureFile().length()).body(file);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	public ResponseEntity<Object> editProfile(long userId, User newUser, User requestUser, MultipartFile imageFile)
			throws IOException, SQLException {
		Optional<User> currentUser = users.findById(userId);
		if (currentUser.isPresent()) {
			User user = currentUser.get();
			if (!user.equals(requestUser)) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			updateImageProfile(user, imageFile);
			if (newUser.getAboutMe() != null) {
				user.setAboutMe(newUser.getAboutMe());
			}
			user.setName(newUser.getName());
			user.setLastName(newUser.getLastName());
			if (newUser.getEncodedPassword() != null) {
				user.setEncodedPassword(passwordEncoder.encode(newUser.getEncodedPassword()));
			}
			users.save(user);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	private void updateImageProfile(User user, MultipartFile imageField) throws IOException, SQLException {
		if (imageField != null && !imageField.isEmpty()) {
			user.setProfilePirctureFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
		} else {
			User dbUser = users.findById(user.getId()).orElseThrow();
			if (dbUser.getProfilePircture() != null) {
				user.setProfilePirctureFile(BlobProxy.generateProxy(dbUser.getProfilePirctureFile().getBinaryStream(),
						dbUser.getProfilePirctureFile().length()));
			}
		}
	}

	public Page<Game> getMoreCartGames(long userId, int page, User currentUser) {
		User user = users.findById(userId).orElseThrow();
		if (!user.getId().equals(currentUser.getId())) {
			return null;
		}
		if (page <= (int) Math.ceil(users.countGamesInCartByUserId(userId) / 3)) {
			return users.findGamesInCartByUserId(userId, PageRequest.of(page, 3));
		}
		return null;
	}

	public ResponseEntity<Object> addCart(User requestUser, long id, long userId) {
		Optional<User> currentUser = users.findById(userId);
		if (currentUser.isPresent()) {
			User user = currentUser.get();
			if (!user.equals(requestUser)) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			Optional<Game> opGame = games.findById(id);
			if (!opGame.isPresent()) {
				return new ResponseEntity<>(users.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
						HttpStatus.NOT_FOUND);
			}
			Game game = opGame.get();
			if (user.getCart().contains(game) || game.getDeleted()
					|| purchaseService.hasUserBoughtGame(user, game.getId())) {
				return new ResponseEntity<>(users.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
						HttpStatus.FORBIDDEN);
			}
			user.addGameToCart(game);
			users.save(user);
			return new ResponseEntity<>(users.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	public ResponseEntity<Object> deleteCart(User requestUser, long id, long userId) {
		Optional<User> currentUser = users.findById(userId);
		if (currentUser.isPresent()) {
			User user = currentUser.get();
			if (!user.equals(requestUser)) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			Optional<Game> opGame = games.findById(id);
			if (!opGame.isPresent()) {
				return new ResponseEntity<>(users.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
						HttpStatus.NOT_FOUND);
			}
			Game game = opGame.get();
			if (!user.getCart().contains(game)) {
				return new ResponseEntity<>(users.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
						HttpStatus.NOT_FOUND);
			}
			user.removeGameFromCart(game);
			users.save(user);
			return new ResponseEntity<>(users.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	public ResponseEntity<Page<Game>> cart(User requestUser, long userId) {
		Optional<User> currentUser = users.findById(userId);
		if (currentUser.isPresent()) {
			User user = currentUser.get();
			if (!user.equals(requestUser)) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			return new ResponseEntity<>(users.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	public Page<Game> findGamesInCartByUserId(Long userId, Pageable pageable) {
		return users.findGamesInCartByUserId(userId, pageable);
	}

	public int countGamesInCartByUserId(Long userId) {
		return users.countGamesInCartByUserId(userId);
	}

	public ResponseEntity<User> register(User user) throws IOException {
		if (!this.existMail(user.getMail())) {
			Resource image = new ClassPathResource("/static/images/avatar.png");
			user.setProfilePirctureFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
			user.setProfilePircture("/static/images/avatar.png");
			user.setEncodedPassword(passwordEncoder.encode(user.getEncodedPassword()));
			user.setRoles("USER");
			if (user.getAboutMe() == null) {
				user.setAboutMe("");
			}
			this.save(user);

			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@Transactional
	public void deleteGameFromAllCarts(Long gameId) {
		Optional<Game> opGame = games.findById(gameId);
		if (opGame.isPresent()) {
			Game game = opGame.get();
			for (User u : users.findUsersByGameInCart(gameId)) {
				u.setTotalPrice(u.getTotalPrice() - game.getPrice());
				users.save(u);
			}
			users.deleteFromUserCartByGameId(gameId);
		} else {
			throw new RuntimeException("Game not found");
		}
	}
}
