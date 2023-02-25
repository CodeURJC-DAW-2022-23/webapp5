package app.controller.restController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;
import org.hibernate.engine.jdbc.BlobProxy;

import app.model.Game;
import app.model.modelRest.GameDetails;
import app.service.ReviewService;
import app.service.GameService;

@RestController
@RequestMapping("/api/games")
public class GameRestController {

    @Autowired
    private GameService gameService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/")
    public ResponseEntity<List<Game>> getGames() {
        // Before returning a page it confirms that there are more left
        if (gameService.countGames() > 0) {
            return new ResponseEntity<>(gameService.findGames(PageRequest.of(0, 6)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/moreIndexGames/{page}")
	public List<Game> getMoreIndexGames(@PathVariable int page) {
		// Before returning a page it confirms that there are more left
		if (page <= (int) Math.ceil(gameService.countGames()/6)) {
			return gameService.findGames(PageRequest.of(page,6));
		}
		return null;
	}

    @GetMapping("/search")
    public ResponseEntity<List<Game>> search(String name, String category) {
		if (name == null )
			name = "";
            List<Game> gamesFound = gameService.findByCategoryAndName(name, category, PageRequest.of(0,6));
            if (gamesFound.size() > 0){
                return new ResponseEntity<>(gamesFound, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
    }

    @GetMapping("/moreFoundGames/{page}")
	public List<Game> getMoreFoundGames(@PathVariable int page, String category, String name) {
		// Before returning a page it confirms that there are more left
		if (name.equals("null")){
			name = null;
		}
		if (category.equals("null")){
			category = null;
		}
		if (page <= (int) Math.ceil(gameService.countByCategoryAndName(name, category)/6)) {
			return gameService.findByCategoryAndName(name, category, PageRequest.of(page,6));
		}
		return null;
	}

    // Creates a game
    @PostMapping("/")
    public ResponseEntity<Game> newGameProcess(Game game, MultipartFile imageField, List<MultipartFile> imageFields)
            throws IOException, SQLException {
        updateImageGame(game, imageField);
        updateGameplayImages(game, imageFields);
        gameService.save(game);
        return new ResponseEntity<>(game, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDetails> gameDetails(@PathVariable long id) {
        Optional<Game> opGame = gameService.findById(id);
        if (opGame.isPresent()) {
            Game game = opGame.get();
            GameDetails gameDetails = new GameDetails(game, reviewService.findByGame(game, PageRequest.of(0, 6)));
            return new ResponseEntity<>(gameDetails, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a game
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGame(@PathVariable long id) {
        Optional<Game> opGame = gameService.findById(id);
        if (opGame.isPresent()) {
            Game game = opGame.get();
            game.setDeleted(true);
            gameService.save(game);
            return new ResponseEntity<>(game, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Edit a game
    @PutMapping("/{id}")
    public ResponseEntity<Game> editGame(Game game, MultipartFile imageField, List<MultipartFile> imageFields,
            @PathVariable long id) throws IOException, SQLException {
        Optional<Game> opGame = gameService.findById(id);
        if (opGame.isPresent()) {
            Game currentGame = opGame.get();
            updateImageGame(currentGame, imageField);
            updateGameplayImages(currentGame, imageFields);
            currentGame.editGame(game);
            gameService.save(currentGame);
            return new ResponseEntity<>(game, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/coverImage")
    public ResponseEntity<Resource> downloadImageProfile(@PathVariable long id) throws SQLException {
        Optional<Game> game = gameService.findById(id);
        if (game.isPresent() && game.get().getTitleImage() != null) {
            Resource file = new InputStreamResource(game.get().getTitleImageFile().getBinaryStream());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(game.get().getTitleImageFile().length()).body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/gameplayImage/{index}")
    public ResponseEntity<Resource> downloadGameplayImages(@PathVariable long id, @PathVariable int index)
            throws SQLException {
        Optional<Game> game = gameService.findById(id);
        if (game.isPresent() && game.get().getGameplayImages() != null && index > 0
                && index <= game.get().getGameplayImages().size()) {
            Resource file = new InputStreamResource(
                    game.get().getGameplayImagesFiles().get(index - 1).getBinaryStream());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(game.get().getGameplayImagesFiles().get(index - 1).length()).body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private void updateGameplayImages(Game game, List<MultipartFile> imageFields) {
        if (imageFields != null && !imageFields.get(0).getOriginalFilename().equals("") && !imageFields.isEmpty()) {
            game.setGameplayImagesFiles(imageFields.stream().map(file -> {
                try {
                    return BlobProxy.generateProxy(file.getInputStream(), file.getSize());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList()));
            game.setGameplayImages(imageFields.stream().map(file -> "").collect(Collectors.toList()));
        } else {
            Game dbGame = gameService.findById(game.getId()).orElseThrow();
            game.setGameplayImagesFiles(dbGame.getGameplayImagesFiles());
        }
    }

    private void updateImageGame(Game game, MultipartFile imageField) throws IOException, SQLException {
        if (imageField != null && !imageField.isEmpty()) {
            game.setTitleImageFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
            game.setTitleImage("");
        } else {
            Game dbGame = gameService.findById(game.getId()).orElseThrow();
            game.setTitleImageFile(dbGame.getTitleImageFile());
        }
    }

}
