package app.controller;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.PageRequest;

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
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("emptyCart", currentUser.getCart().isEmpty());
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
			model.addAttribute("userCart", userService.findGamesInCartByUserId(currentUser.getId(), PageRequest.of(0,3)));
			model.addAttribute("moreGamesInCart", currentUser.getCart().size() > 3);
		} else {
			model.addAttribute("logged", false);
		}
		model.addAttribute("popularGames", gameService.findRecomendnoreg(5));
	}

    @GetMapping("/{userId}/addToCart/{id}")
	public String addCart(Model model, @PathVariable long id, @PathVariable long userId) {
		try{
			Game game = gameService.findById(id).orElseThrow();
			User user = userService.findById(userId).orElseThrow();
			if (!user.getId().equals(currentUser.getId())) {
				throw new Exception();
			}
			if(currentUser.getCart().contains(game) || game.getDeleted()) {
				throw new Exception();
			}
            currentUser.addGameToCart(game);
			model.addAttribute("game", game);
            userService.save(currentUser);
            return "redirect:/game/{id}";
		}catch(Exception e){
			return "redirect:/error";
		}
	}

	@GetMapping("/{userId}/deleteFromCart/{id}")
	public String deleteFromCart(Model model, @PathVariable long id, @PathVariable long userId) {
		try{
			Game game = gameService.findById(id).orElseThrow();
			User user = userService.findById(userId).orElseThrow();
			if (!user.getId().equals(currentUser.getId())) {
				throw new Exception();
			}
            currentUser.removeGameFromCart(game);
            userService.save(currentUser);
            return "redirect:/cart/{userId}";
		}catch(Exception e){
			return "redirect:/error";
		}
	}

	@GetMapping("/{userId}/deleteCart/{id}")
	public String deleteCart(Model model, @PathVariable long id, @PathVariable long userId) {
		try{
			Game game = gameService.findById(id).orElseThrow();
			User user = userService.findById(userId).orElseThrow();
			if (!user.getId().equals(currentUser.getId())) {
				throw new Exception();
			}
            currentUser.removeGameFromCart(game);
            userService.save(currentUser);
            return "redirect:/";
		}catch(Exception e){
			return "redirect:/error";
		}
	}

	@GetMapping("/cart/{id}")
	public String cart(Model model, @PathVariable long id) {
		try{
			User user = userService.findById(id).orElseThrow();
			if (!user.getId().equals(currentUser.getId())) {
				throw new Exception();
			}
            return "cart";
		}catch(Exception e){
			return "redirect:/error";
		}
	}


}
