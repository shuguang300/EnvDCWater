package com.env.dcwater.component;

import java.io.File;

import android.app.Application;
import android.os.Environment;

/**
 * 设置系统的常量
 * @author sk
 *
 */
public class DCWaterApp extends Application{
	
	private File cache,files;
	
	public static final String CACHE_PATH_STRING = "cache";
	
	public static final String ROOT_PATH_STRING = "EnvDCWater";
	
	public static final String FILES_PATH_STRING = "files";
	
	public static final String TAG_STRING = "DCWaterApp";
	
	public static final String PREFERENCE_STRING = "EnvDCWater";
	
	public static final String PREFERENCE_ISLOGIN_STRING = "IsLogin";
	
	public static final String PREFERENCE_USERNAME_STRING ="UserName";
	
	public static final String PREFERENCE_USERPSW_STRING ="UserPassword";
	
	public static final String PREFERENCE_USERROLE_STRING ="UserRole";
	
	public static final String PREFERENCE_USERID_STRING ="UserID";
	
	public static final String PREFERENCE_PLANTID_STRING ="PlantID";
	
	public static final String PREFERENCE_PLANTNAME_STRING ="PlantName";
	
	public static final String PREFERENCE_PLANTTYPE_STRING ="PlantType";
	
	public static final String PREFERENCE_REALNAME_STRING ="RealUserName";
	
	public static final String PREFERENCE_ACTSTATE_STRING ="AccountState";
	
	public static final String PREFERENCE_POSITIONID_STRING ="PositionID";
	
	public static final String PREFERENCE_POSITIONNAME_STRING ="PositionName";
	
	public static final int StartupStayTime = 1000; //登陆界面的延迟
	
	@Override
	public void onCreate() {
		super.onCreate();
		if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)){
			cache = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+ROOT_PATH_STRING+File.separator+CACHE_PATH_STRING);
			files = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+ROOT_PATH_STRING+File.separator+FILES_PATH_STRING);
			if(!cache.exists()){
				cache.mkdirs();
			}
			if(!files.exists()){
				files.mkdirs();
			}
		}
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
}
