package akhilanmart.dao;

import akhilanmart.config.DatabaseConfig;
import akhilanmart.model.Order;
import akhilanmart.model.OrderItem;
import akhilanmart.model.OrderStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public Long createOrder(Connection conn, Order order) throws SQLException {
        String sql = "INSERT INTO orders (buyer_id, total_amount, status) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, order.getBuyerId());
            pstmt.setBigDecimal(2, order.getTotalAmount());
            pstmt.setString(3, order.getStatus().name());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    long generatedId = rs.getLong(1);
                    order.setId(generatedId);
                    return generatedId;
                }
            }
        }
        throw new SQLException("Failed to create order, no ID returned.");
    }

    public void createOrderItem(Connection conn, OrderItem item) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, item.getOrderId());
            pstmt.setLong(2, item.getProductId());
            pstmt.setInt(3, item.getQuantity());
            pstmt.setBigDecimal(4, item.getUnitPrice());
            pstmt.executeUpdate();
        }
    }

    public Order findById(Long orderId) throws SQLException {
        String sql = "SELECT o.id, o.buyer_id, u.name AS buyer_name, u.email AS buyer_email, " +
                "o.total_amount, o.status, o.created_at " +
                "FROM orders o JOIN users u ON o.buyer_id = u.id WHERE o.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(findItemsByOrderId(conn, orderId));
                    return order;
                }
            }
        }
        return null;
    }

    public List<Order> findByBuyer(Long buyerId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.id, o.buyer_id, u.name AS buyer_name, u.email AS buyer_email, " +
                "o.total_amount, o.status, o.created_at " +
                "FROM orders o JOIN users u ON o.buyer_id = u.id " +
                "WHERE o.buyer_id = ? ORDER BY o.id DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, buyerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(findItemsByOrderId(conn, order.getId()));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    public List<Order> findAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.id, o.buyer_id, u.name AS buyer_name, u.email AS buyer_email, " +
                "o.total_amount, o.status, o.created_at " +
                "FROM orders o JOIN users u ON o.buyer_id = u.id ORDER BY o.id DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order order = mapOrder(rs);
                order.setItems(findItemsByOrderId(conn, order.getId()));
                orders.add(order);
            }
        }
        return orders;
    }

    public List<OrderItem> findSellerOrders(Long sellerId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.unit_price, " +
                "p.name AS product_name, p.category AS product_category, p.image_url AS product_image_url, " +
                "p.seller_id, su.name AS seller_name, o.status, o.created_at AS order_date " +
                "FROM order_items oi " +
                "JOIN orders o ON oi.order_id = o.id " +
                "JOIN products p ON oi.product_id = p.id " +
                "JOIN users su ON p.seller_id = su.id " +
                "WHERE p.seller_id = ? ORDER BY o.id DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, sellerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = mapOrderItem(rs);
                    items.add(item);
                }
            }
        }
        return items;
    }

    public boolean updateStatus(Long orderId, OrderStatus status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status.name());
            pstmt.setLong(2, orderId);
            return pstmt.executeUpdate() > 0;
        }
    }

    private List<OrderItem> findItemsByOrderId(Connection conn, Long orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.unit_price, " +
                "p.name AS product_name, p.category AS product_category, p.image_url AS product_image_url, " +
                "p.seller_id, su.name AS seller_name " +
                "FROM order_items oi " +
                "JOIN products p ON oi.product_id = p.id " +
                "JOIN users su ON p.seller_id = su.id " +
                "WHERE oi.order_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapOrderItem(rs));
                }
            }
        }
        return items;
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setBuyerId(rs.getLong("buyer_id"));
        order.setBuyerName(rs.getString("buyer_name"));
        order.setBuyerEmail(rs.getString("buyer_email"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setStatus(OrderStatus.fromString(rs.getString("status")));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        return order;
    }

    private OrderItem mapOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getLong("id"));
        item.setOrderId(rs.getLong("order_id"));
        item.setProductId(rs.getLong("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setProductName(rs.getString("product_name"));
        item.setProductCategory(rs.getString("product_category"));
        item.setProductImageUrl(rs.getString("product_image_url"));
        item.setSellerId(rs.getLong("seller_id"));
        item.setSellerName(rs.getString("seller_name"));
        return item;
    }
}
