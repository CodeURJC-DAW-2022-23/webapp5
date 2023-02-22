package app.controller.restController;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.Email.EmailDetails;
import app.Email.EmailServiceImpl;
import app.model.Game;
import app.model.Purchase;
import app.model.User;
import app.service.GameService;
import app.service.PurchaseService;
import app.service.UserService;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private EmailServiceImpl emailService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Game>> cart(HttpServletRequest request, @PathVariable long userId) {
        Optional<User> userPrincipal = userService.findByMail(request.getUserPrincipal().getName());
        if (userPrincipal.isPresent()) {
            User user = userPrincipal.get();
            User requestUser = userService.findById(userId).orElseThrow();
			if (!user.equals(requestUser)){
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
            return new ResponseEntity<>(userService.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addGame/{id}/{userId}")
    public ResponseEntity<Object> addCart(HttpServletRequest request, @PathVariable long id, @PathVariable long userId) {
        Optional<User> userPrincipal = userService.findByMail(request.getUserPrincipal().getName());
        if (userPrincipal.isPresent()) {
            User user = userPrincipal.get();
            User requestUser = userService.findById(userId).orElseThrow();
			if (!user.equals(requestUser)){
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
            Optional<Game> opGame = gameService.findById(id);
            if (!opGame.isPresent()) {
                return new ResponseEntity<>(userService.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
                        HttpStatus.NOT_FOUND);
            }
            Game game = opGame.get();
            if (user.getCart().contains(game) || game.getDeleted()
                    || purchaseService.hasUserBoughtGame(user, game.getId())) {
                return new ResponseEntity<>(userService.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
                        HttpStatus.FORBIDDEN);
            }
            user.addGameToCart(game);
            userService.save(user);
            return new ResponseEntity<>(userService.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteGame/{id}/{userId}")
    public ResponseEntity<Object> deleteCart(HttpServletRequest request, @PathVariable long id, @PathVariable long userId) {
        Optional<User> userPrincipal = userService.findByMail(request.getUserPrincipal().getName());
        if (userPrincipal.isPresent()) {
            User user = userPrincipal.get();
            User requestUser = userService.findById(userId).orElseThrow();
			if (!user.equals(requestUser)){
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
            Optional<Game> opGame = gameService.findById(id);
            if (!opGame.isPresent()) {
                return new ResponseEntity<>(userService.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
                        HttpStatus.NOT_FOUND);
            }
            Game game = opGame.get();
            if (!user.getCart().contains(game)) {
                return new ResponseEntity<>(userService.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
                        HttpStatus.NOT_FOUND);
            }
            user.removeGameFromCart(game);
            userService.save(user);
            return new ResponseEntity<>(userService.findGamesInCartByUserId(user.getId(), PageRequest.of(0, 3)),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<Object> checkoutProcess(HttpServletRequest request, String billing_street,
            String billing_apartment, String billing_city, String billing_country, String billing_postcode,
            String billing_phone, @PathVariable long userId) {
        Optional<User> userPrincipal = userService.findByMail(request.getUserPrincipal().getName());
        if (userPrincipal.isPresent()) {
            User user = userPrincipal.get();
            User requestUser = userService.findById(userId).orElseThrow();
			if (!user.equals(requestUser)){
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
            if (user.getCart().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            if (user.getBillingInformation().equals("")) {
                String billingInfo = billing_street + " " +
                        billing_apartment + ", " + billing_city + billing_country + ", " + billing_postcode + ", "
                        + billing_phone;
                user.setBillingInformation(billingInfo);
            }
            Purchase purchase = new Purchase(user.getCart(), user);
            purchaseService.save(purchase);
            user.purchase();
            userService.save(user);
            EmailDetails emailDetails = new EmailDetails(user.getMail(), "Thank you for your purchase!");
            emailDetails.generatePurchaseMessage(purchase, user);
            emailService.sendSimpleMail(emailDetails);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
