package app.controller;



import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

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

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
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

	@GetMapping("/profile/{id}")
	public String profile(Model model, @PathVariable long id) {
		User user;
		try{
			user = userService.findById(id).orElseThrow();
		}catch(Exception e){
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
		Optional<User> user = userService.findById(id);
		if (user.isPresent() && user.get().getProfilePirctureFile() != null) {
			Resource file = new InputStreamResource(user.get().getProfilePirctureFile().getBinaryStream());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").contentLength(user.get().getProfilePirctureFile().length()).body(file);
		} else {
			return ResponseEntity.notFound().build();
		}
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
	public String editProfileProcess(Model model, User newUser, MultipartFile imageField) throws IOException, SQLException {
		updateImageProfile(currentUser, imageField);
		currentUser.setName(newUser.getName());
		System.out.println("Contrasena" + newUser.getEncodedPassword());
		if (!newUser.getEncodedPassword().equals("")){
			currentUser.setEncodedPassword(passwordEncoder.encode(newUser.getEncodedPassword()));
		}
		currentUser.setAboutMe(newUser.getAboutMe());
		userService.save(currentUser);
		return "redirect:/profile/" + currentUser.getId();
	}

	private void updateImageProfile(User user, MultipartFile imageField) throws IOException, SQLException {
		if (!imageField.isEmpty()) {
			user.setProfilePirctureFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
		} else {
			User dbUser = userService.findById(user.getId()).orElseThrow();
			if (dbUser.getProfilePircture() != null) {
				user.setProfilePirctureFile(BlobProxy.generateProxy(dbUser.getProfilePirctureFile().getBinaryStream(), dbUser.getProfilePirctureFile().length()));
			}
		}
	}
}