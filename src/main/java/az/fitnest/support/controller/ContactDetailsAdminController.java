package az.fitnest.support.controller;

import az.fitnest.support.dto.ContactDetailsDto;
import az.fitnest.support.dto.ContactDetailsUpdateRequest;
import az.fitnest.support.service.ContactDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/support")
@RequiredArgsConstructor
@Tag(name = "Contact Details Admin", description = "Təşkilatın əlaqə məlumatlarını idarə etmək üçün administrativ ucluqlar. ADMIN rolu tələb olunur.")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class ContactDetailsAdminController {

    private final ContactDetailsService contactDetailsService;

    @Operation(summary = "Əlaqə məlumatlarını yeniləyin", description = "Təşkilatın əlaqə məlumatlarını yeniləyir. ADMIN rolu tələb olunur.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Əlaqə məlumatları uğurla yeniləndi"),
            @ApiResponse(responseCode = "400", description = "Yanlış sorğu"),
            @ApiResponse(responseCode = "404", description = "Əlaqə məlumatları tapılmadı")
    })
    @PutMapping("/contact-details")
    public ResponseEntity<ContactDetailsDto> updateContactDetails(
            @Valid @RequestBody ContactDetailsUpdateRequest request) {
        return ResponseEntity.ok(contactDetailsService.updateContactDetails(request));
    }
}
