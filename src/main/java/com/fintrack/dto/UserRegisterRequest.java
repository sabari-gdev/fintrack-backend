package com.fintrack.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user registration request
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    @NotBlank(message = "Email address is required.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 20, message = "The password should be at least 8 characters and max. 20 characters")
    private String password;

    @NotBlank(message = "First name is required.")
    @Size(min = 2, max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(min = 1, max = 100)
    private String lastName;
}
