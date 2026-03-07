package az.fitnest.support.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contact_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactDetails extends BaseAuditableEntity {

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;
}
