package com.pioneer.pixel.test.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Request payload for updating a phone number.")
public class UpdatePhoneRequest {

    @NotBlank
    @Pattern(regexp = "\\d{11}")
    @Schema(description = "New phone number in 11 digits format", required = true)
    private String phone;
}