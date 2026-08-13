package akhilanmart.controller;

import akhilanmart.model.Product;
import akhilanmart.model.User;
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

@WebServlet(urlPatterns = {"/seller/products", "/seller/product/new", "/seller/product/edit", "/seller/product/delete"})
public class SellerProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User seller = (User) session.getAttribute("user");
        String path = req.getServletPath();

        try {
            if ("/seller/product/new".equals(path)) {
                req.setAttribute("categories", ProductService.CATEGORIES);
                req.getRequestDispatcher("/seller-product-form.jsp").forward(req, resp);

            } else if ("/seller/product/edit".equals(path)) {
                String idStr = req.getParameter("id");
                if (idStr == null) {
                    resp.sendRedirect(req.getContextPath() + "/seller/products");
                    return;
                }
                Long productId = Long.parseLong(idStr);
                Product product = productService.getProductById(productId);

                if (product == null || !product.getSellerId().equals(seller.getId())) {
                    session.setAttribute("flashError", "Product not found or unauthorized access.");
                    resp.sendRedirect(req.getContextPath() + "/seller/products");
                    return;
                }

                req.setAttribute("product", product);
                req.setAttribute("categories", ProductService.CATEGORIES);
                req.getRequestDispatcher("/seller-product-form.jsp").forward(req, resp);

            } else {
                // Listing seller products
                List<Product> products = productService.getProductsBySeller(seller.getId());
                req.setAttribute("products", products);
                req.getRequestDispatcher("/seller-products.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error processing request: " + e.getMessage());
            req.getRequestDispatcher("/500.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User seller = (User) session.getAttribute("user");
        String path = req.getServletPath();

        try {
            if ("/seller/product/delete".equals(path)) {
                Long productId = Long.parseLong(req.getParameter("id"));
                productService.deleteProduct(productId, seller.getId());
                session.setAttribute("flashSuccess", "Product successfully deleted.");
                resp.sendRedirect(req.getContextPath() + "/seller/products");
                return;
            }

            String idStr = req.getParameter("id");
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            BigDecimal price = new BigDecimal(req.getParameter("price"));
            Integer stock = Integer.parseInt(req.getParameter("stock"));
            String category = req.getParameter("category");
            String imageUrl = req.getParameter("imageUrl");

            if (idStr != null && !idStr.trim().isEmpty()) {
                // Update existing product
                Long productId = Long.parseLong(idStr);
                productService.updateProduct(productId, seller.getId(), name, description, price, stock, category, imageUrl);
                session.setAttribute("flashSuccess", "Product updated successfully!");
            } else {
                // Create new product
                productService.addProduct(seller.getId(), name, description, price, stock, category, imageUrl);
                session.setAttribute("flashSuccess", "New product published successfully!");
            }

            resp.sendRedirect(req.getContextPath() + "/seller/products");

        } catch (Exception e) {
            session.setAttribute("flashError", e.getMessage());
            req.setAttribute("categories", ProductService.CATEGORIES);
            if (req.getParameter("id") != null && !req.getParameter("id").trim().isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/seller/product/edit?id=" + req.getParameter("id"));
            } else {
                resp.sendRedirect(req.getContextPath() + "/seller/product/new");
            }
        }
    }
}
