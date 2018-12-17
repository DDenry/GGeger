package com.GGeger.pattern.chain;

import com.GGeger.entity.StudentsCountInfo;

public class StudentsCountInfoInterpreter extends EntityInterpreter {
	public StudentsCountInfoInterpreter() {
		this.className = StudentsCountInfo.class.getSimpleName();
	}

	@Override
	protected <T> void interpreted(T entity) {
		
		StudentsCountInfo studentsCountInfo = (StudentsCountInfo) entity;
		
		System.out.println("");
		System.out.println("StudentsCountInfo:");
		System.out.println("Total students' count is :" + studentsCountInfo.getStudentsCount());
		System.out.println("Male students' count is :" + studentsCountInfo.getMaleStudentsCount());
		System.out.println("Female students' count is :" + studentsCountInfo.getFemaleStudentsCount());
	}

}
