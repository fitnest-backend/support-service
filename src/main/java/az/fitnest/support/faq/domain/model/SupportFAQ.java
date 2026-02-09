package az.fitnest.support.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "support_faqs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupportFAQ {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id")
    private Long faqId;
    
    @Column(name = "question", nullable = false, columnDefinition = "TEXT")
    private String question;
    
    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer;
}
