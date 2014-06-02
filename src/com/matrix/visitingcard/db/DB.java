package com.matrix.visitingcard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DB {

	public static final String DATABASE_NAME = "visitingcard.db";
	public static final int DATABASE_VERSION = 1;

	private static SQLiteDatabase db;

	public static void open(Context context) {
		db = new DatabaseHelper(context).getWritableDatabase();
	}

	public static SQLiteDatabase getDb() {
		if (db == null)
			throw new IllegalStateException("Db not opened");
		return db;
	}

	public static void close() {
		if (db == null)
			throw new IllegalStateException("Db not opened");
		if (db.isOpen()) {
			db.close();
			db = null;
		}
	}
}