package br.com.ticket.microservice.infra.mapper;

import br.com.ticket.microservice.core.app.dto.*;
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

    public static TicketDomain toDomain(TicketAdminRequestDto adminDto) {
        if(adminDto == null) return null;

        TicketDomain ticket = new TicketDomain();

        ticket.setTitle(adminDto.getTitle());
        ticket.setDescription(adminDto.getDescription());
        ticket.setCategory(adminDto.getCategory());
        ticket.setPriority(adminDto.getPriority());
        ticket.setStatus(Status.OPEN);
        ticket.setClientDomain(new UserDomain(adminDto.getClientUuid()));

        if(adminDto.getTechnicianUuid() != null){
            ticket.setTechnicianDomain(new UserDomain(adminDto.getTechnicianUuid()));
        }

        return ticket;
    }

    public static TicketDomain toDomain(TicketUpdateDto updateDto) {
        if (updateDto == null) return null;

        TicketDomain ticket = new TicketDomain();

        ticket.setDescription(updateDto.getDescription());
        ticket.setCategory(updateDto.getCategory());
        ticket.setStatus(updateDto.getStatus());
        ticket.setPriority(updateDto.getPriority());

        if (updateDto.getTechnicianUuid() != null) {
            ticket.setTechnicianDomain(new UserDomain(updateDto.getTechnicianUuid()));
        }

        return ticket;
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
        jpa.setTechnicianUuid(ticket.getTechnicianDomain() != null ? ticket.getTechnicianDomain().getUuid() : null);

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
        ticket.setTechnicianDomain(
                jpa.getTechnicianUuid() != null
                        ? new UserDomain(jpa.getTechnicianUuid())
                        : null
        );

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
