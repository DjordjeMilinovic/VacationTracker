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
    @Override
    public int insertVacationDays(MultipartFile file) {
        int totalAdded = 0;
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
        return totalAdded;
    }
}
