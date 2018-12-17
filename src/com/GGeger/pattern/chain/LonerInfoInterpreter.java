package com.GGeger.pattern.chain;

import com.GGeger.entity.LonerInfo;

public class LonerInfoInterpreter extends EntityInterpreter {
	public LonerInfoInterpreter() {
		this.className = LonerInfo.class.getSimpleName();
	}

	@Override
	protected <T> void interpreted(T entity) {
		LonerInfo lonerInfo = (LonerInfo) entity;

		System.out.println("");
		System.out.println("LonerInfo:");

		// Student EntityInterpreter
		LonerInterpreter.getInstance().getChainOfInterpreters().distribute(lonerInfo.getStudent());

		System.out.println("Meal count is :" + lonerInfo.getMealCount());

		//
		for (int i = 0; i < lonerInfo.getBillsInfoList().size(); i++)
			// Bill EntityInterpreter
			LonerInterpreter.getInstance().getChainOfInterpreters().distribute(lonerInfo.getBillsInfoList().get(i));
	}

}
