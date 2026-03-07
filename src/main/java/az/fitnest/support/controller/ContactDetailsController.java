package az.fitnest.support.controller;

import az.fitnest.support.dto.ContactDetailsDto;
import az.fitnest.support.service.ContactDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/support")
@RequiredArgsConstructor
@Tag(name = "Contact Details", description = "Təşkilatın əlaqə məlumatlarını əldə etmək üçün ucluq")
public class ContactDetailsController {

    private final ContactDetailsService contactDetailsService;

    @Operation(summary = "Əlaqə məlumatlarını əldə edin", description = "Təşkilatın e-poçt və mobil nömrəsini qaytarır.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Əlaqə məlumatları uğurla əldə edildi"),
            @ApiResponse(responseCode = "404", description = "Əlaqə məlumatları tapılmadı")
    })
    @GetMapping("/contactDetails")
    public ResponseEntity<ContactDetailsDto> getContactDetails() {
        return ResponseEntity.ok(contactDetailsService.getContactDetails());
    }
}
