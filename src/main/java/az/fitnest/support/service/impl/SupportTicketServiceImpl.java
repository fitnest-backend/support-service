package az.fitnest.support.service.impl;

import az.fitnest.support.dto.SupportTicketDto;
import az.fitnest.support.dto.SupportTicketRequest;
import az.fitnest.support.mapper.SupportTicketMapper;
import az.fitnest.support.model.entity.SupportTicket;
import az.fitnest.support.exception.ResourceNotFoundException;
import az.fitnest.support.repository.SupportTicketRepository;
import az.fitnest.support.service.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportTicketServiceImpl implements SupportTicketService {

    private final SupportTicketRepository ticketRepository;
    private final az.fitnest.support.service.TranslationService translationService;
    private final az.fitnest.support.client.UserServiceGrpcClient userServiceGrpcClient;

    @Override
    @Transactional
    public SupportTicketDto createTicket(Long userId, SupportTicketRequest request) {
        SupportTicket ticket = new SupportTicket();
        ticket.setUserId(userId);
        ticket.setTopic(request.topic());
        ticket.setMessage(request.message());
        ticket.setStatus("OPEN");
        ticket.setCreatedAt(LocalDateTime.now());

        SupportTicket saved = ticketRepository.save(ticket);
        return mapToDto(saved);
    }

    @Override
    public List<SupportTicketDto> getUserTickets(Long userId) {
        return ticketRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupportTicketDto> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SupportTicketDto updateTicketStatus(Long id, String status) {
        SupportTicket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
        ticket.setStatus(status);
        SupportTicket saved = ticketRepository.save(ticket);
        return mapToDto(saved);
    }

    @Override
    public SupportTicketDto getTicketById(Long id) {
        SupportTicket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
        return mapToDto(ticket);
    }

    private SupportTicketDto mapToDto(SupportTicket ticket) {
        if (ticket == null) {
            return null;
        }
        String originalStatus = ticket.getStatus();
        String userLanguage = resolveUserLanguage();
        String translatedStatus = (originalStatus != null) ? translationService.getTranslatedValue("SUPPORT_TICKET_STATUS", originalStatus, "name", userLanguage) : null;
        if (translatedStatus == null || translatedStatus.isEmpty()) {
            translatedStatus = originalStatus;
        }
        return SupportTicketMapper.toDto(ticket, translatedStatus);
    }

    private String resolveUserLanguage() {
        Long userId = az.fitnest.support.util.UserContext.getCurrentUserId();
        if (userId != null) {
            try {
                var user = userServiceGrpcClient.getUserById(userId);
                if (user != null && user.getLanguage() != null && !user.getLanguage().isBlank()) {
                    return user.getLanguage().toUpperCase();
                }
            } catch (Exception ignored) {
            }
        }
        try {
            org.springframework.web.context.request.RequestAttributes requestAttributes = 
                    org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof org.springframework.web.context.request.ServletRequestAttributes) {
                jakarta.servlet.http.HttpServletRequest request = 
                        ((org.springframework.web.context.request.ServletRequestAttributes) requestAttributes).getRequest();
                String acceptLanguage = request.getHeader("Accept-Language");
                if (acceptLanguage != null && !acceptLanguage.trim().isEmpty()) {
                    String localeLang = org.springframework.context.i18n.LocaleContextHolder.getLocale().getLanguage()
                            .toUpperCase();
                    if (localeLang.equals("EN") || localeLang.equals("RU") || localeLang.equals("AZ")) {
                        return localeLang;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return "AZ";
    }

}
