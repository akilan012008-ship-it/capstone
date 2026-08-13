<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart - Akhilan Mart</title>
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
                <li><a href="${pageContext.request.contextPath}/" class="nav-link">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/products" class="nav-link">Products</a></li>
            </ul>
            <div class="nav-actions">
                <c:if test="${sessionScope.user != null}">
                    <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline btn-sm">My Orders</a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm">Logout</a>
                </c:if>
            </div>
        </div>
    </nav>

    <!-- MAIN CART CONTAINER -->
    <div class="container" style="margin-top: 2.5rem; margin-bottom: 4rem;">
        <h1 class="section-title" style="margin-bottom: 0.5rem;">Your Shopping Cart</h1>
        <p style="color: var(--text-secondary); margin-bottom: 2rem;">Review items before proceeding to checkout</p>

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
            <c:when test="${not empty cartItems}">
                <div style="display: grid; grid-template-columns: 2.5fr 1fr; gap: 2rem;">
                    <!-- CART TABLE -->
                    <div class="table-wrapper">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Product</th>
                                    <th>Unit Price</th>
                                    <th>Quantity</th>
                                    <th>Subtotal</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${cartItems}" var="item">
                                    <tr>
                                        <td>
                                            <div style="display: flex; align-items: center; gap: 1rem;">
                                                <img src="${item.product.imageUrl}" alt="${item.product.name}" style="width: 54px; height: 54px; object-fit: cover; border-radius: var(--radius-sm);">
                                                <div>
                                                    <a href="${pageContext.request.contextPath}/product-details?id=${item.product.id}" style="font-weight: 700;">
                                                        ${item.product.name}
                                                    </a>
                                                    <div style="font-size: 0.75rem; color: var(--text-secondary);">Seller: ${item.product.sellerName}</div>
                                                </div>
                                            </div>
                                        </td>
                                        <td id="unit-price-${item.productId}" data-price="${item.product.price}">
                                            ₹<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00"/>
                                        </td>
                                        <td>
                                            <div style="display: flex; align-items: center; gap: 0.5rem;">
                                                <button type="button" onclick="updateCartQuantity('${item.productId}', ${item.quantity - 1}, '${pageContext.request.contextPath}')" class="btn btn-outline btn-sm" style="padding: 2px 8px;">-</button>
                                                <span style="font-weight: 700; width: 24px; text-align: center;">${item.quantity}</span>
                                                <button type="button" onclick="updateCartQuantity('${item.productId}', ${item.quantity + 1}, '${pageContext.request.contextPath}')" class="btn btn-outline btn-sm" style="padding: 2px 8px;">+</button>
                                            </div>
                                        </td>
                                        <td id="subtotal-${item.productId}" style="font-weight: 700; color: var(--accent);">
                                            ₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>
                                        </td>
                                        <td>
                                            <form action="${pageContext.request.contextPath}/cart/remove" method="post" style="margin:0;">
                                                <input type="hidden" name="productId" value="${item.productId}">
                                                <button type="submit" class="btn btn-danger btn-sm" onclick="return confirmDelete('Remove this item from your cart?')">🗑️</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <!-- CART SUMMARY -->
                    <div class="card-panel" style="height: fit-content;">
                        <h3 class="panel-title" style="margin-bottom: 1rem;">Order Summary</h3>
                        
                        <div style="display: flex; justify-content: space-between; margin-bottom: 1rem; color: var(--text-secondary);">
                            <span>Subtotal</span>
                            <span style="color: var(--text-primary); font-weight: 600;">₹<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/></span>
                        </div>

                        <div style="display: flex; justify-content: space-between; margin-bottom: 1.5rem; color: var(--text-secondary);">
                            <span>Shipping / Delivery</span>
                            <span style="color: var(--success); font-weight: 600;">FREE</span>
                        </div>

                        <div style="border-top: 1px solid var(--border); padding-top: 1rem; margin-bottom: 1.5rem; display: flex; justify-content: space-between; align-items: center;">
                            <span style="font-weight: 700; font-size: 1.1rem;">Total Amount</span>
                            <span id="cart-grand-total" style="font-family: var(--font-heading); font-size: 1.6rem; font-weight: 800; color: var(--accent);">
                                ₹<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/>
                            </span>
                        </div>

                        <a href="${pageContext.request.contextPath}/checkout" class="btn btn-accent" style="width: 100%; text-align: center;">Proceed to Checkout →</a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div style="text-align: center; padding: 4rem 1rem; background: var(--surface); border: 1px dashed var(--border); border-radius: var(--radius-lg);">
                    <div style="font-size: 3.5rem; margin-bottom: 1rem;">🛒</div>
                    <h2>Your cart is currently empty</h2>
                    <p style="color: var(--text-secondary); margin-bottom: 1.5rem;">Explore thousands of listings from independent sellers.</p>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Start Shopping Now</a>
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
