package az.fitnest.support.configuration;

import az.fitnest.support.model.entity.ContactDetails;
import az.fitnest.support.model.entity.SupportFAQ;
import az.fitnest.support.model.entity.SupportTicket;
import az.fitnest.support.repository.ContactDetailsRepository;
import az.fitnest.support.repository.SupportFAQRepository;
import az.fitnest.support.repository.SupportTicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MockDataInitializer implements CommandLineRunner {

    private final SupportFAQRepository faqRepository;
    private final SupportTicketRepository ticketRepository;
    private final ContactDetailsRepository contactDetailsRepository;

    @Override
    public void run(String... args) {
        initializeFAQs();
        initializeTickets();
        initializeContactDetails();
    }

    private void initializeFAQs() {
        if (faqRepository.count() == 0) {
            log.info("Initializing mock FAQs...");
            List<SupportFAQ> faqs = List.of(
                    new SupportFAQ("How do I reset my password?", "You can reset your password by clicking 'Forgot Password' on the login screen and following the instructions sent to your email."),
                    new SupportFAQ("What payment methods do you accept?", "We accept major credit/debit cards (Visa, Mastercard) and Apple Pay/Google Pay through our secure payment gateway."),
                    new SupportFAQ("Can I cancel my subscription any time?", "Yes, you can cancel your subscription at any time through the 'My Profile' > 'Subscriptions' section. You will retain access until the end of the current billing period."),
                    new SupportFAQ("How do I contact my trainer?", "Once you have an active fitness plan, you can message your trainer directly through the 'Chat' feature in the mobile app."),
                    new SupportFAQ("Is my data secure?", "Yes, we use industry-standard encryption and security protocols to protect your personal and health data.")
            );
            faqRepository.saveAll(faqs);
            log.info("Successfully initialized {} mock FAQs.", faqs.size());
        }
    }

    private void initializeTickets() {
        if (ticketRepository.count() == 0) {
            log.info("Initializing mock tickets...");
            List<SupportTicket> tickets = List.of(
                    new SupportTicket(1L, "Payment Issue", "I was charged twice for my last subscription renewal.", "OPEN", LocalDateTime.now()),
                    new SupportTicket(2L, "App Crash", "The app crashes every time I try to upload a progress photo.", "IN_PROGRESS", LocalDateTime.now().minusDays(1)),
                    new SupportTicket(3L, "Feature Request", "It would be great if we could sync data with Apple Health.", "CLOSED", LocalDateTime.now().minusWeeks(1))
            );
            ticketRepository.saveAll(tickets);
            log.info("Successfully initialized {} mock tickets.", tickets.size());
        }
    }

    private void initializeContactDetails() {
        if (contactDetailsRepository.count() == 0) {
            log.info("Initializing default contact details...");
            ContactDetails contactDetails = new ContactDetails();
            contactDetails.setEmail("support@fitnest.az");
            contactDetails.setMobileNumber("+994 50 123 45 67");
            contactDetailsRepository.save(contactDetails);
            log.info("Successfully initialized default contact details.");
        }
    }
}
