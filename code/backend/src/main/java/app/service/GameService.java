package app.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import java.sql.Blob;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import app.model.Game;
import app.model.User;
import app.repository.GameRepository;
import java.util.ArrayList;

@Service
public class GameService {

	@Autowired
	private GameRepository games;

	@Autowired
	private UserService userService;

	@Autowired
	private PurchaseService purchaseService;

	public Game saveNewGame(Game game, MultipartFile imageField, List<MultipartFile> imageFields)
			throws IOException, SQLException {
		updateImageGame(game, imageField);
		updateGameplayImages(game, imageFields);
		games.save(game);
		return game;
	}

	public ResponseEntity<Game> editGame(long id, Game game, MultipartFile imageField, List<MultipartFile> imageFields)
			throws IOException, SQLException {
		try{
			Game currentGame = games.findById(id).orElseThrow();
		updateImageGame(currentGame, imageField);
		updateGameplayImages(currentGame, imageFields);
		currentGame.editGame(game);
		games.save(currentGame);
		return new ResponseEntity<>(currentGame, HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	public ResponseEntity<Game> deleteById(long id) {
		try{
			Game game = games.findById(id).orElseThrow();
			game.setDeleted(true);
			games.save(game);
			userService.deleteGameFromAllCarts(id);
			return new ResponseEntity<>(game, HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	public void save(Game game) {
		games.save(game);
	}

	public Page<Game> getSearchGames(int page, String name, String category) {
		if (name == null || name.equals("null")) {
			name = "";
		}
		if (category == null || category.equals("null")) {
			category = "";
		}
		if (page <= (int) Math.ceil(this.countByCategoryAndName(name, category) / 6)) {
			return this.findByCategoryAndName(name, category, PageRequest.of(page, 6));
		}
		return null;
	}

	public Page<Game> getMoreIndexGames(int page) {
		if (page <= (int) Math.ceil(games.count() / 6)) {
			return games.findGames(PageRequest.of(page, 6));
		}
		return null;
	}

	public Page<Game> findGames(Pageable pageable) {
		return games.findGames(pageable);
	}

	public List<Game> findRecomendNoReg(Integer num) {
		return games.findRecomendnoreg(num);
	}

	public ResponseEntity<Resource> downloadImageProfile(long id) throws SQLException {
		Optional<Game> game = this.findById(id);
		if (game.isPresent() && game.get().getTitleImage() != null) {
			Resource file = new InputStreamResource(game.get().getTitleImageFile().getBinaryStream());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					.contentLength(game.get().getTitleImageFile().length()).body(file);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	public ResponseEntity<Resource> downloadGameplayImages(long id, int index)
			throws SQLException {
		Optional<Game> game = games.findById(id);
		if (game.isPresent() && game.get().getGameplayImages() != null) {
			Resource file = new InputStreamResource(
					game.get().getGameplayImagesFiles().get(index - 1).getBinaryStream());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
					.contentLength(game.get().getGameplayImagesFiles().get(index - 1).length()).body(file);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	public List<Game> recomendationGames(User currentUser, int num) {
		if (currentUser == null || currentUser.getRoles().contains("ADMIN")
				|| purchaseService.purchasedGamesByUser(currentUser).isEmpty()) {
			return this.findRecomendNoReg(num);
		} else {
			String category = games.findRecomendCategory(currentUser.getId());
			List<Game> recomended = games.findRecomendbyCategory(category, currentUser.getId(), num);
			if (recomended.isEmpty()) {
				recomended.addAll(this.findRecomendNoReg(num));
			} else if (recomended.size() < num) {
				recomended.addAll(this.findRecomendNoReg(num - recomended.size()));
			}
			return recomended;
		}
	}

	public Optional<Game> findById(long id) {
		return games.findById(id);
	}

	public Page<Game> findByCategoryAndName(String name, String category, Pageable pageable) {
		if (name == null)
			name = "";
		if (category == null || category.isEmpty())
			return games.findByName(name, pageable);
		return games.findByCategoryAndName(name, category, pageable);
	}

	public int countByCategoryAndName(String name, String category) {
		if (name == null)
			name = "";
		if (category == null || category.isEmpty())
			return games.countByName(name);
		return games.countByCategoryAndName(name, category);
	}

	private void updateGameplayImages(Game game, List<MultipartFile> imageFields) {
		if (imageFields != null && !imageFields.get(0).getOriginalFilename().equals("") && !imageFields.isEmpty()) {
			game.setGameplayImages(new ArrayList<>());
			game.setGameplayImagesFiles(imageFields.stream().map(file -> {
				try {
					Blob generateProxy = BlobProxy.generateProxy(file.getInputStream(), file.getSize());
					game.getGameplayImages().add("isNice");
					return generateProxy;
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}).collect(Collectors.toList()));
		} else {
			Game dbGame = games.findById(game.getId()).orElseThrow();
			game.setGameplayImagesFiles(dbGame.getGameplayImagesFiles());
		}
	}

	private void updateImageGame(Game game, MultipartFile imageField) throws IOException, SQLException {
		if (imageField != null && !imageField.isEmpty()) {
			game.setTitleImageFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
			game.setTitleImage("isNice");
		} else {
			Game dbGame = games.findById(game.getId()).orElseThrow();
			game.setTitleImageFile(dbGame.getTitleImageFile());
		}
	}
}
