package az.fitnest.support.service;

import az.fitnest.support.dto.PublicLandingContactMessageRequest;
import az.fitnest.support.model.entity.LandingContactMessage;
import az.fitnest.support.repository.LandingContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class LandingContactMessageService {

    private static final Set<String> TOPICS = Set.of("question", "support", "partnership", "other");
    private static final Pattern EMAIL = Pattern.compile("^[^\\s@<>]{1,64}@[^\\s@<>]{1,255}$");

    private final LandingContactMessageRepository repository;

    @Transactional
    public void create(PublicLandingContactMessageRequest request) {
        String name = clean(request.name(), 80);
        String email = clean(request.email(), 120).toLowerCase();
        String topic = clean(request.topic(), 40).toLowerCase();
        String message = cleanMultiline(request.message(), 2000);

        if (name.length() < 2 || message.length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid contact message");
        }
        if (!EMAIL.matcher(email).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email");
        }
        if (!TOPICS.contains(topic)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid topic");
        }

        LandingContactMessage row = new LandingContactMessage();
        row.setName(name);
        row.setEmail(email);
        row.setTopic(topic);
        row.setMessage(message);
        row.setStatus("NEW");
        row.setCreatedAt(LocalDateTime.now());
        repository.save(row);
    }

    private static String clean(String value, int max) {
        if (value == null) {
            return "";
        }
        String trimmed = value.replace('<', ' ').replace('>', ' ').trim().replaceAll("\\s+", " ");
        return trimmed.length() > max ? trimmed.substring(0, max) : trimmed;
    }

    private static String cleanMultiline(String value, int max) {
        if (value == null) {
            return "";
        }
        String trimmed = value.replace('<', ' ').replace('>', ' ').trim();
        return trimmed.length() > max ? trimmed.substring(0, max) : trimmed;
    }
}
