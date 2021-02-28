package com.example.aviasale;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestSimple {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(100);
        list.add(200);
        list.add(300);
        list.add(400);

        for (int i = 0; i < list.size(); i+=2) {
            System.out.println(list.get(i));
            System.out.println(list.get(i + 1));
        }
    }
}
