package ru.netology.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ServiceMethods {
    public String getDate(int daysToAdd, String pattern) {
        LocalDate date = LocalDate.now().plusDays(daysToAdd);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        return date.format(formatter);
    }

    public String getPastDate(int minusDays, String pattern) {
        LocalDate date = LocalDate.now().minusDays(minusDays);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        return date.format(formatter);
    }
}
