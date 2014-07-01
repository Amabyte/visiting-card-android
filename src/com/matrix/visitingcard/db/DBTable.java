package com.matrix.visitingcard.db;

import android.database.sqlite.SQLiteDatabase;

public abstract class DBTable {
	public abstract boolean setUp(SQLiteDatabase db);
}