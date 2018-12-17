package com.GGeger.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTransfer {

	public static Date string2Date(String stringFromat) {
		//
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.parse(stringFromat, new ParsePosition(0));
	}

	public static String date2String(long date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(date));
	}
}
