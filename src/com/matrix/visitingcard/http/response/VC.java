package com.matrix.visitingcard.http.response;

import java.util.ArrayList;

public class VC {
	protected int id;
	protected int userId;
	protected int visitingCardTemplateId;
	protected String createdAt;
	protected String updatedAt;
	protected ImageUrl imagUrls;
	protected ArrayList<KeysAndValues> keysAndValues;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getVisitingCardTemplateId() {
		return visitingCardTemplateId;
	}

	public void setVisitingCardTemplateId(int visitingCardTemplateId) {
		this.visitingCardTemplateId = visitingCardTemplateId;
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

	public ImageUrl getImagUrls() {
		return imagUrls;
	}

	public void setImagUrls(ImageUrl imagUrls) {
		this.imagUrls = imagUrls;
	}

	public ArrayList<KeysAndValues> getKeysAndValues() {
		return keysAndValues;
	}

	public void setKeysAndValues(ArrayList<KeysAndValues> keysAndValues) {
		this.keysAndValues = keysAndValues;
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



	public class ImageUrl {
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

	public class KeysAndValues {

		private String key;
		private String value;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}
}
