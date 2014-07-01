package com.matrix.visitingcard.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import android.util.Log;

public class ZipUtil {

	private static String LOG_TAG = ZipUtil.class.getSimpleName();

	public synchronized static void Unzip(final File file,
			final File destination) throws ZipException, IOException {
		new Thread() {
			public void run() {
				// long START_TIME = System.currentTimeMillis();
				// long FINISH_TIME = 0;
				// long ELAPSED_TIME = 0;
				try {
					ZipInputStream zin = new ZipInputStream(
							new FileInputStream(file));

					//Clear files inside destination
					FileUtil.deleteFilesRecursively(destination);
					//Create destination directory if already doesn't exist 
					destination.mkdir();
					
					String workingDir = destination.getAbsolutePath() + "/";

					byte buffer[] = new byte[4096];
					int bytesRead;
					ZipEntry entry = null;
					while ((entry = zin.getNextEntry()) != null) {
						if (entry.isDirectory()) {
							File dir = new File(workingDir, entry.getName());
							if (!dir.exists()) {
								dir.mkdir();
							}
							// Log.i(LOG_TAG, "[DIR] " + entry.getName());
						} else {
							FileOutputStream fos = new FileOutputStream(
									workingDir + entry.getName());
							while ((bytesRead = zin.read(buffer)) != -1) {
								fos.write(buffer, 0, bytesRead);
							}
							fos.close();
							// Log.i(LOG_TAG, "[FILE] " + entry.getName());
						}
					}
					zin.close();

					// FINISH_TIME = System.currentTimeMillis();
					// ELAPSED_TIME = FINISH_TIME - START_TIME;
					// Log.i(LOG_TAG, "COMPLETED in " + (ELAPSED_TIME / 1000)
					// + " seconds.");
				} catch (Exception e) {
					Log.e(LOG_TAG, "FAILED" + e.toString());
				}
			};
		}.start();
	}
}
