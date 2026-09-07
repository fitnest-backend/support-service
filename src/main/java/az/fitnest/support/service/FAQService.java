package az.fitnest.support.service;

import az.fitnest.support.dto.FAQDto;
import az.fitnest.support.dto.FAQRequest;
import az.fitnest.support.dto.PaginatedResponse;

import java.util.List;

public interface FAQService {
    List<FAQDto> getPublicFaqs();

    PaginatedResponse<FAQDto> getAllFAQs(int page, int size);

    PaginatedResponse<FAQDto> getAllFAQs(int page, int size, Long categoryId);

    FAQDto getFAQById(Long id);

    FAQDto createFAQ(FAQRequest request);

    FAQDto updateFAQ(Long id, FAQRequest request);

    void deleteFAQ(Long id);
}
