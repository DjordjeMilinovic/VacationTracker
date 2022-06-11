package com.djordje.vacationtracker.controllers;

import com.djordje.vacationtracker.services.EmployeeService;
import com.djordje.vacationtracker.services.VacationDaysService;
import com.djordje.vacationtracker.services.VacationService;
import com.djordje.vacationtracker.util.EmployeeResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private VacationDaysService vacationDaysService;
    @Autowired
    private VacationService vacationService;

    /*
    * returns information about vacation days for an employee for a given year*/
    @GetMapping(value = "/vacationDaysDetails/{email}/{year}")
    public EmployeeResponseMessage getVacationDaysDetails(@PathVariable String email, @PathVariable int year) {
        int totalDays = employeeService.getTotalVacationDays(email, year);
        int usedDays = employeeService.getUsedVacationDays(email, year);
        int unusedDays = employeeService.getUnusedVacationDays(email, year);

        EmployeeResponseMessage response = new EmployeeResponseMessage();
        response.setEmail(email);
        if(totalDays>0){
                response.setTotalDays(totalDays);
                response.setUsedDays(usedDays);
                response.setUnusedDays(unusedDays);
        }
        return response;
    }

}
