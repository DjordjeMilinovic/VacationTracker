package com.djordje.vacationtracker.services;

import com.djordje.vacationtracker.models.VacationDays;
import com.djordje.vacationtracker.models.VacationDaysId;
import com.djordje.vacationtracker.repositories.EmployeeRepository;
import com.djordje.vacationtracker.repositories.VacationDaysRepository;
import com.djordje.vacationtracker.util.CsvVacationReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
public class VacationDaysServiceImpl implements VacationDaysService{
    @Autowired
    CsvVacationReader csvVacationReader;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    VacationDaysRepository vacationDaysRepository;
    /*
     * Method inserts a number of vacation days for a year, for each employee that's in the file.
     * If an employee with the given email doesn't exist in the database insertion is skipped.
     * Method returns a String with information about the number of successful insertions.*/
    @Override
    public String insertVacationDays(MultipartFile file) {
        int totalAdded = 0;
        if(!csvVacationReader.csvExtensionCheck(file))
            return "File must be .csv!";
        List<String> daysData = csvVacationReader.readVacationDays(file);
        int year = Integer.parseInt(daysData.get(0));
        daysData.remove(0);
        String email, days;
        for(int i = 0; i<daysData.size(); i+=2){
            email =daysData.get(i);
            days = daysData.get(i+1);
            if(employeeRepository.existsById(email) &&
                    !vacationDaysRepository.existsById(new VacationDaysId(year, email))){
                vacationDaysRepository.save(new VacationDays(year, email, Integer.parseInt(days)));
                totalAdded++;
            }
        }
        if(totalAdded>0)
            return "Successful insertions: "+totalAdded+".";
        else
            return "No successful insertions";
    }
}
