package com.httpso_hello.hello.helper;

import android.text.format.Time;

/**
 * Created by mixir on 14.08.2017.
 */

public class ConverterDate {




    //Входящее int лайков, исходящее Strung вида "Понравилось 5 людям"
    public static String likeStr(int likeInt){
        String likeStr = "";
        if (likeInt != 0) {
            if (likeInt == 1) likeStr = "Понравилось 1 человеку"; else likeStr = "Понравилось " + likeInt + " людям";
            if ((likeInt > 20) && (likeInt % 10 == 1)) likeStr = "Понравилось " + likeInt + " человеку";
        } else likeStr = "Нет оценок";
        return likeStr;
    }

    //конвертер URL аватарки
    public static String convertUrlAvatar(String oldAva) {
        char[] choldAva = oldAva.toCharArray();
        String newAva = "https://o-hello.com/upload/";
//        for (int i = 0; i < choldAva.length; i++){
//            if (i > 19) newAva = newAva + Character.toString(choldAva[i]);
//        }
        newAva += oldAva.substring(20);
        return newAva;
    }

    public static String convertUriSubHost(String path){
        return path.substring(20);
    }

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
        if (Integer.parseInt(month) == 1) nameOfMonth = "янв";
        else if (Integer.parseInt(month) == 2) nameOfMonth = "фев";
        else if (Integer.parseInt(month) == 3) nameOfMonth = "мар";
        else if (Integer.parseInt(month) == 4) nameOfMonth = "апр";
        else if (Integer.parseInt(month) == 5) nameOfMonth = "май";
        else if (Integer.parseInt(month) == 6) nameOfMonth = "июн";
        else if (Integer.parseInt(month) == 7) nameOfMonth = "июл";
        else if (Integer.parseInt(month) == 8) nameOfMonth = "авг";
        else if (Integer.parseInt(month) == 9) nameOfMonth = "сен";
        else if (Integer.parseInt(month) == 10) nameOfMonth = "окт";
        else if (Integer.parseInt(month) == 11) nameOfMonth = "ноя";
        else if (Integer.parseInt(month) == 12) nameOfMonth = "дек";
        else nameOfMonth = "";

        return hour + ":" + minute;
    }





    // метод для обработки даты в списке контактов
    public static String convertDateForContacts(String oldDate) {
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
        if (Integer.parseInt(month) == 1) nameOfMonth = "янв";
        else if (Integer.parseInt(month) == 2) nameOfMonth = "фев";
        else if (Integer.parseInt(month) == 3) nameOfMonth = "мар";
        else if (Integer.parseInt(month) == 4) nameOfMonth = "апр";
        else if (Integer.parseInt(month) == 5) nameOfMonth = "май";
        else if (Integer.parseInt(month) == 6) nameOfMonth = "июн";
        else if (Integer.parseInt(month) == 7) nameOfMonth = "июл";
        else if (Integer.parseInt(month) == 8) nameOfMonth = "авг";
        else if (Integer.parseInt(month) == 9) nameOfMonth = "сен";
        else if (Integer.parseInt(month) == 10) nameOfMonth = "окт";
        else if (Integer.parseInt(month) == 11) nameOfMonth = "ноя";
        else if (Integer.parseInt(month) == 12) nameOfMonth = "дек";
        else nameOfMonth = "";

        //Если сегодня то выводим только время, если вчера то выводим "вчера"
        if ((dayInt == dayCurrentInt) && (monthInt == monthCurrentInt) && (yearInt == yearCurrentInt)) {
            return hour + ":" + minute;
        } else if ((dayCurrentInt - dayInt == 1) && (monthInt == monthCurrentInt) && (yearInt == yearCurrentInt)) {
            return "вчера";
        } else {
            return day + nameOfMonth;
        }
    }





    // метод для обработки даты последнего входа
    public static String convertDateForEnter(String oldDate, int gender) {
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

        int monthCurrentInt = Integer.parseInt(monthCurrent);
        int monthInt = Integer.parseInt(month);
        int yearCurrentInt = Integer.parseInt(yearCurrent);
        int yearInt = Integer.parseInt(year);
        int dayCurrentInt = Integer.parseInt(dayCurrent);
        int dayInt = Integer.parseInt(day);
        int hourCurrentInt = Integer.parseInt(hourCurrent);
        int hourInt = Integer.parseInt(hour);
        int minuteCurrentInt = Integer.parseInt(minuteCurrent);
        int minuteInt = Integer.parseInt(minute);

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

        if (Integer.parseInt(day) < 10) day = Integer.toString(Integer.parseInt(day) % 10);

        //Если сегодня то выводим только время, если вчера то выводим "вчера"
        if (gender == 2) {
            if ((dayInt == dayCurrentInt) && (monthInt == monthCurrentInt) && (yearInt == yearCurrentInt)) {
                if (hourCurrentInt == hourInt){
                    if (minuteCurrentInt == minuteInt) return "Была в сети только что";
                    else return "Была в сети " + Integer.toString(minuteCurrentInt-minuteInt) + " минут назад";
                }
                else if ((hourCurrentInt - hourInt == 1) && (minuteInt > minuteCurrentInt)) return "Была в сети " + Integer.toString(60 - minuteInt + minuteCurrentInt) + " минут назад";
                else return "Была в сети сегодня в " + hour + ":" + minute;
            } else if ((dayCurrentInt - dayInt == 1) && (monthInt == monthCurrentInt) && (yearInt == yearCurrentInt)) {
                return "Была в сети вчера в " + hour + ":" + minute;
            } else {
                return "Была в сети " + day + " " +  nameOfMonth + " в " + hour + ":" + minute;
            }
        } else {
            if ((dayInt == dayCurrentInt) && (monthInt == monthCurrentInt) && (yearInt == yearCurrentInt)) {
                if (hourCurrentInt == hourInt){
                    if (minuteCurrentInt == minuteInt) return "Был в сети только что";
                    else return "Был в сети " + Integer.toString(minuteCurrentInt-minuteInt) + " минут назад";
                }
                else if ((hourCurrentInt - hourInt == 1) && (minuteInt > minuteCurrentInt)) return "Был в сети " + Integer.toString(60 - minuteInt + minuteCurrentInt) + " минут назад";
                else return "Был в сети сегодня в " + hour + ":" + minute;
            } else if ((dayCurrentInt - dayInt == 1) && (monthInt == monthCurrentInt) && (yearInt == yearCurrentInt)) {
                return "Был в сети вчера в " + hour + ":" + minute;
            } else {
                return "Был в сети " + day + " " + nameOfMonth + " в " + hour + ":" + minute;
            }
        }
    }





    //метод для обработки гостя
    public static String convertDateForGuest(String oldDate) {
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

        int monthCurrentInt = Integer.parseInt(monthCurrent);
        int monthInt = Integer.parseInt(month);
        int yearCurrentInt = Integer.parseInt(yearCurrent);
        int yearInt = Integer.parseInt(year);
        int dayCurrentInt = Integer.parseInt(dayCurrent);
        int dayInt = Integer.parseInt(day);
        int hourCurrentInt = Integer.parseInt(hourCurrent);
        int hourInt = Integer.parseInt(hour);
        int minuteCurrentInt = Integer.parseInt(minuteCurrent);
        int minuteInt = Integer.parseInt(minute);

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

        if (Integer.parseInt(day) < 10) day = Integer.toString(Integer.parseInt(day) % 10);

        //Если сегодня то выводим только время, если вчера то выводим "вчера"
        if ((dayInt == dayCurrentInt) && (monthInt == monthCurrentInt) && (yearInt == yearCurrentInt)) {
            if (hourCurrentInt == hourInt) {
                if (minuteCurrentInt == minuteInt) return "Только что";
                else return Integer.toString(minuteCurrentInt - minuteInt) + " минут назад";
            } else if ((hourCurrentInt - hourInt == 1) && (minuteInt > minuteCurrentInt))
                return Integer.toString(60 - minuteInt + minuteCurrentInt) + " минут назад";
            else return "Сегодня в " + hour + ":" + minute;
        } else if ((dayCurrentInt - dayInt == 1) && (monthInt == monthCurrentInt) && (yearInt == yearCurrentInt)) {
            return "Вчера в " + hour + ":" + minute;
        } else {
            return day + " " + nameOfMonth;
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
    //Метод для получения кол-ва баллов по ID
    public static int getSummaById(String id) {
        if (id.equals("tobuy10points")) return 10;
        else if (id.equals("buy20points")) return 20;
        else if (id.equals("buy30points")) return 30;
        else if (id.equals("buy40points")) return 40;
        else if (id.equals("buy50points")) return 50;
        else if (id.equals("buy60points")) return 60;
        else if (id.equals("buy70points")) return 70;
        else if (id.equals("buy80points")) return 80;
        else if (id.equals("buy90points")) return 90;
        else if (id.equals("buy100points")) return 100;
        else if (id.equals("buy200points")) return 200;
        else if (id.equals("buy300points")) return 300;
        else if (id.equals("buy400points")) return 400;
        else if (id.equals("buy500points")) return 500;
        else if (id.equals("buy1000points")) return 1000;
        else return 1;
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

    //Получение название месяца
    public static String getMonthName(int id) {
        if (id == 1) return " января ";
        else if (id == 2) return " февраля ";
        else if (id == 3) return " марта ";
        else if (id == 4) return " апреля ";
        else if (id == 5) return " мая ";
        else if (id == 6) return " июня ";
        else if (id == 7) return " июля ";
        else if (id == 8) return " августа ";
        else if (id == 9) return " сентября ";
        else if (id == 10) return " октября ";
        else if (id == 11) return " ноября ";
        else if (id == 12) return " декабря ";
        else return "";
    }

    //Получение дня
    public static int getDay(String oldDate) {
        char[] chOldDate = oldDate.toCharArray();
        String day = Character.toString(chOldDate[8]) + Character.toString(chOldDate[9]);

        return Integer.parseInt(day);
    }
}
