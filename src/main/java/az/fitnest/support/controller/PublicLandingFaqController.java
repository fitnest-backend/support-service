package az.fitnest.support.controller;

import az.fitnest.support.dto.PublicLandingFaqsResponse;
import az.fitnest.support.service.FAQCategoryService;
import az.fitnest.support.service.FAQService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/public/landing")
@RequiredArgsConstructor
@Tag(name = "Landing Public", description = "Unauthenticated FAQ content used by the website.")
public class PublicLandingFaqController {

    private static final CacheControl JSON_CACHE = CacheControl
            .maxAge(Duration.ofMinutes(2))
            .cachePublic()
            .staleWhileRevalidate(Duration.ofMinutes(2));

    private final FAQService faqService;
    private final FAQCategoryService faqCategoryService;

    @Operation(
            summary = "Public FAQs",
            description = "Localized FAQ questions, answers, and categories. Same records admins edit."
    )
    @GetMapping("/faqs")
    public ResponseEntity<PublicLandingFaqsResponse> getFaqs() {
        PublicLandingFaqsResponse body = new PublicLandingFaqsResponse(
                faqService.getPublicFaqs(),
                faqCategoryService.getAllCategories()
        );
        return ResponseEntity.ok()
                .cacheControl(JSON_CACHE)
                .header("X-Content-Type-Options", "nosniff")
                .header(HttpHeaders.VARY, "Accept-Language")
                .body(body);
    }
}
