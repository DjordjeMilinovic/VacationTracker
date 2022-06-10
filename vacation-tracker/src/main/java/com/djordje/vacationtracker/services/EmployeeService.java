package com.djordje.vacationtracker.services;

import com.djordje.vacationtracker.models.Employee;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {
    public boolean employeeExists(String email);
    public Employee insertEmployee(Employee e);
    public String insertEmployees(MultipartFile file);

}
