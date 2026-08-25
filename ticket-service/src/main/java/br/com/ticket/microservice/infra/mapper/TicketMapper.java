package br.com.ticket.microservice.infra.mapper;

import br.com.ticket.microservice.core.app.dto.TicketRequestDto;
import br.com.ticket.microservice.core.app.dto.TicketResponseDto;
import br.com.ticket.microservice.core.app.dto.TicketResumedResponseDto;
import br.com.ticket.microservice.core.app.dto.user.UserResponseDto;
import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.domain.UserDomain;
import br.com.ticket.microservice.core.enums.Status;
import br.com.ticket.microservice.infra.persistence.entity.TicketJpaEntity;

public class TicketMapper {

    /* ========= DTO -> DOMAIN ========= */

    public static TicketDomain toDomain(TicketRequestDto req) {
        if (req == null) return null;

        TicketDomain ticket = new TicketDomain();

        ticket.setTitle(req.getTitle());
        ticket.setDescription(req.getDescription());
        ticket.setCategory(req.getCategory());
        ticket.setPriority(req.getPriority());
        ticket.setStatus(Status.OPEN);

        ticket.setClientDomain(new UserDomain(req.getClientUuid()));
        ticket.setTechnicianDomain(new UserDomain(req.getTechnicianUuid()));

        return ticket;
    }

    /* ========= DOMAIN -> DTO (Completo) ========= */
    public static TicketResponseDto toResponseDto(TicketDomain ticket) {
        if (ticket == null) return null;

        return TicketResponseDto.builder()
                .uuid(ticket.getUuid())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .category(ticket.getCategory())
                .priority(ticket.getPriority())
                .client(toResponseDto(ticket.getClientDomain()))
                .technician(toResponseDto(ticket.getTechnicianDomain()))
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }

    /* ========= DOMAIN -> DTO (Resumo) ========= */
    public static TicketResumedResponseDto toResumedResponseDto(TicketDomain ticket) {
        if (ticket == null) return null;

        return TicketResumedResponseDto.builder()
                .uuid(ticket.getUuid())
                .title(ticket.getTitle())
                .category(ticket.getCategory())
                .priority(ticket.getPriority())
                .client(toResponseDto(ticket.getClientDomain()))
                .technician(toResponseDto(ticket.getTechnicianDomain()))
                .build();
    }

    /* ========= DOMAIN -> JPA ENTITY ========= */
    public static TicketJpaEntity toJpaEntity(TicketDomain ticket) {
        if (ticket == null) return null;

        TicketJpaEntity jpa = new TicketJpaEntity();
        jpa.setUuid(ticket.getUuid());
        jpa.setTitle(ticket.getTitle());
        jpa.setDescription(ticket.getDescription());
        jpa.setStatus(ticket.getStatus());
        jpa.setCategory(ticket.getCategory());
        jpa.setPriority(ticket.getPriority());

        jpa.setClientUuid(ticket.getClientDomain().getUuid());
        jpa.setTechnicianUuid(ticket.getTechnicianDomain().getUuid());

        jpa.setCreatedAt(ticket.getCreatedAt());
        jpa.setUpdatedAt(ticket.getUpdatedAt());

        return jpa;
    }

    /* ========= JPA ENTITY -> DOMAIN ========= */
    public static TicketDomain toDomain(TicketJpaEntity jpa) {
        if (jpa == null) return null;

        TicketDomain ticket = new TicketDomain();
        ticket.setUuid(jpa.getUuid());
        ticket.setTitle(jpa.getTitle());
        ticket.setDescription(jpa.getDescription());
        ticket.setStatus(jpa.getStatus());
        ticket.setCategory(jpa.getCategory());
        ticket.setPriority(jpa.getPriority());

        ticket.setClientDomain(new UserDomain(jpa.getClientUuid()));
        ticket.setTechnicianDomain(new UserDomain(jpa.getTechnicianUuid()));

        ticket.setCreatedAt(jpa.getCreatedAt());
        ticket.setUpdatedAt(jpa.getUpdatedAt());

        return ticket;
    }

    private static UserResponseDto toResponseDto(UserDomain user) {
        if (user == null) {
            return null;
        }

        return UserResponseDto.builder()
                .uuid(user.getUuid())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

}
