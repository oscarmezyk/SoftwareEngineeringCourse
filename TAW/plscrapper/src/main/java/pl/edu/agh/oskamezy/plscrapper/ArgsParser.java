package pl.edu.agh.oskamezy.plscrapper;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.Locale;


public class ArgsParser {

    public String day;
    public LocalTime startTime;
    public LocalTime endTime;
    public LocalDate startDate;
    public LocalDate endDate;
    
    public void parse(String[] args) {
        // accepts args: Monday 10:00 12:00 20240506 20240506
        if (args.length != 5) {
            System.err.println("Usage: java ProgramParser <day> <start_time> <end_time> <start_date> <end_date> ");
            System.err.println("Note: set <day> to Day of Week to get only selected days. Set to NONE to get all songs within given time range");
            System.exit(1);
        }

        this.day = args[0];

        try {
            // Parse arguments
            this.startTime = parseTime(args[1]);
            this.endTime = parseTime(args[2]);
            this.startDate = parseDate(args[3]);
            this.endDate = parseDate(args[4]);

        } catch(DateTimeParseException e) {
			System.out.println("Invalid parsing: " + e.toString());
			throw e;
        }

        // Example: Print parsed values
        System.out.println("Day: " + this.day);
        //System.out.println("Day: " + this.startDate.getDayOfWeek());
        System.out.println("Start Time: " + this.startTime);
        System.out.println("End Time: " + this.endTime);
        System.out.println("Start Date: " + this.startDate);
        System.out.println("End Date: " + this.endDate);
        
        checkTimes();
        checkDates();
    }
    
    public List<String> getDates(String day, LocalDate beg, LocalDate end) {
    	
    	LinkedList<String> output = new LinkedList<>();
    	end = end.plusDays(1);

    	List<LocalDate> dates = beg.datesUntil(end).collect(Collectors.toList());
        Iterator<LocalDate> iterator = dates.iterator();

        if (!day.equals("NULL") && !day.equals(null)) {
            while (iterator.hasNext()) {
            	LocalDate locDate = iterator.next();
            	if( locDate.getDayOfWeek() == DayOfWeek.valueOf(day.toUpperCase(Locale.ROOT)) ) {
            		output.add(locDate.toString());
            		System.out.println(locDate);
            	}
            }
        } else {
            while (iterator.hasNext()) {
            	LocalDate locDate = iterator.next();
        		output.add(locDate.toString());
        		System.out.println(locDate);
            }
        }
        return output;
    }
    
    
    private void checkTimes() {
        // Get current date and time
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();
    	
    	if(this.startTime.compareTo(endTime) > 0) {
            System.err.println("Start time " + this.startTime + " and end time " + this.endTime + " are wrong!");
            System.exit(1); 
    	}
    	if(this.startTime.compareTo(now) > 0 && this.startDate.compareTo(today) > 0) {
            System.err.println("Start time " + this.startTime + " is in furture!");
            System.exit(1); 
    	}

    }
    
    private void checkDates() {
        // Get current date and time
        LocalDate today = LocalDate.now();
   	
    	if(this.startDate.compareTo(endDate) > 0) {
            System.err.println("Start date " + this.startDate + " and end date " + this.endDate + " are wrong!");
            System.exit(1); 
    	}
    	if(this.startDate.compareTo(today) > 0) {
            System.err.println("Start date " + this.startDate + " is in future!");
            System.exit(1);
    	}
    }

    
    private static LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE);
    }

    private static LocalTime parseTime(String timeStr) {
        return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
    }
}