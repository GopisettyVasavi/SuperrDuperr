package com.superrduperr.todo.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 *  Util class to write any commonly used methods.
 * @author Vasavi
 *
 */
public class TodoAppUtil {
	/**
	 * This method will take date in String format "dd-MM-yyyy HH:mm:ss" and return LocalDateTime object.
	 * @param dateTime
	 * @return
	 */
	public static LocalDateTime formatDateTime(String dateTime) {
		if(null!=dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"); 
		LocalDateTime timeStamp=LocalDateTime.parse(dateTime, formatter);
		return timeStamp;
		}
		return null;
	}

}
