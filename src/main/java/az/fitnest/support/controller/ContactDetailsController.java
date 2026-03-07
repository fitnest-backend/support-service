package az.fitnest.support.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/support")
public class ContactDetailsController {

    @Operation(summary = "Get Contact Details", description = "Retrieve the organization's contact details including email and mobile number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved contact details")
    })
    @GetMapping("/contactDetails")
    public ContactDetails getContactDetails() {
        return new ContactDetails("support@fitnest.az", "+994 50 123 45 67");
    }

    private record ContactDetails(String email, String mobileNumber) {}
}
