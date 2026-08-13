package akhilanmart.dao;

import akhilanmart.config.DatabaseConfig;
import akhilanmart.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private static final String BASE_PRODUCT_QUERY =
            "SELECT p.id, p.seller_id, u.name AS seller_name, p.name, p.description, p.price, p.stock, " +
            "p.category, p.image_url, p.created_at, " +
            "COALESCE(AVG(r.rating), 0.0) AS avg_rating, COUNT(r.id) AS review_count " +
            "FROM products p " +
            "JOIN users u ON p.seller_id = u.id " +
            "LEFT JOIN reviews r ON p.id = r.product_id ";

    public Product createProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (seller_id, name, description, price, stock, category, image_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, product.getSellerId());
            pstmt.setString(2, product.getName());
            pstmt.setString(3, product.getDescription());
            pstmt.setBigDecimal(4, product.getPrice());
            pstmt.setInt(5, product.getStock());
            pstmt.setString(6, product.getCategory());
            pstmt.setString(7, product.getImageUrl());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    product.setId(rs.getLong(1));
                }
            }
            return findById(product.getId());
        }
    }

    public Product findById(Long id) throws SQLException {
        if (id == null) return null;
        String sql = BASE_PRODUCT_QUERY + "WHERE p.id = ? GROUP BY p.id, u.name";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapProduct(rs);
                }
            }
        }
        return null;
    }

    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = BASE_PRODUCT_QUERY + "GROUP BY p.id, u.name ORDER BY p.id DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        }
        return products;
    }

    public List<Product> searchProducts(String query, String category, String sortBy) throws SQLException {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_PRODUCT_QUERY);
        List<Object> params = new ArrayList<>();
        
        sql.append("WHERE 1=1 ");
        
        if (query != null && !query.trim().isEmpty()) {
            sql.append("AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?) ");
            String term = "%" + query.trim().toLowerCase() + "%";
            params.add(term);
            params.add(term);
        }

        if (category != null && !category.trim().isEmpty() && !"All".equalsIgnoreCase(category.trim())) {
            sql.append("AND LOWER(p.category) = ? ");
            params.add(category.trim().toLowerCase());
        }

        sql.append("GROUP BY p.id, u.name ");

        if ("price_asc".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY p.price ASC ");
        } else if ("price_desc".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY p.price DESC ");
        } else if ("rating".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY avg_rating DESC ");
        } else {
            sql.append("ORDER BY p.id DESC ");
        }

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProduct(rs));
                }
            }
        }
        return products;
    }

    public List<Product> findByCategory(String category) throws SQLException {
        return searchProducts(null, category, null);
    }

    public List<Product> findBySeller(Long sellerId) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = BASE_PRODUCT_QUERY + "WHERE p.seller_id = ? GROUP BY p.id, u.name ORDER BY p.id DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, sellerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProduct(rs));
                }
            }
        }
        return products;
    }

    public boolean updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock = ?, category = ?, image_url = ? WHERE id = ? AND seller_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setInt(4, product.getStock());
            pstmt.setString(5, product.getCategory());
            pstmt.setString(6, product.getImageUrl());
            pstmt.setLong(7, product.getId());
            pstmt.setLong(8, product.getSellerId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteProduct(Long id, Long sellerId) throws SQLException {
        String sql = sellerId != null ? 
                "DELETE FROM products WHERE id = ? AND seller_id = ?" : 
                "DELETE FROM products WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            if (sellerId != null) {
                pstmt.setLong(2, sellerId);
            }
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean updateStock(Connection conn, Long productId, int quantityToReduce) throws SQLException {
        String sql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantityToReduce);
            pstmt.setLong(2, productId);
            pstmt.setInt(3, quantityToReduce);
            return pstmt.executeUpdate() > 0;
        }
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setSellerId(rs.getLong("seller_id"));
        p.setSellerName(rs.getString("seller_name"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStock(rs.getInt("stock"));
        p.setCategory(rs.getString("category"));
        p.setImageUrl(rs.getString("image_url"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setAverageRating(rs.getDouble("avg_rating"));
        p.setReviewCount(rs.getInt("review_count"));
        return p;
    }
}
