<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Akhilan Mart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
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
                <a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-sm">Register</a>
            </div>
        </div>
    </nav>

    <!-- AUTH FORM CONTAINER -->
    <div class="auth-container">
        <div class="auth-card">
            <div class="auth-header">
                <h1 class="auth-title">Welcome Back</h1>
                <p class="auth-subtitle">Log in to your Akhilan Mart account</p>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="flash-message flash-error">
                    ⚠️ ${errorMessage}
                </div>
            </c:if>
            <c:if test="${not empty sessionScope.flashError}">
                <div class="flash-message flash-error">
                    ⚠️ ${sessionScope.flashError}
                </div>
                <c:remove var="flashError" scope="session" />
            </c:if>
            <c:if test="${param.logout == 'success'}">
                <div class="flash-message flash-success">
                    ✅ You have been logged out successfully.
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="form-group">
                    <label class="form-label" for="email">Email Address</label>
                    <input type="email" id="email" name="email" class="form-control" value="${email}" placeholder="e.g. buyer@akhilanmart.com" required autofocus>
                </div>

                <div class="form-group">
                    <label class="form-label" for="password">Password</label>
                    <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required>
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 1rem;">Sign In</button>
            </form>

            <div class="auth-footer">
                Don't have an account? <a href="${pageContext.request.contextPath}/register">Create one now</a>
            </div>

            <!-- DEMO CREDENTIALS BOX -->
            <div class="demo-credentials-box">
                <div class="demo-credentials-title">🔑 Capstone Demo Accounts</div>
                <div style="line-height: 1.5; color: var(--text-secondary);">
                    <div><strong>Admin:</strong> admin@akhilanmart.com / Admin@123</div>
                    <div><strong>Buyer:</strong> buyer@akhilanmart.com / Buyer@123</div>
                    <div><strong>Seller:</strong> seller@akhilanmart.com / Seller@123</div>
                </div>
            </div>
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
