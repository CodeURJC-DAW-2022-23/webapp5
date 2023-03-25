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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
    @Operation(summary = "Get a page of games")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the games",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = Game.class)) }),
        @ApiResponse(responseCode = "404", description = "Games not found",
            content = @Content) })
    @GetMapping("/")
    public ResponseEntity<Page<Game>> getGames() {
        Page<Game> findGames = gameService.findGames(PageRequest.of(0, 6));
        if (findGames.getNumberOfElements() > 0) {
            return new ResponseEntity<>(findGames, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "Get more games")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the games",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = Game.class)) }),
        @ApiResponse(responseCode = "404", description = "Games not found",
            content = @Content) })
    @GetMapping("/moreIndexGames/{page}")
    public Page<Game> getMoreIndexGames(@PathVariable int page) {
        return gameService.getMoreIndexGames(page);
    }
    @Operation(summary = "Get a page of games by name or category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the games by name or category",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = Game.class)) }),
        @ApiResponse(responseCode = "404", description = "Games not found",
            content = @Content) })
    @GetMapping("/search")
    public ResponseEntity<Page<Game>> search(String name, String category) {
        Page<Game> searchGames = gameService.getSearchGames(0, name, category);
        if (searchGames.getNumberOfElements() > 0) {
            return new ResponseEntity<>(searchGames, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "Get more games by name or category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the games by name or category",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = Game.class)) }),
        @ApiResponse(responseCode = "404", description = "Games not found",
            content = @Content) })
    @GetMapping("/moreFoundGames/{page}")
    public Page<Game> getMoreFoundGames(@Parameter(description= "page of games")@PathVariable int page, String category, String name) {
        return gameService.getSearchGames(page, name, category);
    }
    @Operation(summary = "Creates a new game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Game created", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class)) }) })
    // Creates a game
    @PostMapping("/")
    public ResponseEntity<Game> newGameProcess(Game game, MultipartFile imageField, List<MultipartFile> imageFields)
            throws IOException, SQLException {
        Game saveNewGame = gameService.saveNewGame(game, imageField, imageFields);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(game.getId()).toUri();
        return ResponseEntity.created(location).body(saveNewGame);
    }
    @Operation(summary = "Get a game by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the game",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = GameDetails.class)) }),
        @ApiResponse(responseCode = "404", description = "Game not found",
            content = @Content),
        @ApiResponse(responseCode = "403", description = "forbiden o dont have permissions",
            content = @Content)})

    @GetMapping("/{id}")
    public ResponseEntity<GameDetails> gameDetails( @Parameter (description = "id of the game")@PathVariable long id) {
        Optional<Game> opGame = gameService.findById(id);
        if (opGame.isPresent()) {

            Game game = opGame.get();
            GameDetails gameDetails = new GameDetails(game, reviewService.findByGame(game, PageRequest.of(0, 6)));
            return new ResponseEntity<>(gameDetails, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @Operation(summary = "Delete a game")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Game deleted",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = Game.class)) }),
        @ApiResponse(responseCode = "404", description = "Game not found",
            content = @Content) })
    // Delete a game
    @DeleteMapping("/{id}")
    public ResponseEntity<Game> deleteGame(@Parameter (description = "id of the game")@PathVariable long id) {
        return gameService.deleteById(id);
    }
    @Operation(summary = "Edit a game")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Game edited",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = Game.class)) }),
        @ApiResponse(responseCode = "404", description = "Game not found",
            content = @Content) })

    // Edit a game
    @PutMapping("/{id}")
    public ResponseEntity<Game> editGame(Game game, MultipartFile imageField, List<MultipartFile> imageFields,
    @Parameter (description = "id of the game")@PathVariable long id) throws IOException, SQLException {
        return gameService.editGame(id, game, imageField, imageFields);
    }
    @Operation(summary = "Get the cover image of a game")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the cover image",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = Game.class)) }),
        @ApiResponse(responseCode = "404", description = "Cover not found",
            content = @Content) })
    @GetMapping("/{id}/coverImage")
    public ResponseEntity<Resource> downloadImageProfile( @Parameter (description = "id of the game")@PathVariable long id) throws SQLException {
        return gameService.downloadImageProfile(id);
    }
    @Operation(summary = "Get the gameplay images of a game")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the gameplay images",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = Game.class)) }),
        @ApiResponse(responseCode = "404", description = "Gameplay images not found",
            content = @Content) })
    @GetMapping("/{id}/gameplayImage/{index}")
    public ResponseEntity<Resource> downloadGameplayImages(@Parameter (description = "id of the game")@PathVariable long id, @Parameter (description = "index of the gameplay image")@PathVariable int index)
            throws SQLException {
        return gameService.downloadGameplayImages(id, index);
    }
}