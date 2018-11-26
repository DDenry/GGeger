package com.GGeger.program;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.GGeger.entity.Bill;
import com.GGeger.entity.BillModel;
import com.GGeger.process.Data2Bill;
import com.GGeger.process.DataDigger;
import com.GGeger.process.FormatLog;
import com.GGeger.process.FormatOutput;

public class BaseProcess implements DataDigger, Data2Bill, FormatOutput, FormatLog {

	@Override
	public void digData(String path) {
		// TODO Auto-generated method stub
	}

	@Override
	public Bill transfer2Bill(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void outputResult() {
	}

	@Override
	public void log(String content) {
		//
		System.out.println("");
		System.out.println(">>>>>>>>>>>>>>>>>>>>");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		System.out.println(content);
		System.out.println("");
	}

	@Override
	public BillModel _transfer2Bill(String data) {
		// TODO Auto-generated method stub
		return null;
	}

}
