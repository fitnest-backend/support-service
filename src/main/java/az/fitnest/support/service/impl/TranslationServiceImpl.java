package az.fitnest.support.service.impl;

import az.fitnest.support.model.entity.Translation;
import az.fitnest.support.repository.TranslationRepository;
import az.fitnest.support.service.TranslationService;
import org.springframework.stereotype.Service;

@Service
public class TranslationServiceImpl implements TranslationService {
    private final TranslationRepository translationRepository;
    private final org.springframework.web.client.RestTemplate restTemplate;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TranslationServiceImpl.class);

    @jakarta.persistence.PersistenceContext
    private jakarta.persistence.EntityManager entityManager;

    @org.springframework.beans.factory.annotation.Autowired
    @org.springframework.context.annotation.Lazy
    private TranslationServiceImpl self;

    @org.springframework.beans.factory.annotation.Autowired
    private TranslationEntityResolver translationEntityResolver;

    public TranslationServiceImpl(TranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
        org.springframework.http.client.SimpleClientHttpRequestFactory factory = new org.springframework.http.client.SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(1000);
        factory.setReadTimeout(1500);
        this.restTemplate = new org.springframework.web.client.RestTemplate(factory);
    }

    @Override
    public String getTranslatedValue(String entityType, String entityId, String fieldName, String languageCode) {
        if (languageCode == null || languageCode.equalsIgnoreCase("AZ")) {
            return null;
        }

        if (entityType != null) {
            String normType = entityType.toUpperCase();
            if (normType.equals("SUPPORT_TICKET_STATUS") || normType.equals("SUPPORTTICKETSTATUS") || normType.equals("TICKET_STATUS") || normType.equals("TICKETSTATUS")) {
                String status = entityId.toUpperCase();
                if (languageCode.equalsIgnoreCase("EN")) {
                    switch (status) {
                        case "OPEN": return "Open";
                        case "RESOLVED": return "Resolved";
                        case "CLOSED": return "Closed";
                        default: return entityId;
                    }
                } else if (languageCode.equalsIgnoreCase("RU")) {
                    switch (status) {
                        case "OPEN": return "Открыт";
                        case "RESOLVED": return "Решено";
                        case "CLOSED": return "Закрыт";
                        default: return entityId;
                    }
                }
                return entityId;
            }
        }

        String existingValue = translationRepository.findByEntityTypeAndEntityIdAndLanguageCodeAndFieldName(
                entityType.toUpperCase(),
                entityId,
                languageCode.toUpperCase(),
                fieldName
        )
        .map(Translation::getFieldValue)
        .orElse(null);

        if (existingValue != null) {
            return existingValue;
        }

        try {
            Class<?> entityClass = translationEntityResolver.getEntityClass(entityType);
            if (entityClass != null) {
                Object entity = null;
                try {
                    Long longId = Long.parseLong(entityId);
                    entity = entityManager.find(entityClass, longId);
                } catch (NumberFormatException e) {
                    entity = entityManager.find(entityClass, entityId);
                }

                if (entity != null) {
                    String originalValueAz = translationEntityResolver.extractFieldValue(entity, fieldName);
                    if (originalValueAz != null && !originalValueAz.trim().isEmpty()) {
                        String translatedValue = translateText(originalValueAz, languageCode.toLowerCase());
                        if (translatedValue != null && !translatedValue.trim().isEmpty()) {
                            self.saveOrUpdateTranslation(entityType, entityId, languageCode, fieldName, translatedValue);
                            return translatedValue;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Soft fallback translation failed for entityType={}, entityId={}, fieldName={}, lang={}",
                    entityType, entityId, fieldName, languageCode, e);
        }

        return null;
    }

    private String translateText(String text, String targetLanguage) {
        try {
            String googleTranslated = translateWithGoogle(text, targetLanguage);
            if (googleTranslated != null && !googleTranslated.trim().isEmpty()) {
                log.info("Translation successful using Google Translate [AZ -> {}]: '{}' -> '{}'", 
                    targetLanguage.toUpperCase(), text, googleTranslated);
                return googleTranslated;
            }
        } catch (Exception e) {
            log.error("Google Translate failed. Error: {}", e.getMessage());
        }
        return null;
    }

    private String translateWithGoogle(String text, String targetLanguage) {
        try {
            java.net.URI uri = org.springframework.web.util.UriComponentsBuilder
                .fromUriString("https://translate.googleapis.com/translate_a/single")
                .queryParam("client", "gtx")
                .queryParam("sl", "az")
                .queryParam("tl", targetLanguage.toLowerCase())
                .queryParam("dt", "t")
                .queryParam("q", text)
                .build()
                .toUri();

            log.info("Google Translate Request [AZ -> {}]: '{}'", targetLanguage.toUpperCase(), text);
            String response = restTemplate.getForObject(uri, String.class);
            if (response != null) {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                com.fasterxml.jackson.databind.JsonNode rootNode = mapper.readTree(response);
                if (rootNode.isArray() && rootNode.size() > 0) {
                    com.fasterxml.jackson.databind.JsonNode firstArray = rootNode.get(0);
                    if (firstArray.isArray() && firstArray.size() > 0) {
                        com.fasterxml.jackson.databind.JsonNode translationPair = firstArray.get(0);
                        if (translationPair.isArray() && translationPair.size() > 0) {
                            return translationPair.get(0).asText();
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Google Translation API failed for text '{}' to '{}': {}", text, targetLanguage, e.getMessage());
        }
        return null;
    }

    @org.springframework.transaction.annotation.Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public void saveOrUpdateTranslation(String entityType, String entityId, String languageCode, String fieldName, String fieldValue) {
        String normalizedEntityType = entityType.toUpperCase();
        String normalizedLanguageCode = languageCode.toUpperCase();

        log.info("Database Save: entityType={}, entityId={}, languageCode={}, fieldName={}, fieldValue='{}'", 
            normalizedEntityType, entityId, normalizedLanguageCode, fieldName, fieldValue);

        Translation existing = translationRepository.findByEntityTypeAndEntityIdAndLanguageCodeAndFieldName(
                normalizedEntityType, entityId, normalizedLanguageCode, fieldName
        ).orElse(null);

        if (existing != null) {
            log.info("Updating existing translation record ID={}", existing.getId());
            existing.setFieldValue(fieldValue);
            translationRepository.save(existing);
        } else {
            log.info("Creating new translation record");
            Translation translation = Translation.builder()
                    .entityType(normalizedEntityType)
                    .entityId(entityId)
                    .languageCode(normalizedLanguageCode)
                    .fieldName(fieldName)
                    .fieldValue(fieldValue)
                    .build();
            translationRepository.save(translation);
        }
    }
}
