package app.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import app.model.Game;
import app.model.User;
import app.service.GameService;
import app.service.UserService;

@Controller
public class SearchController {
	
	@Autowired
	private UserService userService;

    @Autowired
    private GameService gameService;
	
	
	User currentUser;

	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest request) {

		Principal principal = request.getUserPrincipal();

		if(principal != null) {
			userService.findByMail(principal.getName()).ifPresent(u -> currentUser = u);
			model.addAttribute("logged", true);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("emptyCart", currentUser.getCart().isEmpty());
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
		} else {
			model.addAttribute("logged", false);
		}
    }

    @GetMapping("/search")
	public String search(Model model, String category) {
        List<Game> gamesFound = gameService.findByCategoryAndName("", category);
        model.addAttribute("games", gamesFound);
        model.addAttribute("lastSearch", "");
        model.addAttribute("found", gamesFound.size() > 0);
		return "search";
	}

    @PostMapping("/search")
    public String search(Model model, String name, String category) {
        List<Game> gamesFound = gameService.findByCategoryAndName(name, category);
        model.addAttribute("games", gamesFound);
        model.addAttribute("found", gamesFound.size() > 0);
        model.addAttribute("lastSearch", name);
        return "search";
    }

}