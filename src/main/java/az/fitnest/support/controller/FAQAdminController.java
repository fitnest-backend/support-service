package az.fitnest.support.controller;

import az.fitnest.support.dto.FAQDto;
import az.fitnest.support.dto.FAQRequest;
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
@Tag(name = "FAQ Admin", description = "Administrative endpoints for managing FAQs")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class FAQAdminController {

    private final FAQService faqService;

    @Operation(summary = "Create FAQ (Admin)", description = "Creates a new FAQ. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "FAQ created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<FAQDto> createFAQ(@Valid @RequestBody FAQRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(faqService.createFAQ(request));
    }

    @Operation(summary = "Update FAQ (Admin)", description = "Updates an existing FAQ. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "FAQ updated successfully"),
            @ApiResponse(responseCode = "404", description = "FAQ not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FAQDto> updateFAQ(
            @Parameter(description = "ID of the FAQ") @PathVariable Long id,
            @Valid @RequestBody FAQRequest request) {
        return ResponseEntity.ok(faqService.updateFAQ(id, request));
    }

    @Operation(summary = "Delete FAQ (Admin)", description = "Deletes an FAQ. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "FAQ deleted successfully"),
            @ApiResponse(responseCode = "404", description = "FAQ not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFAQ(@Parameter(description = "ID of the FAQ to delete") @PathVariable Long id) {
        faqService.deleteFAQ(id);
        return ResponseEntity.noContent().build();
    }
}
