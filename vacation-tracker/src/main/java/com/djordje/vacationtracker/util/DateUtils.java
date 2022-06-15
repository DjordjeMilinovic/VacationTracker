package com.djordje.vacationtracker.util;

import org.springframework.stereotype.Component;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Component
public class DateUtils {

    //returns a Date created using the given String; format of the
    public Date convertStringToDate(String date){
        Date converted = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM d, y", new Locale("en"));
        if(dateFormat == null)
            return null;
        converted =  dateFormat.parse(date, new ParsePosition(0));
        return  converted;
    }

    //converts java.sql.Date to Calendar
    public Calendar dateToCalendar(java.sql.Date date){
        String[] dateInfo = date.toString().split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(dateInfo[0]),
                Integer.parseInt(dateInfo[1]) - 1,
                Integer.parseInt(dateInfo[2]) );
        return calendar;
    }
}
