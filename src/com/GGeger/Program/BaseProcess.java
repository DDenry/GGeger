package com.GGeger.Program;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.GGeger.Entity.Bill;
import com.GGeger.Interface.Data2Bill;
import com.GGeger.Interface.DataDigger;
import com.GGeger.Interface.FormatLog;
import com.GGeger.Interface.FormatOutput;

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
		System.out.println(">>>>>>>>>>>>>>>>>>>>");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		System.out.println(content);
	}

}
