package com.GGeger.process;

import com.GGeger.entity.Bill;
import com.GGeger.entity.BillModel;

public interface Data2Bill {
	/**
	 * �ı�����ת�����˵�
	 */
	Bill transfer2Bill(String data);
	
	BillModel _transfer2Bill(String data);
}
