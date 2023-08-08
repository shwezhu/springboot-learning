package david.zhu;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

// Do not implement javax.servlet.Filter interface, keep your code in Spring world
// Spring guarantees that the OncePerRequestFilter is executed only once for a given request.
public class RobotFilter extends OncePerRequestFilter {
    private final String HEADER_NAME = "x-robot-password";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // 0. Should execute filter?
        // if you don't do this whenever you access your website url on browser,
        // you will get checked if you are Mr Robot, and cannot get the form log in
        if (!Collections.list(request.getHeaderNames()).contains(HEADER_NAME)) {
            filterChain.doFilter(request, response);
        }

        // 1. Authentication Decision
        var password = request.getHeader(HEADER_NAME);
        if (!"beep-boop".equals(password)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            // Because use emoji here, set encoding utf-8
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-type", "text/plain;charset=utf-8");
            response.getWriter().println("You are not Mr Robot 🤖️");
            return;
        }

        // The SecurityContextHolder is where Spring Security stores the details of who is authenticated.
        // The simplest way to indicate a user is authenticated is to set the SecurityContextHolder directly:
        // We start by creating an empty SecurityContext.
        // You should create a new SecurityContext instance instead of using SecurityContextHolder.getContext().setAuthentication(authentication)
        // to avoid race conditions across multiple threads.
        // https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html
        var newContext = SecurityContextHolder.createEmptyContext();
        // We can use newContext.setAuthentication(UsernamePasswordAuthenticationToken.authenticated()) here,
        // but this needs us to provide credentials, principal, and authorities,
        // our robot doesn't have this kind of thing, so we create a new authentication
        newContext.setAuthentication(new RobotAuthentication());
        SecurityContextHolder.setContext(newContext);
        // Every Filter has a doFilter() method, but they are not called by for loop over a list of filters
        // but actually a chain of responsibility, video: https://youtu.be/iJ2muJniikY, at 00:42:53
        filterChain.doFilter(request, response);
        // return;
        // 2. Rest
    }
}
