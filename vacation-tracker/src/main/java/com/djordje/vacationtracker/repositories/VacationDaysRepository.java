package com.djordje.vacationtracker.repositories;

import com.djordje.vacationtracker.models.VacationDays;
import com.djordje.vacationtracker.models.VacationDaysId;
import org.springframework.data.repository.CrudRepository;

public interface VacationDaysRepository extends CrudRepository<VacationDays, VacationDaysId> {
}
