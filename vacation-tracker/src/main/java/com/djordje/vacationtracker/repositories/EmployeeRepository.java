package com.djordje.vacationtracker.repositories;

import com.djordje.vacationtracker.models.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, String> {
}
