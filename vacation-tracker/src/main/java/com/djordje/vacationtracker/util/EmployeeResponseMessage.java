package com.djordje.vacationtracker.util;

public class EmployeeResponseMessage {

    private String email;
    private int totalDays;
    private int usedDays;
    private int unusedDays;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public int getUsedDays() {
        return usedDays;
    }

    public void setUsedDays(int usedDays) {
        this.usedDays = usedDays;
    }

    public int getUnusedDays() {
        return unusedDays;
    }

    public void setUnusedDays(int unusedDays) {
        this.unusedDays = unusedDays;
    }
}
