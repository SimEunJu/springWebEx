package kr.co.ex.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateUtils {
	public static LocalDateTime getADaysAgo(int days){
		return LocalDateTime.of(LocalDate.now().minusWeeks(days), LocalTime.MIDNIGHT);
	}
	public static LocalDateTime getAFewWeeksAgo(int weeks){
		return LocalDateTime.of(LocalDate.now().minusWeeks(weeks), LocalTime.MIDNIGHT);
	}
	public static LocalDateTime getAFewMonthAgo(int months){
		return LocalDateTime.of(LocalDate.now().minusMonths(months), LocalTime.MIDNIGHT);
	}
	public static LocalDateTime getAFewYearsAgo(int years){
		return LocalDateTime.of(LocalDate.now().minusYears(years), LocalTime.MIDNIGHT);
	}
	
}
