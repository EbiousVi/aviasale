package com.example.aviasale.domain.dto.apiDto;

public class PassengersData {
    private String passengerName;
    private String passengerId;
    private String phone;
    private String email;

    public PassengersData() {
    }

    @Override
    public String toString() {
        return "PassengersData{" +
                "passengerName='" + passengerName + '\'' +
                ", passengerId='" + passengerId + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
