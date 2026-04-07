package az.fitnest.support.dto;

 import jakarta.validation.constraints.NotBlank;

 public record CreateTranslationRequest(
         @NotBlank String entityType,
         @NotBlank String entityId,
         @NotBlank String languageCode,
         @NotBlank String fieldName,
         @NotBlank String fieldValue
 ) {
 }
