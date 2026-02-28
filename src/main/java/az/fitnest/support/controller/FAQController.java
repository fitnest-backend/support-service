package az.fitnest.support.controller;

import az.fitnest.support.dto.FAQDto;
import az.fitnest.support.dto.PaginatedResponse;
import az.fitnest.support.service.FAQService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/faqs")
@RequiredArgsConstructor
@Tag(name = "FAQ", description = "Endpoints for viewing frequently asked questions")
public class FAQController {

    private final FAQService faqService;

    @Operation(summary = "Get all FAQs", description = "Returns a paginated list of all FAQs.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "FAQs retrieved successfully")})
    @GetMapping
    public ResponseEntity<PaginatedResponse<FAQDto>> getAllFAQs(
            @Parameter(description = "Page index (1-based)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Items per page") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(faqService.getAllFAQs(page, size));
    }
}
