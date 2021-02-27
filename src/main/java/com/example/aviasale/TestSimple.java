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
        List<String> list = new ArrayList<>();
        list.add("CEK");
        list.add("VKO");
        List<String> list2 = new ArrayList<>();
        list2.add("VKO");

        boolean b = list.removeAll(list2);
        System.out.println(list);
    }
}
