package com.GGeger.entity;

public class Bill {
	// �˵�ʱ��
	private long millis;
	//ѧ��ʵ��
	private Student student;
	// ��������
	private String canteenName;
	// ���ѽ��
	private float price;
	// ˢ����
	private int pointofsales;

	public Bill(BillBuilder billBuilder) {
		this.millis = billBuilder.millis;
		this.student = billBuilder.student;
		this.canteenName = billBuilder.canteenName;
		this.price = billBuilder.price;
		this.pointofsales = billBuilder.pos;
	}

	/**
	 * ������ģʽ��Builder��
	 */
	public static class BillBuilder {
		long millis = -1;
		Student student = null;
		String canteenName = null;
		float price = 0.0f;
		int pos = -1;

		public BillBuilder setMillis(long millis) {
			this.millis = millis;
			return this;
		}

		public BillBuilder setStudent(Student student) {
			this.student = student;
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

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
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

}
