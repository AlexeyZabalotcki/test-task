package com.pioneer.pixel.test.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Data transfer object for user's account balances.")
public class AccountDto {
    @Schema(description = "Current account balance.")
    private BigDecimal balance;
    @Schema(description = "Initial account balance at the time of user creation.")
    private BigDecimal initialBalance;
}