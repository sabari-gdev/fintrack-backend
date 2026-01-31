package com.fintrack.controller;

import com.fintrack.dto.ApiResponse;
import com.fintrack.dto.UserRegisterResponse;
import com.fintrack.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User Controller
 * <p>
 * Handles user profile operations
 * All endpoints require JWT authentication
 * <p>
 * PROTECTED ENDPOINTS:
 * - GET /api/users/me - Get current user profile
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    /**
     * Get Current User Profile
     * <p>
     * URL: GET <a href="http://localhost:8080/api/users/me">...</a>
     * Headers: Authorization: Bearer <jwt-token>
     * <p>
     * Returns the currently authenticated user's profile
     * <p>
     * HOW IT WORKS:
     * 1. JWT filter validates token
     * 2. JWT filter loads user from database
     * 3. JWT filter sets authentication in SecurityContext
     * 4. Spring automatically injects Authentication object
     * 5. We extract User from authentication
     *
     * @param authentication - Injected by Spring Security (contains current user)
     * @return ApiResponse with user profile
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserRegisterResponse>> getCurrentUser(
            Authentication authentication) {

        // Extract user from authentication (set by JWT filter)
        User user = (User) authentication.getPrincipal();

        log.info("Fetching profile for user ID: {}", user.getId());

        // Convert Entity to DTO (don't send password!)
        UserRegisterResponse userResponse = new UserRegisterResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());

        ApiResponse<UserRegisterResponse> response = ApiResponse.success(
                "User profile details retrieved successfully",
                userResponse
        );

        return ResponseEntity.ok(response);
    }
}