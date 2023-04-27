package com.User.services;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class ReaderOperation {
    public long dateCals(LocalDate nowDate1) {
        try {
            LocalDate nowDate = LocalDate.now();
            long countDays = ChronoUnit.DAYS.between(nowDate1, nowDate);
            if (countDays > 7) {
                countDays = (long) (countDays * 1.5);
                return countDays;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
