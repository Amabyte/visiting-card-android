package com.matrix.visitingcard.http.response;

import java.util.ArrayList;

import com.matrix.visitingcard.logger.VLogger;

public class FriendsVC extends VC {
	private static ArrayList<VC> instance = null;

	public synchronized static ArrayList<VC> getAllVC() {
		if (instance == null) {
			instance = new ArrayList<VC>();
		}
		return instance;
	}

	public static void setVCS(ArrayList<VC> parseVC) {
		instance = parseVC;
		VLogger.i("Overriding singlton");
	}
}