package com.hfad.healthstory.firebase_classes;

public class User {
    String password;
    String pathToUserData;

    public User() {}
    public User(String password, String pathToUserData) {
        this.password = password;
        this.pathToUserData = pathToUserData;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setPathToUserData(String pathToUserData) {
        this.pathToUserData = pathToUserData;
    }
    public String getPassword() {
        return password;
    }
    public String getPathToUserData() {
        return pathToUserData;
    }
}