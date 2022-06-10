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

    public int insertEmployees(MultipartFile file){
        int totalAdded = 0; //counts how many employees will be added
        //get the data from the file
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
        return totalAdded;
    }
}
