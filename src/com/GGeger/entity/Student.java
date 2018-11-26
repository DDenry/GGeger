package com.GGeger.entity;

public class Student {

	/**
	 * Student实体:包含学生Id、学生性别、年级(由班级解析获取)、
	 */

	//
	private String studentId;
	//
	private String gender;
	//
	private String className;
	//
	private int grade;

	public Student(String studentId, String gender, String className) {
		this.studentId = studentId;
		this.gender = gender;
		this.className = className;

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

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public int getGrade() {
		return grade;
	}

	/**
	 * 大一:1 大二:2 大三:3 大四:4 研一:5 研二:6 研三:7
	 */
	// 根据班级名称获取年级信息
	private int className2Grade(String className) {
		// TODO:将获取到的班级名称通过当前年份转换为年级信息

		//
		return 0;
	}

}
