package com.GGeger.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LonerStudent {

	/**
	 * StudentId、个人吃饭总次数、好友列表（分别与好友吃饭次数以及好友总数量）
	 */

	private String studentId;

	private int mealCount = 0;

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
