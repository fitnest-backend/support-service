package az.fitnest.support.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupportTicketRequest {
    @NotBlank(message = "Topic is required")
    private String topic;

    @NotBlank(message = "Message is required")
    private String message;
}
