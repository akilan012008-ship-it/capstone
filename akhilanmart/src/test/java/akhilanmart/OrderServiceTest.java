package akhilanmart;

import akhilanmart.dao.CartDAO;
import akhilanmart.dao.OrderDAO;
import akhilanmart.dao.ProductDAO;
import akhilanmart.model.CartItem;
import akhilanmart.model.Product;
import akhilanmart.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class OrderServiceTest {

    private OrderDAO orderDAO;
    private CartDAO cartDAO;
    private ProductDAO productDAO;
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderDAO = Mockito.mock(OrderDAO.class);
        cartDAO = Mockito.mock(CartDAO.class);
        productDAO = Mockito.mock(ProductDAO.class);
        orderService = new OrderService(orderDAO, cartDAO, productDAO);
    }

    @Test
    public void testPlaceOrderWithEmptyCartFails() throws SQLException {
        when(cartDAO.findCartByUser(100L)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            orderService.placeOrder(100L);
        });

        assertTrue(exception.getMessage().contains("cart is empty"));
    }

    @Test
    public void testPlaceOrderStockExceededFails() throws SQLException {
        Product p = new Product();
        p.setId(1L);
        p.setName("Wireless Keyboard");
        p.setStock(2);
        p.setPrice(BigDecimal.valueOf(1500));

        CartItem item = new CartItem(1L, 100L, 1L, 5); // Wants 5, stock is 2
        item.setProduct(p);

        when(cartDAO.findCartByUser(100L)).thenReturn(List.of(item));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            orderService.placeOrder(100L);
        });

        assertTrue(exception.getMessage().contains("Stock unavailable"));
    }
}
