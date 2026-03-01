package az.fitnest.support.service;

import az.fitnest.support.dto.SupportTicketDto;
import az.fitnest.support.dto.SupportTicketRequest;

import java.util.List;

public interface SupportTicketService {
    SupportTicketDto createTicket(Long userId, SupportTicketRequest request);

    List<SupportTicketDto> getUserTickets(Long userId);

    List<SupportTicketDto> getAllTickets();

    SupportTicketDto updateTicketStatus(Long id, String status);

    SupportTicketDto getTicketById(Long id);
}
