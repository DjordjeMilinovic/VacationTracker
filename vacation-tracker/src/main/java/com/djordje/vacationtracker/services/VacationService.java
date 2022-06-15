package com.djordje.vacationtracker.services;

import com.djordje.vacationtracker.models.Vacation;
import com.djordje.vacationtracker.util.VacationRangeRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VacationService {
    public String insertVacation(MultipartFile file);
    public Vacation addNewVacation(Vacation v);
    public List<Vacation> getAllVacationFromTo(VacationRangeRequest vacationRangeRequest);
}
