package az.fitnest.support.controller;

import az.fitnest.support.dto.SupportTicketDto;
import az.fitnest.support.service.SupportTicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/support")
@RequiredArgsConstructor
@Tag(name = "Support Admin", description = "Dəstək biletləri və FAQ-ları idarə etmək üçün administrativ ucluqlar. Bu ucluqlar yalnız ADMIN roluna malik istifadəçilər tərəfindən istifadə edilə bilər.")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class SupportAdminController {

    private final SupportTicketService ticketService;

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
