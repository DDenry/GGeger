package com.GGeger.entity;

public class Student {

	/**
	 * Student实体:包含学生Id、学生性别、年级(由班级解析获取)、
	 */

	// 学生Id
	private String studentId;
	// 学生性别
	private String gender;
	// 学生班级
	private String major;
	// 学生年级
	private String grade;

	public Student(String studentId, String gender, String major, String grade) {
		this.studentId = studentId;
		this.gender = gender;
		this.major = major;
		this.grade = grade;
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
