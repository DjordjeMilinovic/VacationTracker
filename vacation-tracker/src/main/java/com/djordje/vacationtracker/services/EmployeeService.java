package com.djordje.vacationtracker.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeService {
    public boolean employeeExists(String email);
    public String insertEmployees(MultipartFile file);
    public int getTotalVacationDays(String email, int year);
    public int getUsedVacationDays(String email, int year);
    public int getUnusedVacationDays(String email, int year);

}
