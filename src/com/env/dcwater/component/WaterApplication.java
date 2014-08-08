package com.env.dcwater.component;
import java.io.File;
import java.io.IOException;
import com.env.dcwater.util.SystemMethod;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;

/**
 * 设置系统的常量
 * 
 * @author sk
 * 
 */
public class WaterApplication extends Application {

	private File cache, files,logs;

	public static final String PACKAGE_STRING = "com.env.dcwater";

	public static final String CACHE_PATH_STRING = "cache";

	public static final String ROOT_PATH_STRING = "EnvDCWater";
	
	public static final String ROOT_PATROL_PATH_STRING = "EnvEasyPatrol";
	
	public static final String DB_PATROL_PATH_STRING = "databases";

	public static final String FILES_PATH_STRING = "Files";

	public static final String PICTURE_PATH_STRING = "Picture";
	
	public static final String LOG_PATH_STRING = "Log";

	public static final String TAG_STRING = "DCWaterApp";

	public static final String PREFERENCE_STRING = "EnvDCWater";

	public static final String PREFERENCE_FIRSTRUN_STRING = "First";

	public static final String PREFERENCE_WELCOME_STRING = "Welcome";

	public static final String PREFERENCE_ISLOGIN_STRING = "IsLogin";

	public static final String PREFERENCE_USERNAME_STRING = "UserName";

	public static final String PREFERENCE_USERPSW_STRING = "UserPassword";

	public static final String PREFERENCE_USERROLE_STRING = "UserRole";

	public static final String PREFERENCE_USERID_STRING = "UserID";

	public static final String PREFERENCE_PLANTID_STRING = "PlantID";

	public static final String PREFERENCE_PLANTNAME_STRING = "PlantName";

	public static final String PREFERENCE_PLANTTYPE_STRING = "PlantType";

	public static final String PREFERENCE_REALNAME_STRING = "RealUserName";

	public static final String PREFERENCE_ACTSTATE_STRING = "AccountState";

	public static final String PREFERENCE_POSITIONID_STRING = "PositionID";

	public static final String PREFERENCE_POSITIONNAME_STRING = "PositionName";

	public static final int StartupStayTime = 1000; // 登陆界面的延迟

	private SharedPreferences sp;

	private Editor ed;
	
	private Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		
		//将应用程序使用自定义的错误处理
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(context);
		
		
		sp = getSharedPreferences(PREFERENCE_STRING, Context.MODE_PRIVATE);
		ed = sp.edit();
		if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			cache = new File(SystemMethod.getDownloadPngFolderPath());
			files = new File(SystemMethod.getDownloadFileFolderPath());
			logs = new File(SystemMethod.getLogFileFolderPath());
			if (!cache.exists()) {
				cache.mkdirs();
			}
			if (!files.exists()) {
				files.mkdirs();
			}
			if(!logs.exists()){
				logs.mkdirs();
			}
		}
		if (sp.getBoolean(PREFERENCE_FIRSTRUN_STRING, true)) {
			String path = SystemMethod.getInternalDataBasePath(context);
			try {
				SystemMethod.copyDataBase(path, context);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ed.putBoolean(PREFERENCE_FIRSTRUN_STRING, false);
		ed.commit();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
