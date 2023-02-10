package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.model.Review;
import app.repository.ReviewRepository;

@Service
public class ReviewService {
    @Autowired
	private ReviewRepository reviews;

	public void save(Review review) {
		reviews.save(review);
	}

	

}
