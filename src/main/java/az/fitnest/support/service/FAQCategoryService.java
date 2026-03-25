package az.fitnest.support.service;

import az.fitnest.support.dto.FAQCategoryDto;
import az.fitnest.support.dto.FAQCategoryRequest;
import java.util.List;

public interface FAQCategoryService {
    List<FAQCategoryDto> getAllCategories();
    FAQCategoryDto getCategoryById(Long id);
    FAQCategoryDto createCategory(FAQCategoryRequest request);
    FAQCategoryDto updateCategory(Long id, FAQCategoryRequest request);
    void deleteCategory(Long id);
}

