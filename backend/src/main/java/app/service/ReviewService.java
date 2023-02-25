package app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import app.model.Game;
import app.model.Review;
import app.model.User;
import app.repository.ReviewRepository;

@Service
public class ReviewService {
    @Autowired
	private ReviewRepository reviews;

	public void save(Review review) {
		reviews.save(review);
	}

	public List<Review> findAll() {
		return reviews.findAll();
	}

	public Optional<Review> findById(long id) {
		return reviews.findById(id);
	}

	public void deleteById(long id) {
		reviews.deleteById(id);
	}
	
	public boolean reviewedByUser(User user, Game game){
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
		if (reviews.findByUserAndGame(user, game).isPresent()){
			list.add(reviews.findByUserAndGame(user, game).get());
		}
		return list;
	}
}
