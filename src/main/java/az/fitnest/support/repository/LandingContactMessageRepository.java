package az.fitnest.support.repository;

import az.fitnest.support.model.entity.LandingContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LandingContactMessageRepository extends JpaRepository<LandingContactMessage, Long> {
}
