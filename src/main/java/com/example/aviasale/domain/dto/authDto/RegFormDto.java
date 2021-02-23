package com.example.aviasale.domain.dto.authDto;

public class RegFormDto {
    private String email;
    private String password;

    public RegFormDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
