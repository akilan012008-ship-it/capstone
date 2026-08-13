package akhilanmart.controller;

import akhilanmart.model.Product;
import akhilanmart.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/admin/products", "/admin/product/delete"})
public class AdminProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Product> products = productService.getAllProducts();
            req.setAttribute("products", products);
            req.getRequestDispatcher("/admin-products.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading products: " + e.getMessage());
            req.getRequestDispatcher("/500.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String path = req.getServletPath();

        try {
            if ("/admin/product/delete".equals(path)) {
                Long productId = Long.parseLong(req.getParameter("id"));
                productService.deleteProduct(productId, null); // null sellerId allows admin deletion
                session.setAttribute("flashSuccess", "Product listing removed from marketplace.");
            }
        } catch (Exception e) {
            session.setAttribute("flashError", "Failed to remove product: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }
}
