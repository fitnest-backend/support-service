package az.fitnest.support.repository;

import az.fitnest.support.model.entity.FAQCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FAQCategoryRepository extends JpaRepository<FAQCategory, Long> {
    Optional<FAQCategory> findByName(String name);
}

