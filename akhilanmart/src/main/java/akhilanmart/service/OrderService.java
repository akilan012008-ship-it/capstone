package akhilanmart.service;

import akhilanmart.config.DatabaseConfig;
import akhilanmart.dao.CartDAO;
import akhilanmart.dao.OrderDAO;
import akhilanmart.dao.ProductDAO;
import akhilanmart.model.CartItem;
import akhilanmart.model.Order;
import akhilanmart.model.OrderItem;
import akhilanmart.model.OrderStatus;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO;
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.cartDAO = new CartDAO();
        this.productDAO = new ProductDAO();
    }

    public OrderService(OrderDAO orderDAO, CartDAO cartDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    public Order placeOrder(Long buyerId) throws Exception {
        if (buyerId == null) {
            throw new IllegalArgumentException("Buyer ID is required.");
        }

        List<CartItem> cartItems = cartDAO.findCartByUser(buyerId);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Your cart is empty. Cannot proceed with checkout.");
        }

        // Validate stock for all items before beginning transaction
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            if (item.getProduct() == null) {
                throw new IllegalStateException("Product details missing for item in cart.");
            }
            if (item.getQuantity() > item.getProduct().getStock()) {
                throw new IllegalStateException("Stock unavailable for '" + item.getProduct().getName() +
                        "'. Requested: " + item.getQuantity() + ", Available: " + item.getProduct().getStock());
            }
            totalAmount = totalAmount.add(item.getSubtotal());
        }

        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false); // Begin Transaction

            // 1. Create Order
            Order order = new Order();
            order.setBuyerId(buyerId);
            order.setTotalAmount(totalAmount);
            order.setStatus(OrderStatus.PENDING);

            Long orderId = orderDAO.createOrder(conn, order);
            order.setId(orderId);

            // 2. Insert Order Items & Reduce Stock
            for (CartItem item : cartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(orderId);
                orderItem.setProductId(item.getProductId());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setUnitPrice(item.getProduct().getPrice()); // Store unit price at purchase time

                orderDAO.createOrderItem(conn, orderItem);

                // Reduce stock with atomic safety check
                boolean stockReduced = productDAO.updateStock(conn, item.getProductId(), item.getQuantity());
                if (!stockReduced) {
                    throw new IllegalStateException("Failed to update stock for product: " + item.getProduct().getName());
                }
            }

            // 3. Clear Cart
            cartDAO.clearCart(conn, buyerId);

            // 4. Commit Transaction
            conn.commit();
            return orderDAO.findById(orderId);

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on failure
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public List<Order> getBuyerOrders(Long buyerId) throws SQLException {
        return orderDAO.findByBuyer(buyerId);
    }

    public Order getOrderDetails(Long orderId, Long userId, boolean isAdmin) throws Exception {
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found.");
        }

        if (!isAdmin && !order.getBuyerId().equals(userId)) {
            // Check if user is seller of any item in this order
            boolean isSellerInOrder = order.getItems().stream()
                    .anyMatch(item -> item.getSellerId() != null && item.getSellerId().equals(userId));
            if (!isSellerInOrder) {
                throw new IllegalAccessException("Unauthorized to view this order.");
            }
        }

        return order;
    }

    public List<OrderItem> getSellerOrders(Long sellerId) throws SQLException {
        return orderDAO.findSellerOrders(sellerId);
    }

    public List<Order> getAllOrders() throws SQLException {
        return orderDAO.findAllOrders();
    }

    public boolean updateOrderStatus(Long orderId, String newStatusStr) throws Exception {
        OrderStatus status = OrderStatus.fromString(newStatusStr);
        if (status == null) {
            throw new IllegalArgumentException("Invalid order status.");
        }

        Order order = orderDAO.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found.");
        }

        return orderDAO.updateStatus(orderId, status);
    }
}
