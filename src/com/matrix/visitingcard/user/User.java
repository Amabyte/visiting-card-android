package com.matrix.visitingcard.user;

import java.util.ArrayList;
import java.util.zip.ZipInputStream;

import android.graphics.Bitmap;

public class User {

	private static User user = null;
	private String name;
	private ArrayList<String> ContactPersonal;
	private ArrayList<String> ContactBusiness;
	private ArrayList<String> ContactOffice;
	private ArrayList<String> ContactResidence;
	private String address;
	private String designation;
	private String companyName;
	private String companyAddress;
	private Bitmap companyLogo;
	private Bitmap userPhoto;

	public static User getInstance() {
		if (user != null) {
			return user;
		} else {
			return createNewUser();
		}
	}

	private static User createNewUser() {
		return null;
	}

}
