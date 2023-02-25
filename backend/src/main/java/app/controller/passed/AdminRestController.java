package app.controller.passed;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.web.bind.annotation.PatchMapping;


import app.model.Game;
import app.service.GameService;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private GameService gameService;
    
    @PostMapping("/newGame")
	public ResponseEntity<Game> newgameProcess(Game game, MultipartFile imageField, List<MultipartFile> imageFields) throws IOException, SQLException{
		updateImageGame(game, imageField);
		updateGameplayImages(game, imageFields);
		gameService.save(game);
		return new ResponseEntity<>(game, HttpStatus.CREATED);
	}

    @DeleteMapping("/deleteGame/{id}")
	public ResponseEntity<Object> deleteGame(@PathVariable long id) {
		Optional<Game> opGame = gameService.findById(id);
        if (opGame.isPresent()){
            Game game = opGame.get();
            game.setDeleted(true);
            gameService.save(game);
            return new ResponseEntity<>(game, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
	}

    @PatchMapping("/editGame/{id}")
	public ResponseEntity<Game> editGame(Game game, MultipartFile imageField, List<MultipartFile> imageFields, @PathVariable long id) throws IOException, SQLException {
		Optional<Game> opGame = gameService.findById(id);
        if (opGame.isPresent()) {
        Game currentGame = opGame.get();
		updateImageGame(currentGame, imageField);
		updateGameplayImages(currentGame, imageFields);
		currentGame.editGame(game);
		gameService.save(currentGame);
		return new ResponseEntity<>(game, HttpStatus.OK);
    }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
	}

    private void updateGameplayImages(Game game, List<MultipartFile> imageFields) {
		if (imageFields!= null && !imageFields.get(0).getOriginalFilename().equals("") && !imageFields.isEmpty()) {
			game.setGameplayImagesFiles(imageFields.stream().map(file -> {
				try {
					return BlobProxy.generateProxy(file.getInputStream(), file.getSize());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}).collect(Collectors.toList()));
			game.setGameplayImages(imageFields.stream().map(file -> "").collect(Collectors.toList()));
		}else{
			Game dbGame = gameService.findById(game.getId()).orElseThrow();
			game.setGameplayImagesFiles( dbGame.getGameplayImagesFiles());			
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
