package akhilanmart;

import akhilanmart.model.OrderStatus;
import akhilanmart.model.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorizationTest {

    @Test
    public void testRoleParsing() {
        assertEquals(Role.BUYER, Role.fromString("buyer"));
        assertEquals(Role.SELLER, Role.fromString("SELLER"));
        assertEquals(Role.ADMIN, Role.fromString("Admin"));
        assertNull(Role.fromString("INVALID_ROLE"));
    }

    @Test
    public void testOrderStatusParsing() {
        assertEquals(OrderStatus.PENDING, OrderStatus.fromString("pending"));
        assertEquals(OrderStatus.SHIPPED, OrderStatus.fromString("SHIPPED"));
        assertEquals(OrderStatus.DELIVERED, OrderStatus.fromString("Delivered"));
        assertNull(OrderStatus.fromString("UNKNOWN"));
    }
}
