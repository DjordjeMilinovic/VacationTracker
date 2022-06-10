package com.djordje.vacationtracker.models;

import java.io.Serializable;

public class VacationDaysId implements Serializable {
    private int year;

   private String email;

    public VacationDaysId(int year, String email) {
        this.year = year;
        this.email = email;
    }

    public VacationDaysId(){}

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
}
