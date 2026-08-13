package akhilanmart.dao;

import akhilanmart.config.DatabaseConfig;
import akhilanmart.model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    public Review createReview(Review review) throws SQLException {
        String sql = "INSERT INTO reviews (user_id, product_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, review.getUserId());
            pstmt.setLong(2, review.getProductId());
            pstmt.setInt(3, review.getRating());
            pstmt.setString(4, review.getComment());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    review.setId(rs.getLong(1));
                }
            }
            return review;
        }
    }

    public List<Review> findByProduct(Long productId) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.id, r.user_id, u.name AS user_name, r.product_id, r.rating, r.comment, r.created_at " +
                "FROM reviews r JOIN users u ON r.user_id = u.id " +
                "WHERE r.product_id = ? ORDER BY r.id DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Review r = new Review();
                    r.setId(rs.getLong("id"));
                    r.setUserId(rs.getLong("user_id"));
                    r.setUserName(rs.getString("user_name"));
                    r.setProductId(rs.getLong("product_id"));
                    r.setRating(rs.getInt("rating"));
                    r.setComment(rs.getString("comment"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    reviews.add(r);
                }
            }
        }
        return reviews;
    }

    public boolean checkPurchased(Long userId, Long productId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM order_items oi " +
                "JOIN orders o ON oi.order_id = o.id " +
                "WHERE o.buyer_id = ? AND oi.product_id = ? AND o.status != 'CANCELLED'";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.setLong(2, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean checkExistingReview(Long userId, Long productId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reviews WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.setLong(2, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
