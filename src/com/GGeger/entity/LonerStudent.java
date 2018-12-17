package com.GGeger.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LonerStudent {

	private String studentId;

	private int mealCount = 0;

	private List<Bill> billsInfoList = new ArrayList<Bill>();

	private HashMap<String, Integer> posibleFriendsList = new HashMap<String, Integer>();

	private List<String> realFriendsList = new ArrayList<String>();

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
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

	public HashMap<String, Integer> getPosibleFriendsList() {
		return posibleFriendsList;
	}

	public void setPosibleFriendsList(HashMap<String, Integer> posibleFriendsList) {
		this.posibleFriendsList = posibleFriendsList;
	}

	public List<String> getRealFriendsList() {
		return realFriendsList;
	}

}
