package com.pioneer.pixel.test.task.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

@Data
@Schema(description = "Request payload for user login. Provide email or phone and password.")
public class LoginRequest {
    @JsonProperty("username")
    @NotBlank
    @Schema(description = "Email address or phone number of the user")
    private String username;

    @JsonProperty("password")
    @NotBlank
    @Schema(description = "User's password")
    private String password;
}