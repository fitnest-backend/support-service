package az.fitnest.support.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FAQRequest {
    @NotBlank(message = "Question must not be blank")
    private String question;
    @NotBlank(message = "Answer must not be blank")
    private String answer;
}
