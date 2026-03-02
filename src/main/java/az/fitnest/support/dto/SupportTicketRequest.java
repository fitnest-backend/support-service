package az.fitnest.support.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SupportTicketRequest(
    @NotBlank(message = "Topic is required")
    String topic,
    @NotBlank(message = "Message is required")
    String message
) {}
