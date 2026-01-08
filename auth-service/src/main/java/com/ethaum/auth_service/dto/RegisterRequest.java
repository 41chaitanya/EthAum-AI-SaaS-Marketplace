package com.ethaum.auth_service.dto;

import com.ethaum.auth_service.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @Email
    private String email;

    @NotBlank
    private String password;

    private Role role; // STARTUP / ENTERPRISE
}
