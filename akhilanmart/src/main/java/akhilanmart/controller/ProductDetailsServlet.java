package akhilanmart.controller;

import akhilanmart.model.Product;
import akhilanmart.model.Review;
import akhilanmart.model.User;
import akhilanmart.service.ProductService;
import akhilanmart.service.ReviewService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/product-details")
public class ProductDetailsServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/products");
            return;
        }

        try {
            Long productId = Long.parseLong(idStr);
            Product product = productService.getProductById(productId);

            if (product == null) {
                req.setAttribute("errorMessage", "Product not found.");
                req.getRequestDispatcher("/404.jsp").forward(req, resp);
                return;
            }

            List<Review> reviews = reviewService.getProductReviews(productId);

            HttpSession session = req.getSession(false);
            User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
            boolean canReview = false;

            if (currentUser != null) {
                canReview = reviewService.canUserReview(currentUser.getId(), productId);
            }

            req.setAttribute("product", product);
            req.setAttribute("reviews", reviews);
            req.setAttribute("canReview", canReview);

            req.getRequestDispatcher("/product-details.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/products");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading product details: " + e.getMessage());
            req.getRequestDispatcher("/500.jsp").forward(req, resp);
        }
    }
}
