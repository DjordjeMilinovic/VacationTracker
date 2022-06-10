package com.djordje.vacationtracker;

import com.djordje.vacationtracker.util.CsvVacationReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VacationTrackerApplication {
	public static void main(String[] args) {
		SpringApplication.run(VacationTrackerApplication.class, args);
	}

}
