package com.env.dcwater.util;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 获取私有目录里的数据库时使用的方法
 * @author sk
 *
 */
public class SqliteHelper extends SQLiteOpenHelper{
	public static final String TAG_STRING = "SqliteHelper";
	
	private static SqliteHelper INSTANCE = null;
	
	public  static final String DATABASE_NAME = "envdcwater.db";  
	
    private static final int DATABASE_VERSION = 0;

	private SqliteHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public synchronized static SqliteHelper getInstance(Context context){		
		if(INSTANCE==null){
			INSTANCE = new SqliteHelper(context);			
		}
		return INSTANCE;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
