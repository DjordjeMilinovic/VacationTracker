package com.djordje.vacationtracker.services;

import com.djordje.vacationtracker.models.Vacation;
import com.djordje.vacationtracker.repositories.EmployeeRepository;
import com.djordje.vacationtracker.repositories.VacationDaysRepository;
import com.djordje.vacationtracker.repositories.VacationRepository;
import com.djordje.vacationtracker.util.CsvVacationReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
public class VacationServiceImpl implements VacationService{
    @Autowired
    CsvVacationReader csvVacationReader;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    VacationRepository vacation;

    /*
     * Method inserts information about employees vacations into the database.
     * If an employee with the given email doesn't exist in the database insertion is skipped.
     * Method returns a String with information about the number of successful insertions.*/
    @Override
    public String insertVacation(MultipartFile file) {
        int totalAdded = 0;
        if(!csvVacationReader.csvExtensionCheck(file))
            return "File must be .csv!";
        List<String> vacationData = csvVacationReader.readVacationDates(file);
        for(int i =0; i< vacationData.size(); i+=3){
            String email = vacationData.get(i);
            if(!employeeRepository.existsById(email)){
                continue;
            }
            Date startDate = csvVacationReader.convertStringToDate(vacationData.get(i+1));
            Date endDate = csvVacationReader.convertStringToDate(vacationData.get(i+2));
            java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
            vacation.save(new Vacation(vacationData.get(i), sqlStartDate, sqlEndDate));
            totalAdded++;
        }
        if(totalAdded>0)
            return "Successful insertions: "+totalAdded+".";
        else
            return "No successful insertions";
    }
}
