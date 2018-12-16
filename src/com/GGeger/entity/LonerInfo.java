package com.GGeger.entity;

public class LonerInfo {

	/**
	 * 孤僻学子 studentId,性别,专业,年级,吃饭次数,与所有学生平均吃饭次数的差值
	 */
	
	private Student student;
	private int mealCount;

	public LonerInfo(Student student, LonerStudent lonerStudent) {
		this.student = student;
		this.mealCount = lonerStudent.getMealCount();
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
}
