package az.fitnest.support.dto;

import lombok.Builder;

@Builder
public record FAQCategoryDto(
    Long id,
    String name
) {}
