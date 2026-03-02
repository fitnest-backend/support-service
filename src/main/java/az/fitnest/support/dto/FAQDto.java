package az.fitnest.support.dto;

import lombok.Builder;

@Builder
public record FAQDto(
    Long id,
    String question,
    String answer
) {}
