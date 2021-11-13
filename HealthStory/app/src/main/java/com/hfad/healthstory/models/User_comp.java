package com.hfad.healthstory.models;

public class User_comp {
    String name;
    Age age;

    public User_comp()
    {

    }

    public Age getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public void setAge(Age age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }
}
