package app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import app.model.Game;
import app.model.Review;
import app.model.User;
import app.repository.GameRepository;
import app.repository.ReviewRepository;
import app.repository.UserRepository;

@Service
public class ReviewService {
	@Autowired
	private ReviewRepository reviews;

	@Autowired
	private GameRepository games;

	@Autowired
	private UserRepository users;

	@Autowired
	private PurchaseService purchaseService;

	public void save(Review review) {
		reviews.save(review);
	}

	public Optional<Review> findById(long id) {
		return reviews.findById(id);
	}

	public void deleteById(long id) {
		reviews.deleteById(id);
	}

	public boolean reviewedByUser(User user, Game game) {
		return reviews.findByUserAndGame(user, game).isPresent();
	}

	public Page<Review> findByGame(Game game, Pageable pageable) {
		return reviews.findByGame(game, pageable);
	}

	public Page<Review> findByGameAndNotUser(Game game, User user, Pageable pageable) {
		return reviews.findByGameAndNotUser(game, user, pageable);
	}

	public int countByGame(Game game) {
		return reviews.countByGame(game);
	}

	public int countByGameAndNotUser(Game game, User user) {
		return reviews.countByGameAndNotUser(game, user);
	}

	public List<Review> findByUserInGame(User user, Game game) {
		ArrayList<Review> list = new ArrayList<Review>();
		if (reviews.findByUserAndGame(user, game).isPresent()) {
			list.add(reviews.findByUserAndGame(user, game).get());
		}
		return list;
	}

	public Page<Review> getReviews(int page, long id, User currentUser) {
		Game game = games.findById(id).get();
		if (page <= (int) Math.ceil(reviews.countByGameAndNotUser(game, currentUser) / 6)) {
			return reviews.findByGameAndNotUser(game, currentUser, PageRequest.of(page, 6));
		}
		return null;
	}

	public ResponseEntity<Review> addReview(long gameId, User currentUser, long userId, String comment,
			int reviewRate) {
		Optional<Game> opGame = games.findById(gameId);
		Optional<User> opUser = users.findById(userId);
		if (opGame.isPresent() && opUser.isPresent()) {
			Game game = opGame.get();
			User user = opUser.get();
			if (!user.getId().equals(currentUser.getId()) | !purchaseService.hasUserBoughtGame(user, gameId)
					| this.reviewedByUser(user, game)) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			Review review = new Review(user, game, reviewRate, comment);
			game.addReview(review);
			games.save(game);

			return new ResponseEntity<>(review, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	public ResponseEntity<Review> deleteReview(long reviewId, User user) {
		Optional<Review> opReview = reviews.findById(reviewId);
		if (opReview.isPresent()) {
			Review review = opReview.get();
			if (!user.getId().equals(review.getUser().getId()) && !user.getRoles().contains("ADMIN")) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			Game game = review.getGame();
			game.deleteReview(review);
			games.save(game);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
