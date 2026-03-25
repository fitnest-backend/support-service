package az.fitnest.support.service.impl;

import az.fitnest.support.dto.FAQCategoryDto;
import az.fitnest.support.dto.FAQCategoryRequest;
import az.fitnest.support.mapper.FAQCategoryMapper;
import az.fitnest.support.model.entity.FAQCategory;
import az.fitnest.support.repository.FAQCategoryRepository;
import az.fitnest.support.service.FAQCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FAQCategoryServiceImpl implements FAQCategoryService {
    private final FAQCategoryRepository categoryRepository;

    @Override
    public List<FAQCategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(FAQCategoryMapper::toDto)
                .toList();
    }

    @Override
    public FAQCategoryDto getCategoryById(Long id) {
        FAQCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return FAQCategoryMapper.toDto(category);
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
        categoryRepository.deleteById(id);
    }
}
