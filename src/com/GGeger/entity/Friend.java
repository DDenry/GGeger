package com.GGeger.entity;

import java.util.List;

public class Friend {
	private String student_id;
	private List<String> friends_id;

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	public List<String> getFriends_id() {
		return friends_id;
	}

	public void setFriends_id(List<String> friends_id) {
		this.friends_id = friends_id;
	}

	public int getFriendsCount() {
		return friends_id.size();
	}

}
