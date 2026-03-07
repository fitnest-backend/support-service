package az.fitnest.support.controller;

import az.fitnest.support.dto.FAQDto;
import az.fitnest.support.dto.FAQRequest;
import az.fitnest.support.dto.SupportTicketDto;
import az.fitnest.support.service.FAQService;
import az.fitnest.support.service.SupportTicketService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/support")
@RequiredArgsConstructor
@Tag(name = "Support Admin", description = "Dəstək biletləri və FAQ-ları idarə etmək üçün administrativ ucluqlar. Bu ucluqlar yalnız ADMIN və SUPER_ADMIN rollarına malik istifadəçilər tərəfindən istifadə edilə bilər.")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class SupportAdminController {

    private final FAQService faqService;
    private final SupportTicketService ticketService;

    @Operation(summary = "FAQ yaradın", description = "Yeni FAQ yaradır. ADMIN rolu tələb olunur.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "FAQ uğurla yaradıldı"),
            @ApiResponse(responseCode = "400", description = "Yanlış sorğu")
    })
    @PostMapping("/faqs")
    public ResponseEntity<FAQDto> createFAQ(@Valid @RequestBody FAQRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(faqService.createFAQ(request));
    }

    @Operation(summary = "FAQ-nu yeniləyin", description = "Mövcud FAQ-nu yeniləyir. ADMIN rolu tələb olunur.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "FAQ uğurla yeniləndi"),
            @ApiResponse(responseCode = "404", description = "FAQ tapılmadı")
    })
    @PutMapping("/faqs/{id}")
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
    @DeleteMapping("/faqs/{id}")
    public ResponseEntity<Void> deleteFAQ(@Parameter(description = "Silinəcək FAQ-nun ID-si") @PathVariable Long id) {
        faqService.deleteFAQ(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Bütün biletləri əldə edin", description = "Sistemdəki bütün dəstək biletlərini qaytarır. ADMIN rolu tələb olunur.")
    @GetMapping("/tickets")
    public ResponseEntity<List<SupportTicketDto>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @Operation(summary = "Bilet statusunu yeniləyin", description = "Dəstək biletinin statusunu yeniləyir. ADMIN rolu tələb olunur.")
    @PatchMapping("/tickets/{id}/status")
    public ResponseEntity<SupportTicketDto> updateTicketStatus(
            @Parameter(description = "Biletin ID-si") @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(ticketService.updateTicketStatus(id, status));
    }
}
