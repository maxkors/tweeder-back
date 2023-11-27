package com.maxkors.tweeder.security;

public record SignupRequest(String username, String password, String name, String email) {
}
