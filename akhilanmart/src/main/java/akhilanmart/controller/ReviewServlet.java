package akhilanmart.controller;

import akhilanmart.model.User;
import akhilanmart.service.ReviewService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/review/add")
public class ReviewServlet extends HttpServlet {

    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String productIdStr = req.getParameter("productId");
        String ratingStr = req.getParameter("rating");
        String comment = req.getParameter("comment");

        try {
            Long productId = Long.parseLong(productIdStr);
            Integer rating = Integer.parseInt(ratingStr);

            reviewService.addReview(user.getId(), productId, rating, comment);
            session.setAttribute("flashSuccess", "Thank you for your review!");

            resp.sendRedirect(req.getContextPath() + "/product-details?id=" + productId);
        } catch (Exception e) {
            session.setAttribute("flashError", e.getMessage());
            if (productIdStr != null) {
                resp.sendRedirect(req.getContextPath() + "/product-details?id=" + productIdStr);
            } else {
                resp.sendRedirect(req.getContextPath() + "/products");
            }
        }
    }
}
