package com.GGeger.Entity;

public class Bill {
	private long millis;
	private String studentId;
	private String canteenNumber;
	private int pos;

	public long getMillis() {
		return millis;
	}

	public void setMillis(long millis) {
		this.millis = millis;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getCanteenNumber() {
		return canteenNumber;
	}

	public void setCanteenNumber(String canteenNumber) {
		this.canteenNumber = canteenNumber;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}
}
