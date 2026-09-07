package az.fitnest.support.dto;

import java.util.List;

public record PublicLandingFaqsResponse(
    List<FAQDto> items,
    List<FAQCategoryDto> categories
) {}
