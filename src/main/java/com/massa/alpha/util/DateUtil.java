package com.massa.alpha.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public DateUtil() {
    }

    public static Date now() {
        return new Date();
    }

    public static Date stringToDate(String date, String format) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(format);
            return fmt.parse(date);
        } catch (ParseException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static String dateToString(Date date, String format) {
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.format(date);
    }

    public static Date setYear(Date date, int year) {
        return set(date, 1, year);
    }

    public static Date setMonth(Date date, int month) {
        return set(date, 2, month);
    }

    public static Date setDayOfMonth(Date date, int dayOfMonth) {
        return set(date, 5, dayOfMonth);
    }

    public static Date setHourOfDay(Date date, int hourOfDay) {
        return set(date, 11, hourOfDay);
    }

    public static Date setMinute(Date date, int minute) {
        return set(date, 12, minute);
    }

    public static Date setSecond(Date date, int second) {
        return set(date, 13, second);
    }

    public static Date setMilliSecond(Date date, int second) {
        return set(date, 14, second);
    }

    public static Date createDate(int year, int month, int dayOfMonth) {
        return createDate(year, month, dayOfMonth, 0, 0, 0, 0);
    }

    public static Date createDate(int year, int month, int dayOfMonth, int hourOfDay) {
        return createDate(year, month, dayOfMonth, hourOfDay, 0, 0, 0);
    }

    public static Date createDate(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
        return createDate(year, month, dayOfMonth, hourOfDay, minute, 0, 0);
    }

    public static Date createDate(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second) {
        return createDate(year, month, dayOfMonth, hourOfDay, minute, second, 0);
    }

    public static Date createDate(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second, int milliSeconds) {
        Calendar c = Calendar.getInstance();
        c.set(1, year);
        c.set(2, month);
        c.set(5, dayOfMonth);
        c.set(11, hourOfDay);
        c.set(12, minute);
        c.set(13, second);
        c.set(14, milliSeconds);
        return c.getTime();
    }

    public static Date set(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(calendarField, amount);
            return c.getTime();
        }
    }

    public static Date addYears(Date date, int amount) {
        return add(date, 1, amount);
    }

    public static Date addMonths(Date date, int amount) {
        return add(date, 2, amount);
    }

    public static Date addDays(Date date, int amount) {
        return add(date, 5, amount);
    }

    public static Date addHours(Date date, int amount) {
        return add(date, 10, amount);
    }

    public static Date addMinutes(Date date, int amount) {
        return add(date, 12, amount);
    }

    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(calendarField, amount);
            return c.getTime();
        }
    }

    public static int getYear() {
        return getYear((Date)null);
    }

    public static int getYear(Date date) {
        long tick = date == null ? System.currentTimeMillis() : date.getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(tick);
        return c.get(1);
    }

    public static int getMonthOfYear() {
        return getMonthOfYear((Date)null);
    }

    public static int getMonthOfYear(Date date) {
        long tick = date == null ? System.currentTimeMillis() : date.getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(tick);
        return c.get(2) + 1;
    }

    public static int getDayOfWeek() {
        return getDayOfWeek((Date)null);
    }

    public static int getDayOfWeek(Date date) {
        long tick = date == null ? System.currentTimeMillis() : date.getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(tick);
        return c.get(7);
    }

    public static int getDayOfMonth() {
        return getDayOfMonth((Date)null);
    }

    public static int getDayOfMonth(Date date) {
        long tick = date == null ? System.currentTimeMillis() : date.getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(tick);
        return c.get(5);
    }

    public static int getDayOfYear() {
        return getDayOfYear((Date)null);
    }

    public static int getDayOfYear(Date date) {
        long tick = date == null ? System.currentTimeMillis() : date.getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(tick);
        return c.get(6);
    }

    public static Date getFirstDateOfMonth() {
        return getFirstDateOfMonth((Date)null);
    }

    public static Date getFirstDateOfMonth(Date date) {
        long tick = date == null ? System.currentTimeMillis() : date.getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(tick);
        c.set(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        c.set(14, 0);
        return c.getTime();
    }

    public static Date getFirstDateOfWeek(Date date) {
        long tick = date == null ? System.currentTimeMillis() : date.getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(tick);
        c.set(7, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        c.set(14, 0);
        return c.getTime();
    }

    public static Date getLastDateOfMonth() {
        return getLastDateOfMonth((Date)null);
    }

    public static Date getLastDateOfMonth(Date date) {
        long tick = date == null ? System.currentTimeMillis() : date.getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(tick);
        c.set(5, getLastDayOfMonth(date));
        c.set(11, 23);
        c.set(12, 59);
        c.set(13, 59);
        c.set(14, 999);
        return c.getTime();
    }

    public static Date getLastDateOfWeek(Date date) {
        long tick = date == null ? System.currentTimeMillis() : date.getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(tick);
        c.set(7, 7);
        c.set(11, 23);
        c.set(12, 59);
        c.set(13, 59);
        c.set(14, 999);
        return c.getTime();
    }

    public static Date getFirstDateOfDay() {
        return getFirstDateOfDay((Date)null);
    }

    public static Date getFirstDateOfDay(Date date) {
        return new Date(getFirstTimeOfDay(date));
    }

    public static long getFirstTimeOfDay() {
        return getFirstTimeOfDay((Date)null);
    }

    public static long getFirstTimeOfDay(Date date) {
        long tick = date == null ? System.currentTimeMillis() : date.getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(tick);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        c.set(14, 0);
        return c.getTimeInMillis();
    }

    public static long getFirstTimeOfHour() {
        return getFirstTimeOfHour((Date)null);
    }

    public static long getFirstTimeOfHour(Date date) {
        long tick = date == null ? System.currentTimeMillis() : date.getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(tick);
        c.set(12, 0);
        c.set(13, 0);
        c.set(14, 0);
        return c.getTimeInMillis();
    }

    public static Date getLastDateOfDay() {
        return getLastDateOfDay((Date)null);
    }

    public static Date getLastDateOfDay(Date date) {
        return new Date(getLastTimeOfDay(date));
    }

    public static long getLastTimeOfDay() {
        return getLastTimeOfDay((Date)null);
    }

    public static long getLastTimeOfDay(Date date) {
        long tick = date == null ? System.currentTimeMillis() : date.getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(tick);
        c.set(11, 23);
        c.set(12, 59);
        c.set(13, 59);
        c.set(14, 999);
        return c.getTimeInMillis();
    }

    public static int getLastDayOfMonth() {
        return getLastDayOfMonth((Date)null);
    }

    public static int getLastDayOfMonth(Date date) {
        long tick = date == null ? System.currentTimeMillis() : date.getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(tick);
        return c.getActualMaximum(5);
    }

    public static int getDifferenceOfDate(int nYear1, int nMonth1, int nDate1, int nYear2, int nMonth2, int nDate2) {
        Calendar cal = Calendar.getInstance();
        int nTotalDate1 = 0;
        int nTotalDate2 = 0;
        int nDiffOfYear = 0;
        int nDiffOfDay = 0;
        int i;
        if (nYear1 > nYear2) {
            for(i = nYear2; i < nYear1; ++i) {
                cal.set(i, 12, 0);
                nDiffOfYear += cal.get(6);
            }

            nTotalDate1 += nDiffOfYear;
        } else if (nYear1 < nYear2) {
            for(i = nYear1; i < nYear2; ++i) {
                cal.set(i, 12, 0);
                nDiffOfYear += cal.get(6);
            }

            nTotalDate2 += nDiffOfYear;
        }

        cal.set(nYear1, nMonth1 - 1, nDate1);
        nDiffOfDay = cal.get(6);
        nTotalDate1 += nDiffOfDay;
        cal.set(nYear2, nMonth2 - 1, nDate2);
        nDiffOfDay = cal.get(6);
        nTotalDate2 += nDiffOfDay;
        return nTotalDate1 - nTotalDate2;
    }

    public static void main(String[] args) {
        System.out.println("getDayOfMonth()=" + getDayOfMonth());
        System.out.println("getDayOfWeek()=" + getDayOfWeek());
        System.out.println("getDayOfYear()=" + getDayOfYear());
        System.out.println("getFirstDateOfDay()=" + getFirstDateOfDay());
        System.out.println("getFirstDateOfMonth()=" + getFirstDateOfMonth());
        System.out.println("getFirstTimeOfDay()=" + getFirstTimeOfDay());
        System.out.println("getLastDateOfDay()=" + getLastDateOfDay());
        System.out.println("getLastDateOfMonth()=" + getLastDateOfMonth());
        System.out.println("getLastDayOfMonth()=" + getLastDayOfMonth());
        System.out.println("getLastTimeOfDay()=" + getLastTimeOfDay());
        System.out.println("getMonthOfYear()=" + getMonthOfYear());
        System.out.println("getYear()=" + getYear());
        System.out.println("getDifferenceOfDate()=" + getDifferenceOfDate(2014, 3, 31, 2014, 3, 1));
    }
}
