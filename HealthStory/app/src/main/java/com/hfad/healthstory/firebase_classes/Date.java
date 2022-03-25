package com.hfad.healthstory.firebase_classes;

import com.google.firebase.database.DataSnapshot;

public class Date {

    String day; /// in 01 style!
    String month;
    String year;

    public Date() {}

 /*   public Date(DataSnapshot dateSnapshot)
    {
        day = (dateSnapshot.child("day").getValue(S.class));
        month = dateSnapshot.child("month").getValue(Integer.class);
        year =dateSnapshot.child("year").getValue(Integer.class);
    }*/

    public Date(int day, int month, int year){
        this.day = dayToString(day);
        this.month = monthToString(month);
        this.year = yearToString(year);
    }

    public void setDay(String day) {
        this.day = day;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public void setYear(String year) {
        this.year = year;
    }

    public String getDay() {
        return day;
    }
    public String getMonth() {
        return month;
    }
    public String getYear() {
        return year;
    }

    @Override
    public String toString() {
        return day + "." + month + "." + year;
    }
  /*  @Override
    public String toString() {
        String dayStr = String.valueOf(day);
        if (day < 10)
            dayStr = "0" + dayStr;
        String monthStr = String.valueOf(month);
        if (month < 10)
            monthStr = "0" + monthStr;
        String yearStr = String.valueOf(year);
        while (yearStr.length() < 4)
            yearStr = "0" + yearStr;
        return dayStr + "." + monthStr + "." + yearStr;
    }*/

    public String dayToString(int day)
    {
        String dayStr = String.valueOf(day);
        if (day < 10)
            dayStr = "0" + dayStr;
        return dayStr;
    }

    public String monthToString(int month)
    {
        String monthStr = String.valueOf(month);
        if (month < 10)
            monthStr = "0" + monthStr;
        return monthStr;
    }

    public String yearToString(int year)
    {
        String yearStr = String.valueOf(year);
        while (yearStr.length() < 4)
            yearStr = "0" + yearStr;
        return yearStr;
    }

    public Boolean isLater(Date date)
    {
        if (date.year.compareTo(year) > 0) // date.year < this
            return false;
        else if (date.year.compareTo(year) < 0)
            return true;
        else
        {
            if (date.month.compareTo(month) > 0)
                return false;
            else if (date.month.compareTo(month) < 0)
                return true;
            else
            {
                if (date.day.compareTo(day) > 0)
                    return false;
                else if (date.day.compareTo(day) < 0)
                    return true;
            }
        }
        return true; // equality
    }
}
