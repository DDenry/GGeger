package com.GGeger.Utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTransfer {
	/**
	 * �ַ���ת��Ϊ��������
	 */
	public static Date string2Date(String stringFromat) {
		// ��������ʱ���ʽת��������
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.parse(stringFromat, new ParsePosition(0));
	}
}
