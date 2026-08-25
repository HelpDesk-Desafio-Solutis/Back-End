package br.com.ticket.microservice.core.domain;

import br.com.ticket.microservice.core.enums.Role;

import java.util.UUID;

public class UserDomain {

    private UUID uuid;
    private String name;
    private String email;
    private Role role;

    public UserDomain() {
    }

    public UserDomain(UUID uuid) {
        this.uuid = uuid;
    }

    public UserDomain(UUID uuid, String name, String email, Role role) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
