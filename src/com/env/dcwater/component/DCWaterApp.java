package com.env.dcwater.component;

import android.app.Application;

public class DCWaterApp extends Application{
	public static final String PREFERENCE_STRING = "EnvDcWater";
	public static final int StartupStayTime = 0; //登陆界面的延迟
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
}
