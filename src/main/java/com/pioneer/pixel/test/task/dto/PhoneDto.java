package com.pioneer.pixel.test.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for user's phone number.")
public class PhoneDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier of the phone record.", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(description = "Phone number in 11 digits format.")
    private String phone;
}