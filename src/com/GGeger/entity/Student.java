package com.GGeger.entity;

public class Student {

	/**
	 * Studentʵ��:����ѧ��Id��ѧ���Ա��꼶(�ɰ༶������ȡ)��
	 */

	// ѧ��Id
	private String studentId;
	// ѧ���Ա�
	private String gender;
	// ѧ���༶
	private String major;
	// ѧ���꼶
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
