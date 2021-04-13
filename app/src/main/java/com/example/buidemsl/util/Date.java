package com.example.buidemsl.util;

public class Date {

    public String day;
    public String month;
    public String year;

    public Date(int day, int month, int year) {
        this.day = dosDigits(day);
        this.month = dosDigits(month + 1);
        this.year = String.valueOf(year);
    }

    public Date(String date, boolean SQLDate) {

        if (SQLDate) {
            String[] dateParts = date.split("-");
            day = dateParts[2];
            month = dateParts[1];
            year = dateParts[0];
        }
        else {
            String[] dateParts = date.split("/");
            day = dateParts[0];
            month = dateParts[1];
            year = dateParts[2];
        }
    }

    public String getEuropeanDate() {
        return this.day + "/" + this.month + "/" + this.year;
    }

    public String getSQLDate() {
        return this.year + "-" + this.month + "-" + this.day;
    }

    public static String dosDigits(int number) {
        return (number < 9) ? ("0" + number) : String.valueOf(number);
    }

    public int getDay() {
        return Integer.parseInt(this.day);
    }

    public int getYear() {
        return Integer.parseInt(this.year);
    }

    public int getMonth() {
        return Integer.parseInt(this.month) - 1;
    }
}