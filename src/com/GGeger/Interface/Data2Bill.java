package com.GGeger.Interface;

import com.GGeger.Entity.Bill;

public interface Data2Bill {
	/**
	 * 文本数据转换成账单
	 */
	Bill transfer2Bill(String data);
}
