package app.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.PageRequest;

import app.model.Game;
import app.model.User;
import app.service.PurchaseService;
import app.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private PurchaseService purchaseService;

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
	}

	@GetMapping("/profile/{id}")
	public String profile(Model model, @PathVariable long id) {
		User user;
		try {
			user = userService.findById(id).orElseThrow();
		} catch (Exception e) {
			return "redirect:/error";
		}
		if (user.getId().equals(currentUser.getId())) {
			model.addAttribute("user", user);
			List<Game> purchasedGames = purchaseService.purchasedGamesByUser(user);
			model.addAttribute("myGames", purchasedGames);
			model.addAttribute("haveGames", !purchasedGames.isEmpty());
			model.addAttribute("gamesNumber", purchasedGames.size());
			return "user-profile";
		}

		return "redirect:/error";
	}

	@GetMapping("/{id}/imageProfile")
	public ResponseEntity<Resource> downloadImageProfile(@PathVariable long id) throws SQLException {
		return userService.downloadImageProfile(id);
	}

	@GetMapping("/editProfile/{id}")
	public String editProfile(Model model, @PathVariable long id) {
		User user = userService.findById(id).orElseThrow();
		if (user.getId().equals(currentUser.getId())) {
			model.addAttribute("user", user);
			return "editProfile";
		}
		return "redirect:/error";
	}

	@PostMapping("/editProfile")
	public String editProfileProcess(Model model, User newUser, @RequestParam MultipartFile imageField)
			throws IOException, SQLException {
		ResponseEntity<Object> editUser = userService.editProfile(currentUser.getId(), newUser, currentUser,
				imageField);
		if (editUser.getStatusCode().is2xxSuccessful()) {
			return "redirect:/profile/" + currentUser.getId();
		} else {
			return "redirect:/error";
		}
	}
}