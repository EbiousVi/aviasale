package com.example.aviasale.domain.json;

public class ContactsJson {
    private String email;
    private String phone;

    @Override
    public String toString() {
        return "JsonMap{" +
                "email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public ContactsJson() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
