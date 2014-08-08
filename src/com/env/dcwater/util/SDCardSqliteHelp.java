package com.env.dcwater.util;

import java.io.File;
import com.env.dcwater.component.CustomContextWrap;
import com.env.dcwater.component.WaterApplication;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

/**
 * 
 * 获取sd卡上的数据库时使用的方法
 * @author sk
 *
 */
public class SDCardSqliteHelp extends SQLiteOpenHelper{
	
	public static final String TAG_STRING = "SDCardSqliteHelp";
	
	private static SDCardSqliteHelp INSTANCE = null;
	
	public  static final String DATABASE_NAME = "easypatrol.db";  
	
    private static final int DATABASE_VERSION = 0;
    
    public synchronized static SDCardSqliteHelp getInstance(Context context){
    	if(INSTANCE == null){
    		INSTANCE = new SDCardSqliteHelp(context);
    	}
		return INSTANCE;
    } 
    
    private SDCardSqliteHelp(Context context){
		super(new CustomContextWrap(context,getDirPath()), DATABASE_NAME, null, DATABASE_VERSION);
	}

	
	public static String getDirPath(){
		StringBuilder sb = new StringBuilder();
		sb.append(Environment.getExternalStorageDirectory().getAbsolutePath()).append(File.separator)
		.append(WaterApplication.ROOT_PATROL_PATH_STRING).append(File.separator).append(WaterApplication.DB_PATROL_PATH_STRING);
		return sb.toString();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
