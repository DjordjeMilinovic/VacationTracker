package com.djordje.vacationtracker.services;

import com.djordje.vacationtracker.models.Employee;
import com.djordje.vacationtracker.models.Vacation;
import com.djordje.vacationtracker.repositories.EmployeeRepository;
import com.djordje.vacationtracker.repositories.VacationRepository;
import com.djordje.vacationtracker.util.CsvVacationReader;
import com.djordje.vacationtracker.util.DateUtils;
import com.djordje.vacationtracker.util.VacationRangeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class VacationServiceImpl implements VacationService{
    @Autowired
    CsvVacationReader csvVacationReader;
    @Autowired
    DateUtils dateUtils;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    VacationRepository vacationRepository;


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
            Date startDate = dateUtils.convertStringToDate(vacationData.get(i+1));
            Date endDate = dateUtils.convertStringToDate(vacationData.get(i+2));
            java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
            vacationRepository.save(new Vacation(vacationData.get(i), sqlStartDate, sqlEndDate));
            totalAdded++;
        }
        if(totalAdded>0)
            return "Successful insertions: " + totalAdded + ".";
        else
            return "No successful insertions";
    }

    /*
    * Inserts a new vacation for an employee into the database and returns the new vacation;*/
    @Override
    public Vacation addNewVacation(Vacation v) {
        if(employeeRepository.existsById(v.getEmail())){

            //create Calendar objects for start and end dates of the new vacation
            Calendar calendarStart = dateUtils.dateToCalendar(v.getStartDate());
            Calendar calendarEnd = dateUtils.dateToCalendar(v.getEndDate());

            Calendar calendarStartCurrent, calendarEndCurrent;
            Employee employee = employeeRepository.findById(v.getEmail()).get();
            for(Vacation vacation : employee.getVacations()){
                //Date comparison (StartA <= EndB) and (EndA >= StartB)

                //create Calendar objects for current vacation start and end dates
                calendarStartCurrent = dateUtils.dateToCalendar(vacation.getStartDate());
                calendarEndCurrent = dateUtils.dateToCalendar(vacation.getEndDate());
                //compare dates
                if(calendarStart.compareTo(calendarEndCurrent)<=0 &&
                calendarEnd.compareTo(calendarStartCurrent)>=0){
                    return null;
                }
            }
            //add new vacation
            Vacation addVacation = new Vacation(v.getEmail(), v.getStartDate(), v.getEndDate());
            return vacationRepository.save(addVacation);

        }
        return null;
    }

    /*
    * returns a list of all vacations that are included in the given time period (from - to dates)*/
    @Override
    public List<Vacation> getAllVacationFromTo(VacationRangeRequest vacationRangeRequest) {
        List<Vacation> vacationsList = new ArrayList<>();
        String email = vacationRangeRequest.getEmail();
        java.sql.Date from = vacationRangeRequest.getFrom();
        java.sql.Date to = vacationRangeRequest.getTo();

        //creating Calendar for from and to dates
        String[] fromDate = from.toString().split("-");
        String[] toDate = to.toString().split("-");
        Calendar fromCalendar = dateUtils.dateToCalendar(from);
        Calendar toCalendar = dateUtils.dateToCalendar(to);

        if(employeeRepository.existsById(email)){
            Employee employee = employeeRepository.findById(email).get();
            List<Vacation> vacations = employee.getVacations();

            for(Vacation v : vacations){
                //creating a Calendar for start and end date for Vacation v
                Calendar starCalendar = dateUtils.dateToCalendar(v.getStartDate());
                Calendar endCalendar = dateUtils.dateToCalendar(v.getEndDate());

                //check if Vacation v falls in the specified time period
                if(starCalendar.compareTo(toCalendar)<=0 &&
                        endCalendar.compareTo(fromCalendar)>=0){
                    vacationsList.add(v);
                }

            }


        }
        return vacationsList;
    }
}
