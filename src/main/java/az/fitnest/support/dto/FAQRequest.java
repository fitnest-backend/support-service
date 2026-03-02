package az.fitnest.support.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record FAQRequest(
    @NotBlank(message = "Question must not be blank")
    String question,
    @NotBlank(message = "Answer must not be blank")
    String answer
) {}
