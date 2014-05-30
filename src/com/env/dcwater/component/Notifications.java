package com.env.dcwater.component;
import android.app.NotificationManager;

public class Notifications {
	
	public static final String TAG_STRING = "Notifications";
	
	private NotificationManager nfm;
	private static Notifications Instance = null;
	
	private Notifications (){
		
	}
	
	public static Notifications getInstance() {
		if(Instance == null){
			Instance = new Notifications();
		}
		return Instance;
	}
}
