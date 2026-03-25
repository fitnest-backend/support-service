package az.fitnest.support.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record FAQCategoryRequest(
    @NotBlank(message = "Category name must not be blank")
    String name
) {}
