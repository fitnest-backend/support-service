package az.fitnest.support.controller;

import az.fitnest.support.dto.SupportTicketDto;
import az.fitnest.support.dto.SupportTicketRequest;
import az.fitnest.support.service.SupportTicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "Support Ticket", description = "İstifadəçi dəstək biletlərini idarə etmək üçün ucluqlar")
@SecurityRequirement(name = "bearerAuth")
public class SupportTicketController {

    private final SupportTicketService ticketService;

    @Operation(summary = "Dəstək bileti yaradın", description = "Autentifikasiya olunmuş istifadəçi üçün yeni dəstək bileti yaradır.")
    @PostMapping
    public ResponseEntity<SupportTicketDto> createTicket(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody SupportTicketRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(userId, request));
    }

    @Operation(summary = "İstifadəçi biletlərini əldə edin", description = "Autentifikasiya olunmuş istifadəçi üçün bütün dəstək biletlərini qaytarır.")
    @GetMapping
    public ResponseEntity<List<SupportTicketDto>> getUserTickets(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(ticketService.getUserTickets(userId));
    }

    @Operation(summary = "Bileti ID vasitəsilə əldə edin", description = "ID-si verilmiş xüsusi dəstək biletini qaytarır.")
    @GetMapping("/{id}")
    public ResponseEntity<SupportTicketDto> getTicketById(
            @AuthenticationPrincipal Long userId,
            @Parameter(description = "Biletin ID-si") @PathVariable Long id) {
        SupportTicketDto ticket = ticketService.getTicketById(id);
        // Basic security check: only the owner or an admin (handled in admin controller) can see the ticket
        if (!ticket.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(ticket);
    }
}
