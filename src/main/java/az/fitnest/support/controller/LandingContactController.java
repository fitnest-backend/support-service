package az.fitnest.support.controller;

import az.fitnest.support.dto.ContactDetailsDto;
import az.fitnest.support.exception.ResourceNotFoundException;
import az.fitnest.support.service.ContactDetailsService;
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
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/public/landing")
@RequiredArgsConstructor
@Tag(name = "Landing Public", description = "Unauthenticated landing contact details used by the website footer.")
public class LandingContactController {

    private static final CacheControl JSON_CACHE = CacheControl
            .maxAge(Duration.ofMinutes(2))
            .cachePublic()
            .staleWhileRevalidate(Duration.ofMinutes(2));
    private static final Pattern EMAIL = Pattern.compile("^[^\\s@<>]{1,64}@[^\\s@<>]{1,255}$");
    private static final Pattern PHONE = Pattern.compile("^[+0-9() \\-]{5,32}$");

    private final ContactDetailsService contactDetailsService;

    @Operation(summary = "Public contact details", description = "Email and phone for the landing footer. Same record admins edit.")
    @GetMapping("/contact")
    public ResponseEntity<ContactDetailsDto> getContact() {
        ContactDetailsDto body;
        try {
            body = sanitize(contactDetailsService.getContactDetails());
        } catch (ResourceNotFoundException ignored) {
            body = new ContactDetailsDto(null, null);
        }
        return ResponseEntity.ok()
                .cacheControl(JSON_CACHE)
                .header("X-Content-Type-Options", "nosniff")
                .header(HttpHeaders.VARY, "Accept-Language")
                .body(body);
    }

    private static ContactDetailsDto sanitize(ContactDetailsDto source) {
        return new ContactDetailsDto(publicEmail(source.email()), publicPhone(source.mobileNumber()));
    }

    private static String publicEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        String trimmed = email.trim();
        if (trimmed.length() > 120 || !EMAIL.matcher(trimmed).matches()) {
            return null;
        }
        return trimmed;
    }

    private static String publicPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return null;
        }
        String trimmed = phone.trim();
        if (trimmed.length() > 32) {
            trimmed = trimmed.substring(0, 32);
        }
        if (!PHONE.matcher(trimmed).matches()) {
            return null;
        }
        return trimmed;
    }
}
