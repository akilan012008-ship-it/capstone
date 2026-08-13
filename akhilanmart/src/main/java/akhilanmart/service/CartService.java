package akhilanmart.service;

import akhilanmart.dao.CartDAO;
import akhilanmart.dao.ProductDAO;
import akhilanmart.model.CartItem;
import akhilanmart.model.Product;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class CartService {

    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartService() {
        this.cartDAO = new CartDAO();
        this.productDAO = new ProductDAO();
    }

    public CartService(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    public boolean addToCart(Long userId, Long productId, int quantity) throws Exception {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }

        Product product = productDAO.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found.");
        }

        CartItem existing = cartDAO.findCartItem(userId, productId);
        int totalRequested = (existing != null ? existing.getQuantity() : 0) + quantity;

        if (totalRequested > product.getStock()) {
            throw new IllegalArgumentException("Cannot add to cart. Only " + product.getStock() + " units available in stock.");
        }

        return cartDAO.addItem(userId, productId, quantity);
    }

    public boolean updateCartQuantity(Long userId, Long productId, int quantity) throws Exception {
        if (quantity <= 0) {
            return cartDAO.removeItem(userId, productId);
        }

        Product product = productDAO.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found.");
        }

        if (quantity > product.getStock()) {
            throw new IllegalArgumentException("Requested quantity exceeds available stock (" + product.getStock() + ").");
        }

        return cartDAO.updateQuantity(userId, productId, quantity);
    }

    public boolean removeFromCart(Long userId, Long productId) throws SQLException {
        return cartDAO.removeItem(userId, productId);
    }

    public List<CartItem> getCart(Long userId) throws SQLException {
        return cartDAO.findCartByUser(userId);
    }

    public BigDecimal calculateCartTotal(List<CartItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        if (items != null) {
            for (CartItem item : items) {
                total = total.add(item.getSubtotal());
            }
        }
        return total;
    }

    public boolean clearCart(Long userId) throws SQLException {
        return cartDAO.clearCart(userId);
    }
}
