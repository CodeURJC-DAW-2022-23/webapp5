package app.controller;



import java.security.Principal;
import java.sql.SQLException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

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

		if(principal != null) {
			userService.findByMail(principal.getName()).ifPresent(u -> currentUser = u);
			model.addAttribute("logged", true);
			model.addAttribute("userName", currentUser.getName());
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
		} else {
			model.addAttribute("logged", false);
		}
	}

	@GetMapping("/profile/{id}")
	public String profile(Model model, @PathVariable long id) {
		User user = userService.findById(id).orElseThrow();
		if (user.getId().equals(currentUser.getId())) {
            model.addAttribute("user", user);
            model.addAttribute("gamesNumber", purchaseService.numberOfGames(user));
			return "user-profile";
		}

		return "redirect:/error";
	}

    @GetMapping("/{id}/imageProfile")
	public ResponseEntity<Resource> downloadImageProfile(@PathVariable long id) throws SQLException {
		Optional<User> user = userService.findById(id);
		if (user.isPresent() && user.get().getProfilePirctureFile() != null) {
			Resource file = new InputStreamResource(user.get().getProfilePirctureFile().getBinaryStream());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").contentLength(user.get().getProfilePirctureFile().length()).body(file);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}