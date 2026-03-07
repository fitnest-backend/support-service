package az.fitnest.support.repository;

import az.fitnest.support.model.entity.ContactDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactDetailsRepository extends JpaRepository<ContactDetails, Long> {

    default Optional<ContactDetails> findSingleton() {
        return findAll().stream().findFirst();
    }
}

