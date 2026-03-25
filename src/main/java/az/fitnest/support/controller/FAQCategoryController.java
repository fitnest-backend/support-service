package az.fitnest.support.controller;

import az.fitnest.support.dto.FAQCategoryDto;
import az.fitnest.support.dto.FAQCategoryRequest;
import az.fitnest.support.service.FAQCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/faq-categories")
@RequiredArgsConstructor
@Tag(name = "FAQ Category", description = "FAQ kateqoriyalarının idarə olunması üçün ucluqlar")
public class FAQCategoryController {
    private final FAQCategoryService categoryService;

    @Operation(summary = "Bütün kateqoriyaları əldə edin")
    @GetMapping
    public ResponseEntity<List<FAQCategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(summary = "Kateqoriya yaradın")
    @PostMapping
    public ResponseEntity<FAQCategoryDto> createCategory(@Valid @RequestBody FAQCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request));
    }

    @Operation(summary = "Kateqoriyanı yeniləyin")
    @PutMapping("/{id}")
    public ResponseEntity<FAQCategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody FAQCategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @Operation(summary = "Kateqoriyanı silin")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}

