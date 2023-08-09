package david.zhu;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

// Do not implement javax.servlet.Filter interface, keep your code in Spring world
// Spring guarantees that the OncePerRequestFilter is executed only once for a given request.
public class RobotFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;

    // 实际传入的是 RobotAuthenticationProvider, 在SecurityFilterChain被调用
    public RobotFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // We use AuthenticationManager to transform an authentication request
    // into an authenticated object
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // 0. Should execute filter
        String HEADER_NAME = "x-robot-password";
        if (!Collections.list(request.getHeaderNames()).contains(HEADER_NAME)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1. Authentication Decision
        // 可以对比一下上一版的RobotFilter实现, 在该视频的1:53:55, https://youtu.be/iJ2muJniikY
        var password = request.getHeader(HEADER_NAME);
        var authRequest = RobotAuthentication.unauthenticated(password);

        try {
            // authenticationManager其实就是RobotAuthenticationProvider
            var authentication = authenticationManager.authenticate(authRequest);
            var newContext = SecurityContextHolder.createEmptyContext();
            newContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(newContext);
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            // Because use emoji here, set encoding utf-8
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-type", "text/plain;charset=utf-8");
            response.getWriter().println(e.getMessage());
        }
    }
}
