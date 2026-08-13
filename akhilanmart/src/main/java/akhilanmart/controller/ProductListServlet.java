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

@WebServlet("/products")
public class ProductListServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String searchQuery = req.getParameter("search");
        String category = req.getParameter("category");
        String sortBy = req.getParameter("sortBy");

        try {
            List<Product> products = productService.searchProducts(searchQuery, category, sortBy);

            req.setAttribute("products", products);
            req.setAttribute("searchQuery", searchQuery != null ? searchQuery : "");
            req.setAttribute("selectedCategory", category != null ? category : "All");
            req.setAttribute("selectedSortBy", sortBy != null ? sortBy : "latest");
            req.setAttribute("categories", ProductService.CATEGORIES);

            req.getRequestDispatcher("/products.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error searching products: " + e.getMessage());
            req.getRequestDispatcher("/500.jsp").forward(req, resp);
        }
    }
}
