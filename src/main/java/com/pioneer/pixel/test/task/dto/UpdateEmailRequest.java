package com.pioneer.pixel.test.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request payload for updating an email address.")
public class UpdateEmailRequest {

    @NotBlank
    @Email
    @Schema(description = "New email address", required = true)
    private String email;
}