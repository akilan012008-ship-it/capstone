package akhilanmart.service;

import akhilanmart.dao.ReviewDAO;
import akhilanmart.model.Review;
import akhilanmart.util.ValidationUtil;

import java.sql.SQLException;
import java.util.List;

public class ReviewService {

    private final ReviewDAO reviewDAO;

    public ReviewService() {
        this.reviewDAO = new ReviewDAO();
    }

    public ReviewService(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    public Review addReview(Long userId, Long productId, Integer rating, String comment) throws Exception {
        if (userId == null) {
            throw new IllegalArgumentException("User must be logged in to post a review.");
        }
        if (productId == null) {
            throw new IllegalArgumentException("Product ID is required.");
        }
        if (!ValidationUtil.isValidRating(rating)) {
            throw new IllegalArgumentException("Rating must be an integer between 1 and 5.");
        }

        // Verify purchase
        if (!reviewDAO.checkPurchased(userId, productId)) {
            throw new IllegalStateException("Only verified buyers who have purchased this product can leave a review.");
        }

        // Check duplicate review
        if (reviewDAO.checkExistingReview(userId, productId)) {
            throw new IllegalStateException("You have already reviewed this product.");
        }

        Review review = new Review();
        review.setUserId(userId);
        review.setProductId(productId);
        review.setRating(rating);
        review.setComment(comment != null ? comment.trim() : "");

        return reviewDAO.createReview(review);
    }

    public List<Review> getProductReviews(Long productId) throws SQLException {
        return reviewDAO.findByProduct(productId);
    }

    public boolean canUserReview(Long userId, Long productId) throws SQLException {
        if (userId == null || productId == null) return false;
        return reviewDAO.checkPurchased(userId, productId) && !reviewDAO.checkExistingReview(userId, productId);
    }
}
