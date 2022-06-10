package com.djordje.vacationtracker.repositories;

import com.djordje.vacationtracker.models.Vacation;
import org.springframework.data.repository.CrudRepository;

public interface VacationRepository extends CrudRepository<Vacation, Long> {
}
