package com.GGeger.pattern.chain;

import com.GGeger.entity.LonerResult;

public class LonerResultInterpreter extends EntityInterpreter {

	public LonerResultInterpreter() {
		this.className = LonerResult.class.getSimpleName();
	}

	@Override
	protected <T> void interpreted(T entity) {

		LonerResult lonerResult = (LonerResult) entity;

		System.out.println("");
		System.out.println("LonerResult:");
		System.out.println("Data earliest time is :" + lonerResult.getBillStartTime());
		System.out.println("Data lastest time is :" + lonerResult.getBillStopTime());

		// StudentCountInfo EntityInterpreter
		LonerInterpreter.getInstance().getChainOfInterpreters().distribute(lonerResult.getStudentsCountInfo());

		System.out.println("Average meal count is :" + lonerResult.getAverageMealCount());

		// LonerInfo EntityInterpreter
		for (int i = 0; i < lonerResult.getLonerInfos().size(); i++)
			LonerInterpreter.getInstance().getChainOfInterpreters().distribute(lonerResult.getLonerInfos().get(i));

	}

}
