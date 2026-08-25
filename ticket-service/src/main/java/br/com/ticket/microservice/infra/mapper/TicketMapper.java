package br.com.ticket.microservice.infra.mapper;

import br.com.ticket.microservice.core.app.dto.TicketRequestDto;
import br.com.ticket.microservice.core.app.dto.TicketResponseDto;
import br.com.ticket.microservice.core.app.dto.TicketResumedResponseDto;
import br.com.ticket.microservice.core.domain.TicketDomain;
import br.com.ticket.microservice.core.enums.Status;
import br.com.ticket.microservice.infra.persistence.entity.TicketJpaEntity;
import br.com.user.microservice.infra.mapper.UserMapper;

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

        ticket.setClientDomain(UserMapper.toDomain(req.getClientUuid()));
        ticket.setTechnicianDomain(UserMapper.toDomain(req.getTechnicianUuid()));

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
                .client(UserMapper.toResponseDto(ticket.getClientDomain()))
                .technician(UserMapper.toResponseDto(ticket.getTechnicianDomain()))
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
                .client(UserMapper.toResponseDto(ticket.getClientDomain()))
                .technician(UserMapper.toResponseDto(ticket.getTechnicianDomain()))
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

        jpa.setClient(UserMapper.toJpaEntitySimple(ticket.getClientDomain()));
        jpa.setTechnician(UserMapper.toJpaEntitySimple(ticket.getTechnicianDomain()));

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

        ticket.setClientDomain(UserMapper.toDomain(jpa.getClient()));
        ticket.setTechnicianDomain(UserMapper.toDomain(jpa.getTechnician()));

        ticket.setCreatedAt(jpa.getCreatedAt());
        ticket.setUpdatedAt(jpa.getUpdatedAt());

        return ticket;
    }

}
