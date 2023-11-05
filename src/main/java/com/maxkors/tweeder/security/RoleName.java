package com.maxkors.tweeder.security;

public enum RoleName {
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");

    private String name;

    RoleName(String name) {
        this.name = name;
    }

    public String value() {
        return this.name;
    }
}
