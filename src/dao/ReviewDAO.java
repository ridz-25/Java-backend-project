package src.dao;

import java.util.List;
import java.util.Optional;

import src.model.Review;

public interface ReviewDAO {

    boolean saveReview(Review review);

    Optional<Review> getReviewById(int reviewId);

    List<Review> getReviewsByGig(int gigId);

    List<Review> getReviewsBySeller(int sellerId);

    boolean updateReview(Review review);

    boolean deleteReview(int reviewId);

    double calculateAverageRating(int gigId);
}