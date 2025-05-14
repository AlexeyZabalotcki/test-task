package com.pioneer.pixel.test.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "Data transfer object representing a user with related emails, phones, and account.")
public class UserDto {
    @Schema(description = "Unique identifier of the user.")
    private Long id;
    @Schema(description = "User's full name.")
    private String name;
    @Schema(description = "User's date of birth.")
    private LocalDate dateOfBirth;
    @Schema(description = "List of user's email records.")
    private List<EmailDto> emails;
    @Schema(description = "List of user's phone records.")
    private List<PhoneDto> phones;
    @Schema(description = "User's account information.")
    private AccountDto account;
}