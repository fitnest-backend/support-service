package az.fitnest.support.mapper;

import az.fitnest.support.dto.SupportTicketDto;
import az.fitnest.support.model.entity.SupportTicket;

public final class SupportTicketMapper {

    private SupportTicketMapper() {
    }

    public static SupportTicketDto toDto(SupportTicket ticket) {
        if (ticket == null) {
            return null;
        }
        return toDto(ticket, ticket.getStatus());
    }

    public static SupportTicketDto toDto(SupportTicket ticket, String status) {
        if (ticket == null) {
            return null;
        }
        return SupportTicketDto.builder()
                .id(ticket.getId())
                .userId(ticket.getUserId())
                .topic(ticket.getTopic())
                .message(ticket.getMessage())
                .status(status)
                .createdAt(ticket.getCreatedAt())
                .build();
    }
}
