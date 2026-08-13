<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products Marketplace - Akhilan Mart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/responsive.css">
    <style>
        .filter-bar {
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: var(--radius-lg);
            padding: 1.25rem 1.5rem;
            margin: 2rem 0;
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            justify-content: space-between;
            gap: 1rem;
        }

        .search-form {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            flex-grow: 1;
            max-width: 450px;
        }

        .search-input {
            width: 100%;
            padding: 0.65rem 1rem;
            background: var(--surface-secondary);
            border: 1px solid var(--border);
            border-radius: var(--radius-md);
            color: var(--text-primary);
            font-family: var(--font-body);
        }

        .category-pills {
            display: flex;
            flex-wrap: wrap;
            gap: 0.5rem;
            margin-bottom: 2rem;
        }

        .cat-pill {
            padding: 0.5rem 1.25rem;
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: var(--radius-full);
            color: var(--text-secondary);
            font-size: 0.9rem;
            font-weight: 600;
            transition: var(--transition);
        }

        .cat-pill:hover, .cat-pill.active {
            background: var(--primary);
            border-color: var(--primary);
            color: #FFF;
        }
    </style>
</head>
<body>

    <!-- NAVBAR -->
    <nav class="navbar">
        <div class="container nav-container">
            <a href="${pageContext.request.contextPath}/" class="brand-logo">
                <span class="brand-akhilan">Akhilan</span><span class="brand-mart">Mart</span>
            </a>
            <ul class="nav-links">
                <li><a href="${pageContext.request.contextPath}/" class="nav-link">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/products" class="nav-link active">Products</a></li>
                <c:if test="${sessionScope.user != null}">
                    <c:choose>
                        <c:when test="${sessionScope.user.role == 'SELLER'}">
                            <li><a href="${pageContext.request.contextPath}/seller/dashboard" class="nav-link">Seller Panel</a></li>
                        </c:when>
                        <c:when test="${sessionScope.user.role == 'ADMIN'}">
                            <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link">Admin Dashboard</a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="${pageContext.request.contextPath}/orders" class="nav-link">My Orders</a></li>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </ul>
            <div class="nav-actions">
                <a href="${pageContext.request.contextPath}/cart" class="btn btn-outline btn-sm">🛒 Cart</a>
                <c:if test="${sessionScope.user != null}">
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm">Logout</a>
                </c:if>
                <c:if test="${sessionScope.user == null}">
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-sm">Login</a>
                </c:if>
            </div>
        </div>
    </nav>

    <div class="container">
        <!-- HEADER -->
        <div style="margin-top: 2.5rem;">
            <h1 class="section-title" style="font-size: 2.2rem;">Marketplace Products</h1>
            <p style="color: var(--text-secondary);">Browse authentic listings from independent verified sellers</p>
        </div>

        <!-- SEARCH & FILTER BAR -->
        <div class="filter-bar">
            <form action="${pageContext.request.contextPath}/products" method="get" class="search-form">
                <input type="hidden" name="category" value="${selectedCategory}">
                <input type="hidden" name="sortBy" value="${selectedSortBy}">
                <input type="text" name="search" class="search-input" placeholder="Search by product name or description..." value="${searchQuery}">
                <button type="submit" class="btn btn-primary btn-sm">Search</button>
            </form>

            <div style="display: flex; align-items: center; gap: 0.75rem;">
                <label style="font-size: 0.85rem; color: var(--text-secondary); font-weight: 600;">Sort By:</label>
                <form action="${pageContext.request.contextPath}/products" method="get" style="margin:0;">
                    <input type="hidden" name="search" value="${searchQuery}">
                    <input type="hidden" name="category" value="${selectedCategory}">
                    <select name="sortBy" onchange="this.form.submit()" class="search-input" style="padding: 0.4rem 0.8rem; font-size: 0.85rem;">
                        <option value="latest" ${selectedSortBy == 'latest' ? 'selected' : ''}>Latest Arrivals</option>
                        <option value="price_asc" ${selectedSortBy == 'price_asc' ? 'selected' : ''}>Price: Low to High</option>
                        <option value="price_desc" ${selectedSortBy == 'price_desc' ? 'selected' : ''}>Price: High to Low</option>
                        <option value="rating" ${selectedSortBy == 'rating' ? 'selected' : ''}>Highest Rated</option>
                    </select>
                </form>
            </div>
        </div>

        <!-- CATEGORY PILLS -->
        <div class="category-pills">
            <a href="${pageContext.request.contextPath}/products?search=${searchQuery}&category=All&sortBy=${selectedSortBy}" 
               class="cat-pill ${selectedCategory == 'All' || empty selectedCategory ? 'active' : ''}">All Categories</a>
            <c:forEach items="${categories}" var="cat">
                <a href="${pageContext.request.contextPath}/products?search=${searchQuery}&category=${cat}&sortBy=${selectedSortBy}" 
                   class="cat-pill ${selectedCategory == cat ? 'active' : ''}">${cat}</a>
            </c:forEach>
        </div>

        <!-- PRODUCT GRID -->
        <c:choose>
            <c:when test="${not empty products}">
                <div class="product-grid">
                    <c:forEach items="${products}" var="prod">
                        <div class="product-card">
                            <div class="product-img-wrapper">
                                <img src="${prod.imageUrl}" alt="${prod.name}" loading="lazy">
                                <span class="category-badge">${prod.category}</span>
                            </div>
                            <div class="product-info">
                                <div class="product-seller">Seller: <strong>${prod.sellerName}</strong></div>
                                <a href="${pageContext.request.contextPath}/product-details?id=${prod.id}">
                                    <h3 class="product-title">${prod.name}</h3>
                                </a>
                                <div class="rating-stars">
                                    ★ <fmt:formatNumber value="${prod.averageRating}" maxFractionDigits="1" minFractionDigits="1" />
                                    <span class="rating-count">(${prod.reviewCount} reviews)</span>
                                </div>
                                <div class="product-footer">
                                    <div>
                                        <div class="product-price">₹<fmt:formatNumber value="${prod.price}" pattern="#,##0.00"/></div>
                                        <c:choose>
                                            <c:when test="${prod.stock > 0}">
                                                <span class="stock-tag in-stock">${prod.stock} in stock</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="stock-tag out-stock">Out of stock</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <form action="${pageContext.request.contextPath}/cart/add" method="post">
                                        <input type="hidden" name="productId" value="${prod.id}">
                                        <input type="hidden" name="redirectUrl" value="${pageContext.request.requestURI}?${pageContext.request.queryString}">
                                        <c:choose>
                                            <c:when test="${prod.stock > 0}">
                                                <button type="submit" class="btn btn-accent btn-sm">Add to Cart</button>
                                            </c:when>
                                            <c:otherwise>
                                                <button type="button" class="btn btn-outline btn-sm" disabled>Out of Stock</button>
                                            </c:otherwise>
                                        </c:choose>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div style="text-align: center; padding: 4rem 1rem; background: var(--surface); border: 1px dashed var(--border); border-radius: var(--radius-lg); margin-bottom: 4rem;">
                    <div style="font-size: 3rem; margin-bottom: 1rem;">🔍</div>
                    <h3>No products found</h3>
                    <p style="color: var(--text-secondary); margin-bottom: 1.5rem;">Try refining your search terms or clearing selected category filters.</p>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Reset All Filters</a>
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
</body>
</html>
