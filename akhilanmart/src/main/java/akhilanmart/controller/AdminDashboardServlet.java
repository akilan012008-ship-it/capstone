package akhilanmart.controller;

import akhilanmart.model.Order;
import akhilanmart.model.Product;
import akhilanmart.model.Role;
import akhilanmart.model.User;
import akhilanmart.service.OrderService;
import akhilanmart.service.ProductService;
import akhilanmart.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<User> allUsers = userService.getAllUsers();
            List<Product> allProducts = productService.getAllProducts();
            List<Order> allOrders = orderService.getAllOrders();

            long totalUsers = allUsers.size();
            long totalBuyers = allUsers.stream().filter(u -> u.getRole() == Role.BUYER).count();
            long totalSellers = allUsers.stream().filter(u -> u.getRole() == Role.SELLER).count();
            long totalProducts = allProducts.size();
            long totalOrders = allOrders.size();

            BigDecimal totalMarketplaceRevenue = allOrders.stream()
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            req.setAttribute("totalUsers", totalUsers);
            req.setAttribute("totalBuyers", totalBuyers);
            req.setAttribute("totalSellers", totalSellers);
            req.setAttribute("totalProducts", totalProducts);
            req.setAttribute("totalOrders", totalOrders);
            req.setAttribute("totalMarketplaceRevenue", totalMarketplaceRevenue);

            req.setAttribute("recentUsers", allUsers.size() > 5 ? allUsers.subList(0, 5) : allUsers);
            req.setAttribute("recentOrders", allOrders.size() > 5 ? allOrders.subList(0, 5) : allOrders);

            req.getRequestDispatcher("/admin-dashboard.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading admin dashboard: " + e.getMessage());
            req.getRequestDispatcher("/500.jsp").forward(req, resp);
        }
    }
}
