package com.GGeger.pattern.chain;

import com.GGeger.entity.Student;

public class StudentInterpreter extends EntityInterpreter {

	public StudentInterpreter() {
		this.className = Student.class.getSimpleName();
	}

	@Override
	protected <T> void interpreted(T entity) {

		Student student = (Student) entity;

		System.out.println("");
		System.out.println("StudentInfo:");
		System.out.println("StudentID:" + student.getStudentId());
		System.out.println("Gender:" + student.getGender());
		System.out.println("Identity:" + student.getIdentity());
		System.out.println("Grade:" + student.getGrade());
		System.out.println("Major:" + student.getMajor());
	}

}
