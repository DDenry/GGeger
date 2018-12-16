package com.GGeger.topic;

import com.GGeger.entity.LonerResult;
import com.GGeger.pattern.factory.ResultFactory;
import com.GGeger.pattern.factory.RuledResult;
import com.GGeger.utils.Logger;

public class ResultShow {

	public ResultShow() {
		// TODO Auto-generated constructor stub
	}

	// 反序列化孤僻结果
	public void analyseResult() {

		//
		lonerResult();

		// 显示主菜单
//		Main.ShowMenu();
	}

	private void lonerResult() {

		ResultFactory resultFactory = new ResultFactory();

		// 孤僻结果分析
		RuledResult ruleResult = resultFactory.getResult(LonerResult.class);

		LonerResult lonerResult = ruleResult.foldResult();

		Logger.getInstance().print("学生总数：" + lonerResult.getStudentsCountInfo().getStudentsCount());
		Logger.getInstance().print("男生人数：" + lonerResult.getStudentsCountInfo().getMaleStudentsCount());
		Logger.getInstance().print("女生人数：" + lonerResult.getStudentsCountInfo().getFemaleStudentsCount());
	}
}
