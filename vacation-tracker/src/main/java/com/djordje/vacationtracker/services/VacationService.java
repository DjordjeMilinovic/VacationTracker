package com.djordje.vacationtracker.services;

import com.djordje.vacationtracker.models.Vacation;
import org.springframework.web.multipart.MultipartFile;

public interface VacationService {
    public String insertVacation(MultipartFile file);

    public Vacation addNewVacation(Vacation v);
}
