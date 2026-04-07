package az.fitnest.support.service.impl;

 import az.fitnest.support.model.entity.Translation;
 import az.fitnest.support.repository.TranslationRepository;
 import az.fitnest.support.service.TranslationService;
 import lombok.RequiredArgsConstructor;
 import org.springframework.stereotype.Service;

 @Service
 @RequiredArgsConstructor
 public class TranslationServiceImpl implements TranslationService {
     private final TranslationRepository translationRepository;

     @Override
     public String getTranslatedValue(String entityType, String entityId, String fieldName, String languageCode) {
         if (languageCode == null || languageCode.equalsIgnoreCase("AZ")) {
             return null;
         }

         return translationRepository.findByEntityTypeAndEntityIdAndLanguageCodeAndFieldName(
                 entityType.toUpperCase(),
                 entityId,
                 languageCode.toUpperCase(),
                 fieldName
         )
         .map(Translation::getFieldValue)
         .orElse(null);
     }
 }
