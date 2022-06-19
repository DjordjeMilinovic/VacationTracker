package com.djordje.vacationtracker.util;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CsvVacationReader {
    private List<String> list;

    @PostConstruct
    void init() {
        list = new ArrayList<>();
    }

    //returns true if the given file is a .csv file
    public boolean csvExtensionCheck(MultipartFile file) {
        if (file == null) {
            return false;
        }
        String name = file.getOriginalFilename();
        if (name == null) {
            return false;
        }
        Pattern p = Pattern.compile("(.+)\\.csv");
        Matcher m = p.matcher(name);
        return m.matches();
    }

    public CSVReader createReader(MultipartFile file, int skipLines) throws IOException {
        Reader reader = new InputStreamReader(file.getInputStream());
        CSVParser csvParser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(false)
                .build();
        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withCSVParser(csvParser)
                .withSkipLines(skipLines)
                .build();
        return csvReader;
    }

    public List<String[]> readVacationDates(MultipartFile file) throws Exception {
        CSVReader csvReader = createReader(file, 1);
        List<String[]> list = csvReader.readAll();
        return list;
    }

    public List<String[]> readEmployeeProfiles(MultipartFile file) throws Exception {
        CSVReader csvReader = createReader(file, 2);
        List<String[]> list = csvReader.readAll();
        return list;
    }

    public List<String[]> readVacationDays(MultipartFile file) throws Exception {
        CSVReader csvReader = createReader(file, 0);
        List<String[]> list = csvReader.readAll();
        return list;
    }

}
