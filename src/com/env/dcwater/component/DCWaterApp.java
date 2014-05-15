package com.env.dcwater.component;

import android.app.Application;

/**
 * 设置系统的常量
 * @author sk
 *
 */
public class DCWaterApp extends Application{
	
	public static final String TAG_STRING = "DCWaterApp";
	
	public static final String PREFERENCE_STRING = "EnvDcWater";
	
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
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
}
