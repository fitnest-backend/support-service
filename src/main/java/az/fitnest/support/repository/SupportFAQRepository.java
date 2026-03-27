package az.fitnest.support.repository;

import az.fitnest.support.model.entity.SupportFAQ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportFAQRepository extends JpaRepository<SupportFAQ, Long> {
    Page<SupportFAQ> findAllByCategory_Id(Long categoryId, Pageable pageable);

    @Query("SELECT f FROM SupportFAQ f JOIN FETCH f.category WHERE (:categoryId IS NULL OR f.category.id = :categoryId)")
    Page<SupportFAQ> findAllWithCategory(@Param("categoryId") Long categoryId, Pageable pageable);
}
