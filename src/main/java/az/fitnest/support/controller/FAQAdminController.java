package az.fitnest.support.controller;

import az.fitnest.support.dto.FAQDto;
import az.fitnest.support.dto.FAQRequest;
import az.fitnest.support.dto.PaginatedResponse;
import az.fitnest.support.service.FAQService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/faqs")
@RequiredArgsConstructor
@Tag(name = "FAQ Admin", description = "FAQ-ları idarə etmək üçün administrativ ucluqlar. ADMIN rolu tələb olunur.")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class FAQAdminController {
    private final FAQService faqService;

    @Operation(summary = "Bütün FAQ-ları əldə edin", description = "Bütün FAQ-ların səhifələnmiş siyahısını qaytarır.")
    @GetMapping
    public ResponseEntity<PaginatedResponse<FAQDto>> getAllFAQs(
            @Parameter(description = "Səhifə indeksi (1-dən başlayaraq)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Hər səhifədəki elementlərin sayı") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "FAQ kateqoriyasının ID-si") @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(faqService.getAllFAQs(page, size, categoryId));
    }

    @Operation(summary = "FAQ yaradın", description = "Yeni FAQ yaradır. ADMIN rolu tələb olunur.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "FAQ uğurla yaradıldı"),
            @ApiResponse(responseCode = "400", description = "Yanlış sorğu")
    })
    @PostMapping
    public ResponseEntity<FAQDto> createFAQ(@Valid @RequestBody FAQRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(faqService.createFAQ(request));
    }

    @Operation(summary = "FAQ-nu yeniləyin", description = "Mövcud FAQ-nu yeniləyir. ADMIN rolu tələb olunur.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "FAQ uğurla yeniləndi"),
            @ApiResponse(responseCode = "404", description = "FAQ tapılmadı")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FAQDto> updateFAQ(
            @Parameter(description = "FAQ-nun ID-si") @PathVariable Long id,
            @Valid @RequestBody FAQRequest request) {
        return ResponseEntity.ok(faqService.updateFAQ(id, request));
    }

    @Operation(summary = "FAQ-nu silin", description = "FAQ-nu silir. ADMIN rolu tələb olunur.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "FAQ uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "FAQ tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFAQ(@Parameter(description = "Silinəcək FAQ-nun ID-si") @PathVariable Long id) {
        faqService.deleteFAQ(id);
        return ResponseEntity.noContent().build();
    }
}
