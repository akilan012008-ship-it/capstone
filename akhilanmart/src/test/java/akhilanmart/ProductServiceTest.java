package akhilanmart;

import akhilanmart.dao.ProductDAO;
import akhilanmart.model.Product;
import akhilanmart.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    private ProductDAO productDAO;
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        productDAO = Mockito.mock(ProductDAO.class);
        productService = new ProductService(productDAO);
    }

    @Test
    public void testAddProductValid() throws Exception {
        when(productDAO.createProduct(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setId(10L);
            return p;
        });

        Product created = productService.addProduct(
                5L,
                "Wireless Earbuds",
                "High quality bluetooth earphone",
                BigDecimal.valueOf(1999.00),
                20,
                "Electronics",
                "https://example.com/img.jpg"
        );

        assertNotNull(created);
        assertEquals("Wireless Earbuds", created.getName());
        assertEquals(20, created.getStock());
    }

    @Test
    public void testAddProductNegativePriceFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            productService.addProduct(5L, "Sample", "Desc", BigDecimal.valueOf(-100), 10, "Electronics", null);
        });
    }

    @Test
    public void testUpdateProductUnauthorizedSellerFails() throws SQLException {
        Product mockExisting = new Product();
        mockExisting.setId(10L);
        mockExisting.setSellerId(5L); // Owned by Seller 5

        when(productDAO.findById(10L)).thenReturn(mockExisting);

        // Seller 9 tries to edit Seller 5's product
        assertThrows(IllegalAccessException.class, () -> {
            productService.updateProduct(10L, 9L, "Hacked Title", "Desc", BigDecimal.valueOf(500), 10, "Electronics", null);
        });
    }

    @Test
    public void testDeleteProductUnauthorizedSellerFails() throws SQLException {
        Product mockExisting = new Product();
        mockExisting.setId(10L);
        mockExisting.setSellerId(5L);

        when(productDAO.findById(10L)).thenReturn(mockExisting);

        assertThrows(IllegalAccessException.class, () -> {
            productService.deleteProduct(10L, 9L);
        });
    }
}
