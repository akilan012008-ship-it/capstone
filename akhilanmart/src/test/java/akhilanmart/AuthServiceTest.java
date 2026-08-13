package akhilanmart;

import akhilanmart.dao.UserDAO;
import akhilanmart.model.Role;
import akhilanmart.model.User;
import akhilanmart.service.AuthService;
import akhilanmart.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    private UserDAO userDAO;
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        userDAO = Mockito.mock(UserDAO.class);
        authService = new AuthService(userDAO);
    }

    @Test
    public void testSuccessfulBuyerRegistration() throws Exception {
        when(userDAO.findByEmail("newbuyer@akhilanmart.com")).thenReturn(null);
        when(userDAO.createUser(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(100L);
            return u;
        });

        User registered = authService.registerUser("Test Buyer", "newbuyer@akhilanmart.com", "Buyer@123", "Buyer@123", Role.BUYER);

        assertNotNull(registered);
        assertEquals("Test Buyer", registered.getName());
        assertEquals(Role.BUYER, registered.getRole());
        assertTrue(PasswordUtil.checkPassword("Buyer@123", registered.getPasswordHash()));
    }

    @Test
    public void testDuplicateEmailRegistrationFails() throws Exception {
        User existing = new User("Existing", "buyer@akhilanmart.com", "hash", Role.BUYER);
        when(userDAO.findByEmail("buyer@akhilanmart.com")).thenReturn(existing);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.registerUser("Test Buyer", "buyer@akhilanmart.com", "Buyer@123", "Buyer@123", Role.BUYER);
        });

        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    public void testPublicAdminRegistrationForbidden() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.registerUser("Hacker", "hacker@akhilanmart.com", "Admin@123", "Admin@123", Role.ADMIN);
        });

        assertTrue(exception.getMessage().contains("Public admin registration is strictly prohibited"));
    }

    @Test
    public void testSuccessfulAuthentication() throws Exception {
        String hashedPassword = PasswordUtil.hashPassword("Buyer@123");
        User mockUser = new User(1L, "Akhilan Buyer", "buyer@akhilanmart.com", hashedPassword, Role.BUYER, null);
        when(userDAO.findByEmail("buyer@akhilanmart.com")).thenReturn(mockUser);

        User authenticated = authService.authenticate("buyer@akhilanmart.com", "Buyer@123");

        assertNotNull(authenticated);
        assertEquals("Akhilan Buyer", authenticated.getName());
    }

    @Test
    public void testAuthenticationFailsWithWrongPassword() throws Exception {
        String hashedPassword = PasswordUtil.hashPassword("Buyer@123");
        User mockUser = new User(1L, "Akhilan Buyer", "buyer@akhilanmart.com", hashedPassword, Role.BUYER, null);
        when(userDAO.findByEmail("buyer@akhilanmart.com")).thenReturn(mockUser);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.authenticate("buyer@akhilanmart.com", "WrongPass123");
        });

        assertEquals("Invalid email or password.", exception.getMessage());
    }
}
