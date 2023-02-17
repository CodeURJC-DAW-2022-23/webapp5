package app.controller;

import java.io.IOException;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
    public String cartCheckout(Model model,@PathVariable long userId) {
        try{
            User user = userService.findById(userId).orElseThrow();
            if (!user.getId().equals(currentUser.getId())){
                throw new Exception();
            } 
            if (currentUser.getCart().isEmpty()){
                throw new Exception();
            } else if (currentUser.getBillingInformation() != null){
                return "checkout";
            } else {
                userService.save(currentUser);
                return "redirect:/";
            }

            
        } catch(Exception e){
            return "redirect:/error";
        }
	}

    @PostMapping("/{userId}/checkout")
    public String checkoutProcess(Model model,@PathVariable long userId,String billing_firstname,String billing_lastname,String billing_street,String billing_apartment,String billing_city,String billing_country,String billing_postcode,String billing_email,String billing_phone,String billing_aditionalinfo) {
        try{
            User user = userService.findById(userId).orElseThrow();
            if (!user.getId().equals(currentUser.getId())){
                throw new Exception();
            } 
            String billingInfo = billing_firstname + billing_lastname + billing_street + 
            billing_apartment + billing_city + billing_country + billing_postcode + 
            billing_email + billing_phone + billing_aditionalinfo;
            user.setBillingInformation(billingInfo);
            return "redirect:/";
        } catch(Exception e){
            return "redirect:/error";
        }
			
	}
}
