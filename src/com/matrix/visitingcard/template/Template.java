package com.matrix.visitingcard.template;

public class Template {

	private final static int TOTAL_NUM_TEMPLATES = 2;
	private final static String PATH_PREFIX = "file:///android_asset/vc";
	private final static String PATH_POSTFIX = "/index.html";

	
	public static String getTemplatePath(int index) {

		if (index >= TOTAL_NUM_TEMPLATES) {
			index = 0;
		}
		return PATH_PREFIX + index + PATH_POSTFIX;
	}

}
