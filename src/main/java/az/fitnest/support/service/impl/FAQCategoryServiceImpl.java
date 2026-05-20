package az.fitnest.support.service.impl;

import az.fitnest.support.dto.FAQCategoryDto;
import az.fitnest.support.dto.FAQCategoryRequest;
import az.fitnest.support.mapper.FAQCategoryMapper;
import az.fitnest.support.model.entity.FAQCategory;
import az.fitnest.support.repository.FAQCategoryRepository;
import az.fitnest.support.repository.SupportFAQRepository;
import az.fitnest.support.service.FAQCategoryService;
import az.fitnest.support.service.TranslationService;
import az.fitnest.support.client.UserServiceGrpcClient;
import az.fitnest.support.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FAQCategoryServiceImpl implements FAQCategoryService {
    private final FAQCategoryRepository categoryRepository;
    private final SupportFAQRepository faqRepository;
    private final TranslationService translationService;
    private final UserServiceGrpcClient userServiceGrpcClient;

    @Override
    public List<FAQCategoryDto> getAllCategories() {
        String language = resolveUserLanguage();
        return categoryRepository.findAll().stream()
                .map(category -> mapToDto(category, language))
                .toList();
    }

    @Override
    public FAQCategoryDto getCategoryById(Long id) {
        String language = resolveUserLanguage();
        FAQCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return mapToDto(category, language);
    }

    private FAQCategoryDto mapToDto(FAQCategory category, String language) {
        String localizedName = translationService.getTranslatedValue("FAQ_CATEGORY", String.valueOf(category.getId()), "name", language);
        if (localizedName == null || localizedName.isBlank()) {
            localizedName = category.getName();
        }
        return FAQCategoryDto.builder()
                .id(category.getId())
                .name(localizedName)
                .build();
    }

    private String resolveUserLanguage() {
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

    @Override
    @Transactional
    public FAQCategoryDto createCategory(FAQCategoryRequest request) {
        FAQCategory category = FAQCategory.builder()
                .name(request.name())
                .build();
        return FAQCategoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public FAQCategoryDto updateCategory(Long id, FAQCategoryRequest request) {
        FAQCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(request.name());
        return FAQCategoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (faqRepository.existsByCategoryId(id)) {
            throw new ConflictException("Category cannot be deleted because it has associated FAQs", "CATEGORY_HAS_FAQS");
        }
        categoryRepository.deleteById(id);
    }
}
