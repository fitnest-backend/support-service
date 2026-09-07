package az.fitnest.support.controller;

import az.fitnest.support.dto.PublicLandingContactMessageRequest;
import az.fitnest.support.service.LandingContactMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/landing")
@RequiredArgsConstructor
@Tag(name = "Landing Public", description = "Unauthenticated contact form used by the website.")
public class PublicLandingContactMessageController {

    private final LandingContactMessageService landingContactMessageService;

    @Operation(summary = "Public contact form", description = "Stores a website contact message. No auth.")
    @PostMapping("/contact-messages")
    public ResponseEntity<Void> create(@Valid @RequestBody PublicLandingContactMessageRequest request) {
        landingContactMessageService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
