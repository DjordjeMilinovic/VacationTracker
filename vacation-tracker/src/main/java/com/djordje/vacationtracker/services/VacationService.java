package com.djordje.vacationtracker.services;

import org.springframework.web.multipart.MultipartFile;

public interface VacationService {
    public String insertVacation(MultipartFile file);
}
