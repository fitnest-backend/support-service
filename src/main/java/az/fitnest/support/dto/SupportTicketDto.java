package az.fitnest.support.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record SupportTicketDto(
    Long id,
    Long userId,
    String topic,
    String message,
    String status,
    LocalDateTime createdAt
) {}
