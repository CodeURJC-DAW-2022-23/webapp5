package app.controller;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import app.model.Purchase;
import app.model.User;
import app.service.GameService;
import app.service.PurchaseService;
import app.service.UserService;

@Controller
public class CheckoutController {
	
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
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
		} else {
			model.addAttribute("logged", false);
		}
	}
    @GetMapping("/checkout/{id}")
    public String cartCheckout(Model model,@PathVariable long id) {
        try{
            User user = userService.findById(id).orElseThrow();
            if (!user.getId().equals(currentUser.getId())){
                throw new Exception();
            } 
            if (currentUser.getCart().isEmpty()){
                throw new Exception();
            }
            model.addAttribute("hasBillingInformation", !currentUser.getBillingInformation().equals(""));
            return "checkout";
        } catch(Exception e){
            return "redirect:/error";
        }
	}

    @PostMapping("/checkout/{id}")
    public String checkoutProcess(Model model,@PathVariable long id,String billing_street,String billing_apartment,String billing_city,String billing_country,String billing_postcode,String billing_phone) {
        try{
            User user = userService.findById(id).orElseThrow();
            if (!user.getId().equals(currentUser.getId())){
                throw new Exception();
            }
            if (user.getBillingInformation().equals("")){
                String billingInfo =  billing_street + " " +
                billing_apartment + ", " + billing_city + billing_country + ", " + billing_postcode + ", "
                 + billing_phone;
                user.setBillingInformation(billingInfo);
            }
            Purchase purchase = new Purchase(user.getCart(), user);
            purchaseService.save(purchase);
            user.purchase();
            userService.save(user);
            return "redirect:/";
        } catch(Exception e){
            return "redirect:/error";
        }
	}
}
