package az.fitnest.support.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "landing_contact_messages")
@Getter
@Setter
@NoArgsConstructor
public class LandingContactMessage extends BaseEntity {

    @Column(name = "sender_name", nullable = false, length = 80)
    private String name;

    @Column(name = "sender_email", nullable = false, length = 120)
    private String email;

    @Column(name = "topic", nullable = false, length = 40)
    private String topic;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
