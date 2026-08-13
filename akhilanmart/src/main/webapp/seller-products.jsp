<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Products - Seller Panel - Akhilan Mart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/responsive.css">
</head>
<body>

    <!-- NAVBAR -->
    <nav class="navbar">
        <div class="container nav-container">
            <a href="${pageContext.request.contextPath}/" class="brand-logo">
                <span class="brand-akhilan">Akhilan</span><span class="brand-mart">Mart</span>
            </a>
            <ul class="nav-links">
                <li><a href="${pageContext.request.contextPath}/seller/dashboard" class="nav-link">Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/seller/products" class="nav-link active">My Products</a></li>
                <li><a href="${pageContext.request.contextPath}/seller/orders" class="nav-link">Customer Orders</a></li>
            </ul>
            <div class="nav-actions">
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm">Logout</a>
            </div>
        </div>
    </nav>

    <!-- PRODUCTS CONTAINER -->
    <div class="container" style="margin-top: 2.5rem; margin-bottom: 4rem;">
        <div class="dashboard-header">
            <div>
                <h1 class="dashboard-title">My Product Inventory</h1>
                <p style="color: var(--text-secondary);">Manage and update listings published under your seller account</p>
            </div>
            <a href="${pageContext.request.contextPath}/seller/product/new" class="btn btn-primary">➕ Create New Listing</a>
        </div>

        <c:if test="${not empty sessionScope.flashSuccess}">
            <div class="flash-message flash-success">
                ✅ ${sessionScope.flashSuccess}
            </div>
            <c:remove var="flashSuccess" scope="session" />
        </c:if>
        <c:if test="${not empty sessionScope.flashError}">
            <div class="flash-message flash-error">
                ⚠️ ${sessionScope.flashError}
            </div>
            <c:remove var="flashError" scope="session" />
        </c:if>

        <c:choose>
            <c:when test="${not empty products}">
                <div class="table-wrapper">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Product Details</th>
                                <th>Category</th>
                                <th>Price</th>
                                <th>Stock</th>
                                <th>Rating</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${products}" var="prod">
                                <tr>
                                    <td>
                                        <div style="display: flex; align-items: center; gap: 0.85rem;">
                                            <img src="${prod.imageUrl}" alt="${prod.name}" style="width: 48px; height: 48px; object-fit: cover; border-radius: var(--radius-sm);">
                                            <div>
                                                <strong>${prod.name}</strong>
                                                <div style="font-size: 0.75rem; color: var(--text-secondary);">ID: #${prod.id}</div>
                                            </div>
                                        </div>
                                    </td>
                                    <td><span class="category-badge" style="position:static;">${prod.category}</span></td>
                                    <td style="font-weight: 700; color: var(--accent);">₹<fmt:formatNumber value="${prod.price}" pattern="#,##0.00"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${prod.stock > 0}">
                                                <span class="stock-tag in-stock">${prod.stock} left</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="stock-tag out-stock">Out of stock</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <div style="color: #FBBF24; font-size: 0.85rem;">
                                            ★ <fmt:formatNumber value="${prod.averageRating}" maxFractionDigits="1" minFractionDigits="1" /> (${prod.reviewCount})
                                        </div>
                                    </td>
                                    <td>
                                        <div style="display: flex; gap: 0.5rem;">
                                            <a href="${pageContext.request.contextPath}/seller/product/edit?id=${prod.id}" class="btn btn-outline btn-sm">Edit</a>
                                            <form action="${pageContext.request.contextPath}/seller/product/delete" method="post" style="margin:0;">
                                                <input type="hidden" name="id" value="${prod.id}">
                                                <button type="submit" class="btn btn-danger btn-sm" onclick="return confirmDelete('Delete this product listing permanently?')">Delete</button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div style="text-align: center; padding: 4rem 1rem; background: var(--surface); border: 1px dashed var(--border); border-radius: var(--radius-lg);">
                    <div style="font-size: 3.5rem; margin-bottom: 1rem;">📦</div>
                    <h2>No products listed yet</h2>
                    <p style="color: var(--text-secondary); margin-bottom: 1.5rem;">Start selling by adding your first product listing.</p>
                    <a href="${pageContext.request.contextPath}/seller/product/new" class="btn btn-primary">Add Product Now</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- FOOTER -->
    <footer>
        <div class="container footer-bottom">
            &copy; 2026 Akhilan Mart. Capstone Project Identifier: <code>akhilanmart</code>.
        </div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script src="${pageContext.request.contextPath}/js/cart.js"></script>
</body>
</html>
