package akhilanmart.controller;

import akhilanmart.model.CartItem;
import akhilanmart.model.User;
import akhilanmart.service.CartService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(urlPatterns = {"/cart", "/cart/add", "/cart/update", "/cart/remove"})
public class CartServlet extends HttpServlet {

    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            List<CartItem> cartItems = cartService.getCart(user.getId());
            BigDecimal cartTotal = cartService.calculateCartTotal(cartItems);

            req.setAttribute("cartItems", cartItems);
            req.setAttribute("cartTotal", cartTotal);
            req.getRequestDispatcher("/cart.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading cart: " + e.getMessage());
            req.getRequestDispatcher("/500.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getServletPath();

        try {
            if ("/cart/add".equals(action)) {
                Long productId = Long.parseLong(req.getParameter("productId"));
                int quantity = 1;
                if (req.getParameter("quantity") != null) {
                    quantity = Integer.parseInt(req.getParameter("quantity"));
                }
                cartService.addToCart(user.getId(), productId, quantity);
                session.setAttribute("flashSuccess", "Item added to your shopping cart!");

                String redirect = req.getParameter("redirectUrl");
                if (redirect != null && !redirect.trim().isEmpty()) {
                    resp.sendRedirect(redirect);
                } else {
                    resp.sendRedirect(req.getContextPath() + "/cart");
                }

            } else if ("/cart/update".equals(action)) {
                Long productId = Long.parseLong(req.getParameter("productId"));
                int quantity = Integer.parseInt(req.getParameter("quantity"));
                cartService.updateCartQuantity(user.getId(), productId, quantity);

                // Handle AJAX request
                if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
                    List<CartItem> updatedCart = cartService.getCart(user.getId());
                    BigDecimal newTotal = cartService.calculateCartTotal(updatedCart);
                    
                    resp.setContentType("application/json");
                    PrintWriter out = resp.getWriter();
                    out.print("{\"success\": true, \"cartTotal\": \"" + newTotal + "\"}");
                    out.flush();
                    return;
                }

                resp.sendRedirect(req.getContextPath() + "/cart");

            } else if ("/cart/remove".equals(action)) {
                Long productId = Long.parseLong(req.getParameter("productId"));
                cartService.removeFromCart(user.getId(), productId);
                session.setAttribute("flashSuccess", "Item removed from cart.");
                resp.sendRedirect(req.getContextPath() + "/cart");
            }
        } catch (Exception e) {
            if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("application/json");
                PrintWriter out = resp.getWriter();
                out.print("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
                out.flush();
                return;
            }

            session.setAttribute("flashError", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/cart");
        }
    }
}
