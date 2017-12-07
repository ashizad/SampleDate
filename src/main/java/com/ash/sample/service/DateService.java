package com.ash.sample.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ash.sample.model.DateUnit;

/**
 * <p>This service helps to manage DateUnit activities</p>
 * @author AshI01
 *
 */

public interface DateService {

	
	/**
	 * @author AshI01
	 * To pass string input to this method and build DateUnit object 
	 * @param text entry input which has both dates as String  text
	 * @return DateUnit object after finishing build the object from String
	 */
	Optional<List<DateUnit>> buildDate(String text) ;
	
	/**
	 * @author AshI01
	 * Compare two DateUnit objects and sort them based on which one is the earliest one. 
	 * @param date
	 * @param other
	 * @return sorted list 
	 */
	List<DateUnit> sortEarliestDate(DateUnit date, DateUnit other);
	
	/**
	 * @author AshI01
	 * process dates input from file
	 * @param filePath path of your input file
	 * @return Output of the application is string of:(DD MM YYYY, DD MM YYYY, difference )
		• Where the first date is the earliest, the second date is the latest and the difference based on days.
	 */
	Optional<String> proccessFile(String filePath);
	
	
	/**
	 * @author AshI01
	 * Process plain input data which can read in pairs of dates in the following format
	 * @param text  -input format MM YYYY, DD MM YYYY
	 * @return Output of the application is string of:(DD MM YYYY, DD MM YYYY, difference ) 
	 */
	Optional<String> processInput(String text);
	
	
	/**
	 * @author AshI01
	 * To validate day, month and year to make sure is matched with calendar.
	 * @param dateList two dateUnit objects as list<>
	 * @return return true if all day, month and year are correct based on Calendar otherwise throws an exception with message.
	 */
	List<DateUnit> validateDate(List<DateUnit> dateList);
	
	/**
	 * Calculate the difference between two dates
	 * @param date1 DateUnit object
	 * @param date2 DateUnit object
	 * @return the difference based on days.
	 */
	public long  daysBetweenDates(DateUnit date1, DateUnit date2);
}
