package com.djordje.vacationtracker.services;

import org.springframework.web.multipart.MultipartFile;

public interface VacationDaysService {
    public int insertVacationDays(MultipartFile file);
}
