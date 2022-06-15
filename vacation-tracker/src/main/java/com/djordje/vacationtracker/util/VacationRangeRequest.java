package com.djordje.vacationtracker.util;

import java.sql.Date;

public class VacationRangeRequest {
    private String email;
    private Date from;
    private Date to;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }
}
