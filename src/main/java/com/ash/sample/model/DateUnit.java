package com.ash.sample.model;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ash Izadi
 *
 */
public class DateUnit {

	private static final Logger log = LoggerFactory.getLogger(DateUnit.class);
	// number of days for each month in order
	public static List<Integer> NUMBEROFDAYS = Arrays.asList(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
	//the number of days in a 400 year cycle
	private static final int DAYS_PER_CYCLE = 146097;
	//The number of days from year zero to year 1970. There are five 400 year cycles from year zero to 2000. There are 7 leap years from 1970 to 2000.
    static final long DAYS_0000_TO_1970 = (DAYS_PER_CYCLE * 5L) - (30L * 365L + 7L);
    private static final int BASE_YEAR = 1970;
	
    //Accumulating:	12/1 1/1 2/1   3/1   4/1   5/1   6/1   7/1   8/1   9/1   10/1   11/1   12/1
    static final int[] ACCUMULATED_DAYS_IN_MONTH_LEAP = {  -30,  0, 31, 59+1, 90+1,120+1,151+1,181+1,212+1,243+1, 273+1, 304+1, 334+1};
    //  12   1   2   3   4   5   6   7   8   9  10  11  12
    static final int[] DAYS_IN_MONTH   = { 31, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    //  12/1 1/1 2/1 3/1 4/1 5/1 6/1 7/1 8/1 9/1 10/1 11/1 12/1
    static final int[] ACCUMULATED_DAYS_IN_MONTH  = {  -30,  0, 31, 59, 90,120,151,181,212,243, 273, 304, 334};
    //This table covers all the years that can be
    // supported by the POSIX time_t (32-bit) after the Epoch. Note
    // that the data type is int[].
    private static final int[] FIXED_DATES = {
        719163, // 1970
        719528, // 1971
        719893, // 1972
        720259, // 1973
        720624, // 1974
        720989, // 1975
        721354, // 1976
        721720, // 1977
        722085, // 1978
        722450, // 1979
        722815, // 1980
        723181, // 1981
        723546, // 1982
        723911, // 1983
        724276, // 1984
        724642, // 1985
        725007, // 1986
        725372, // 1987
        725737, // 1988
        726103, // 1989
        726468, // 1990
        726833, // 1991
        727198, // 1992
        727564, // 1993
        727929, // 1994
        728294, // 1995
        728659, // 1996
        729025, // 1997
        729390, // 1998
        729755, // 1999
        730120, // 2000
        730486, // 2001
        730851, // 2002
        731216, // 2003
        731581, // 2004
        731947, // 2005
        732312, // 2006
        732677, // 2007
        733042, // 2008
        733408, // 2009
        733773, // 2010
    };
    /**
     * Divides two integers and returns the floor of the quotient.
     * For example, <code>floorDivide(-1, 4)</code> returns -1 while
     * -1/4 is 0.
     *
     * @param n the numerator
     * @param d a divisor that must be greater than 0
     * @return the floor of the quotient
     */
    public static final long floorDivide(long n, long d) {
        return ((n >= 0) ?
                (n / d) : (((n + 1L) / d) - 1L));
    }
    public static final long TIME_UNDEFINED = Long.MIN_VALUE;
    public static final int JANUARY = 1;
    public static final int FEBRUARY = 2;
    public static final int MARCH = 3;
    public static final int APRIL = 4;
    public static final int MAY = 5;
    public static final int JUNE = 6;
    public static final int JULY = 7;
    public static final int AUGUST = 8;
    public static final int SEPTEMBER = 9;
    public static final int OCTOBER = 10;
    public static final int NOVEMBER = 11;
    public static final int DECEMBER = 12;
    
	static final int SECOND_IN_MILLIS = 1000;
	static final int MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
	static final int HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
	static final int DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;
	// The number of days between January 1, 1 and January 1, 1970 
	static final int EPOCH_OFFSET = 719163;

	private int year;
	private int month;
	private int day;
	private long fraction;      // time of day value in millisecond
	private int hours;
	private int minutes;
	private int seconds;
	private int millis; 
	private int zoneOffset;

	

	public DateUnit() {
		// placeholder
	}

	public DateUnit(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public boolean equals(DateUnit other) {
		if (day == other.day && month == other.month && year == other.year) {
			return true;
		} else
			return false;
	}

	
	//check Leap Year 
	public boolean isLeapYear(int year) {
		return ((year & 3) == 0) && ((year % 100) != 0 || (year % 400) == 0);
	}

		
	/**
	 * To get milisec for our date 
	 * @param date
	 * @return
	 */
	public long getTimeOfOurDateUnit(DateUnit date) {
		 long gd = getFixedDate(date.getYear(), date.getMonth() , date.getDay());
		 long ms = (gd - EPOCH_OFFSET) * DAY_IN_MILLIS + getTimeOfDay(date);
		 return ms - date.getZoneOffset();
	}
	
	public long getFixedDate(int year, int month, int dayOfMonth) {
		final int JANUARY = 1;
		boolean isJan1 = month == JANUARY && dayOfMonth == 1;

		// Look up the pre-calculated fixed date table
		int n = year - BASE_YEAR;
		if (n >= 0 && n < FIXED_DATES.length) {
			long jan1 = FIXED_DATES[n];
			return isJan1 ? jan1 : jan1 + getDayOfYear(year, month, dayOfMonth) - 1;
		}

		long prevyear = (long) year - 1;
		long days = dayOfMonth;

		if (prevyear >= 0) {
			days += (365 * prevyear) + (prevyear / 4) - (prevyear / 100) + (prevyear / 400)
					+ ((367 * month - 362) / 12);
		} else {
			days += (365 * prevyear) + floorDivide(prevyear, 4) - floorDivide(prevyear, 100)
					+ floorDivide(prevyear, 400) + floorDivide((367 * month - 362), 12);
		}

		if (month > FEBRUARY) {
			days -= isLeapYear(year) ? 1 : 2;
		}

		return days;
	}
	
	final long getDayOfYear(int year, int month, int dayOfMonth) {
		return (long) dayOfMonth
				+ (isLeapYear(year) ? ACCUMULATED_DAYS_IN_MONTH_LEAP[month] : ACCUMULATED_DAYS_IN_MONTH[month]);
	}
	
	
	 protected long getTimeOfDay(DateUnit date) {
	        long fraction = date.getTimeOfDay();
	        if (fraction != TIME_UNDEFINED) {
	            return fraction;
	        }
	        fraction = getTimeOfDayValue(date);
	        date.setTimeOfDay(fraction);
	        return fraction;
	    }
	
	   public long getTimeOfDayValue(DateUnit date) {
	        long fraction = date.getHours();
	        fraction *= 60;
	        fraction += date.getMinutes();
	        fraction *= 60;
	        fraction += date.getSeconds();
	        fraction *= 1000;
	        fraction += date.getMillis();
	        return fraction;
	    }

	protected void setTimeOfDay(long fraction) {
		this.fraction = fraction;
	}   

	public long getTimeOfDay() {
		return fraction;
	}
    public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public int getMillis() {
		return millis;
	}

	public void setMillis(int millis) {
		this.millis = millis;
	}

	public int getZoneOffset() {
		return zoneOffset;
	}

	public void setZoneOffset(int zoneOffset) {
		this.zoneOffset = zoneOffset;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
