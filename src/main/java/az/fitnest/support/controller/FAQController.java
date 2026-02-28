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
@Tag(name = "FAQ", description = "Tez-tez verilən suallara baxmaq üçün ucluqlar")
public class FAQController {

    private final FAQService faqService;

    @Operation(summary = "Bütün FAQ-ları əldə edin", description = "Bütün FAQ-ların səhifələnmiş siyahısını qaytarır.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "FAQ-lar uğurla əldə edildi")})
    @GetMapping
    public ResponseEntity<PaginatedResponse<FAQDto>> getAllFAQs(
            @Parameter(description = "Səhifə indeksi (1-dən başlayaraq)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Hər səhifədəki elementlərin sayı") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(faqService.getAllFAQs(page, size));
    }
}
