package com.ash.sample.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ash.sample.model.DateUnit;

/**
 * @author Ash Izadi
 * <p> service layer for DateUnit actions </p>
 */
@Service
public class DateServiceImpl implements DateService {

	private static final Logger log = LoggerFactory.getLogger(DateServiceImpl.class) ;

	@Autowired
	FileReaderService fileReaderService;
	//This Predicate method is to validate user's entry
	public static Predicate<DateUnit> isDateValid() {
		return c ->
		{
			// before we pass into variable, we apply normal validation
			if (c.getYear() < 1900 || c.getYear() > 2010) {
				throw new IllegalArgumentException(" 'year' must be between 1900 and 2010!");
			}
			// make sure number of days for each month is correct
			
			if (c.getMonth() < 1 || c.getMonth() > 12) {
				throw new IllegalArgumentException(" month should be between 1 and 12 !");
			}
			if (c.getDay() < 1 || c.getDay() > 31) {
				throw new IllegalArgumentException(" 'day' is not valid!");
			}
			// February has 29 days and we have to check it
			if (c.getDay() > 29 && c.getMonth() == 2) {
				throw new IllegalArgumentException(" 'February' has 29 days, you have entered invalid 'day' input!");
			}
			//This one must be after checking month
			if (c.getDay() < 1 || c.getDay() > DateUnit.NUMBEROFDAYS.get(c.getMonth() - 1) && c.getMonth() != 2) {
				throw new IllegalArgumentException(" Please correct the day of your month!");
			}
			//checking whether the date is a leap-year 
			if (c.getDay() == 28 && c.getMonth() == 2 && ((c.getYear() - 2000) % 4 == 0)) {
				throw new IllegalArgumentException(" The year you have entered is a Leap-year, the day on 'February' should be 29 days, please correct it and try it again!");
			}
			return true;
		};
	}
	//map user's input to our date format, this functions takes care of each individual entry
	public Function<String, DateUnit> mapToDate = c -> {
		log.debug("Building Date object from input .....");
		DateUnit dateObject = new DateUnit();
		
		List<String> list = Arrays.asList(c.trim().split(" "));
		if(list.size()!=3)
		{
			log.warn("The format is not correct, please enter on this format: DD MM YYYY!");
			throw new RuntimeException("ERROR:please enter your date on this format: DD MM YYYY!");
		}
		//now we have to check the size of each element, it must be two char
		IntStream.range(0, list.size())
		.forEach( index ->
		{
			/**
			 * Based on requirement, the input format is 'DD MM YYYY'
			 * as a result, first index must be day, second one is month and last index is year 
			 */
			try {
				switch(index) {
				case 0:
					if(list.get(index).length() != 2)
						throw new RuntimeException("ERROR: Day must be in this format: DD !");
					dateObject.setDay(Integer.parseInt(list.get(index)));
					break;
				case 1:
					if(list.get(index).length() != 2)
						throw new RuntimeException("ERROR: Month must be in this format MM !");
					dateObject.setMonth(Integer.parseInt(list.get(index)));
					break;
				case 2:
					if(list.get(index).length() != 4)
						throw new RuntimeException("ERROR: Year must be in this format YYYY !");
					dateObject.setYear(Integer.parseInt(list.get(index)));
					break;
			}
				
			}catch(NumberFormatException ex) {
				throw new RuntimeException("ERROR: Please type in digits only!");
			}
		});
		
		return dateObject;
	};
	
	/* This compares one date to another to find out if it's earlier than or
	 * not, if so, returning true.
	 **/
	  public List<DateUnit> sortEarliestDate(DateUnit date, DateUnit other)
	  {
		  List<DateUnit> list = new ArrayList<>();
	    if( ( date.getYear()<other.getYear() ) || ( date.getYear() ==other.getYear()  && date.getMonth() <other.getMonth() ) 
	    		||( date.getYear() <=other.getYear() && date.getMonth() == other.getMonth() && date.getDay() < other.getDay() ) )
	    {
	    	list.add(0, date);
	    	list.add(1, other);
	    	log.debug("The first date-input is earlier than the second input...." );
	    	
	    }
	    else
	    {
	    	list.add(0, other);
	    	list.add(1, date);
	    	log.debug("The second date-input is earlier than the first input....");
	    }
	    return list;
	  }
	  
	  public Optional<List<DateUnit>> buildDate(String text) {
		
		List<DateUnit> listOfDate = Arrays.asList(text.split(","))
							.stream()
							.map(mapToDate).collect(Collectors.toList());
			if(listOfDate.size()!=2)
				throw new IllegalArgumentException("must provide two dates with ','");
			return Optional.of(listOfDate);
		}

	  //to process the output
	public String processResult(DateUnit date1, DateUnit date2) {
		long difference = daysBetweenDates(date1,date2);
		String firstItem = (date1.getDay().SIZE <2? "0"+date1.getDay():date1.getDay() ) + " " +(date1.getMonth().SIZE <2? "0"+date1.getMonth():date1.getMonth() ) + " " + date1.getYear();
		String secondItem = (date2.getDay().SIZE <2? "0"+date2.getDay():date2.getDay() ) + " " +(date2.getMonth().SIZE <2? "0"+date2.getMonth():date2.getMonth() ) + " " + date2.getYear();
		
		return   firstItem+ " , " + secondItem +" , "+ difference +" days";
	}
	//Validation, make it reference method to be able test this Predicate
	public List<DateUnit> validateDate(List<DateUnit> dateList) {
		return  dateList.stream().filter(isDateValid()).collect(Collectors.toList());
	}
	
	public Optional<String> processInput(String text) {
		// step1: create date objects
		Optional<List<DateUnit>> dateList = buildDate(text);

		// step 2: validate dates
		List<DateUnit> valiatedList = validateDate(dateList.get());

		// step 3: sort objects
		if (valiatedList.size() != 2)
			throw new RuntimeException("Two dats are not available .... ");
		List<DateUnit> sortDateList = sortEarliestDate(valiatedList.get(0), valiatedList.get(1));

		// step 4: difference days apply and display output
		String result = processResult(sortDateList.get(0), sortDateList.get(1));
		
		return Optional.of(result);
	}
	

	public Optional<String> proccessFile(String filePath) {
		Optional<String> entryText = Optional.empty(); 
		//read from file
		try {
			entryText = fileReaderService.readTextFile(filePath);
		} catch (IOException e) {
			log.warn("issue on readinf file ...." + e );
			e.printStackTrace();
			throw new RuntimeException("Please specify full path with URL Encoding for instance for \\ use %5C encode");
		}catch(Exception ex) {
			throw new RuntimeException("Please specify full path with URL Encoding for instance for \\ use %5C encode");
		}
		//if all good we process the input
		Optional<String> inputResult = processInput(entryText.get());
		return inputResult;
	}
	
	//to calculate the difference
	public long  daysBetweenDates(DateUnit date1, DateUnit date2) {
		long firstTime = date1.getTimeOfOurDateUnit(date1);
		long secondTime = date2.getTimeOfOurDateUnit(date2);
		long diffTime = secondTime - firstTime;
		long diffDays = diffTime / (1000 * 60 * 60 * 24);
		//we always show them positive
		diffDays = Math.abs(diffDays);
		return diffDays;
		
	}
	
	
}
