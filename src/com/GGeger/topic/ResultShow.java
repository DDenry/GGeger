package com.GGeger.topic;

import com.GGeger.entity.LonerResult;
import com.GGeger.pattern.chain.LonerInterpreter;
import com.GGeger.pattern.factory.ResultFactory;
import com.GGeger.pattern.factory.RuledResult;

public class ResultShow {

	public ResultShow() {
		// TODO Auto-generated constructor stub
	}

	// 反序列化孤僻结果
	public void analyseResult() {

		//
		lonerResult();

		// 显示主菜单
		// Main.ShowMenu();
	}

	public void lonerResult() {

		ResultFactory resultFactory = new ResultFactory();

		// 孤僻结果分析
		RuledResult ruleResult = resultFactory.getResult(LonerResult.class);

		LonerResult lonerResult = ruleResult.foldResult();

		// Student解释器
		LonerInterpreter.getInstance().getChainOfInterpreters().distribute(lonerResult);

	}
}
