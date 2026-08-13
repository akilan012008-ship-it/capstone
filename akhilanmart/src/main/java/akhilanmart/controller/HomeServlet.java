package akhilanmart.controller;

import akhilanmart.model.Product;
import akhilanmart.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"", "/home"})
public class HomeServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Product> featuredProducts = productService.getAllProducts();
            if (featuredProducts.size() > 8) {
                featuredProducts = featuredProducts.subList(0, 8);
            }
            req.setAttribute("featuredProducts", featuredProducts);
            req.setAttribute("categories", ProductService.CATEGORIES);
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading home page content: " + e.getMessage());
            req.getRequestDispatcher("/500.jsp").forward(req, resp);
        }
    }
}
