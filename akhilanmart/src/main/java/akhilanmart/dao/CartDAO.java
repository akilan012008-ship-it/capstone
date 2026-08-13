package akhilanmart.dao;

import akhilanmart.config.DatabaseConfig;
import akhilanmart.model.CartItem;
import akhilanmart.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    public boolean addItem(Long userId, Long productId, int quantity) throws SQLException {
        CartItem existing = findCartItem(userId, productId);
        if (existing != null) {
            return updateQuantity(userId, productId, existing.getQuantity() + quantity);
        }

        String sql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.setLong(2, productId);
            pstmt.setInt(3, quantity);
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<CartItem> findCartByUser(Long userId) throws SQLException {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT c.id, c.user_id, c.product_id, c.quantity, " +
                "p.name, p.description, p.price, p.stock, p.category, p.image_url, p.seller_id, u.name AS seller_name " +
                "FROM cart_items c " +
                "JOIN products p ON c.product_id = p.id " +
                "JOIN users u ON p.seller_id = u.id " +
                "WHERE c.user_id = ? ORDER BY c.id ASC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getLong("id"));
                    item.setUserId(rs.getLong("user_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setQuantity(rs.getInt("quantity"));

                    Product p = new Product();
                    p.setId(rs.getLong("product_id"));
                    p.setName(rs.getString("name"));
                    p.setDescription(rs.getString("description"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setStock(rs.getInt("stock"));
                    p.setCategory(rs.getString("category"));
                    p.setImageUrl(rs.getString("image_url"));
                    p.setSellerId(rs.getLong("seller_id"));
                    p.setSellerName(rs.getString("seller_name"));

                    item.setProduct(p);
                    items.add(item);
                }
            }
        }
        return items;
    }

    public CartItem findCartItem(Long userId, Long productId) throws SQLException {
        String sql = "SELECT id, user_id, product_id, quantity FROM cart_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.setLong(2, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new CartItem(rs.getLong("id"), rs.getLong("user_id"), rs.getLong("product_id"), rs.getInt("quantity"));
                }
            }
        }
        return null;
    }

    public boolean updateQuantity(Long userId, Long productId, int quantity) throws SQLException {
        if (quantity <= 0) {
            return removeItem(userId, productId);
        }
        String sql = "UPDATE cart_items SET quantity = ? WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setLong(2, userId);
            pstmt.setLong(3, productId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean removeItem(Long userId, Long productId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.setLong(2, productId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean clearCart(Connection conn, Long userId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            return pstmt.executeUpdate() >= 0;
        }
    }

    public boolean clearCart(Long userId) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return clearCart(conn, userId);
        }
    }
}
