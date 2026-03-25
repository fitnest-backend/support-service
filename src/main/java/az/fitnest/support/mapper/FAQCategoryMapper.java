package az.fitnest.support.mapper;

import az.fitnest.support.dto.FAQCategoryDto;
import az.fitnest.support.model.entity.FAQCategory;

public final class FAQCategoryMapper {
    private FAQCategoryMapper() {}

    public static FAQCategoryDto toDto(FAQCategory category) {
        if (category == null) return null;
        return FAQCategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
