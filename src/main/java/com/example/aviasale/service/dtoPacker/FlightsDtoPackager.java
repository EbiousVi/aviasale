package com.example.aviasale.service.dtoPacker;

import java.util.List;

public interface FlightsDtoPackager<T, R> {
    List<T> wrapToDto(List<R> list);
}
