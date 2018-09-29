package com.httpso_hello.hello.helper;

import android.text.format.Time;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mixir on 14.08.2017.
 */

public class ConverterDate {
    // метод для обработки даты в чате
    public static String convertDayForChat(String oldDate) {
        String nameOfMonth;

        //распарсиваем дату с серва
        char[] chOldDate = oldDate.toCharArray();
        String year = Character.toString(chOldDate[0]) + Character.toString(chOldDate[1]) + Character.toString(chOldDate[2]) + Character.toString(chOldDate[3]);
        String month = Character.toString(chOldDate[5]) + Character.toString(chOldDate[6]);
        String day = Character.toString(chOldDate[8]) + Character.toString(chOldDate[9]);
        String hour = Character.toString(chOldDate[11]) + Character.toString(chOldDate[12]);
        String minute = Character.toString(chOldDate[14]) + Character.toString(chOldDate[15]);

        //получение текущей даты
        Time time = new Time(Time.getCurrentTimezone());
        time.setToNow();

        //распарсиваем текущую дату
        char[] chCurrentDate = time.toString().toCharArray();
        String yearCurrent = Character.toString(chCurrentDate[0]) + Character.toString(chCurrentDate[1]) + Character.toString(chCurrentDate[2]) + Character.toString(chCurrentDate[3]);
        String monthCurrent = Character.toString(chCurrentDate[4]) + Character.toString(chCurrentDate[5]);
        String dayCurrent = Character.toString(chCurrentDate[6]) + Character.toString(chCurrentDate[7]);
        String hourCurrent = Character.toString(chCurrentDate[9]) + Character.toString(chCurrentDate[10]);
        String minuteCurrent = Character.toString(chCurrentDate[11]) + Character.toString(chCurrentDate[12]);

        int dayCurrentInt = Integer.parseInt(dayCurrent);
        int dayInt = Integer.parseInt(day);

        int monthCurrentInt = Integer.parseInt(monthCurrent);
        int monthInt = Integer.parseInt(month);

        int yearCurrentInt = Integer.parseInt(yearCurrent);
        int yearInt = Integer.parseInt(year);

        //присваеваем имена месяцам
        if (Integer.parseInt(month) == 1) nameOfMonth = "января";
        else if (Integer.parseInt(month) == 2) nameOfMonth = "февраля";
        else if (Integer.parseInt(month) == 3) nameOfMonth = "марта";
        else if (Integer.parseInt(month) == 4) nameOfMonth = "апреля";
        else if (Integer.parseInt(month) == 5) nameOfMonth = "мая";
        else if (Integer.parseInt(month) == 6) nameOfMonth = "июня";
        else if (Integer.parseInt(month) == 7) nameOfMonth = "июля";
        else if (Integer.parseInt(month) == 8) nameOfMonth = "августа";
        else if (Integer.parseInt(month) == 9) nameOfMonth = "сентября";
        else if (Integer.parseInt(month) == 10) nameOfMonth = "октября";
        else if (Integer.parseInt(month) == 11) nameOfMonth = "ноября";
        else if (Integer.parseInt(month) == 12) nameOfMonth = "декабря";
        else nameOfMonth = "";

        //Если сегодня то выводим только время, если вчера то выводим "вчера" и время
        if ((dayInt == dayCurrentInt) && (monthInt == monthCurrentInt) && (yearInt == yearCurrentInt)) {
            return "сегодня";
        } else if ((dayCurrentInt - dayInt == 1) && (monthInt == monthCurrentInt) && (yearInt == yearCurrentInt)) {
            return "вчера";
        } else {
            return day + " " + nameOfMonth;
        }
    }

    // метод для обработки времени в чате
    public static String convertDateForChat(String oldDate) {
        Date date = getAppDateFromServerDate(oldDate);
        Time time = new Time(Time.getCurrentTimezone());
        time.setToNow();

        int hourInt = date.getHours();

        String minutToString;
        if (date.getMinutes() > 10) minutToString = Integer.toString(date.getMinutes());
        else minutToString = "0" + Integer.toString(date.getMinutes());

        return Integer.toString(hourInt) + ":" + minutToString;
    }

    // метод для обработки даты в списке контактов
    public static String convertDateForContacts(String oldDate) {
        Date date = getAppDateFromServerDate(oldDate);
        Time time = new Time(Time.getCurrentTimezone());
        time.setToNow();
        Date curentDate = getCurentDateFromTime(time);

        int yearCurrentInt = curentDate.getYear() + 1900;
        int yearInt = date.getYear() + 1900;

        int monthCurrentInt = curentDate.getMonth();
        int monthInt = date.getMonth();
        String nameOfMonth = getNameOfMonth(monthInt, "little");

        int dayCurrentInt = curentDate.getDate();
        int dayInt = date.getDate();

        int hourCurrentInt = curentDate.getHours();
        int hourInt = date.getHours();

        int minuteCurrentInt = curentDate.getMinutes();
        int minuteInt = date.getMinutes();
        String minutToString;
        if (date.getMinutes() > 10) minutToString = Integer.toString(date.getMinutes());
        else minutToString = "0" + Integer.toString(date.getMinutes());

        if (dayInt < 10) dayInt = dayInt % 10;
        //Если сегодня то выводим только время, если вчера то выводим "вчера"
        if ((dayInt == dayCurrentInt) && (monthInt == monthCurrentInt) && (yearInt == yearCurrentInt)) {
            return Integer.toString(hourInt) + ":" + minutToString;
        } else if ((dayCurrentInt - dayInt == 1) && (monthInt == monthCurrentInt) && (yearInt == yearCurrentInt)) {
            return "вчера";
        } else {
            return Integer.toString(dayInt) + nameOfMonth;
        }
    }

    // метод для обработки даты последнего входа
    public static String convertDateForEnter(String oldDate, int gender) {
        Date date = getAppDateFromServerDate(oldDate);
        Time time = new Time(Time.getCurrentTimezone());
        time.setToNow();
        Date curentDate = getCurentDateFromTime(time);

        int yearCurrentInt = curentDate.getYear() + 1900;
        int yearInt = date.getYear() + 1900;

        int monthCurrentInt = curentDate.getMonth();
        int monthInt = date.getMonth();
        String nameOfMonth = getNameOfMonth(monthInt, "full");

        int dayCurrentInt = curentDate.getDate();
        int dayInt = date.getDate();

        int hourCurrentInt = curentDate.getHours();
        int hourInt = date.getHours();

        int minuteCurrentInt = curentDate.getMinutes();
        int minuteInt = date.getMinutes();
        String minutToString;
        if (date.getMinutes() > 10) minutToString = Integer.toString(date.getMinutes());
        else minutToString = "0" + Integer.toString(date.getMinutes());

        if (dayInt < 10) dayInt = dayInt % 10;
        if (gender == 2) {
            if ((dayInt == dayCurrentInt) && (monthCurrentInt == (monthInt + 1)) && (yearInt == yearCurrentInt)) {
                if (hourCurrentInt == hourInt){
                    if (minuteCurrentInt == minuteInt) return "Была в сети только что";
                    else return "Была в сети " + Integer.toString(minuteCurrentInt-minuteInt) + " минут назад";
                }
                else if ((hourCurrentInt - hourInt == 1) && (minuteInt > minuteCurrentInt)) return "Была в сети " + Integer.toString(60 - minuteInt + minuteCurrentInt) + " минут назад";
                else return "Была в сети сегодня в " + Integer.toString(hourInt) + ":" + minutToString;
            } else if ((dayCurrentInt - dayInt == 1) && (monthInt == monthCurrentInt) && (yearInt == yearCurrentInt)) {
                return "Была в сети вчера в " + Integer.toString(hourInt) + ":" + minutToString;
            } else {
                return "Была в сети " + Integer.toString(dayInt) + " " +  nameOfMonth + " в " + Integer.toString(hourInt) + ":" + minutToString;
            }
        } else {
            if ((dayInt == dayCurrentInt) && (monthInt == monthCurrentInt) && (yearInt == yearCurrentInt)) {
                if (hourCurrentInt == hourInt){
                    if (minuteCurrentInt == minuteInt) return "Был в сети только что";
                    else return "Был в сети " + Integer.toString(minuteCurrentInt-minuteInt) + " минут назад";
                }
                else if ((hourCurrentInt - hourInt == 1) && (minuteInt > minuteCurrentInt)) return "Был в сети " + Integer.toString(60 - minuteInt + minuteCurrentInt) + " минут назад";
                else return "Был в сети сегодня в " + Integer.toString(hourInt) + ":" + minutToString;
            } else if ((dayInt - dayCurrentInt == 1) && (monthInt == monthCurrentInt) && (yearInt == yearCurrentInt)) {
                return "Был в сети вчера в " + Integer.toString(hourInt) + ":" + minutToString;
            } else {
                return "Был в сети " + Integer.toString(dayInt) + " " + nameOfMonth + " в " + Integer.toString(hourInt) + ":" + minutToString;
            }
        }
    }

    //метод для обработки гостя
    public static String convertDateForGuest(String oldDate) {
        Date date = getAppDateFromServerDate(oldDate);
        Time time = new Time(Time.getCurrentTimezone());
        time.setToNow();
        Date curentDate = getCurentDateFromTime(time);

        int yearCurrentInt = curentDate.getYear() + 1900;
        int yearInt = date.getYear() + 1900;

        int monthCurrentInt = curentDate.getMonth();
        int monthInt = date.getMonth();
        String nameOfMonth = getNameOfMonth(monthInt, "full");

        int dayCurrentInt = curentDate.getDate();
        int dayInt = date.getDate();

        int hourCurrentInt = curentDate.getHours();
        int hourInt = date.getHours();

        int minuteCurrentInt = curentDate.getMinutes();
        int minuteInt = date.getMinutes();
        String minutToString;
        if (date.getMinutes() > 10) minutToString = Integer.toString(date.getMinutes());
        else minutToString = "0" + Integer.toString(date.getMinutes());

        if (dayInt < 10) dayInt = dayInt % 10;
        if ((dayInt == dayCurrentInt) && (monthInt == monthCurrentInt) && (yearInt == yearCurrentInt)) {
            if (hourCurrentInt == hourInt) {
                if (minuteCurrentInt == minuteInt) return "Только что";
                else return Integer.toString(minuteCurrentInt - minuteInt) + " минут назад";
            } else if ((hourCurrentInt - hourInt == 1) && (minuteInt > minuteCurrentInt))
                return Integer.toString(60 - minuteInt + minuteCurrentInt) + " минут назад";
            else return "Сегодня в " + Integer.toString(hourInt) + ":" + minutToString;
        } else if ((dayCurrentInt - dayInt == 1) && (monthInt == monthCurrentInt) && (yearInt == yearCurrentInt)) {
            return "Вчера в " + Integer.toString(hourInt) + ":" + minutToString;
        } else {
            return Integer.toString(dayInt) + " " + nameOfMonth + " в " + Integer.toString(hourInt) + ":" + minutToString;
        }
    }

    // метод для обработки даты в возраст(возращает ввиде "20 лет")
    public static String convertDateToAge(String oldDate) {
        String age = "1";
        int ageInt = 1;
        String ageStr;

        //распарсиваем дату с серва
        char[] chOldDate = oldDate.toCharArray();
        String year = Character.toString(chOldDate[0]) + Character.toString(chOldDate[1]) + Character.toString(chOldDate[2]) + Character.toString(chOldDate[3]);
        String month = Character.toString(chOldDate[5]) + Character.toString(chOldDate[6]);
        String day = Character.toString(chOldDate[8]) + Character.toString(chOldDate[9]);

        //получение текущей даты
        Time time = new Time(Time.getCurrentTimezone());
        time.setToNow();

        //распарсиваем текущую дату
        char[] chCurrentDate = time.toString().toCharArray();
        String yearCurrent = Character.toString(chCurrentDate[0]) + Character.toString(chCurrentDate[1]) + Character.toString(chCurrentDate[2]) + Character.toString(chCurrentDate[3]);
        String monthCurrent = Character.toString(chCurrentDate[4]) + Character.toString(chCurrentDate[5]);
        String dayCurrent = Character.toString(chCurrentDate[6]) + Character.toString(chCurrentDate[7]);

        int yearCurrentInt = Integer.parseInt(yearCurrent);
        int yearInt = Integer.parseInt(year);
        int monthCurrentInt = Integer.parseInt(monthCurrent);
        int monthInt = Integer.parseInt(month);
        int dayCurrentInt = Integer.parseInt(dayCurrent);
        int dayInt = Integer.parseInt(day);

        if (monthCurrentInt - monthInt > 0){
            ageInt = yearCurrentInt - yearInt;
        } else if ((monthCurrentInt - monthInt == 0) && (dayCurrentInt - dayInt >= 0)) {
            ageInt = yearCurrentInt - yearInt;
        } else ageInt = yearCurrentInt - yearInt - 1;


        if(ageInt != 0) {
            if(ageInt == 1) ageStr = " год";
            else if((ageInt >= 2) && (ageInt < 5)) ageStr = " года";
            else if((ageInt >= 5) && (ageInt < 21)) ageStr = " лет";
            else if((ageInt >= 21) && (ageInt % 10 == 1)) ageStr = " год";
            else if((ageInt >= 21) && (ageInt % 10 == 2)) ageStr = " года";
            else if((ageInt >= 21) && (ageInt % 10 == 3)) ageStr = " года";
            else if((ageInt >= 21) && (ageInt % 10 == 4)) ageStr = " года";
            else ageStr = " лет";
        } else {
            ageStr = " ";
        }

        age = Integer.toString(ageInt);



        return age + ageStr;
    }

    //Конвертер даты рождения с сервера
    public static String getBirthdate(String oldDate) {
        String monthStr = "";
        char[] chOldDate = oldDate.toCharArray();
        String year = Character.toString(chOldDate[0]) + Character.toString(chOldDate[1]) + Character.toString(chOldDate[2]) + Character.toString(chOldDate[3]);
        String month = Character.toString(chOldDate[5]) + Character.toString(chOldDate[6]);
        String day = Character.toString(chOldDate[8]) + Character.toString(chOldDate[9]);
        if (month.equals("01")) monthStr = " января ";
        else if (month.equals("02")) monthStr = " февраля ";
        else if (month.equals("03")) monthStr = " марта ";
        else if (month.equals("04")) monthStr = " апреля ";
        else if (month.equals("05")) monthStr = " мая ";
        else if (month.equals("06")) monthStr = " июня ";
        else if (month.equals("07")) monthStr = " июля ";
        else if (month.equals("08")) monthStr = " августа ";
        else if (month.equals("09")) monthStr = " сентября ";
        else if (month.equals("10")) monthStr = " октября ";
        else if (month.equals("11")) monthStr = " ноября ";
        else if (month.equals("12")) monthStr = " декабря ";

        return day + monthStr + year + " г.";
    }

    //Конвертер даты рождения на сервер
    public static String sendBirthdate(int year, int month, int day) {
        String newBirthDate = "";
        if ((month > 9) && (day > 9)) newBirthDate = Integer.toString(year) + "-" + Integer.toString(month+1) + "-" + Integer.toString(day) + " 00:00:00";
        else if ((month <= 9) && (day > 9)) newBirthDate = Integer.toString(year) + "-0" + Integer.toString(month+1) + "-" + Integer.toString(day) + " 00:00:00";
        else if ((month > 9) && (day <= 9)) newBirthDate = Integer.toString(year) + "-" + Integer.toString(month+1) + "-0" + Integer.toString(day) + " 00:00:00";
        else if ((month <= 9) && (day <= 9)) newBirthDate = Integer.toString(year) + "-0" + Integer.toString(month+1) + "-0" + Integer.toString(day) + " 00:00:00";

        return newBirthDate;
    }

    //Получения года
    public static int getYear(String oldDate) {
        char[] chOldDate = oldDate.toCharArray();
        String year = Character.toString(chOldDate[0]) + Character.toString(chOldDate[1]) + Character.toString(chOldDate[2]) + Character.toString(chOldDate[3]);

        return Integer.parseInt(year);
    }

    //Получение месяца
    public static int getMonth(String oldDate) {
        char[] chOldDate = oldDate.toCharArray();
        String month = Character.toString(chOldDate[5]) + Character.toString(chOldDate[6]);

        return Integer.parseInt(month) - 1;
    }

    //Получение дня
    public static int getDay(String oldDate) {
        char[] chOldDate = oldDate.toCharArray();
        String day = Character.toString(chOldDate[8]) + Character.toString(chOldDate[9]);

        return Integer.parseInt(day);
    }













    public static Date getAppDateFromServerDate(String oldDate) {
        Locale locale = new Locale("ru");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", locale);
        Date date = null;
        try {
            date = dateFormat.parse(oldDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        date.setHours(date.getHours() - (date.getTimezoneOffset()/60));
        return date;
    }
    public static Date getCurentDateFromTime(Time time) {
        Locale locale = new Locale("ru");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss", locale);
        Date date = null;
        try {
            date = dateFormat.parse(time.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String getNameOfMonth(int monthInt, String howLong) {
        String nameOfMonth = "";
        if (howLong.equals("full")) {
            if (monthInt == 0) nameOfMonth = "января";
            else if (monthInt == 1) nameOfMonth = "февраля";
            else if (monthInt == 2) nameOfMonth = "марта";
            else if (monthInt == 3) nameOfMonth = "апреля";
            else if (monthInt == 4) nameOfMonth = "мая";
            else if (monthInt == 5) nameOfMonth = "июня";
            else if (monthInt == 6) nameOfMonth = "июля";
            else if (monthInt == 7) nameOfMonth = "августа";
            else if (monthInt == 8) nameOfMonth = "сентября";
            else if (monthInt == 9) nameOfMonth = "октября";
            else if (monthInt == 10) nameOfMonth = "ноября";
            else if (monthInt == 11) nameOfMonth = "декабря";
            else nameOfMonth = "";
        }
        if (howLong.equals("little")) {
            if (monthInt == 0) nameOfMonth = "янв";
            else if (monthInt == 1) nameOfMonth = "фев";
            else if (monthInt == 2) nameOfMonth = "мар";
            else if (monthInt == 3) nameOfMonth = "апр";
            else if (monthInt == 4) nameOfMonth = "май";
            else if (monthInt == 5) nameOfMonth = "июн";
            else if (monthInt == 6) nameOfMonth = "июл";
            else if (monthInt == 7) nameOfMonth = "авг";
            else if (monthInt == 8) nameOfMonth = "сен";
            else if (monthInt == 9) nameOfMonth = "окт";
            else if (monthInt == 10) nameOfMonth = "ноя";
            else if (monthInt == 11) nameOfMonth = "дек";
            else nameOfMonth = "";
        }

        return nameOfMonth;
    }
}
