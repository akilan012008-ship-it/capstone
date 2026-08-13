<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account - Akhilan Mart</title>
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
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline btn-sm">Login</a>
            </div>
        </div>
    </nav>

    <!-- AUTH CONTAINER -->
    <div class="auth-container">
        <div class="auth-card">
            <div class="auth-header">
                <h1 class="auth-title">Create Account</h1>
                <p class="auth-subtitle">Join Akhilan Mart as a Buyer or Seller</p>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="flash-message flash-error">
                    ⚠️ ${errorMessage}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="post" onsubmit="return validateRegisterForm();">
                <div class="form-group">
                    <label class="form-label">I want to register as:</label>
                    <div class="role-selector">
                        <label class="role-option">
                            <input type="radio" name="role" value="BUYER" ${empty role || role == 'BUYER' ? 'checked' : ''}>
                            <div class="role-box">
                                <div class="role-icon">🛍️</div>
                                <div><strong>Buyer</strong></div>
                            </div>
                        </label>
                        <label class="role-option">
                            <input type="radio" name="role" value="SELLER" ${role == 'SELLER' ? 'checked' : ''}>
                            <div class="role-box">
                                <div class="role-icon">🏪</div>
                                <div><strong>Seller</strong></div>
                            </div>
                        </label>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="regName">Full Name / Store Name</label>
                    <input type="text" id="regName" name="name" class="form-control" value="${name}" placeholder="John Doe" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="regEmail">Email Address</label>
                    <input type="email" id="regEmail" name="email" class="form-control" value="${email}" placeholder="john@example.com" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="regPassword">Password</label>
                    <input type="password" id="regPassword" name="password" class="form-control" placeholder="At least 6 characters" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="regConfirmPassword">Confirm Password</label>
                    <input type="password" id="regConfirmPassword" name="confirmPassword" class="form-control" placeholder="Re-enter password" required>
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 1rem;">Create Account</button>
            </form>

            <div class="auth-footer">
                Already have an account? <a href="${pageContext.request.contextPath}/login">Sign in</a>
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
    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
