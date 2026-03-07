package az.fitnest.support.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ContactDetailsUpdateRequest(
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email must not be blank")
    String email,

    @NotBlank(message = "Mobile number must not be blank")
    @Pattern(
        regexp = "^\\+994 \\d{2} \\d{3} \\d{2} \\d{2}$",
        message = "Mobile number must be in format +994 xx xxx xx xx"
    )
    @JsonProperty("mobile_number")
    String mobileNumber
) {}
