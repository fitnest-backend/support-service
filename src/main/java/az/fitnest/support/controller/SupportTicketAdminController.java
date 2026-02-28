package az.fitnest.support.controller;

import az.fitnest.support.dto.SupportTicketDto;
import az.fitnest.support.service.SupportTicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/tickets")
@RequiredArgsConstructor
@Tag(name = "Support Ticket Admin", description = "Dəstək biletlərini idarə etmək üçün administrativ ucluqlar")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class SupportTicketAdminController {

    private final SupportTicketService ticketService;

    @Operation(summary = "Bütün biletləri əldə edin (Admin)", description = "Sistemdəki bütün dəstək biletlərini qaytarır.")
    @GetMapping
    public ResponseEntity<List<SupportTicketDto>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @Operation(summary = "Bilet statusunu yeniləyin (Admin)", description = "Dəstək biletinin statusunu yeniləyir.")
    @PatchMapping("/{id}/status")
    public ResponseEntity<SupportTicketDto> updateTicketStatus(
            @Parameter(description = "Biletin ID-si") @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(ticketService.updateTicketStatus(id, status));
    }
}
