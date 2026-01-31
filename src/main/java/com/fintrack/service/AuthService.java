package com.fintrack.service;

import com.fintrack.dto.LoginRequest;
import com.fintrack.dto.LoginResponse;
import com.fintrack.dto.UserRegisterRequest;
import com.fintrack.dto.UserRegisterResponse;
import com.fintrack.entity.User;
import com.fintrack.exception.AuthException;
import com.fintrack.exception.DuplicateEmailException;
import com.fintrack.repository.UserRepository;
import com.fintrack.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenUtil tokenUtil;


    public UserRegisterResponse registerUser(UserRegisterRequest request) {
        log.info("Attempting to register user with email: {}", request.getEmail());

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed: Email already exists - {}", request.getEmail());
            throw new DuplicateEmailException("Email already registered: " + request.getEmail());
        }

        // Create new User entity
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        // Encrypt password
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encryptedPassword);

        // Save to database
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        // Convert Entity to DTO
        return mapToUserResponse(savedUser);
    }


    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        // STEP 1: Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: User not found - {}", request.getEmail());
                    // SECURITY: Generic message - don't reveal if email exists
                    return new AuthException("Invalid email or password");
                });

        // STEP 2: Verify password
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!passwordMatches) {
            log.warn("Login failed: Invalid password for user - {}", request.getEmail());
            // SECURITY: Same generic message
            throw new AuthException("Invalid email or password");
        }

        // STEP 3: Generate JWT token
        String token = tokenUtil.generateToken(user.getId(), user.getEmail());
        log.info("Login successful for user ID: {} - Token generated", user.getId());

        // STEP 4: Build response
        return LoginResponse.builder()
                .accessToken(token)
                .user(mapToUserResponse(user))
                .build();
    }


    private UserRegisterResponse mapToUserResponse(User user) {
        UserRegisterResponse response = new UserRegisterResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}
