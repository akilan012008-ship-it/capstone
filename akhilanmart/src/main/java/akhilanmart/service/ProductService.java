package akhilanmart.service;

import akhilanmart.dao.ProductDAO;
import akhilanmart.model.Product;
import akhilanmart.util.ValidationUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ProductService {

    public static final List<String> CATEGORIES = Arrays.asList(
            "Accessories", "Books", "Clothing", "Electronics", "Home"
    );

    private final ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Product addProduct(Long sellerId, String name, String description, BigDecimal price, Integer stock, String category, String imageUrl) throws Exception {
        validateProductInputs(sellerId, name, price, stock, category);
        
        String cleanImageUrl = (imageUrl != null && !imageUrl.trim().isEmpty()) ? 
                imageUrl.trim() : "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80";

        Product product = new Product();
        product.setSellerId(sellerId);
        product.setName(name.trim());
        product.setDescription(description != null ? description.trim() : "");
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category.trim());
        product.setImageUrl(cleanImageUrl);

        return productDAO.createProduct(product);
    }

    public Product updateProduct(Long productId, Long sellerId, String name, String description, BigDecimal price, Integer stock, String category, String imageUrl) throws Exception {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID is required.");
        }
        
        Product existing = productDAO.findById(productId);
        if (existing == null) {
            throw new IllegalArgumentException("Product not found.");
        }

        // Verify seller ownership (unless admin overriding via null sellerId)
        if (sellerId != null && !existing.getSellerId().equals(sellerId)) {
            throw new IllegalAccessException("Unauthorized: You can only edit your own products.");
        }

        validateProductInputs(existing.getSellerId(), name, price, stock, category);

        existing.setName(name.trim());
        existing.setDescription(description != null ? description.trim() : "");
        existing.setPrice(price);
        existing.setStock(stock);
        existing.setCategory(category.trim());
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            existing.setImageUrl(imageUrl.trim());
        }

        boolean updated = productDAO.updateProduct(existing);
        if (!updated) {
            throw new SQLException("Failed to update product.");
        }
        return productDAO.findById(productId);
    }

    public boolean deleteProduct(Long productId, Long sellerId) throws Exception {
        Product existing = productDAO.findById(productId);
        if (existing == null) {
            throw new IllegalArgumentException("Product not found.");
        }
        if (sellerId != null && !existing.getSellerId().equals(sellerId)) {
            throw new IllegalAccessException("Unauthorized: You can only delete your own products.");
        }
        return productDAO.deleteProduct(productId, sellerId);
    }

    public Product getProductById(Long id) throws SQLException {
        return productDAO.findById(id);
    }

    public List<Product> getAllProducts() throws SQLException {
        return productDAO.findAll();
    }

    public List<Product> searchProducts(String query, String category, String sortBy) throws SQLException {
        return productDAO.searchProducts(query, category, sortBy);
    }

    public List<Product> getProductsBySeller(Long sellerId) throws SQLException {
        return productDAO.findBySeller(sellerId);
    }

    private void validateProductInputs(Long sellerId, String name, BigDecimal price, Integer stock, String category) {
        if (sellerId == null) {
            throw new IllegalArgumentException("Seller ID is required.");
        }
        if (ValidationUtil.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Product name is required.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0.");
        }
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        if (ValidationUtil.isNullOrEmpty(category)) {
            throw new IllegalArgumentException("Category is required.");
        }
    }
}
