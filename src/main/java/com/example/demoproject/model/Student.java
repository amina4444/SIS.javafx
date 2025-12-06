package com.example.demoproject.model;

public class Student {

    private int id;
    private String name;
    private String surname;
    private String email;
    private String group;
    private int gpa;

    public Student(){};

    public Student(int id, String name, String surname){
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public Student(int id, String name, String surname, String email, String group, int gpa) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.group = group;
        this.gpa = gpa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getGpa() {
        return gpa;
    }
    public void setGpa(int gpa) {
        this.gpa = gpa;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + surname +","+email+","+group + "," + gpa;
    }
}
