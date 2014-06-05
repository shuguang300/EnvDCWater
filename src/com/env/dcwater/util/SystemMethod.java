package com.env.dcwater.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.env.dcwater.R;
import com.env.dcwater.activity.LoginActivity;
import com.env.dcwater.activity.ShowBigImageActivity;
import com.env.dcwater.component.DCWaterApp;
import com.env.dcwater.component.SystemParams;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

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
	
	public static String getLocalTempPath(){
		String root = Environment.getExternalStorageDirectory().getAbsolutePath();
		String result = root+File.separator+DCWaterApp.ROOT_PATH_STRING+File.separator+DCWaterApp.CACHE_PATH_STRING;
		return result;
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
	public static void toggleSoftInput(Context context){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//		imm.hideSoftInputFromInputMethod(et.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/*隐藏软键盘*/
	public static void hideSoftInput(Context context){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm.isActive()){
			imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getApplicationWindowToken(), 0);
		}
	}
	
	/**
	 * 获取dpi值
	 * @param wm
	 * @return
	 */
	public static int getDpi (WindowManager wm){
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.densityDpi;
	}
	
	/**
	 * 压缩图片，避免oom
	 * @param pic
	 * @param width 期望的宽
	 * @param height 期望的长
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Bitmap compressBitmap (File pic,float width,float height) throws FileNotFoundException{
		Bitmap bitmap = null;
		BitmapFactory.Options ops = new BitmapFactory.Options();
		ops.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(pic.getAbsolutePath(),ops);
		float old_width = ops.outWidth;
		float old_height = ops.outHeight;
		int scale=1; 
		if(old_width>=old_height){
			scale = (int)(old_width/width);
		}else {
			scale = (int)(height/old_height);
		}
		if(scale<1)scale = 1;
		ops.inSampleSize = scale;
		ops.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(pic.getAbsolutePath(),ops);
		return bitmap;
	}
	
	/**
	 * @param show
	 * @param actionBar
	 */
	public static void setActionBarHomeButton(boolean show, ActionBar actionBar){
		actionBar.setDisplayShowHomeEnabled(show);
		actionBar.setDisplayHomeAsUpEnabled(show);
		actionBar.setDisplayShowTitleEnabled(show);
		actionBar.setHomeButtonEnabled(show);
	}
	
	/**
	 * 注销的方法
	 * @param context
	 */
	public static void logOut(final Context context){
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		adb.setTitle("系统通知").setMessage("确定注销吗？");
		adb.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Editor editor = context.getSharedPreferences(DCWaterApp.PREFERENCE_STRING, Context.MODE_PRIVATE).edit();
				editor.putBoolean(DCWaterApp.PREFERENCE_ISLOGIN_STRING, false);
				editor.commit();
				SystemParams.getInstance().setLoggedUserInfo(null);
				context.startActivity(new Intent(LoginActivity.ACTION_STRING));
				((Activity) context).overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out); 
				((Activity) context).finish();
			}
		}).setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		}).create();
		adb.show();
	}
	
	
	/**
	 * 打开GPS设置界面
	 * @param context
	 */
	public static void startGPSSettings(Context context){
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		((Activity)context).startActivityForResult(intent, 0);
	}
	
	/**
	 * 添加查看大图事件
	 * @param context
	 * @param fileName
	 */
	public static void startBigImageActivity(Context context,String fileName){
		Intent intent = new Intent(ShowBigImageActivity.ACTION_STRING);
		intent.putExtra("file", fileName);
		((Activity) context).startActivity(intent);
		((Activity) context).overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
	}
	
	/**
	 * 拷贝数据库文件到私有文件夹中
	 * @param path需要复制到的路径
	 * @throws IOException 
	 */
	public static void copyDataBase(String path,Context context) throws IOException{
		String dataBaseFolder = "/data/data/" + context.getPackageName() + "/databases";
		File dir = new File(dataBaseFolder);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		InputStream is = context.getResources().openRawResource(R.raw.envdcwater);
		File file = new File(path);
		FileOutputStream fos = new FileOutputStream(file);
		int len = 0;
		byte [] temp = new byte[4096];
		while ((len=is.read(temp))!=-1) {
			fos.write(temp, 0, len);
		}
		fos.close();
		is.close();
		System.out.println("**********************copy done*******************");
	}
	
	/**
	 * 获取存放设备技术文档的文件夹
	 * @return
	 */
	public static String getDownloadFilePath(){
		StringBuilder sb = new StringBuilder();
		sb.append(Environment.getExternalStorageDirectory().getAbsolutePath()).append(File.separator)
		.append(DCWaterApp.ROOT_PATH_STRING).append(File.separator).append(DCWaterApp.FILES_PATH_STRING);
		return sb.toString();
	}
	
	/**
	 * 获取存放设备图标的文件夹
	 * @return
	 */
	public static String getDownloadPngPath(){
		StringBuilder sb = new StringBuilder();
		sb.append(Environment.getExternalStorageDirectory().getAbsolutePath()).append(File.separator)
		.append(DCWaterApp.ROOT_PATH_STRING).append(File.separator).append(DCWaterApp.CACHE_PATH_STRING);
		return sb.toString();
	}
	
	/**
	 * 获取私有路径下的数据库文件的路径
	 * @param context
	 * @return
	 */
	public static String getInternalDataBasePath(Context context){
		StringBuilder sb = new StringBuilder();
		sb.append("/data/data/").append(context.getPackageName()).append("/databases/").append(SqliteHelper.DATABASE_NAME);
		return sb.toString();
	}
	
	/**
	 * 判断文件是否存在
	 * @param fileName
	 * @return
	 */
	public static boolean isDeviceFileExist(String fileName){
		String path = getDownloadFilePath()+"/"+fileName;
		File file = new File(path);
		if(file.exists())return true;
		else return false;
	}
	
	/**
	 * 根据文件后缀名选择打开的程序
	 * @param fileExtensions
	 */
	public static void openFileByFileExtensions(File file, String fileExtensions,Context context,int requestCode){
		Uri path = Uri.fromFile(file); 
		Intent intent = new Intent(Intent.ACTION_VIEW); 
		intent.setDataAndType(path, "application/"+fileExtensions); 
		try { 
			((Activity)context).startActivityForResult(intent, requestCode);
		}  catch (ActivityNotFoundException e) {
		    Toast.makeText(context, "没有找到能打开该文件的应用程序", Toast.LENGTH_SHORT).show();
		    e.printStackTrace(); 
		} 
	}
	
}
