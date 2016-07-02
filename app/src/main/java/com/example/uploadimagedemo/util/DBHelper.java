package com.example.uploadimagedemo.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public Context context;

	// public static SQLiteDatabase db = null;

	public DBHelper(Context context) {
		super(context, Config.DB_NAME, null, 33);
		this.context = context;

		// if (DBHelper.db == null || !DBHelper.db.isOpen()) {
		// DBHelper.db = this.getWritableDatabase();
		// DBHelper.db.enableWriteAheadLogging();
		// }
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void execute(String statment) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			Log.debug(this.getClass() + " :: execute() :: ", statment);
			Log.print(this.getClass() + " :: execute() :: ", statment);
			// DBHelper.db.execSQL(statment);
			db.execSQL(statment);

		} catch (Exception e) {
			Log.error(this.getClass() + " :: execute() ::", e);
			Log.print(e);
		} finally {
			db.close();
			db = null;
		}
	}

	public Cursor query(String statment) {
		Cursor cur = null;
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			Log.debug(this.getClass() + " :: query() :: ", statment);
			Log.print(this.getClass() + " :: query() :: ", statment);
			// cur = DBHelper.db.rawQuery(statment, null);
			cur = db.rawQuery(statment, null);
			cur.moveToPosition(-1);
		} catch (Exception e) {
			Log.error(this.getClass() + " :: query() ::", e);
			Log.print(e);
		} finally {

			db.close();
			db = null;
		}

		return cur;
	}

	public static String getDBStr(String str) {

		str = str != null ? str.replaceAll("'", "''") : null;
		str = str != null ? str.replaceAll("&#039;", "''") : null;
		str = str != null ? str.replaceAll("&amp;", "&") : null;

		return str;

	}

	public void upgrade(int level) {
		switch (level) {
		case 0:
			doUpdate1();

		case 1:

			// doUpdate2();

		case 2:

			// doUpdate3();

		case 3:
			// doUpdate4();
		}
	}

	private void doUpdate1() {

		// Create Table
		this.execute("CREATE TABLE IF NOT EXISTS Category(catId INTEGER, catName TEXT, image TEXT, active INTEGER, totalCounter INTEGER, orderBy INTEGER)");

	}

	public SQLiteDatabase getConnection() {
		SQLiteDatabase dbCon = this.getWritableDatabase();

		return dbCon;
	}

}
