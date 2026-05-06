package ru.slisarenko.spring_security_jwt.model;

public enum Role {
    USER,
    MODERATOR,
    SUPER_ADMIN;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
