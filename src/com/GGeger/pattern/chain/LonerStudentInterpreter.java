package com.GGeger.pattern.chain;

import com.GGeger.entity.LonerStudent;
import com.GGeger.utils.DateTransfer;

public class LonerStudentInterpreter extends EntityInterpreter {

	public LonerStudentInterpreter() {
		this.className = LonerStudent.class.getSimpleName();
	}

	@Override
	protected <T> void interpreted(T entity) {

		LonerStudent lonerStudent = (LonerStudent) entity;

		System.out.println("");
		System.out.println(this.className + "->");

		// Student EntityInterpreter
		LonerInterpreter.getInstance().getChainOfInterpreters().distribute(lonerStudent.getStudent());

		System.out.println("Meal count is :" + lonerStudent.getMealCount());

		System.out.println("Last record time is :" + DateTransfer.date2String(lonerStudent.getLastRecordTime()));

		//
		for (int i = 0; i < lonerStudent.getBillsInfoList().size(); i++)
			// Bill EntityInterpreter
			LonerInterpreter.getInstance().getChainOfInterpreters().distribute(lonerStudent.getBillsInfoList().get(i));
	}

}
