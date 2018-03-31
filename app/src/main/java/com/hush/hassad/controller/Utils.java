package com.hush.hassad.controller;

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
		int hour = date.getHours();
		int minutes = date.getMinutes();

		String time = Utils.changeTo12hr(hour, minutes);

		return time;
	}
}
