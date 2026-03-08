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
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "dd/MM/yyyy")
    LocalDateTime createdAt
) {}
