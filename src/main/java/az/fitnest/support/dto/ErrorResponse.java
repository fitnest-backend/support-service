package az.fitnest.support.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record ErrorResponse(
    @JsonProperty("error") ErrorDetail error
) {
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ErrorDetail(
        String code,
        String message,
        Integer status,
        String path,
        OffsetDateTime timestamp
    ) {}
}
