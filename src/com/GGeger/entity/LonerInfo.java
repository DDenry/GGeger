package com.GGeger.entity;

import java.util.List;

public class LonerInfo {

	/**
	 * 孤僻学子基本信息 吃饭总次数 孤僻学子的账单信息
	 */

	private Student student;
	private int mealCount;
	private List<Bill> billsInfoList;

	public LonerInfo(Student student, LonerStudent lonerStudent) {
		this.student = student;
		this.mealCount = lonerStudent.getMealCount();
		this.billsInfoList = lonerStudent.getBillsInfoList();
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public int getMealCount() {
		return mealCount;
	}

	public void setMealCount(int mealCount) {
		this.mealCount = mealCount;
	}

	public List<Bill> getBillsInfoList() {
		return billsInfoList;
	}

	public void setBillsInfoList(List<Bill> billsInfoList) {
		this.billsInfoList = billsInfoList;
	}
}
