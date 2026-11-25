
package src.service;

import src.dto.ProfileMetricsDTO;
import src.model.Gig;
import src.model.GigStatus;
import src.model.Order;
import src.model.OrderStatus;
import src.model.Review;

public class SellerMetricsService {

    private GigService gigService;
    private OrderService orderService;
    private ReviewService reviewService;

    // Constructor injection
    public SellerMetricsService(
            GigService gigService,
            OrderService orderService,
            ReviewService reviewService) {

        this.gigService = gigService;
        this.orderService = orderService;
        this.reviewService = reviewService;
    }

    // Calculate total gigs of a seller
    private int getTotalGigs(int sellerId) {

        return gigService.getGigsBySeller(sellerId).size();
    }

    // Calculate active gigs of a seller
    private int getActiveGigs(int sellerId) {

        int count = 0;

        for (Gig gig : gigService.getGigsBySeller(sellerId)) {

            if (gig.getStatus() == GigStatus.ACTIVE) {
                count++;
            }
        }

        return count;
    }

    // Calculate completed orders of a seller
    private int getCompletedOrders(int sellerId) {

        int count = 0;

        for (Order order : orderService.getOrdersBySeller(sellerId)) {

            if (order.getStatus() == OrderStatus.COMPLETED) {
                count++;
            }
        }

        return count;
    }

    // Calculate total earnings of a seller
    private double getTotalEarnings(int sellerId) {

        double totalEarnings = 0.0;

        for (Order order : orderService.getOrdersBySeller(sellerId)) {

            if (order.getStatus() == OrderStatus.COMPLETED) {
                totalEarnings += order.getAmount();
            }
        }

        return totalEarnings;
    }

    // Calculate total reviews received by a seller
    private int getTotalReviews(int sellerId) {

        int count = 0;

        for (Gig gig : gigService.getGigsBySeller(sellerId)) {

            count += reviewService
                .getReviewsByGig(gig.getGigId())
                .size();
        }

        return count;
    }

    // Calculate average rating across all seller gigs
    private double getAverageRating(int sellerId) {

        int totalRating = 0;
        int totalReviews = 0;

        for (Gig gig : gigService.getGigsBySeller(sellerId)) {

            for (Review review :
                    reviewService.getReviewsByGig(gig.getGigId())) {

                totalRating += review.getRating();
                totalReviews++;
            }
        }

        return totalReviews == 0
            ? 0.0
            : (double) totalRating / totalReviews;
    }

    // Generate seller profile metrics DTO
    public ProfileMetricsDTO getSellerMetrics(int sellerId) {

        return new ProfileMetricsDTO(
            getTotalGigs(sellerId),
            getActiveGigs(sellerId),
            getCompletedOrders(sellerId),
            getTotalReviews(sellerId),
            getAverageRating(sellerId),
            getTotalEarnings(sellerId)
        );
    }
}
