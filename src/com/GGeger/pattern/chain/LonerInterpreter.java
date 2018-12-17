package com.GGeger.pattern.chain;

public class LonerInterpreter {
	private static volatile LonerInterpreter instance;

	private LonerInterpreter() {
	}

	public static LonerInterpreter getInstance() {
		if (instance == null) {

			synchronized (LonerInterpreter.class) {

				if (instance == null) {

					instance = new LonerInterpreter();

				}

			}

		}

		return instance;
	}

	// 获取实体解释器
	public EntityInterpreter getChainOfInterpreters() {

		// LonerResultEntityInterpreter
		EntityInterpreter lonerResultInterpreter = new LonerResultInterpreter();
		// StudentsCountInfoInterpreter
		EntityInterpreter studentsCountInfoInterpreter = new StudentsCountInfoInterpreter();
		// LonerStudentInterpreter
		EntityInterpreter lonerStudentInterpreter = new LonerStudentInterpreter();
		// BillInterpreter
		EntityInterpreter billInterpreter = new BillInterpreter();
		// StudentEntityInterpreter
		EntityInterpreter studentInterpreter = new StudentInterpreter();

		lonerResultInterpreter.setNextInterpreter(studentsCountInfoInterpreter);
		studentsCountInfoInterpreter.setNextInterpreter(lonerStudentInterpreter);
		lonerStudentInterpreter.setNextInterpreter(billInterpreter);
		billInterpreter.setNextInterpreter(studentInterpreter);

		return lonerResultInterpreter;
	}
}
