package com.example.numbergame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "my_ttn";
	private static final int DB_VERSION =1;
	private static final String CREATE_TABLE ="CREATE TABLE highscore ("
		+"name TEXT, "
		+"date TEXT, "
		+"score LONG "
		+"); ";

	public MyDBHelper(Context mcontext){
		super(mcontext, DB_NAME, null, DB_VERSION);
	}
	
	public MyDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
