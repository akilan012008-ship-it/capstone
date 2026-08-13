<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout - Akhilan Mart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
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
                <li><a href="${pageContext.request.contextPath}/cart" class="nav-link">← Back to Cart</a></li>
            </ul>
        </div>
    </nav>

    <!-- MAIN CHECKOUT CONTAINER -->
    <div class="container" style="margin-top: 2.5rem; margin-bottom: 4rem;">
        <h1 class="section-title" style="margin-bottom: 0.5rem;">Marketplace Checkout</h1>
        <p style="color: var(--text-secondary); margin-bottom: 2rem;">Confirm your shipping details to place your mock order</p>

        <c:if test="${not empty sessionScope.flashError}">
            <div class="flash-message flash-error">
                ⚠️ ${sessionScope.flashError}
            </div>
            <c:remove var="flashError" scope="session" />
        </c:if>

        <form action="${pageContext.request.contextPath}/checkout" method="post">
            <div style="display: grid; grid-template-columns: 1.8fr 1.2fr; gap: 2rem;">
                <!-- SHIPPING & PAYMENT FORM -->
                <div class="card-panel">
                    <h3 class="panel-title">Shipping & Customer Information</h3>

                    <div class="form-group">
                        <label class="form-label">Full Name</label>
                        <input type="text" class="form-control" value="${user.name}" readonly style="opacity: 0.8;">
                    </div>

                    <div class="form-group">
                        <label class="form-label">Email Address</label>
                        <input type="email" class="form-control" value="${user.email}" readonly style="opacity: 0.8;">
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="address">Delivery Address</label>
                        <textarea id="address" name="address" rows="3" class="form-control" placeholder="Enter complete street address, city, state, postal code..." required>123 College Campus Rd, Tech Block A</textarea>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="phone">Phone Number</label>
                        <input type="tel" id="phone" name="phone" class="form-control" value="+91 98765 43210" required>
                    </div>

                    <h3 class="panel-title" style="margin-top: 2rem;">Payment Method</h3>
                    <div style="background: var(--surface-secondary); border: 1px solid var(--border); border-radius: var(--radius-md); padding: 1.25rem;">
                        <label style="display: flex; align-items: center; gap: 0.75rem; cursor: pointer;">
                            <input type="radio" name="paymentMethod" value="COD" checked>
                            <div>
                                <strong>Cash on Delivery / Capstone Mock Payment</strong>
                                <div style="font-size: 0.8rem; color: var(--text-secondary);">No real monetary transaction required for capstone demonstration.</div>
                            </div>
                        </label>
                    </div>
                </div>

                <!-- ORDER SUMMARY SIDEBAR -->
                <div class="card-panel" style="height: fit-content;">
                    <h3 class="panel-title">Order Items (${cartItems.size()})</h3>

                    <div style="display: flex; flex-direction: column; gap: 1rem; margin-bottom: 1.5rem;">
                        <c:forEach items="${cartItems}" var="item">
                            <div style="display: flex; justify-content: space-between; font-size: 0.9rem;">
                                <div>
                                    <div style="font-weight: 700;">${item.product.name}</div>
                                    <div style="color: var(--text-secondary); font-size: 0.75rem;">Qty: ${item.quantity} × ₹<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00"/></div>
                                </div>
                                <div style="font-weight: 700; color: var(--accent);">
                                    ₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <div style="border-top: 1px solid var(--border); padding-top: 1rem; margin-bottom: 1.5rem; display: flex; justify-content: space-between; align-items: center;">
                        <span style="font-weight: 700;">Grand Total</span>
                        <span style="font-family: var(--font-heading); font-size: 1.6rem; font-weight: 800; color: var(--accent);">
                            ₹<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/>
                        </span>
                    </div>

                    <button type="submit" class="btn btn-accent btn-lg" style="width: 100%;">Confirm & Place Order →</button>
                </div>
            </div>
        </form>
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
