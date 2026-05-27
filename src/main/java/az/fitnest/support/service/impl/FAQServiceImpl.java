package az.fitnest.support.service.impl;

import az.fitnest.support.dto.FAQDto;
import az.fitnest.support.dto.FAQRequest;
import az.fitnest.support.dto.PaginatedResponse;
import az.fitnest.support.mapper.FAQMapper;
import az.fitnest.support.model.entity.SupportFAQ;
import az.fitnest.support.exception.ResourceNotFoundException;
import az.fitnest.support.repository.SupportFAQRepository;
import az.fitnest.support.repository.FAQCategoryRepository;
import az.fitnest.support.service.FAQService;
import az.fitnest.support.service.TranslationService;
import az.fitnest.support.client.UserServiceGrpcClient;
import az.fitnest.support.dto.FAQCategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService {

    private final SupportFAQRepository faqRepository;
    private final FAQCategoryRepository categoryRepository;
    private final TranslationService translationService;
    private final UserServiceGrpcClient userServiceGrpcClient;

    @Override
    public PaginatedResponse<FAQDto> getAllFAQs(int page, int size, Long categoryId) {
        String language = resolveUserLanguage();
        PageRequest pageable = PageRequest.of(Math.max(0, page - 1), size);
        Page<SupportFAQ> faqPage = faqRepository.findAllWithCategory(categoryId, pageable);
        List<FAQDto> items = faqPage.getContent().stream()
                .map(faq -> mapToDto(faq, language))
                .collect(Collectors.toList());
        return PaginatedResponse.<FAQDto>builder()
                .items(items)
                .total(faqPage.getTotalElements())
                .page(page)
                .pageSize(size)
                .build();
    }

    @Override
    public FAQDto getFAQById(Long id) {
        String language = resolveUserLanguage();
        SupportFAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.resource_not_found"));
        return mapToDto(faq, language);
    }

    @Override
    @Transactional
    public FAQDto createFAQ(FAQRequest request) {
        SupportFAQ faq = new SupportFAQ();
        faq.setQuestion(request.question());
        faq.setAnswer(request.answer());
        faq.setCategory(categoryRepository.findById(request.categoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
        SupportFAQ saved = faqRepository.save(faq);
        return FAQMapper.toDto(saved);
    }

    @Override
    @Transactional
    public FAQDto updateFAQ(Long id, FAQRequest request) {
        SupportFAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.resource_not_found"));

        faq.setQuestion(request.question());
        faq.setAnswer(request.answer());
        faq.setCategory(categoryRepository.findById(request.categoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
        SupportFAQ saved = faqRepository.save(faq);
        return FAQMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteFAQ(Long id) {
        if (!faqRepository.existsById(id)) {
            throw new ResourceNotFoundException("error.resource_not_found");
        }
        faqRepository.deleteById(id);
    }

    @Override
    public PaginatedResponse<FAQDto> getAllFAQs(int page, int size) {
        return getAllFAQs(page, size, null);
    }

    private FAQDto mapToDto(SupportFAQ faq, String language) {
        String localizedQuestion = translationService.getTranslatedValue("SUPPORT_FAQ", String.valueOf(faq.getId()), "question", language);
        if (localizedQuestion == null || localizedQuestion.isBlank()) {
            localizedQuestion = faq.getQuestion();
        }

        String localizedAnswer = translationService.getTranslatedValue("SUPPORT_FAQ", String.valueOf(faq.getId()), "answer", language);
        if (localizedAnswer == null || localizedAnswer.isBlank()) {
            localizedAnswer = faq.getAnswer();
        }

        String localizedCategoryName = translationService.getTranslatedValue("FAQ_CATEGORY", String.valueOf(faq.getCategory().getId()), "name", language);
        if (localizedCategoryName == null || localizedCategoryName.isBlank()) {
            localizedCategoryName = faq.getCategory().getName();
        }

        return FAQDto.builder()
                .id(faq.getId())
                .question(localizedQuestion)
                .answer(localizedAnswer)
                .category(FAQCategoryDto.builder()
                        .id(faq.getCategory().getId())
                        .name(localizedCategoryName)
                        .build())
                .build();
    }

    private String resolveUserLanguage() {
        try {
            String localeLang = org.springframework.context.i18n.LocaleContextHolder.getLocale().getLanguage()
                    .toUpperCase();
            if (localeLang.equals("EN") || localeLang.equals("RU") || localeLang.equals("AZ")) {
                return localeLang;
            }
        } catch (Exception ignored) {
        }
        Long userId = az.fitnest.support.util.UserContext.getCurrentUserId();
        if (userId != null) {
            try {
                var user = userServiceGrpcClient.getUserById(userId);
                if (user != null && user.getLanguage() != null && !user.getLanguage().isBlank()) {
                    return user.getLanguage().toUpperCase();
                }
            } catch (Exception ignored) {
            }
        }
        return "AZ";
    }
}
