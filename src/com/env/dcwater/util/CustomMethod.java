package com.env.dcwater.util;

import android.content.Context;
import android.content.res.Resources;

/**
 * @author Administrator
 *
 */
public class CustomMethod {
	
//	private static CustomMethod Instance = new CustomMethod();
//	private CustomMethod (){
//	}
//	
//	public static CustomMethod getInstance(Context context){
//		if(Instance ==null){
//			Instance = new CustomMethod();
//		}
//		return Instance;
//	}
//	
	/**
	 * @param context
	 * @return actionbartitlebar textview ID
	 */
	public static int getActionbarTitleTextViewID(Context context){
		int titleID = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		return titleID;
	}
	
}
