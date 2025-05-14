package com.pioneer.pixel.test.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@Schema(description = "Response containing JWT access token.")
public class JwtResponse {
    private String token;
}