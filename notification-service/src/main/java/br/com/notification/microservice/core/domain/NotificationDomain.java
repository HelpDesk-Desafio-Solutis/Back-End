package br.com.notification.microservice.core.domain;

import br.com.notification.microservice.core.enums.Status;
import br.com.notification.microservice.core.enums.Type;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationDomain {

    private UUID uuid;
    private UUID ticketUuid;
    private UUID clientUuid;
    private UUID technicianUuid;
    private String email;
    private String message;
    private Status status;
    private Type type;
    private LocalDateTime createdAt;

    public NotificationDomain() {
    }

    public NotificationDomain(
            UUID uuid,
            UUID ticketUuid,
            UUID clientUuid,
            UUID technicianUuid,
            String email,
            String message,
            Status status,
            Type type,
            LocalDateTime createdAt
    ) {
        this.uuid = uuid;
        this.ticketUuid = ticketUuid;
        this.clientUuid = clientUuid;
        this.technicianUuid = technicianUuid;
        this.email = email;
        this.message = message;
        this.status = status;
        this.type = type;
        this.createdAt = createdAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getTicketUuid() {
        return ticketUuid;
    }

    public void setTicketUuid(UUID ticketUuid) {
        this.ticketUuid = ticketUuid;
    }

    public UUID getClientUuid() {
        return clientUuid;
    }

    public void setClientUuid(UUID clientUuid) {
        this.clientUuid = clientUuid;
    }

    public UUID getTechnicianUuid() {
        return technicianUuid;
    }

    public void setTechnicianUuid(UUID technicianUuid) {
        this.technicianUuid = technicianUuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}