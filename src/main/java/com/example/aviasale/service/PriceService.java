package com.example.aviasale.service;

import com.example.aviasale.domain.pojo.Price;
import com.example.aviasale.repository.FlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PriceService {

    private final FlightsRepository flightsRepository;

    @Autowired
    public PriceService(FlightsRepository flightsRepository) {
        this.flightsRepository = flightsRepository;
    }

    public Price createPrice(Integer flightId, String flightNo, String conditions) {
        Price price = new Price();
        price.setFlightId(flightId);
        price.setValue(this.calculatePrice(flightNo, conditions));
        return price;
    }

    public BigDecimal calculatePrice(String flightNo, String conditions) {
        Double dbPrice = this.calculatePriceFromDB(flightNo);
        if (dbPrice == 0D) {
            return this.calculateRandomPrice(conditions);
        } else {
            return BigDecimal.valueOf(dbPrice);
        }
    }

    public Double calculatePriceFromDB(String flightNo) {
        Optional<Double> priceValue = flightsRepository.avgPriceByFlightNo(flightNo);
        return priceValue.orElse(0D);
    }

    public BigDecimal calculateRandomPrice(String conditions) {
        if (conditions.equalsIgnoreCase("economy")) {
            return BigDecimal.valueOf(5000D + Math.round(Math.random() * 5000));
        } else if (conditions.equalsIgnoreCase("comfort")) {
            return BigDecimal.valueOf(12000D + Math.round(Math.random() * 10000));
        } else if (conditions.equalsIgnoreCase("business")) {
            return BigDecimal.valueOf(23000D + Math.round(Math.random() * 15000));
        }
        return BigDecimal.valueOf(Integer.MAX_VALUE);
    }
}
