package akhilanmart;

import akhilanmart.dao.CartDAO;
import akhilanmart.dao.ProductDAO;
import akhilanmart.model.CartItem;
import akhilanmart.model.Product;
import akhilanmart.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CartServiceTest {

    private CartDAO cartDAO;
    private ProductDAO productDAO;
    private CartService cartService;

    @BeforeEach
    public void setUp() {
        cartDAO = Mockito.mock(CartDAO.class);
        productDAO = Mockito.mock(ProductDAO.class);
        cartService = new CartService(cartDAO, productDAO);
    }

    @Test
    public void testAddToCartExceedingStockFails() throws Exception {
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setStock(5); // Only 5 in stock

        when(productDAO.findById(1L)).thenReturn(mockProduct);
        when(cartDAO.findCartItem(10L, 1L)).thenReturn(null);

        // User requests 10 units when only 5 exist
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.addToCart(10L, 1L, 10);
        });

        assertTrue(exception.getMessage().contains("available in stock"));
    }

    @Test
    public void testCalculateCartTotal() {
        Product p1 = new Product();
        p1.setPrice(BigDecimal.valueOf(100.00));
        CartItem item1 = new CartItem(1L, 10L, 1L, 2); // 200.00
        item1.setProduct(p1);

        Product p2 = new Product();
        p2.setPrice(BigDecimal.valueOf(50.00));
        CartItem item2 = new CartItem(2L, 10L, 2L, 3); // 150.00
        item2.setProduct(p2);

        List<CartItem> cartList = Arrays.asList(item1, item2);
        BigDecimal grandTotal = cartService.calculateCartTotal(cartList);

        assertEquals(0, BigDecimal.valueOf(350.00).compareTo(grandTotal));
    }
}
