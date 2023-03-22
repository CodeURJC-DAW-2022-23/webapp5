package app.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import app.model.User;
import app.service.GameService;
import app.service.UserService;
import app.model.Game;

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

		if (principal != null) {
			userService.findByMail(principal.getName()).ifPresent(u -> currentUser = u);
			model.addAttribute("logged", true);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("emptyCart", currentUser.getCart().isEmpty());
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
			model.addAttribute("userCart",
					userService.findGamesInCartByUserId(currentUser.getId(), PageRequest.of(0, 3)));
			model.addAttribute("moreGamesInCart", currentUser.getCart().size() > 3);
		} else {
			model.addAttribute("logged", false);
		}
		model.addAttribute("popularGames", gameService.findRecomendNoReg(5));
	}

	@GetMapping("/{userId}/addToCart/{id}")
	public String addCart(Model model, @PathVariable long id, @PathVariable long userId) {
		ResponseEntity<Object> addCart = userService.addCart(currentUser, id, userId);
		if (addCart.getStatusCode().is2xxSuccessful()) {
			return "redirect:/game/{id}";
		} else {
			return "redirect:/error";
		}
	}

	@GetMapping("/{userId}/deleteFromCart/{id}")
	public String deleteFromCart(Model model, @PathVariable long id, @PathVariable long userId) {
		ResponseEntity<Object> deleteCart = userService.deleteCart(currentUser, id, userId);
		if (deleteCart.getStatusCode().is2xxSuccessful()) {
			return "redirect:/cart/{userId}";
		} else {
			return "redirect:/error";
		}
	}

	@GetMapping("/{userId}/deleteCart/{id}")
	public String deleteCart(Model model, @PathVariable long id, @PathVariable long userId) {
		ResponseEntity<Object> deleteCart = userService.deleteCart(currentUser, id, userId);
		if (deleteCart.getStatusCode().is2xxSuccessful()) {
			return "redirect:/";
		} else {
			return "redirect:/error";
		}
	}

	@GetMapping("/cart/{id}")
	public String cart(Model model, @PathVariable long id) {
		ResponseEntity<Page<Game>> cart = userService.cart(currentUser, id);
		if (cart.getStatusCode().is2xxSuccessful()) {
			model.addAttribute("cart", cart.getBody());
			return "cart";
		} else {
			return "redirect:/error";
		}
	}

}
