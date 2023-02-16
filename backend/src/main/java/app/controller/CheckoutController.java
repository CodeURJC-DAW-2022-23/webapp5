package app.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import app.model.Game;
import app.model.User;
import app.service.GameService;
import app.service.PurchaseService;
import app.service.UserService;

@Controller
public class CheckoutController {
	
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
			model.addAttribute("userName", currentUser.getName());
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
		} else {
			model.addAttribute("logged", false);
		}
	}
    @GetMapping("/{userId}/checkout")
    public String checkout(Model model,@PathVariable long userId) {
        try{
            User user = userService.findById(userId).orElseThrow();
            if (!user.getId().equals(currentUser.getId())){
                throw new Exception();
            } 
            currentUser.getCart();
            userService.save(currentUser);
            return "redirect:/";
        } catch(Exception e){
            return "redirect:/error";
        }
	}

    @RequestMapping("/{userId}/checkout")

    private void getDataCheckout(User user){
        if(user.getBillingInformation().equals(null)){

        }
    }
}