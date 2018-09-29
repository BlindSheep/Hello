package com.httpso_hello.hello.helper;

/**
 * Created by mixir on 29.07.2018.
 */

public class Converter {
    //Склонение подписчиков
    public static String getFollowers (int count) {
        String result = null;
        if (count != 0) {
            if (count == 1) result = "1 подписчик";
            if ((count == 2) || (count == 3) || (count == 4)) result = Integer.toString(count) + " подписчика";
            if ((count >= 5) && (count < 21)) result = Integer.toString(count) + " подписчиков";
            if ((count >= 21) && (count % 10 == 1)) result = Integer.toString(count) + " подписчик";
            else if ((count >= 22) && (count % 10 == 2)) result = Integer.toString(count) + " подписчика";
            else if ((count >= 23) && (count % 10 == 3)) result = Integer.toString(count) + " подписчика";
            else if ((count >= 24) && (count % 10 == 4)) result = Integer.toString(count) + " подписчика";
            else if (count >= 25) result = Integer.toString(count) + " подписчиков";
        } else result = "Нет подписчиков";
        return result;
    }

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
        String newAva = "https://o-hello.com/upload/";
        newAva += oldAva.substring(20);
        return newAva;
    }

    public static String convertUriSubHost(String path){
        return path.substring(20);
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
}
