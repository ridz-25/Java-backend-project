package dao.impl;

import dao.ReviewDAO;
import model.Review;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewDAOImpl implements ReviewDAO {

    @Override
    public boolean saveReview(Review review) {
        String sql = "INSERT INTO reviews (order_id, gig_id, reviewer_id, rating, comment, created_at) " +
                "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (!review.isValidRating()) throw new IllegalArgumentException("Rating must be between 1 and 5");
            ps.setInt(1, review.getOrderId());
            ps.setInt(2, review.getGigId());
            ps.setInt(3, review.getReviewerId());
            ps.setInt(4, review.getRating());
            ps.setString(5, review.getComment());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not save review", e);
        }
    }

    @Override
    public Optional<Review> getReviewById(int reviewId) {
        String sql = "SELECT * FROM reviews WHERE review_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, reviewId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapReview(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not find review", e);
        }
    }

    @Override
    public List<Review> getReviewsByGig(int gigId) {
        return queryReviews("SELECT * FROM reviews WHERE gig_id=? ORDER BY created_at DESC", gigId);
    }

    @Override
    public List<Review> getReviewsBySeller(int sellerId) {
        String sql = "SELECT r.* FROM reviews r JOIN gigs g ON r.gig_id=g.gig_id " +
                "WHERE g.seller_id=? ORDER BY r.created_at DESC";
        List<Review> reviews = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) reviews.add(mapReview(rs));
            }
            return reviews;
        } catch (SQLException e) {
            throw new RuntimeException("Could not retrieve seller reviews", e);
        }
    }

    @Override
    public boolean updateReview(Review review) {
        if (!review.isValidRating()) throw new IllegalArgumentException("Rating must be between 1 and 5");
        String sql = "UPDATE reviews SET rating=?, comment=? WHERE review_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, review.getRating());
            ps.setString(2, review.getComment());
            ps.setInt(3, review.getReviewId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not update review", e);
        }
    }

    @Override
    public boolean deleteReview(int reviewId) {
        String sql = "DELETE FROM reviews WHERE review_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, reviewId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete review", e);
        }
    }

    @Override
    public double calculateAverageRating(int gigId) {
        String sql = "SELECT AVG(rating) AS average_rating FROM reviews WHERE gig_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, gigId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("average_rating");
                return 0.0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not calculate average rating", e);
        }
    }

    private List<Review> queryReviews(String sql, int gigId) {
        List<Review> reviews = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, gigId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) reviews.add(mapReview(rs));
            }
            return reviews;
        } catch (SQLException e) {
            throw new RuntimeException("Could not retrieve reviews", e);
        }
    }

    private Review mapReview(ResultSet rs) throws SQLException {
        return new Review(rs.getInt("review_id"), rs.getInt("order_id"),
                rs.getInt("gig_id"), rs.getInt("reviewer_id"),
                rs.getInt("rating"), rs.getString("comment"),
                rs.getTimestamp("created_at"));
    }
}
