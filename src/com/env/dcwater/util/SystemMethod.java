package com.env.dcwater.util;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 一个用于存储 android系统常用方法 的类
 * @author sk
 *
 */
public class SystemMethod {
	public static final String TAG_STRING = "CustomMethod";
	/**
	 * 无网络连接
	 */
	public static final int NONETWORK_INTEGER = 0;
	/**
	 * 2g/3g网络连接
	 */
	public static final int MOBILENETWORK_INTEGER = 1;
	/**
	 * wifi网络连接
	 */
	public static final int WIFINETWORK_INTEGER = 2;
	
	/**
	 * @param context
	 * @return actionbartitlebar textview ID
	 */
	public static int getActionbarTitleTextViewID(Context context){
		int titleID = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		return titleID;
	}
	
	/**
	 * @param context
	 * @return  返回当前网络连接的种类
	 */
	public static int getVpnType(Context context){
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if(ni == null) return NONETWORK_INTEGER;
		else {
			if(ni.getType() == ConnectivityManager.TYPE_MOBILE)return MOBILENETWORK_INTEGER;
			else if (ni.getType() == ConnectivityManager.TYPE_WIFI)return WIFINETWORK_INTEGER;
			else return NONETWORK_INTEGER;
		}
	} 
	
	
	
}
