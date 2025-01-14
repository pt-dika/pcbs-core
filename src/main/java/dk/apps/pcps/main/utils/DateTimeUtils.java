package dk.apps.pcps.main.utils;

import org.joda.time.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;

public class DateTimeUtils {

    public static Timestamp getTimeStamp(){
        return new Timestamp(System.currentTimeMillis());
    }

    public static String getDateTime(){
        return getDateTime("MMddhhmmss");
    }

    public static String getDateTime(String format){
        Date currentDate = new Date(System.currentTimeMillis());
        DateFormat df = new SimpleDateFormat(format);
        return df.format(currentDate);
    }

    public static String getDateTime(String format, boolean isLastDay, int days){
        DateTimeFormatter dtf = DateTimeFormatter
                .ofPattern(format);
        LocalDate now = LocalDate.now();
        LocalDate dates = null;
        if (isLastDay)
            dates = now.minusDays(days);
        else
            dates = now.plusDays(days);
        return dtf.format(dates);
    }

    public static long countDays(String from, String to){
        if (from.equals("") || to.equals("")){
            return 0;
        }
        return ChronoUnit.DAYS.between(strToLocalDate(from).atStartOfDay(), strToLocalDate(to).atStartOfDay());
    }


    public static Long timeToSeconds(String time) {
        Date date = strToDateTime("HH:mm:ss", time);
        return date.getTime();
    }

    public static Time strToTime(String val){
        Date dt = strToDateTime("HH:mm:ss", val);
        return new Time(dt.getTime());
    }

    public static LocalTime strToLocalTime(String val){
        return LocalTime.parse(val);
    }

    public static String timeToStr(Time val){
        return strDateTimeFormat("HH:mm:ss", val);
    }

    public static String[] getTimes(String val){
        String tf = "HH:mm:ss";
        Date dt = strToDateTime(tf, val);
        return strDateTimeFormat(tf, dt).split(":");
    }

    public static String dateTimeFormat(String format, LocalDateTime val){
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        String dt = df.format(val);
        return dt;
    }

    public static String strDateTimeFormat(String format, Date val){
        DateFormat df = new SimpleDateFormat(format);
        String dt = df.format(val);
        return dt;
    }

    public static String strDateTimeFormat(String format, Time val){
        DateFormat df = new SimpleDateFormat(format);
        String dt = df.format(val);
        return dt;
    }

    public static Date strToDateTime(String format, String val){
        DateFormat df = new SimpleDateFormat(format);
        Date dt = null;
        try {
            dt = df.parse(val);
        } catch (ParseException e){

        }
        return dt;
    }

    public static LocalDate strToLocalDate(String val){
        LocalDate dt = LocalDate.parse(val);
        return dt;
    }

    public static Timestamp getTimestampFrom(String value){
        return Timestamp.valueOf(value);
    }

    public static HashMap getMapCountIn(Date from, Date to){
        HashMap hashMap = new HashMap();
        DateTime dt1 = new DateTime(from);
        DateTime dt2 = new DateTime(to);
        hashMap.put("days", Days.daysBetween(dt1, dt2).getDays());
        hashMap.put("hours",  Hours.hoursBetween(dt1, dt2).getHours() % 24);
        hashMap.put("minutes", Minutes.minutesBetween(dt1, dt2).getMinutes() % 60);
        hashMap.put("seconds", Seconds.secondsBetween(dt1, dt2).getSeconds() % 60);
        hashMap.put("second", Seconds.secondsBetween(dt1, dt2).getSeconds());
        return hashMap;
    }

    public static boolean isBetweenNow(LocalDateTime start, LocalDateTime end) {
        LocalDateTime tsNow = getTimeStamp().toLocalDateTime();
        return tsNow.isAfter(start) && tsNow.isBefore(end);
    }

    public static boolean isBetweenTime(LocalTime start, LocalTime end) {
        LocalTime timeNow = LocalTime.now();
        return timeNow.isAfter(start) && timeNow.isBefore(end);
    }

    public static HashMap getRangeIn(Date from, Date to){
        HashMap hashMap = new HashMap();
        DateTime dt1 = new DateTime(from);
        DateTime dt2 = new DateTime(to);
        Timestamp tsNow = getTimeStamp();

        hashMap.put("days", Days.daysBetween(dt1, dt2).getDays());
        hashMap.put("hours",  Hours.hoursBetween(dt1, dt2).getHours() % 24);
        hashMap.put("minutes", Minutes.minutesBetween(dt1, dt2).getMinutes() % 60);
        hashMap.put("seconds", Seconds.secondsBetween(dt1, dt2).getSeconds() % 60);
        hashMap.put("second", Seconds.secondsBetween(dt1, dt2).getSeconds());
        return hashMap;
    }
}
