package az.fitnest.support.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicLandingContactMessageRequest(
    @NotBlank @Size(min = 2, max = 80) String name,
    @NotBlank @Email @Size(max = 120) String email,
    @NotBlank @Size(min = 2, max = 40) String topic,
    @NotBlank @Size(min = 2, max = 2000) String message
) {}
