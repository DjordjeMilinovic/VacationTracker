package com.djordje.vacationtracker.util;

import com.djordje.vacationtracker.models.Vacation;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CsvVacationReader {
    private List<String> list;

    @PostConstruct
    void init(){
        list = new ArrayList<>();
    }

    //returns true if the given file is a .csv file
    public boolean csvExtensionCheck(MultipartFile file){
        if(file!=null){
            String name = file.getOriginalFilename();
            Pattern p = Pattern.compile("(.+)\\.csv");
            Matcher m = p.matcher(name);
            if(m.matches()) return true;
        }
        return false;
    }

    //uses the given pattern to match every line of the file and returns a list of all matches
    //skip - how many lines from the top should be ignored
    private List<String> parseFile(MultipartFile file, Pattern p, int skip){
        if(!csvExtensionCheck(file)) return null; //given file was not a .csv file
        list.clear();
        try {
            String fileData = new String(file.getBytes());
            //array where each element is a line in the original file
            String fileLines[] = fileData.split("\r\n");

            for(String s : fileLines){
                if(skip>0){
                    skip--;
                    continue;
                }
                Matcher m = p.matcher(s);
                if(m.matches()) {
                    for(int i =1; i<=m.groupCount();i++){
                        list.add(m.group(i));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(list);
    }

    //returns a List of Strings: employeeEmail1, employeePassword1, employeeEmail2, employeePassword2...
    public List<String> readProfiles(MultipartFile file){
        Pattern pattern = Pattern.compile("(.+),(.+)");
        List<String> profilesList = parseFile(file, pattern, 2);
        return profilesList;
    }


    //returns a List of Strings: employee1, startDate1, endDate1, employee2, startDate2, endDate2...
    public List<String> readVacationDates(MultipartFile file){
        Pattern pattern = Pattern.compile("(.+),\"(.+)\",\"(.+)\"");
        List<String> vacationDatesList = parseFile(file, pattern, 1);
        return vacationDatesList;
    }

    //returns a List of Strings: employee1, vacationDays1, employee2, vacationDays2...
    public List<String> readVacationDays(MultipartFile file){
        Pattern pattern = Pattern.compile("(.+),(.+)");
        String year = "";
        //read the year from the file
        try {
            String data = new String(file.getBytes()).split("\r\n")[0];
            Pattern p = Pattern.compile("(.+),(.+)");
            Matcher m = p.matcher(data);
            if(m.matches()){
                year = m.group(2);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> vacationYearList = parseFile(file, pattern,2);
        vacationYearList.add(0, year);

        return vacationYearList;
    }


    //returns a Date created using the given String; format of the
    public Date convertStringToDate(String date){
        Date converted = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM d, y", new Locale("en"));
        if(dateFormat == null)
            return null;
        converted =  dateFormat.parse(date, new ParsePosition(0));
        return  converted;
    }


}
