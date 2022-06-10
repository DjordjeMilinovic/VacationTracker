package com.djordje.vacationtracker.controllers;

import com.djordje.vacationtracker.models.Employee;
import com.djordje.vacationtracker.models.VacationDays;
import com.djordje.vacationtracker.models.VacationDaysId;
import com.djordje.vacationtracker.repositories.EmployeeRepository;
import com.djordje.vacationtracker.repositories.VacationDaysRepository;
import com.djordje.vacationtracker.services.EmployeeService;
import com.djordje.vacationtracker.services.VacationDaysService;
import com.djordje.vacationtracker.util.CsvVacationReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController()
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private VacationDaysService vacationDaysService;

    //returns the total number of employees added
    @PostMapping(value = "/importEmployees", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public int importEmployees(@RequestParam MultipartFile file){
        int totalAdded = employeeService.insertEmployees(file);
        return totalAdded;
    }

    //returns the total number of employees for which the vacation days were added
    @PostMapping(value = "/importVacationDays", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public int importVacationDays(@RequestParam MultipartFile file){
        int totalAdded = vacationDaysService.insertVacationDays(file);
        return totalAdded;
    }

}
