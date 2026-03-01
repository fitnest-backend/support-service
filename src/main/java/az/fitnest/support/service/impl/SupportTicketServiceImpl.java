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

    @Override
    @Transactional
    public SupportTicketDto createTicket(Long userId, SupportTicketRequest request) {
        SupportTicket ticket = new SupportTicket();
        ticket.setUserId(userId);
        ticket.setTopic(request.getTopic());
        ticket.setMessage(request.getMessage());
        ticket.setStatus("OPEN");
        ticket.setCreatedAt(LocalDateTime.now());

        SupportTicket saved = ticketRepository.save(ticket);
        return SupportTicketMapper.toDto(saved);
    }

    @Override
    public List<SupportTicketDto> getUserTickets(Long userId) {
        return ticketRepository.findByUserId(userId).stream()
                .map(SupportTicketMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupportTicketDto> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(SupportTicketMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SupportTicketDto updateTicketStatus(Long id, String status) {
        SupportTicket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
        ticket.setStatus(status);
        SupportTicket saved = ticketRepository.save(ticket);
        return SupportTicketMapper.toDto(saved);
    }

    @Override
    public SupportTicketDto getTicketById(Long id) {
        SupportTicket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
        return SupportTicketMapper.toDto(ticket);
    }

}
