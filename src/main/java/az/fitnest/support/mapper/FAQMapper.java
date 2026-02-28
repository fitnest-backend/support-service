package az.fitnest.support.mapper;

import az.fitnest.support.dto.FAQDto;
import az.fitnest.support.model.entity.SupportFAQ;

public final class FAQMapper {

    private FAQMapper() {}

    public static FAQDto toDto(SupportFAQ faq) {
        if (faq == null) {
            return null;
        }
        return FAQDto.builder()
                .id(faq.getId())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .build();
    }
}
