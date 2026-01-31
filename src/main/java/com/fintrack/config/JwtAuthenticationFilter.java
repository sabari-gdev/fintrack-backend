package com.fintrack.config;

import com.fintrack.entity.User;
import com.fintrack.repository.UserRepository;
import com.fintrack.util.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JWT Authentication Filter
 * <p>
 * This filter intercepts EVERY request and checks for JWT token.
 * <p>
 * Execution Flow:
 * 1. Extract "Authorization" header from request
 * 2. Extract JWT token (format: "Bearer <token>")
 * 3. Validate token using TokenUtil
 * 4. Extract user ID from token
 * 5. Load user from database
 * 6. Set authentication in Spring Security context
 * 7. Pass request to next filter/controller
 * <p>
 * WHY OncePerRequestFilter?
 * - Guarantees filter runs once per request
 * - Prevents duplicate filtering
 * - Spring Boot best practice
 * <p>
 * ENTERPRISE PATTERN:
 * - Stateless authentication (no sessions)
 * - Token validation on every request
 * - User loaded fresh from database
 * - Works with microservices architecture
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;

    /**
     * Main filter method - called for every HTTP request
     *
     * @param request     - HTTP request
     * @param response    - HTTP response
     * @param filterChain - Chain of filters
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            // STEP 1: Extract JWT token from request header
            String jwt = extractJwtFromRequest(request);

            // STEP 2: If token exists and is valid
            if (jwt != null && tokenUtil.validateToken(jwt)) {

                // STEP 3: Extract user ID from token
                Long userId = tokenUtil.getUserIdFromToken(jwt);

                // STEP 4: Load user from database
                User user = userRepository.findById(userId).orElse(null);

                if (user != null) {
                    // STEP 5: Create authentication object
                    // This tells Spring Security: "This user is authenticated"
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,                    // Principal (authenticated user)
                                    null,                    // Credentials (not needed - already authenticated)
                                    new ArrayList<>()        // Authorities (roles - we'll add later)
                            );

                    // Set additional details (IP address, session ID, etc.)
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // STEP 6: Set authentication in Spring Security context
                    // Now controllers can access current user via SecurityContextHolder
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.debug("JWT authentication successful for user ID: {}", userId);
                } else {
                    log.warn("User not found for ID: {}", userId);
                }
            }

        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        // STEP 7: Continue filter chain (pass to next filter or controller)
        filterChain.doFilter(request, response);
    }

    /**
     * Extract JWT token from Authorization header
     * <p>
     * Expected header format: "Bearer eyJhbGciOiJIUzI1NiJ9..."
     *
     * @param request - HTTP request
     * @return JWT token string (without "Bearer " prefix), or null if not found
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Check if header exists and starts with "Bearer "
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // Remove "Bearer " prefix and return token
            return bearerToken.substring(7);
        }

        return null;
    }
}