package com.matrix.visitingcard.http.response;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class VCTResponse {

	private int id;
	private String name;
	private SampleUrls sampleUrls;
	private ArrayList<KeysAndTypes> keysAndTypes;

	private static ArrayList<VCTResponse> instance = null;

	public synchronized static ArrayList<VCTResponse> getAllVCT() {
		if (instance == null) {
			instance = new ArrayList<VCTResponse>();
		}
		return instance;
	}

	public ArrayList<KeysAndTypes> getKeysAndTypes() {
		return keysAndTypes;
	}

	public void setKeysAndTypes(ArrayList<KeysAndTypes> keysAndTypes) {
		this.keysAndTypes = keysAndTypes;
	}

	public enum KeysType {
		TEXT, TEXTAREA, IMAGE
	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SampleUrls getSampleUrls() {
		return sampleUrls;
	}

	public void setSampleUrls(SampleUrls sampleUrls) {
		this.sampleUrls = sampleUrls;
	}

	public class SampleUrls {
		private String original;
		private String thumb;
		private String medium;

		public String getOriginal() {
			return original;
		}

		public void setOriginal(String original) {
			this.original = original;
		}

		public String getThumb() {
			return thumb;
		}

		public void setThumb(String thumb) {
			this.thumb = thumb;
		}

		public String getMedium() {
			return medium;
		}

		public void setMedium(String medium) {
			this.medium = medium;
		}

	}

	public class KeysAndTypes {
		private KeysType type;
		private String key;

		public KeysType getType() {
			return type;
		}

		public void setType(KeysType type) {
			this.type = type;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

	}
}
