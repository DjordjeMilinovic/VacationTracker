package com.djordje.vacationtracker.services;

import com.djordje.vacationtracker.models.Employee;
import com.djordje.vacationtracker.models.Vacation;
import com.djordje.vacationtracker.models.VacationDays;
import com.djordje.vacationtracker.models.VacationDaysId;
import com.djordje.vacationtracker.repositories.EmployeeRepository;
import com.djordje.vacationtracker.repositories.VacationDaysRepository;
import com.djordje.vacationtracker.repositories.VacationRepository;
import com.djordje.vacationtracker.util.CsvVacationReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private VacationDaysRepository vacationDaysRepository;
    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private CsvVacationReader csvVacationReader;

    /*
    * Method returns true if an employee with the given email is already in the database.*/
    @Override
    public boolean employeeExists(String email) {
        if(employeeRepository.existsById(email))
            return true;
        return false;
    }

    /*
    * Method inserts employees from the given file into the database.
    * If there's and employee with the same email already in the database insertion is skipped.
    * Method returns a String with information about how many employees were added.*/
    @Override
    public String insertEmployees(MultipartFile file){
        int totalAdded = 0; //counts how many employees will be added
        //get the data from the file
        if(!csvVacationReader.csvExtensionCheck(file))
            return "File must be .csv!";
        List<String> employeesData = csvVacationReader.readProfiles(file);
        String email, password;
        for(int i =0; i<employeesData.size(); i+=2){
            email =employeesData.get(i);
            password = employeesData.get(i+1);
            if(!employeeExists(email)){
                employeeRepository.save(new Employee(email, password));
                totalAdded++;
            }
        }
        if(totalAdded>0)
            return "Successfully added "+totalAdded+" employees!";
        else
            return "No new employees were added.";
    }

    /*
    * returns a total number of vacation days for the given year;
    * returns -1 if there's no information about that year;
    * returns -2 if the employee doesn't exit*/
    @Override
    public int getTotalVacationDays(String email, int year){
        int totalDays = 0;
        if(employeeExists(email)){
            Optional<VacationDays> vacationDays = vacationDaysRepository.findById(new VacationDaysId(year, email));
            if(vacationDays.isPresent()){
                totalDays = vacationDays.get().getDays();
                return totalDays;
            }
            return -1;
        }
        return -2;
    }

    /*
    * returns the number of used vacation days for a given year;
    * returns -2 if the employee doesn't exist*/
    @Override
    public int getUsedVacationDays(String email, int year) {
        long usedDays = 0;
        if(employeeExists(email)){
            Employee employee = employeeRepository.findById(email).get();
            List<Vacation> vacations = employee.getVacations();
            for(Vacation v: vacations){

                String startDateInfo[] = v.getStartDate().toString().split("-");
                String endDateInfo[] = v.getEndDate().toString().split("-");
                Calendar calendarStart = Calendar.getInstance();
                calendarStart.set(Integer.parseInt(startDateInfo[0]),
                        Integer.parseInt(startDateInfo[1]),
                        Integer.parseInt(startDateInfo[2]));
                Calendar calendarEnd = Calendar.getInstance();
                calendarEnd.set(Integer.parseInt(endDateInfo[0]),
                        Integer.parseInt(endDateInfo[1]),
                        Integer.parseInt(endDateInfo[2]));
                int startYear = calendarStart.get(Calendar.YEAR);
                int endYear = calendarEnd.get(Calendar.YEAR);

                if(startYear == endYear){
                    if(startYear == year){
                        // calculate difference between start and end
                        usedDays += 1 + Duration.between(calendarStart.toInstant(), calendarEnd.toInstant()).toDays();
                    }
                }
                else{
                    if(startYear == year){
                        //started in December of the given year, calculate between start and Dec 31st
                        Calendar lastDay = Calendar.getInstance();
                        lastDay.set(year,11,31);
                        usedDays += 1 + Duration.between(calendarStart.toInstant(), lastDay.toInstant()).toDays();
                    }
                    else if (endYear == year){
                        //ended in January of the given year, calculate between Jan 1st and end
                        Calendar firstDay = Calendar.getInstance();
                        firstDay.set(year, 0,1);
                        usedDays += 1 + Duration.between(firstDay.toInstant(), calendarStart.toInstant()).get(ChronoUnit.DAYS);
                    }
                }
            }
            return (int) usedDays;
        }
        return -2;
    }

    /*
    * returns number of unused vacation days;
    * returns -1 if there's no information about that year;
    * returns -2 if the employee doesn't exist*/
    @Override
    public int getUnusedVacationDays(String email, int year) {
        int unusedDays = 0, totalDays = 0, usedDays = 0;
        totalDays = getTotalVacationDays(email, year);
        if(totalDays == -2)
            return -2;
        if(totalDays == -1)
            return -1;
        usedDays = getUsedVacationDays(email, year);
        unusedDays = totalDays - usedDays;
        return unusedDays;
    }

}

