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
public class VacationDaysServiceImpl implements VacationDaysService {
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
        int year;
        List<String[]> list = null;

        if (!csvVacationReader.csvExtensionCheck(file)) {
            return "File must be .csv!";
        }

        try {
            list = csvVacationReader.readVacationDays(file);
        } catch (Exception e) {
            return null;
        }

        if (list == null) {
            return null;
        }

        year = Integer.parseInt(list.get(0)[1]);
        for (int i = 0; i < 2; i++) {
            list.remove(0);
        }

        String email, days;
        for (String[] line : list) {
            email = line[0];
            days = line[1];
            if (!employeeRepository.existsById(email)) {
                continue;
            }
            if (vacationDaysRepository.existsById(new VacationDaysId(year, email))) {
                continue;
            }
            vacationDaysRepository.save(new VacationDays(year, email, Integer.parseInt(days)));
            totalAdded++;
        }

        return "Number of insertions: " + totalAdded + ".";
    }
}
