package com.GGeger.entity;

import java.util.HashMap;
import java.util.Map;

public class StudentModel {

	private String id;
	private int mealCount = 0;
	private Map<String, Integer> friends = new HashMap<String, Integer>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMealCount() {
		return mealCount;
	}

	public void setMealCount(int mealCount) {
		this.mealCount = mealCount;
	}

	public Map<String, Integer> getFriends() {
		return friends;
	}

	public void setFriends(Map<String, Integer> friends) {
		this.friends = friends;
	}
}
