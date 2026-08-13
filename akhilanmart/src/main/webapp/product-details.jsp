<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.name} - Akhilan Mart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/responsive.css">
    <style>
        .details-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 3rem;
            margin: 3rem 0;
        }

        .details-image {
            width: 100%;
            border-radius: var(--radius-lg);
            border: 1px solid var(--border);
            overflow: hidden;
            background: var(--surface-secondary);
        }

        .details-image img {
            width: 100%;
            height: 450px;
            object-fit: cover;
        }

        .details-info {
            display: flex;
            flex-direction: column;
        }

        .star-rating-input {
            display: flex;
            gap: 0.5rem;
            font-size: 1.8rem;
            color: var(--text-secondary);
            cursor: pointer;
            margin: 0.5rem 0 1rem;
        }

        .star-rating-input span.active {
            color: #FBBF24;
        }

        .review-card {
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: var(--radius-md);
            padding: 1.25rem;
            margin-bottom: 1rem;
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
                <li><a href="${pageContext.request.contextPath}/products" class="nav-link">Products</a></li>
            </ul>
            <div class="nav-actions">
                <a href="${pageContext.request.contextPath}/cart" class="btn btn-outline btn-sm">🛒 Cart</a>
                <c:if test="${sessionScope.user != null}">
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm">Logout</a>
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

    <!-- MAIN PRODUCT DETAILS CONTAINER -->
    <div class="container">
        <div class="details-grid">
            <!-- IMAGE -->
            <div class="details-image">
                <img src="${product.imageUrl}" alt="${product.name}">
            </div>

            <!-- PRODUCT INFO -->
            <div class="details-info">
                <span class="category-badge" style="position:static; display:inline-block; width:fit-content; margin-bottom: 0.75rem;">
                    ${product.category}
                </span>

                <h1 style="font-family: var(--font-heading); font-size: 2.2rem; font-weight: 800; margin-bottom: 0.5rem;">
                    ${product.name}
                </h1>

                <p style="color: var(--text-secondary); margin-bottom: 1rem;">
                    Sold & fulfilled by: <strong>${product.sellerName}</strong>
                </p>

                <div class="rating-stars" style="font-size: 1.1rem; margin-bottom: 1.25rem;">
                    ★ <fmt:formatNumber value="${product.averageRating}" maxFractionDigits="1" minFractionDigits="1" /> / 5.0
                    <span class="rating-count">(${product.reviewCount} customer reviews)</span>
                </div>

                <div style="font-family: var(--font-heading); font-size: 2.4rem; font-weight: 800; color: var(--accent); margin-bottom: 1.25rem;">
                    ₹<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/>
                </div>

                <div style="margin-bottom: 1.5rem;">
                    <c:choose>
                        <c:when test="${product.stock > 0}">
                            <span class="status-badge status-DELIVERED">In Stock (${product.stock} units left)</span>
                        </c:when>
                        <c:otherwise>
                            <span class="status-badge status-CANCELLED">Out of Stock</span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <p style="color: var(--text-secondary); font-size: 1rem; line-height: 1.7; margin-bottom: 2rem;">
                    ${product.description}
                </p>

                <!-- ADD TO CART FORM -->
                <form action="${pageContext.request.contextPath}/cart/add" method="post" style="display: flex; gap: 1rem; align-items: center;">
                    <input type="hidden" name="productId" value="${product.id}">
                    <c:choose>
                        <c:when test="${product.stock > 0}">
                            <div style="width: 100px;">
                                <input type="number" name="quantity" value="1" min="1" max="${product.stock}" class="form-control" style="text-align: center;">
                            </div>
                            <button type="submit" class="btn btn-accent btn-lg">Add to Shopping Cart</button>
                        </c:when>
                        <c:otherwise>
                            <button type="button" class="btn btn-outline btn-lg" disabled>Out of Stock</button>
                        </c:otherwise>
                    </c:choose>
                </form>
            </div>
        </div>

        <!-- REVIEWS SECTION -->
        <div style="margin: 4rem 0;">
            <h2 class="section-title" style="margin-bottom: 1.5rem;">Customer Reviews & Ratings</h2>

            <!-- WRITE REVIEW FORM (IF ELIGIBLE) -->
            <c:if test="${canReview}">
                <div class="review-card" style="background: var(--surface-secondary); border-color: var(--primary); margin-bottom: 2.5rem;">
                    <h3 style="font-family: var(--font-heading); margin-bottom: 0.5rem;">Write a Product Review</h3>
                    <p style="color: var(--text-secondary); font-size: 0.9rem;">You are a verified purchaser of this product.</p>

                    <form action="${pageContext.request.contextPath}/review/add" method="post" style="margin-top: 1rem;">
                        <input type="hidden" name="productId" value="${product.id}">
                        <input type="hidden" id="selectedRatingInput" name="rating" value="5">

                        <label class="form-label">Rating (1 to 5 Stars):</label>
                        <div class="star-rating-input">
                            <span data-value="1" class="active">★</span>
                            <span data-value="2" class="active">★</span>
                            <span data-value="3" class="active">★</span>
                            <span data-value="4" class="active">★</span>
                            <span data-value="5" class="active">★</span>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="comment">Your Comment</label>
                            <textarea id="comment" name="comment" rows="3" class="form-control" placeholder="Describe your experience with this product..." required></textarea>
                        </div>

                        <button type="submit" class="btn btn-primary btn-sm">Submit Review</button>
                    </form>
                </div>
            </c:if>

            <!-- REVIEWS LIST -->
            <c:choose>
                <c:when test="${not empty reviews}">
                    <c:forEach items="${reviews}" var="rev">
                        <div class="review-card">
                            <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem;">
                                <div>
                                    <strong>${rev.userName}</strong>
                                    <span class="status-badge status-CONFIRMED" style="margin-left: 0.5rem; font-size: 0.65rem;">Verified Purchaser</span>
                                </div>
                                <div style="color: #FBBF24; font-weight: 700;">
                                    <c:forEach begin="1" end="${rev.rating}">★</c:forEach>
                                </div>
                            </div>
                            <p style="color: var(--text-secondary); font-size: 0.95rem;">${rev.comment}</p>
                            <div style="font-size: 0.75rem; color: var(--text-secondary); margin-top: 0.5rem;">
                                Reviewed on <fmt:formatDate value="${rev.createdAt}" pattern="dd MMM yyyy, hh:mm a"/>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p style="color: var(--text-secondary); font-style: italic;">No customer reviews yet for this product.</p>
                </c:otherwise>
            </c:choose>
        </div>
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
