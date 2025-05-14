package com.pioneer.pixel.test.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Schema(description = "Request payload for transferring money from authenticated user to another user.")
public class TransferRequest {
    @NotNull
    @Schema(description = "ID of the recipient user.")
    private Long toUserId;

    @NotNull
    @Schema(description = "Amount to transfer (positive value).")
    private BigDecimal amount;
}