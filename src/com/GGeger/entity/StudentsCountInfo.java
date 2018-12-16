package com.GGeger.entity;

public class StudentsCountInfo {

	private int studentsCount = 0;
	private int maleStudentsCount = 0;
	private int femaleStudentsCount = 0;

	public StudentsCountInfo(int studentsCount, int maleStudentsCount, int femaleStudentsCount) {
		this.studentsCount = studentsCount;
		this.maleStudentsCount = maleStudentsCount;
		this.femaleStudentsCount = femaleStudentsCount;
	}

	public int getStudentsCount() {
		return studentsCount;
	}

	public void setStudentsCount(int studentsCount) {
		this.studentsCount = studentsCount;
	}

	public int getMaleStudentsCount() {
		return maleStudentsCount;
	}

	public void setMaleStudentsCount(int maleStudentsCount) {
		this.maleStudentsCount = maleStudentsCount;
	}

	public int getFemaleStudentsCount() {
		return femaleStudentsCount;
	}

	public void setFemaleStudentsCount(int femaleStudentsCount) {
		this.femaleStudentsCount = femaleStudentsCount;
	}

}
