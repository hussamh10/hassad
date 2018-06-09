package com.hush.hassad.controller;

import java.text.SimpleDateFormat;
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
}
