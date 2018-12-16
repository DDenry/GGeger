package com.GGeger.pattern.factory;

public class ResultFactory {

	public ResultFactory() {
		// TODO Auto-generated constructor stub
	}

	public <T> RuledResult getResult(Class<T> type) {

		switch (type.getSimpleName()) {

		case "LonerResult":
			
			return new LonerStudentResult();

		case "":
			break;

		default:
			break;
		}

		return null;
	}

}
