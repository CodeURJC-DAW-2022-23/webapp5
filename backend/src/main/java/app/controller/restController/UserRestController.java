package app.controller.restController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import app.model.Game;
import app.model.User;
import app.model.modelRest.UserProfile;
import app.service.PurchaseService;
import app.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private PurchaseService purchaseService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/me")
	public ResponseEntity<UserProfile> profile(HttpServletRequest request) {
		Optional<User> userPrincipal = userService.findByMail(request.getUserPrincipal().getName());
		if (userPrincipal.isPresent()) {
			User user = userPrincipal.get();
			List<Game> games = purchaseService.purchasedGamesByUser(user);
			UserProfile userProfile = new UserProfile(user, games);
			return new ResponseEntity<>(userProfile, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

    @GetMapping("/{id}/imageProfile")
	public ResponseEntity<Object> downloadImageProfile(@PathVariable long id) throws SQLException {
		Optional<User> user = userService.findById(id);
		if (user.isPresent() && user.get().getProfilePirctureFile() != null) {
			Resource file = new InputStreamResource(user.get().getProfilePirctureFile().getBinaryStream());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").contentLength(user.get().getProfilePirctureFile().length()).body(file);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/register")
	public ResponseEntity<Object> register(@RequestBody User user) throws IOException {
		if (!userService.existMail(user.getMail())) {
			Resource image = new ClassPathResource("/static/images/avatar.png");
			user.setProfilePirctureFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
			user.setProfilePircture("/static/images/avatar.png");
			user.setEncodedPassword(passwordEncoder.encode(user.getEncodedPassword()));
			user.setRoles("USER");
			if (user.getAboutMe() == null){
				user.setAboutMe("");
			}
			userService.save(user);
			return new ResponseEntity<>(user, HttpStatus.CREATED);
		} else{
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
	}

	@PostMapping("/editImage")
	public ResponseEntity<Object> editImage(@RequestParam MultipartFile imageFile, HttpServletRequest request) throws IOException {
		Optional<User> userPrincipal = userService.findByMail(request.getUserPrincipal().getName());
		
		if (userPrincipal.isPresent() | imageFile != null) {
			User user = userPrincipal.get();
				user.setProfilePirctureFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
				userService.save(user);
				return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("/edit")
	public ResponseEntity<Object> editProfile(User newUser, HttpServletRequest request) throws IOException, SQLException {
		Optional<User> userPrincipal = userService.findByMail(request.getUserPrincipal().getName());
		if (userPrincipal.isPresent()){
			User user = userPrincipal.get();
			if (newUser.getAboutMe() != null){
				user.setAboutMe(newUser.getAboutMe());
			}
			user.setName(newUser.getName());
			user.setLastName(newUser.getLastName());
			if (newUser.getEncodedPassword() != null){
				user.setEncodedPassword(passwordEncoder.encode(newUser.getEncodedPassword()));
			}
			userService.save(user);
			return new ResponseEntity<>(user, HttpStatus.OK);
	}else{
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}


}
