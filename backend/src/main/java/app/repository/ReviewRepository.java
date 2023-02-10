package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}