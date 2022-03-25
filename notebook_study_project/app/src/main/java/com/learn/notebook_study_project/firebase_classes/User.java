package com.learn.notebook_study_project.firebase_classes;

public class User {

    String login;
    String idUserAccount;

    public User() {}
    public User(String login, String idUserAccount) {
        this.login = login;
        this.idUserAccount = idUserAccount;
    }
    public User(String login) {
        this.login = login;
    }

    public String getIdUserAccount() {
        return idUserAccount;
    }

    public void setIdUserAccount(String idUserAccount) {
        this.idUserAccount = idUserAccount;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}