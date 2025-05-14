package com.pioneer.pixel.test.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(description = "Data transfer object for user's email.")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Unique identifier of the email record.", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(description = "Email address.")
    private String email;
}