package app.controller.passed;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.model.Game;
import app.service.GameService;

@RestController
@RequestMapping("/api/search")
public class SearchRestController {

    @Autowired
    private GameService gameService;

    @GetMapping("/")
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
}
