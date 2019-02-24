package kr.co.ex.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
	public static LocalDateTime getToday(){
		return LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
	}
	public static LocalDateTime getADaysAgo(int days){
		return LocalDateTime.of(LocalDate.now().minusDays(days), LocalTime.MIDNIGHT);
	}
	public static LocalDateTime getAFewWeeksAgo(int weeks){
		// monday == 1
		DayOfWeek today = LocalDate.now().getDayOfWeek();
		LocalDate sundayOfThisWeek = LocalDate.now().minusDays(today.getValue() + 1);
		return LocalDateTime.of(sundayOfThisWeek.minusWeeks(weeks), LocalTime.MIDNIGHT);
	}
	public static LocalDateTime getAFewMonthAgo(int months){
		int year = LocalDate.now().getYear();
		int month = LocalDate.now().getMonthValue();
		return LocalDateTime.of(LocalDate.of(year, month, 1).minusMonths(months), LocalTime.MIDNIGHT);
	}
	
}
