package com.GGeger.entity;

public class Student {

	private String studentId;
	private String gender;
	private String major;
	private String grade;

	public Student(String studentId, String gender, String grade, String major) {
		this.studentId = studentId;
		this.gender = gender;
		this.grade = grade;
		this.major = major;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getMajor() {
		return major;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

}
