package app.service;

import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import app.model.Game;
import app.model.Purchase;
import app.model.Review;
import app.model.User;
import javax.annotation.PostConstruct;

@Service
public class SampleDataService {

	@Autowired
	private GameService games;

	@Autowired
	private UserService users;

	@Autowired
	private PurchaseService purchases;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct
	public void init() throws IOException {

		if(games.countByCategoryAndName("", "") > 0) {
			return;
		}

		List<User> generatedUsers = generateUsers();
		for (User user : generatedUsers) {
			users.save(user);
		}
		List<Game> generatedGames = generateGames();
		generateReviews(generatedGames, generatedUsers);

		for (Game game : generatedGames) {
			games.save(game);
		}

		List<Purchase> generatedPurchases = generatePurchases(generatedGames, generatedUsers);
		for (Purchase purchase : generatedPurchases) {
			purchases.save(purchase);
		}
	}

	private List<Review> generateReviews(List<Game> generatedGames, List<User> generatedUsers) {
		List<Review> reviews = new ArrayList<>();
		boolean control = true;
		for (Game game : generatedGames) {
			for (User user : generatedUsers) {
				if (control) {
					Review review = new Review();
					review.setGame(game);
					review.setUser(user);
					review.setRating((int) (Math.random() * 5) + 1);
					review.setComment("This is a comment for the game " + game.getName());
					reviews.add(review);
					game.addReview(review);
				}
				control = !control;
			}
		}
		return reviews;
	}

	private List<Purchase> generatePurchases(List<Game> generatedGames, List<User> generatedUsers) {
		List<Purchase> purchases = new ArrayList<>();
		for (User user : generatedUsers) {
			Purchase purchase = new Purchase();
			purchase.setGames(generatedGames);
			purchase.setUser(user);
			purchases.add(purchase);
		}
		return purchases;
	}

	private List<User> generateUsers() {
		LinkedList<User> users = new LinkedList<>();
		for (int index = 1; index <= 10; index++) {
			User user = new User();
			String name = "user" + index;
			String lastName = "lastname" + index;
			user.setName(name);
			user.setLastName(lastName);
			user.setMail(name + lastName + "@gmail.com");
			user.setEncodedPassword(passwordEncoder.encode("12345678"));
			user.setAboutMe("I am the user " + name + " " + lastName);
			user.setProfilePircture("/static/images/avatar.png");
			user.setRoles("USER");
			setProfilePicture(user, user.getProfilePircture());
			users.add(user);
		}
		for (int index = 1; index <= 10; index++) {
			User user = new User();
			String name = "admin" + index;
			String lastName = "adminLastName" + index;
			user.setName(name);
			user.setLastName(lastName);
			user.setMail(name + lastName + "@gmail.com");
			user.setEncodedPassword(passwordEncoder.encode("12345678"));
			user.setAboutMe("I am the admin " + name + " " + lastName);
			user.setProfilePircture("/static/images/avatar.png");
			user.setRoles("USER", "ADMIN");
			setProfilePicture(user, user.getProfilePircture());
			users.add(user);
		}
		return users;
	}

	private void setProfilePicture(User user, String profilePircture) {
		try {
			Resource image = new ClassPathResource(profilePircture);
			user.setProfilePirctureFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Game> generateGames() throws IOException {
		LinkedList<Game> games = new LinkedList<>();
		LinkedList<String> nombresJuegos = new LinkedList<>();
		nombresJuegos.add("Bloodborne");
		nombresJuegos.add("Dark Souls II");
		nombresJuegos.add("Kingdoms of Amalur Reckoning");
		nombresJuegos.add("The Witcher");
		nombresJuegos.add("Skyrim");
		nombresJuegos.add("Middle eart shadow of mordor");
		nombresJuegos.add("Soul Sacrifice");
		nombresJuegos.add("Diablo III");
		nombresJuegos.add("Dragons Dogma");
		nombresJuegos.add("Lords of the Fallen");
		nombresJuegos.add("Prototype");
		nombresJuegos.add("Risen 3");
		nombresJuegos.add("Neverwinter");
		nombresJuegos.add("Assassins Creed 4");
		nombresJuegos.add("Half-Life 3");
		for (String string : nombresJuegos) {
			Game g1 = new Game();
			g1.setName(string);
			g1.setDescription(
					"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
			g1.setPrice(29.99f);
			g1.setCategory("Action");
			String fileName = string.toLowerCase().replace(' ', '-');
			g1.setTitleImage("/static/images/game-" + fileName + ".jpg");
			setGameImage(g1, g1.getTitleImage());
			g1.setOs("Windows 7");
			g1.setProcessor("Dual core from Intel or AMD at 2.8 GHz");
			g1.setMemory("4 GB RAM");
			g1.setGraphics(
					"NVIDIA GeForce GTX 260 or AMD Radeon HD 4870 (512 MB VRAM with Shader Model 4.0 or higher)");
			g1.setDirectX("Version 11");
			g1.setNetwork("Broadband Internet connection");
			g1.setHardDrive("50 GB available space");
			g1.setSoundCard("DirectX 9.0c compatible sound card");
			for (int index = 2; index <= 7; index++) {
				g1.getGameplayImages()
						.add("/static/images/game-" + fileName + "-" + index + "-500x375.jpg");
			}
			setGameplayImage(g1, g1.getGameplayImages());
			games.add(g1);
		}
		return games;
	}

	private void setGameplayImage(Game game, List<String> gameplayImages) {
		List<Blob> auxArray = new ArrayList<>();
		int index = 0;
		try {
			for (String string : gameplayImages) {
				Resource image = new ClassPathResource(string);
				auxArray.add(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
				game.setGameplayImagesFiles(auxArray);
				index++;
			}
		} catch (IOException e) {
			try {
				for (int i = index; i < gameplayImages.size(); i++) {
				Resource image = new ClassPathResource(gameplayImages.get(0));
				auxArray.add(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
				game.setGameplayImagesFiles(auxArray);
				}
			} catch (Exception e2) {
				e.printStackTrace();
			}
		}
	}

	public void setGameImage(Game game, String classpathResource) throws IOException {
		try {
			Resource image = new ClassPathResource(classpathResource);
			game.setTitleImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}