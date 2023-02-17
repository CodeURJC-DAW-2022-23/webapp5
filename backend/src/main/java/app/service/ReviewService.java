package app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
}
