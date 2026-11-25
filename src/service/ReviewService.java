
package src.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import src.model.Review;
import src.model.Rating;
import src.model.Order;
import src.model.OrderStatus;
import src.model.Buyer;
import src.model.User;
import src.model.Gig;
import src.util.ValidationUtil;
import src.dto.RatingSummaryDTO;

public class ReviewService {

    // In-memory storage
    private ArrayList<Review> reviews = new ArrayList<>();
    private ArrayList<Rating> ratings = new ArrayList<>();

    // Automatic ID generation
    private int nextReviewId = 1;
    private int nextRatingId = 1;

    // Shared services
    private UserService userService;
    private OrderService orderService;
    private GigService gigService;

    public ReviewService(
            UserService userService,
            OrderService orderService,
            GigService gigService) {

        this.userService = userService;
        this.orderService = orderService;
        this.gigService = gigService;
    }

    // Add Review
    public Review addReview(
            int orderId,
            int buyerId,
            int rating,
            String comment) {

        User user = userService.getUserById(buyerId);

        if (!(user instanceof Buyer) || !user.isActive()) {
            throw new IllegalArgumentException(
                "Only active buyers can add reviews."
            );
        }

        Order order = orderService.getOrderById(orderId);

        if (order == null ||
            order.getBuyerId() != buyerId ||
            order.getStatus() != OrderStatus.COMPLETED) {

            throw new IllegalArgumentException(
                "Review can only be added to your completed order."
            );
        }

        if (!ValidationUtil.isValidRating(rating)) {
            throw new IllegalArgumentException(
                "Rating must be between 1 and 5."
            );
        }

        for (Review existingReview : reviews) {
            if (existingReview.getOrderId() == orderId) {
                throw new IllegalArgumentException(
                    "This order already has a review."
                );
            }
        }

        Timestamp currentTime =
            new Timestamp(System.currentTimeMillis());

        Review review = new Review(
            nextReviewId++,
            orderId,
            order.getGigId(),
            buyerId,
            rating,
            comment == null ? "" : comment.trim(),
            currentTime
        );

        reviews.add(review);

        updateGigRating(order.getGigId());

        return review;
    }

    // Get Review by ID
    public Review getReviewById(int reviewId) {

        for (Review review : reviews) {
            if (review.getReviewId() == reviewId) {
                return review;
            }
        }

        return null;
    }

    // Get all Reviews
    public List<Review> getAllReviews() {
        return new ArrayList<>(reviews);
    }

    // Get Reviews by Gig
    public List<Review> getReviewsByGig(int gigId) {

        ArrayList<Review> result = new ArrayList<>();

        for (Review review : reviews) {
            if (review.getGigId() == gigId) {
                result.add(review);
            }
        }

        return result;
    }

    // Get Reviews by Buyer
    public List<Review> getReviewsByBuyer(int buyerId) {

        ArrayList<Review> result = new ArrayList<>();

        for (Review review : reviews) {
            if (review.getReviewerId() == buyerId) {
                result.add(review);
            }
        }

        return result;
    }

    // Update Review
    public boolean updateReview(
            int reviewId,
            int buyerId,
            int newRating,
            String newComment) {

        Review review = getReviewById(reviewId);

        if (review == null ||
            review.getReviewerId() != buyerId ||
            !ValidationUtil.isValidRating(newRating)) {

            return false;
        }

        review.setRating(newRating);
        review.setComment(
            newComment == null ? "" : newComment.trim()
        );

        updateGigRating(review.getGigId());

        return true;
    }

    // Delete Review
    public boolean deleteReview(int reviewId, int buyerId) {

        Review review = getReviewById(reviewId);

        if (review == null ||
            review.getReviewerId() != buyerId) {

            return false;
        }

        int gigId = review.getGigId();

        reviews.remove(review);

        updateGigRating(gigId);

        return true;
    }

    // Get Rating Summary
    public Rating getRatingByGig(int gigId) {

        for (Rating rating : ratings) {
            if (rating.getGigId() == gigId) {
                return rating;
            }
        }

        return null;
    }

    // Get Rating Summary using DTO
    public RatingSummaryDTO getRatingSummaryByGig(int gigId) {

        int totalReviews = 0;
        int totalRating = 0;

        int fiveStarCount = 0;
        int fourStarCount = 0;
        int threeStarCount = 0;
        int twoStarCount = 0;
        int oneStarCount = 0;

        for (Review review : reviews) {

            if (review.getGigId() == gigId) {

                totalReviews++;
                totalRating += review.getRating();

                switch (review.getRating()) {

                    case 5:
                        fiveStarCount++;
                        break;

                    case 4:
                        fourStarCount++;
                        break;

                    case 3:
                        threeStarCount++;
                        break;

                    case 2:
                        twoStarCount++;
                        break;

                    case 1:
                        oneStarCount++;
                        break;
                }
            }
        }

        double averageRating = totalReviews == 0
            ? 0.0
            : (double) totalRating / totalReviews;

        return new RatingSummaryDTO(
            averageRating,
            totalReviews,
            fiveStarCount,
            fourStarCount,
            threeStarCount,
            twoStarCount,
            oneStarCount
        );  
    }

    // Automatically calculate average rating
    private void updateGigRating(int gigId) {

        int totalReviews = 0;
        int totalRating = 0;

        for (Review review : reviews) {
            if (review.getGigId() == gigId) {
                totalReviews++;
                totalRating += review.getRating();
            }
        }

        double averageRating = totalReviews == 0
            ? 0.0
            : (double) totalRating / totalReviews;

        Rating rating = getRatingByGig(gigId);

        if (rating == null) {

            rating = new Rating(
                nextRatingId++,
                gigId,
                averageRating,
                totalReviews
            );

            ratings.add(rating);

        } else {

            rating.setAverageRating(averageRating);
            rating.setTotalReviews(totalReviews);
        }

        Gig gig = gigService.getGigById(gigId);

        if (gig != null) {
            gig.updateRating(averageRating, totalReviews);
        }
    }
}
