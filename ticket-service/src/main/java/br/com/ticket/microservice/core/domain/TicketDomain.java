package br.com.ticket.microservice.core.domain;

import br.com.ticket.microservice.core.enums.Category;
import br.com.ticket.microservice.core.enums.Priority;
import br.com.ticket.microservice.core.enums.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public class TicketDomain {

    private UUID uuid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String title;
    private String description;
    private Status status;
    private Category category;
    private Priority priority;
    private UserDomain clientDomain;
    private UserDomain technicianDomain;

    public TicketDomain(UUID uuid, LocalDateTime createdAt, LocalDateTime updatedAt, String title, String description, Status status, Category category, Priority priority, UserDomain clientDomain, UserDomain technicianDomain) {
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.title = title;
        this.description = description;
        this.status = status;
        this.category = category;
        this.priority = priority;
        this.clientDomain = clientDomain;
        this.technicianDomain = technicianDomain;
    }

    public TicketDomain() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public UserDomain getClientDomain() {
        return clientDomain;
    }

    public void setClientDomain(UserDomain clientDomain) {
        this.clientDomain = clientDomain;
    }

    public UserDomain getTechnicianDomain() {
        return technicianDomain;
    }

    public void setTechnicianDomain(UserDomain technicianDomain) {
        this.technicianDomain = technicianDomain;
    }

}
