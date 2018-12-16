package com.GGeger.program;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.GGeger.entity.Bill;
import com.GGeger.entity.BillModel;
import com.GGeger.process.Data2Bill;
import com.GGeger.process.DataDigger;
import com.GGeger.process.FormatLog;
import com.GGeger.process.FormatOutput;
import com.GGeger.process.ResultHandled;
import com.GGeger.utils.Logger;

public class BaseProcess implements DataDigger, Data2Bill, FormatOutput, FormatLog, ResultHandled {

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
		Logger.getInstance().print("");
		Logger.getInstance().print(">>>>>>>>>>>>>>>>>>>>");
		Logger.getInstance().print(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		Logger.getInstance().print(content);
		Logger.getInstance().print("");
	}

	@Override
	public BillModel _transfer2Bill(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void serialization() {

	}

}
