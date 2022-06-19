package com.djordje.vacationtracker.models;

import javax.persistence.*;

@Entity(name = "vacationdays")
@IdClass(VacationDaysId.class)
public class VacationDays {
    @Id
    private int year;
    @Id
    private String email;
    private int days;

    public VacationDays(int year, String email, int days) {
        this.year = year;
        this.email = email;
        this.days = days;
    }

    public VacationDays() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
