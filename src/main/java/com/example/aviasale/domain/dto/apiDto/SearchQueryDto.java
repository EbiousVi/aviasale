package com.example.aviasale.domain.dto.apiDto;

public class SearchQueryDto {
    private String cityFrom;
    private String cityTo;
    private String date;
    private String conditions;
    private Integer numberOfTickets;

    public SearchQueryDto() {
    }

    @Override
    public String toString() {
        return "SearchQueryDto{" +
                "cityFrom='" + cityFrom + '\'' +
                ", cityTo='" + cityTo + '\'' +
                ", date='" + date + '\'' +
                ", conditions='" + conditions + '\'' +
                ", numberOfTickets=" + numberOfTickets +
                '}';
    }

    public String getCityFrom() {
        return cityFrom;
    }

    public void setCityFrom(String cityFrom) {
        this.cityFrom = cityFrom;
    }

    public String getCityTo() {
        return cityTo;
    }

    public void setCityTo(String cityTo) {
        this.cityTo = cityTo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public Integer getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(Integer numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }
}
