package com.ash.sample.service;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.ash.sample.model.DateUnit;

/**
 * @author Ash Izadi
 *
 */
@RunWith(SpringRunner.class)
public class DateServiceImplTest {


	/**
	 *To check the Service class, we need to have an instance of Service class created and available as
	 * a @Bean so that we can @Autowire it in our test class. This configuration is achieved 
	 * by using the @TestConfiguration annotation.
	 */
	@TestConfiguration
	static class DateServiceImplTestContextConfiguration{
		
		@Bean
		public DateService dateService() {
			return new DateServiceImpl();
		}
		@Bean
		public FileReaderService fileReaderService() {
			return new FileReaderServiceImpl();
		}
	}
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Autowired
	private DateService dataService;
	
	@Autowired 
	FileReaderService fileReaderService;
	
	
	
	@Test
	public void whenValidTextPass_FirstDateObjectShouldBeCreated() {
		String textWithTwoDates =  "10 10 1980, 20 04 2001";
		Optional<List<DateUnit>>  list= dataService.buildDate(textWithTwoDates);
		assertThat( list.get().get(0).getDay()).isEqualTo(10);
	}
	
	@Test
	public void whenValidTextPass_SecondDateObjectShouldBeCreated() {
		String textWithTwoDates =  "10 10 1980, 20 04 2001";
		Optional<List<DateUnit>>  list= dataService.buildDate(textWithTwoDates);
		assertThat( list.get().get(1).getDay()).isEqualTo(20);
	}
	
	@Test
	public void ValidMonthOfFirstObject() {
		String textWithTwoDates =  "10 10 1980, 20 04 2001";
		Optional<List<DateUnit>>  list= dataService.buildDate(textWithTwoDates);
		assertThat( list.get().get(0).getMonth()).isEqualTo(10);
	}
	
	@Test
	public void ValidMonthOfSecondObject() {
		String textWithTwoDates =  "10 10 1980, 20 04 2001";
		Optional<List<DateUnit>>  list= dataService.buildDate(textWithTwoDates);
		assertThat( list.get().get(1).getMonth()).isEqualTo(04);
	}	

	@Test
	public void ValidYearOfFirstObject() {
		String textWithTwoDates =  "10 10 1980, 20 04 2001";
		Optional<List<DateUnit>>  list= dataService.buildDate(textWithTwoDates);
		assertThat( list.get().get(0).getYear()).isEqualTo(1980);
	}
	
	@Test
	public void ValidYearOfSecondObject() {
		String textWithTwoDates =  "10 10 1980, 20 04 2001";
		Optional<List<DateUnit>>  list= dataService.buildDate(textWithTwoDates);
		assertThat( list.get().get(1).getYear()).isEqualTo(2001);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void showException_If_OneDateExist() {
		String textWithOneDate =  "10 10 1980";
		Optional<List<DateUnit>>  list= dataService.buildDate(textWithOneDate);
	}
	
	@Test
	public void showException_If_DateFormat_IsNotCorrect() {
		thrown.expect(RuntimeException.class);
		thrown.expectMessage("ERROR:please enter your date on this format: DD MM YYYY!");
		String textWithOneDate =  "10 101980";
		Optional<List<DateUnit>>  list= dataService.buildDate(textWithOneDate);	
	}
	
	@Test
	public void showException_If_text_is_blank() {
		thrown.expect(RuntimeException.class);
		thrown.expectMessage("ERROR:please enter your date on this format: DD MM YYYY!");
		String textWithOneDate =  "   ";
		Optional<List<DateUnit>>  list= dataService.buildDate(textWithOneDate);	
	}
	
	@Test
	public void Text_With_three_Comma() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("must provide two dates with ','");
		String textWithOneDate =  " 10 10 1980, 10 10 1980, 10 10 1980 ";
		Optional<List<DateUnit>>  list= dataService.buildDate(textWithOneDate);	
	}
	
	@Test
	public void showException_If_month_Is_Not_Fit() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(" month should be between 1 and 12 !");
		List<DateUnit> dateList = new ArrayList<>();
		DateUnit d1 = new DateUnit();
		d1.setDay(11);
		d1.setMonth(13);
		d1.setYear(2005);
		dateList.add(d1);
		
		List<DateUnit> result= dataService.validateDate(dateList);	
	}
	
	
	@Test
	public void showException_If_Year_Is_Not_Fit() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("'year' must be between 1900 and 2010!");
		List<DateUnit> dateList = new ArrayList<>();
		DateUnit d1 = new DateUnit();
		d1.setDay(11);
		d1.setMonth(11);
		d1.setYear(2025);
		dateList.add(d1);
		
		List<DateUnit> result= dataService.validateDate(dateList);	
	}
	
	
	@Test
	public void showException_If_Day_Is_Greater_31() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("'day' is not valid!");
		List<DateUnit> dateList = new ArrayList<>();
		DateUnit d1 = new DateUnit();
		//DateUnit d2 = new DateUnit();
		d1.setDay(34);
		d1.setMonth(11);
		d1.setYear(2000);
		dateList.add(d1);
		//d2.setDay(25);
		//d2.setMonth(1);
		//d2.setYear(1999);
		
		//dateList.add(d2);
		List<DateUnit> result= dataService.validateDate(dateList);	
	}
	
	
	@Test
	public void showException_If_DayofApril_Is_Not_Right() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Please correct the day of your month!");
		List<DateUnit> dateList = new ArrayList<>();
		DateUnit d1 = new DateUnit();
		d1.setDay(31);
		d1.setMonth(4);
		d1.setYear(2000);
		dateList.add(d1);
		List<DateUnit> result= dataService.validateDate(dateList);	
	}

	@Test
	public void Validate_LeapYear() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("The year you have entered is a Leap-year, the day on 'February' should be 29 days, please correct it and try it again!");
		List<DateUnit> dateList = new ArrayList<>();
		DateUnit d1 = new DateUnit();
		//leap year: 2008 feb 29 days
		d1.setDay(28);
		d1.setMonth(2);
		d1.setYear(2008);
		dateList.add(d1);
		List<DateUnit> result= dataService.validateDate(dateList);	
	}
	
	@Test
	public void Validate_Non_LeapYear() {
		List<DateUnit> dateList = new ArrayList<>();
		DateUnit d1 = new DateUnit();
		//leap year: 2008 feb 29 days
		d1.setDay(29);
		d1.setMonth(2);
		d1.setYear(2009);
		dateList.add(d1);
		List<DateUnit> result= dataService.validateDate(dateList);
		assertThat(result.get(0).getDay()).isEqualTo(29);
	}
	
		@Test
	public void sortEarliestDate_From_TwoDates() {
		List<DateUnit> dateList = new ArrayList<>();
		DateUnit d1 = new DateUnit();
		DateUnit d2 = new DateUnit();
		d1.setDay(25);
		d1.setMonth(11);
		d1.setYear(2000);
		dateList.add(d1);
		d2.setDay(7);
		d2.setMonth(7);
		d2.setYear(1999);

		List<DateUnit> result= dataService.sortEarliestDate(d1, d2);
		assertThat(result.get(0).getDay()).isEqualTo(7);
	}
	
	@Test
	public void show_LessThan_1Year_difference_between_Dates() {
		List<DateUnit> dateList = new ArrayList<>();
		DateUnit d1 = new DateUnit();
		DateUnit d2 = new DateUnit();
		d1.setDay(18);
		d1.setMonth(12);
		d1.setYear(1999);
		dateList.add(d1);
		d2.setDay(19);
		d2.setMonth(11);
		d2.setYear(2000);

		long result= dataService.daysBetweenDates(d1, d2);
		assertThat(result).isEqualTo(337);
	}
		@Test
	public void show_OneDay_difference_between_Dates() {
		List<DateUnit> dateList = new ArrayList<>();
		DateUnit d1 = new DateUnit();
		DateUnit d2 = new DateUnit();
		d1.setDay(18);
		d1.setMonth(12);
		d1.setYear(1999);
		dateList.add(d1);
		d2.setDay(19);
		d2.setMonth(12);
		d2.setYear(1999);

		long result= dataService.daysBetweenDates(d1, d2);
		assertThat(result).isEqualTo(1);
	}
	
	
	@Test
	public void show_MoreThan_1Year_difference_between_Dates() {
		List<DateUnit> dateList = new ArrayList<>();
		DateUnit d1 = new DateUnit();
		DateUnit d2 = new DateUnit();
		d1.setDay(18);
		d1.setMonth(12);
		d1.setYear(1999);
		dateList.add(d1);
		d2.setDay(1);
		d2.setMonth(5);
		d2.setYear(1990);

		long result= dataService.daysBetweenDates(d1, d2);
		assertThat(result).isEqualTo(3518);
	}
	
	@Test
	public void difference_between_Dates_Is_367Days() {
		List<DateUnit> dateList = new ArrayList<>();
		DateUnit d1 = new DateUnit();
		DateUnit d2 = new DateUnit();
		d1.setDay(18);
		d1.setMonth(11);
		d1.setYear(1999);
		dateList.add(d1);
		d2.setDay(19);
		d2.setMonth(11);
		d2.setYear(2000);

		long result= dataService.daysBetweenDates(d1, d2);
		assertThat(result).isEqualTo(367);
	}
	
	@Test
	public void show_8_days_difference_between_Dates() {
		List<DateUnit> dateList = new ArrayList<>();
		DateUnit d1 = new DateUnit();
		DateUnit d2 = new DateUnit();
		d1.setDay(18);
		d1.setMonth(11);
		d1.setYear(2000);
		dateList.add(d1);
		d2.setDay(10);
		d2.setMonth(11);
		d2.setYear(2000);

		long result= dataService.daysBetweenDates(d1, d2);
		assertThat(result).isEqualTo(8);
	}
	
	
	
}
