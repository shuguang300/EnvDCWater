package com.env.dcwater.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import com.env.dcwater.R;
import com.env.dcwater.component.DCWaterApp;
import com.env.dcwater.component.NfcActivity;

/**
 * 初始化界面
 * @author sk
 */
public class StartupActivity extends NfcActivity{
	
	public static final String TAG_STRING = "StartupActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_startup);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				 startLogin();
			}
		}, DCWaterApp.StartupStayTime);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	};
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		finish();
	};
	
	/**
	 *  登陆
	 */
	private void startLogin() {
		Intent login = new Intent(StartupActivity.this, LoginActivity.class);
		startActivity(login);
		finish();
	}
}
