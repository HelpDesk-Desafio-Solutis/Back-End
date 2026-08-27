package br.com.notification.microservice.core.domain;

import br.com.notification.microservice.core.enums.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationDomain {

    private UUID uuid;
    private UUID ticketUuid;
    private UUID userUuid;
    private String email;
    private String message;
    private Status status;
    private LocalDateTime createdAt;

    public NotificationDomain() {
    }

    public NotificationDomain(
            UUID uuid,
            UUID ticketUuid,
            UUID userUuid,
            String email,
            String message,
            Status status,
            LocalDateTime createdAt
    ) {
        this.uuid = uuid;
        this.ticketUuid = ticketUuid;
        this.userUuid = userUuid;
        this.email = email;
        this.message = message;
        this.status = status;
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

    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}