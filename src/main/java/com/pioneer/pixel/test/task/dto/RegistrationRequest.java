package com.pioneer.pixel.test.task.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Request payload for registering a new user.")
@Data
public class RegistrationRequest {
    @NotBlank
    @Size(max = 500)
    @Schema(description = "User's full name (max 500 characters)")
    private String name;

    @NotNull
    @JsonProperty("dateOfBirth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "User's date of birth in yyyy-MM-dd format")
    private LocalDate dateOfBirth;

    @NotBlank
    @JsonProperty("password")
    @Schema(description = "Password for authentication (will be stored encrypted)")
    private String password;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Schema(description = "Initial deposit balance for the user's account (must be > 0)")
    private BigDecimal initialBalance;

    @Email
    @NotBlank
    @Schema(description = "Unique email address for the user")
    private String email;

    @NotBlank
    @Pattern(regexp = "\\d{11}")
    @Schema(description = "Unique phone number of 11 digits, e.g., 79207865432")
    private String phone;
}