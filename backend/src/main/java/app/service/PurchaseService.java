package app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import app.email.EmailDetails;
import app.email.EmailServiceImpl;
import app.model.Game;
import app.model.Purchase;
import app.model.User;
import app.repository.PurchaseRepository;
import app.repository.UserRepository;

@Service
public class PurchaseService {
	@Autowired
	private PurchaseRepository purchases;

	@Autowired
	private UserRepository users;

	@Autowired
	private EmailServiceImpl emailService;

	public void save(Purchase purchase) {
		purchases.save(purchase);
	}

	public boolean hasUserBoughtGame(User user, Long gameId) {
		return purchases.hasUserBoughtGame(user, gameId);
	}

	public Optional<Purchase> findById(long id) {
		return purchases.findById(id);
	}

	public void deleteById(long id) {
		purchases.deleteById(id);
	}

	public List<Game> purchasedGamesByUser(User user) {
		return purchases.findGamesByUser(user);
	}

	public int numberOfGames(User user) {
		return purchasedGamesByUser(user).size();
	}

	public ResponseEntity<Object> checkoutProcess(User requestUser, String billing_street,
			String billing_apartment, String billing_city, String billing_country, String billing_postcode,
			String billing_phone, long userId) {
		Optional<User> userPrincipal = users.findById(userId);
		if (userPrincipal.isPresent()) {
			User user = userPrincipal.get();
			if (!user.equals(requestUser)) {
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
			purchases.save(purchase);
			user.purchase();
			users.save(user);
			EmailDetails emailDetails = new EmailDetails(user.getMail(), "Thank you for your purchase!");
			emailDetails.generatePurchaseMessage(purchase, user);
			emailService.sendSimpleMail(emailDetails);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
