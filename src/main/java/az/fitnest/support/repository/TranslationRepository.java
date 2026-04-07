package az.fitnest.support.repository;

 import az.fitnest.support.model.entity.Translation;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.stereotype.Repository;
 import java.util.Optional;

 @Repository
 public interface TranslationRepository extends JpaRepository<Translation, Long> {

     Optional<Translation> findByEntityTypeAndEntityIdAndLanguageCodeAndFieldName(
             String entityType, String entityId, String languageCode, String fieldName);
 }
