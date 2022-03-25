package com.hfad.healthstory.firebase_classes;

public class UserAccount {
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;
    public static final int GENDER_OTHER = 3;

    String firstName;
    String secondName;
    String patronymic;
    int gender;
    Date birthday;
   // Date_str birthday;

    public UserAccount() {}

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /*public void setBirthday(Date_str birthday) {
        this.birthday = birthday;
    }*/

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getSecondName() {
        return secondName;
    }
    public String getPatronymic() {
        return patronymic;
    }
    public Date getBirthday() {
        return birthday;
    }

    /*public Date_str getBirthday() {
        return birthday;
    }*/

    public int getGender() {
        return gender;
    }
}
