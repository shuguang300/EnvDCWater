package com.env.dcwater.component;

import java.io.File;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class CustomContextWrap extends ContextWrapper{

	private String dirPath;
	
	public CustomContextWrap(Context base,String dirPath) {
		super(base);
		this.dirPath = dirPath;
	}
	
	@Override
	public File getDatabasePath(String name) {
		File result = new File(dirPath + File.separator + name);
		if(!result.exists()){
			result.getParentFile().mkdirs();
		}
		return result;
	}
	
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory) {
		return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
	}
	
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory, DatabaseErrorHandler errorHandler) {
		return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name).getAbsolutePath(), factory, errorHandler);
	}

}
