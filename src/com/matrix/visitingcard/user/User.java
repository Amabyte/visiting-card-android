package com.matrix.visitingcard.user;

import android.content.Context;

import com.matrix.visitingcard.constant.Constants;
import com.matrix.visitingcard.util.SharedPrefs;

public class User {

	private boolean isNewUser;
	private String id;
	private String name;
	private String createdAt;
	private String updatedAt;
	private String email;

	private static User instance = null;

	protected User() {
	}

	public synchronized static User getInstance() {
		if (instance == null) {
			instance = new User();

		}
		return instance;
	}

	public boolean isNewUser() {
		return isNewUser;
	}

	public void setNewUser(boolean isNewUser) {
		this.isNewUser = isNewUser;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public static boolean isSignedIn(Context context) {
		return SharedPrefs.getInstance(context).getSharedPrefsValueString(
				Constants.SP.SESSION_ID, null) != null;
	}

}
