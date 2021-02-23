package com.example.aviasale.domain.entity;

import com.example.aviasale.domain.custom_types.StringJsonUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tickets")
@TypeDefs( {@TypeDef( name= "StringJsonObject", typeClass = StringJsonUserType.class)})
public class Tickets {
    public Tickets(String ticketNumber, String bookRef, String passengerId, String passengerName, String contactDataJsonb) {
        this.ticketNumber = ticketNumber;
        this.bookRef = bookRef;
        this.passengerId = passengerId;
        this.passengerName = passengerName;
        this.contactDataJsonb = contactDataJsonb;
    }

    @Id
    @Column(name = "ticket_no")
    private String ticketNumber;
    @Column(name = "book_ref")
    private String bookRef;
    @Column(name = "passenger_id")
    private String passengerId;
    @Column(name = "passenger_name")
    private String passengerName;

    @Column(name = "contact_data")
    @Type(type = "StringJsonObject")
    private String contactDataJsonb;

    public Tickets() {
    }

    @Override
    public String toString() {
        return "Tickets{" +
                "ticketNumber='" + ticketNumber + '\'' +
                ", bookRef='" + bookRef + '\'' +
                ", passengerId='" + passengerId + '\'' +
                ", passengerName='" + passengerName + '\'' +
                ", jsonb=" + contactDataJsonb +
                '}';
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getBookRef() {
        return bookRef;
    }

    public void setBookRef(String bookRef) {
        this.bookRef = bookRef;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getContactDataJsonb() {
        return contactDataJsonb;
    }

    public void setContactDataJsonb(String jsonb) {
        this.contactDataJsonb = jsonb;
    }
}
