package az.fitnest.support.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ContactDetailsDto(
    String email,
    @JsonProperty("mobile_number")
    String mobileNumber
) {}
