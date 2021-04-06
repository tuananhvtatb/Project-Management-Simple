package com.vti.company.utillities;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class HandleDateUtils {
    public LocalDateTime getDateFromString(String value) {
        if(value.isEmpty())
            return null;

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        LocalDateTime date = null;

        date = LocalDateTime.parse(value, dateFormat);

        return date;
    }

    public String getDate(LocalDateTime date) {
        if(date == null)
            return null;

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        return date.format(dateFormat);
    }
}
