package com.GGeger.entity;

public class Student {

	/**
	 * Studentʵ��:����ѧ��Id��ѧ���Ա��꼶(�ɰ༶������ȡ)��
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
	 * ��һ:1 ���:2 ����:3 ����:4 ��һ:5 �ж�:6 ����:7
	 */
	// ���ݰ༶���ƻ�ȡ�꼶��Ϣ
	private int className2Grade(String className) {
		// TODO:����ȡ���İ༶����ͨ����ǰ���ת��Ϊ�꼶��Ϣ

		//
		return 0;
	}

}
