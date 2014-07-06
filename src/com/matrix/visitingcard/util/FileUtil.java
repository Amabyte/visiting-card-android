package com.matrix.visitingcard.util;

import android.webkit.MimeTypeMap;

public class FileUtil {
	public static String getMimeType(String url) {
		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		if (extension != null) {
			MimeTypeMap mime = MimeTypeMap.getSingleton();
			type = mime.getMimeTypeFromExtension(extension);
		}
		return type;
	}
	// public static void deleteFilesRecursively(File path) {
	// if (path.exists()) {
	// File[] files = path.listFiles();
	// if (files == null) {
	// return;
	// }
	// for (int i = 0; i < files.length; i++) {
	// if (files[i].isDirectory()) {
	// deleteFilesRecursively(files[i]);
	// } else {
	// files[i].delete();
	// }
	// }
	// path.delete();
	// }
	//
	// }
	//
	// public static void deleteEntireFolder(File path) {
	// deleteFilesRecursively(path);
	// path.delete();
	// }

}
