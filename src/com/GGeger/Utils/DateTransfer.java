package com.GGeger.Utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTransfer {
	/**
	 * 字符串转换为日期类型
	 */
	public static Date string2Date(String stringFromat) {
		// 根据数据时间格式转换成日期
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.parse(stringFromat, new ParsePosition(0));
	}
}
