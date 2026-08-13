<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Akhilan Mart - Centralized Multi-Seller Marketplace</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
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
                <li><a href="${pageContext.request.contextPath}/" class="nav-link active">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/products" class="nav-link">Products</a></li>
                <li><a href="${pageContext.request.contextPath}/products?category=Electronics" class="nav-link">Categories</a></li>
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
                <c:if test="${sessionScope.user != null}">
                    <a href="${pageContext.request.contextPath}/cart" class="btn btn-outline btn-sm">
                        🛒 Cart
                    </a>
                    <span class="user-greeting">Hi, <strong>${sessionScope.user.name}</strong></span>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm">Logout</a>
                </c:if>
                <c:if test="${sessionScope.user == null}">
                    <a href="${pageContext.request.contextPath}/cart" class="btn btn-outline btn-sm">🛒 Cart</a>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-outline btn-sm">Login</a>
                    <a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-sm">Get Started</a>
                </c:if>
            </div>
        </div>
    </nav>

    <!-- FLASH MESSAGES -->
    <div class="container" style="margin-top: 1.5rem;">
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
    </div>

    <!-- HERO SECTION -->
    <section class="hero-section">
        <div class="container hero-content">
            <div class="hero-pill">
                🚀 Multi-Seller Marketplace Platform
            </div>
            <h1 class="hero-title">
                Discover products from trusted sellers, <span>all in one marketplace.</span>
            </h1>
            <p class="hero-subtitle">
                Explore thousands of verified listings across Electronics, Books, Clothing, Accessories, and Home essentials at competitive prices.
            </p>
            <div class="hero-cta">
                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary btn-lg">Explore Products</a>
                <a href="${pageContext.request.contextPath}/register?role=SELLER" class="btn btn-outline btn-lg">Become a Seller</a>
            </div>
        </div>
    </section>

    <!-- CATEGORIES SECTION -->
    <section class="container" style="margin-top: 4rem;">
        <div class="section-header">
            <div>
                <h2 class="section-title">Shop by Category</h2>
                <p style="color: var(--text-secondary);">Browse top categories curated from independent sellers</p>
            </div>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline btn-sm">View All →</a>
        </div>

        <div class="category-grid">
            <a href="${pageContext.request.contextPath}/products?category=Accessories" class="category-card">
                <div class="category-icon">🎒</div>
                <div class="category-name">Accessories</div>
            </a>
            <a href="${pageContext.request.contextPath}/products?category=Books" class="category-card">
                <div class="category-icon">📚</div>
                <div class="category-name">Books</div>
            </a>
            <a href="${pageContext.request.contextPath}/products?category=Clothing" class="category-card">
                <div class="category-icon">👕</div>
                <div class="category-name">Clothing</div>
            </a>
            <a href="${pageContext.request.contextPath}/products?category=Electronics" class="category-card">
                <div class="category-icon">🎧</div>
                <div class="category-name">Electronics</div>
            </a>
            <a href="${pageContext.request.contextPath}/products?category=Home" class="category-card">
                <div class="category-icon">🏡</div>
                <div class="category-name">Home</div>
            </a>
        </div>
    </section>

    <!-- FEATURED PRODUCTS -->
    <section class="container" style="margin-bottom: 4rem;">
        <div class="section-header">
            <div>
                <h2 class="section-title">Featured Marketplace Listings</h2>
                <p style="color: var(--text-secondary);">Handpicked products with top ratings</p>
            </div>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary btn-sm">Start Shopping →</a>
        </div>

        <div class="product-grid">
            <c:forEach items="${featuredProducts}" var="prod">
                <div class="product-card">
                    <div class="product-img-wrapper">
                        <img src="${prod.imageUrl}" alt="${prod.name}" loading="lazy">
                        <span class="category-badge">${prod.category}</span>
                    </div>
                    <div class="product-info">
                        <div class="product-seller">Sold by <strong>${prod.sellerName}</strong></div>
                        <a href="${pageContext.request.contextPath}/product-details?id=${prod.id}">
                            <h3 class="product-title">${prod.name}</h3>
                        </a>
                        <div class="rating-stars">
                            ★ <fmt:formatNumber value="${prod.averageRating}" maxFractionDigits="1" minFractionDigits="1" />
                            <span class="rating-count">(${prod.reviewCount})</span>
                        </div>
                        <div class="product-footer">
                            <span class="product-price">₹<fmt:formatNumber value="${prod.price}" pattern="#,##0.00"/></span>
                            <form action="${pageContext.request.contextPath}/cart/add" method="post">
                                <input type="hidden" name="productId" value="${prod.id}">
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
    </section>

    <!-- WHY AKILAN MART -->
    <section style="background: var(--surface); border-top: 1px solid var(--border); border-bottom: 1px solid var(--border); padding: 4rem 0;">
        <div class="container">
            <div style="text-align: center; max-width: 600px; margin: 0 auto 3rem;">
                <h2 class="section-title">Why Choose Akhilan Mart?</h2>
                <p style="color: var(--text-secondary);">Connecting independent sellers with global buyers seamlessly</p>
            </div>
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 2rem;">
                <div style="text-align: center; padding: 1.5rem;">
                    <div style="font-size: 2.5rem; margin-bottom: 1rem;">🛡️</div>
                    <h4 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 0.5rem;">Trusted Sellers</h4>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">All sellers undergo verification to ensure high quality products and authentic customer ratings.</p>
                </div>
                <div style="text-align: center; padding: 1.5rem;">
                    <div style="font-size: 2.5rem; margin-bottom: 1rem;">⚡</div>
                    <h4 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 0.5rem;">Easy Shopping</h4>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">Intuitive search, real-time stock checks, dynamic cart calculations, and responsive interface.</p>
                </div>
                <div style="text-align: center; padding: 1.5rem;">
                    <div style="font-size: 2.5rem; margin-bottom: 1rem;">🔒</div>
                    <h4 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 0.5rem;">Secure Accounts</h4>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">Industry-standard jBCrypt password hashing protects buyer, seller, and administrator accounts.</p>
                </div>
                <div style="text-align: center; padding: 1.5rem;">
                    <div style="font-size: 2.5rem; margin-bottom: 1rem;">📦</div>
                    <h4 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 0.5rem;">ACID Checkout</h4>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">Transactional order execution guarantees inventory integrity with automatic commit and rollback protection.</p>
                </div>
            </div>
        </div>
    </section>

    <!-- FOOTER -->
    <footer>
        <div class="container">
            <div class="footer-grid">
                <div>
                    <div class="brand-logo" style="margin-bottom: 1rem;">
                        <span class="brand-akhilan">Akhilan</span><span class="brand-mart">Mart</span>
                    </div>
                    <p style="font-size: 0.9rem; margin-bottom: 1rem;">
                        A centralized multi-seller e-commerce marketplace platform empowering sellers and connecting buyers.
                    </p>
                </div>
                <div class="footer-col">
                    <h4>Quick Links</h4>
                    <ul class="footer-links">
                        <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                        <li><a href="${pageContext.request.contextPath}/products">Browse Products</a></li>
                        <li><a href="${pageContext.request.contextPath}/login">Account Login</a></li>
                        <li><a href="${pageContext.request.contextPath}/register">Register Account</a></li>
                    </ul>
                </div>
                <div class="footer-col">
                    <h4>Categories</h4>
                    <ul class="footer-links">
                        <li><a href="${pageContext.request.contextPath}/products?category=Accessories">Accessories</a></li>
                        <li><a href="${pageContext.request.contextPath}/products?category=Books">Books</a></li>
                        <li><a href="${pageContext.request.contextPath}/products?category=Clothing">Clothing</a></li>
                        <li><a href="${pageContext.request.contextPath}/products?category=Electronics">Electronics</a></li>
                        <li><a href="${pageContext.request.contextPath}/products?category=Home">Home</a></li>
                    </ul>
                </div>
                <div class="footer-col">
                    <h4>Capstone Demo</h4>
                    <p style="font-size: 0.85rem; color: var(--text-secondary);">
                        College Capstone MVP built with Java Servlets, JSP, H2 DB, HikariCP, and jBCrypt.
                    </p>
                </div>
            </div>
            <div class="footer-bottom">
                &copy; 2026 Akhilan Mart. All rights reserved. Capstone Project Identifier: <code>akhilanmart</code>.
            </div>
        </div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
