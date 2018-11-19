package com.GGeger.Entity;

public class Bill {
	// 账单时间
	private long millis;
	// 学生id
	private String studentId;
	// 餐厅名称
	private String canteenName;
	// 消费金额
	private float price;
	// 刷卡机
	private int pointofsales;
	// 班级
	private String className;
	// 性别
	private int gender;

	public Bill(BillBuilder billBuilder) {
		this.millis = billBuilder.millis;
		this.studentId = billBuilder.studentId;
		this.canteenName = billBuilder.canteenName;
		this.price = billBuilder.price;
		this.pointofsales = billBuilder.pos;
		this.gender = billBuilder.gender;
		this.className = billBuilder.className;
	}

	/**
	 * 建造者模式（Builder）
	 */
	public static class BillBuilder {
		long millis = -1;
		String studentId = null;
		String canteenName = null;
		float price = 0.0f;
		int pos = -1;
		int gender = 0;
		String className = "";

		public BillBuilder setMillis(long millis) {
			this.millis = millis;
			return this;
		}

		public BillBuilder setStudentId(String studentId) {
			this.studentId = studentId;
			return this;
		}

		public BillBuilder setCanteenName(String canteenName) {
			this.canteenName = canteenName;
			return this;
		}

		public BillBuilder setPrice(float price) {
			this.price = price;
			return this;
		}

		public BillBuilder setPointofsales(int pos) {
			this.pos = pos;
			return this;
		}

		public BillBuilder setClassName(String className) {
			this.className = className;
			return this;
		}

		public BillBuilder setGender(String gender) {
			this.gender = (gender.equals("男") ? 0 : 1);
			return this;
		}

		//
		public Bill build() {
			return new Bill(this);
		}
	}

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

	public String getCanteenName() {
		return canteenName;
	}

	public void setCanteenName(String canteenName) {
		this.canteenName = canteenName;
	}

	public int getPointofsales() {
		return pointofsales;
	}

	public void setPointofsales(int pos) {
		this.pointofsales = pos;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = (gender.equals("男") ? 0 : 1);
	}
}
