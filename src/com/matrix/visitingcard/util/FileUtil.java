package com.matrix.visitingcard.util;

import java.io.File;

public class FileUtil {
	public static void deleteFilesRecursively(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteFilesRecursively(files[i]);
				} else {
					files[i].delete();
				}
			}
		}

	}

	public static void deleteEntireFolder(File path) {
		deleteFilesRecursively(path);
		path.delete();
	}

}
