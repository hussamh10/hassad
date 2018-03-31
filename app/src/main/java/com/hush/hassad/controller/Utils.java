package com.hush.hassad.controller;

import java.util.Date;

/**
 * Created by hussam on 3/31/18.
 */

public class Utils {
	Utils(){

	}

	public static String getTimeString(Date date){
		int hour = date.getHours();
		int minutes = date.getMinutes();

		String time = hour + ":" + minutes;
		return time;
	}
}
