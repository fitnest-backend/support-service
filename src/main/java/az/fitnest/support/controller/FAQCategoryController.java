package az.fitnest.support.controller;

import az.fitnest.support.dto.FAQCategoryDto;
import az.fitnest.support.service.FAQCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
}
