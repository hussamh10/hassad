package com.hush.hassad.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hussam on 3/31/18.
 */

public class Utils {
	Utils(){

	}

	public static String changeTo12hr(int hour, int mins){
		String half = "AM";
		if (hour > 12){
			hour = hour - 12;
			half = "PM";
		}

		return hour + ":" + mins + " " + half;
	}

	public static String getTimeString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		return sdf.format(date);
	}
	public static boolean isSameDay(Date date1, Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		boolean sameYear = calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
		boolean sameMonth = calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
		boolean sameDay = calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
		return (sameDay && sameMonth && sameYear);
		
	}
}
