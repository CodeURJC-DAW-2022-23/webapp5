package app.controller.restController;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

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
    public ResponseEntity<Page<Game>> getGames() {
        Page<Game> findGames = gameService.findGames(PageRequest.of(0, 6));
        if (findGames.getNumberOfElements() > 0) {
            return new ResponseEntity<>(findGames, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/moreIndexGames/{page}")
    public Page<Game> getMoreIndexGames(@PathVariable int page) {
        return gameService.getMoreIndexGames(page);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Game>> search(String name, String category) {
        Page<Game> searchGames = gameService.getSearchGames(0, name, category);
        if (searchGames.getNumberOfElements() > 0) {
            return new ResponseEntity<>(searchGames, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/moreFoundGames/{page}")
    public Page<Game> getMoreFoundGames(@PathVariable int page, String category, String name) {
        return gameService.getSearchGames(page, name, category);
    }

    // Creates a game
    @PostMapping("/")
    public ResponseEntity<Game> newGameProcess(Game game, MultipartFile imageField, List<MultipartFile> imageFields)
            throws IOException, SQLException {
        Game saveNewGame = gameService.saveNewGame(game, imageField, imageFields);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(game.getId()).toUri();
        return ResponseEntity.created(location).body(saveNewGame);
    }

}