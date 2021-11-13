package com.hfad.healthstory.firebase_classes;

public class Date_str {
    String date;

    public Date_str() {}

    public Date_str(String date)
    {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDay()
    {
        return Integer.parseInt(date.substring(0, 2));
    }

    public int getMonth()
    {
        return Integer.parseInt(date.substring(3, 5));
    }

    public int getYear()
    {
        return Integer.parseInt(date.substring(7, 11));
    }

    public String toString()
    {
        return date;
    }

}
