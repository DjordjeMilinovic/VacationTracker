package com.djordje.vacationtracker.services;

import com.djordje.vacationtracker.models.Employee;
import com.djordje.vacationtracker.models.Vacation;
import com.djordje.vacationtracker.repositories.EmployeeRepository;
import com.djordje.vacationtracker.repositories.VacationRepository;
import com.djordje.vacationtracker.util.CsvVacationReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class VacationServiceImpl implements VacationService{
    @Autowired
    CsvVacationReader csvVacationReader;
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
            Date startDate = csvVacationReader.convertStringToDate(vacationData.get(i+1));
            Date endDate = csvVacationReader.convertStringToDate(vacationData.get(i+2));
            java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
            vacationRepository.save(new Vacation(vacationData.get(i), sqlStartDate, sqlEndDate));
            totalAdded++;
        }
        if(totalAdded>0)
            return "Successful insertions: "+totalAdded+".";
        else
            return "No successful insertions";
    }

    /*
    * Inserts a new vacation for an employee into the database and returns the new vacation;*/
    @Override
    public Vacation addNewVacation(Vacation v) {
        if(employeeRepository.existsById(v.getEmail())){

            //create Calendar objects for start and end dates of the new vacation
            String[] startDateInfo = v.getStartDate().toString().split("-");
            String[] endDateInfo = v.getEndDate().toString().split("-");
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.set(Integer.parseInt(startDateInfo[0]),
                    Integer.parseInt(startDateInfo[1])-1,
                    Integer.parseInt(startDateInfo[2]));
            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.set(Integer.parseInt(endDateInfo[0]),
                    Integer.parseInt(endDateInfo[1])-1,
                    Integer.parseInt(endDateInfo[2]));


            Calendar calendarStartCurrent = Calendar.getInstance();
            Calendar calendarEndCurrent = Calendar.getInstance();

            Employee employee = employeeRepository.findById(v.getEmail()).get();
            for(Vacation vacation : employee.getVacations()){
                //Date comparison (StartA <= EndB) and (EndA >= StartB)

                //create Calendar objects for current vacation start and end dates
                String[] startCurrentInfo = vacation.getStartDate().toString().split("-");
                String[] endCurrentInfo = vacation.getEndDate().toString().split("-");
                calendarStartCurrent.set(Integer.parseInt(startCurrentInfo[0]),
                        Integer.parseInt(startCurrentInfo[1])-1,
                        Integer.parseInt(startCurrentInfo[2]));
                calendarEndCurrent.set(Integer.parseInt(endCurrentInfo[0]),
                        Integer.parseInt(endCurrentInfo[1])-1,
                        Integer.parseInt(endCurrentInfo[2]));


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

}
