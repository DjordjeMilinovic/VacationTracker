package com.djordje.vacationtracker.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name = "vacationdays")
@IdClass(VacationId.class)
public class VacationDays {
    @Id
    private String email;
    @Id
    private int year;
    private int days;

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
