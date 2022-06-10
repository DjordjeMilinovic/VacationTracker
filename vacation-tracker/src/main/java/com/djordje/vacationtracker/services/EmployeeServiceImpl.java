package com.djordje.vacationtracker.services;

import com.djordje.vacationtracker.models.Employee;
import com.djordje.vacationtracker.repositories.EmployeeRepository;
import com.djordje.vacationtracker.util.CsvVacationReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    CsvVacationReader csvVacationReader;

    @Override
    public boolean employeeExists(String email) {
        if(employeeRepository.existsById(email))
            return true;
        return false;
    }

    @Override
    public Employee insertEmployee(Employee e) {
        return employeeRepository.save(e);
    }

    /*
    * Method inserts employees from the given file into the database.
    * If there's and employee with the same email already in the database insertion is skipped.
    * Method returns a String with information about how many employees were added.*/
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
            if(!employeeRepository.existsById(employeesData.get(i))){
                employeeRepository.save(new Employee(email, password));
                totalAdded++;
            }
        }
        if(totalAdded>0)
            return "Successfully added "+totalAdded+" employees!";
        else
            return "No new employees were added.";
    }
}
