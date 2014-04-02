package com.env.dcwater.util;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

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
	
	/**
	 * 获取当前位置信息
	 * @param context
	 */
	public static Location getLocalInfo(Context context,long timeSpan,LocationListener listener){
		LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); //精度要求：ACCURACY_FINE(高)ACCURACY_COARSE(低)
        criteria.setAltitudeRequired(false);              // 不要求海拔信息
        criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH); //方位信息的精度要求：ACCURACY_HIGH(高)ACCURACY_LOW(低)
        criteria.setBearingRequired(false);             // 不要求方位信息
        criteria.setCostAllowed(true);                     // 是否允许付费
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 对电量的要求  (HIGH、MEDIUM)
        String provider=locationManager.getBestProvider(criteria,true);//根据criteria的条件获取最合适的provider。第二个参数是指是否只返回当前处于激活状态的provider。
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, timeSpan, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timeSpan, 0, listener);
        Location location = locationManager.getLastKnownLocation(provider);
        return location;
	}
	
	/**
	 * 获取当前位置信息
	 * @param context
	 */
	public static Location getLocalInfo(Context context,String provider,long timeSpan,LocationListener listener){
		LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(provider, timeSpan, 0, listener);
		Location location = locationManager.getLastKnownLocation(provider);
		return location;
	}
	
	/**
	 * GPS是否打开
	 * @param context
	 * @return
	 */
	public static boolean isGPSEnable(Context context){
		LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return true;
		}else {
			return false;
		}
	}
	
//	/**
//	 * 输入法是否弹出
//	 * @param window
//	 * @return
//	 */
//	public static boolean isSoftInputShow(Window window){
//		if(window.getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED){
//			return true;
//		}else {
//			return false;
//		}
//	}
//	
	/**
	 * 隐藏输入法
	 * @param context
	 */
	public static void hideSoftInput(Context context){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**
	 * 打开GPS设置界面
	 * @param context
	 */
	public static void startGPSSettings(Context context){
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		((Activity)context).startActivityForResult(intent, 0);
	}
	
	
	
}
