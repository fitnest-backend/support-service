package az.fitnest.support.repository;

import az.fitnest.support.model.entity.SupportFAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportFAQRepository extends JpaRepository<SupportFAQ, Long> {
}
