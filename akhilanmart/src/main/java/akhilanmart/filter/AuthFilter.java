package akhilanmart.filter;

import akhilanmart.model.Role;
import akhilanmart.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/cart/*", "/checkout/*", "/orders/*", "/seller/*", "/admin/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = uri.substring(contextPath.length());

        if (user == null) {
            session = httpRequest.getSession(true);
            session.setAttribute("flashError", "Please log in to access this page.");
            session.setAttribute("redirectAfterLogin", uri);
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }

        // Role-based authorization
        if (path.startsWith("/seller") && user.getRole() != Role.SELLER && user.getRole() != Role.ADMIN) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Seller account required.");
            return;
        }

        if (path.startsWith("/admin") && user.getRole() != Role.ADMIN) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Administrator privileges required.");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
