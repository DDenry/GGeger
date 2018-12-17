package com.GGeger.pattern.chain;

import com.GGeger.entity.Bill;
import com.GGeger.utils.DateTransfer;

public class BillInterpreter extends EntityInterpreter {
	public BillInterpreter() {
		this.className = Bill.class.getSimpleName();
	}

	@Override
	protected <T> void interpreted(T entity) {
		Bill bill = (Bill) entity;

		System.out.println("");
		System.out.println(this.className + "->");
		System.out.println("Bill's time is :" + DateTransfer.date2String(bill.getMillis()));
		System.out.println("Bill's place is :" + bill.getCanteenName());
	}

}
