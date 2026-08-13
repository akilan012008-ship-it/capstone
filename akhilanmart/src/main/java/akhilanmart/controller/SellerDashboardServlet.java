package akhilanmart.controller;

import akhilanmart.model.OrderItem;
import akhilanmart.model.Product;
import akhilanmart.model.User;
import akhilanmart.service.OrderService;
import akhilanmart.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/seller/dashboard")
public class SellerDashboardServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User seller = (User) session.getAttribute("user");

        try {
            List<Product> products = productService.getProductsBySeller(seller.getId());
            List<OrderItem> sellerOrderItems = orderService.getSellerOrders(seller.getId());

            int totalProducts = products.size();
            int totalStock = products.stream().mapToInt(Product::getStock).sum();
            int totalOrdersCount = sellerOrderItems.size();
            BigDecimal totalRevenue = sellerOrderItems.stream()
                    .map(OrderItem::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            req.setAttribute("totalProducts", totalProducts);
            req.setAttribute("totalStock", totalStock);
            req.setAttribute("totalOrdersCount", totalOrdersCount);
            req.setAttribute("totalRevenue", totalRevenue);
            req.setAttribute("recentProducts", products.size() > 5 ? products.subList(0, 5) : products);
            req.setAttribute("recentOrders", sellerOrderItems.size() > 5 ? sellerOrderItems.subList(0, 5) : sellerOrderItems);

            req.getRequestDispatcher("/seller-dashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading seller dashboard: " + e.getMessage());
            req.getRequestDispatcher("/500.jsp").forward(req, resp);
        }
    }
}
