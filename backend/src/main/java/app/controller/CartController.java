package app.controller;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import app.model.Game;
import app.model.User;
import app.service.GameService;
import app.service.UserService;

@Controller
public class CartController {
	
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
			model.addAttribute("user", currentUser);
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
		} else {
			model.addAttribute("logged", false);
		}
	}

    @GetMapping("/addToCart/{id}")
	public String addCart(Model model, @PathVariable long id) {
		try{
			Game game = gameService.findById(id).orElseThrow();
            currentUser.addGameToCart(game);
            userService.save(currentUser);
            model.addAttribute("game", game);
            return "redirect:/game/{id}";
		}catch(Exception e){
			return "redirect:/error";
		}
	}
    
    @GetMapping("/deleteFromCart/{id}")
	public String deleteCart(Model model, @PathVariable long id) {
		try{
			Game game = gameService.findById(id).orElseThrow();
            currentUser.removeGameFromCart(game);
            model.addAttribute("game", game);
            userService.save(currentUser);
            return "redirect:/";
		}catch(Exception e){
			return "redirect:/error";
		}
	}


}
