package com.env.dcwater.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;

/**
 * 登录窗口
 * @author sk
 */
public class LoginActivity extends NfcActivity implements OnClickListener{
	
	public static final String TAG_STRING = "LoginActivity";
	
	private Button loginButton;
	private Intent userRightIntent;
	private EditText accountView,passwordView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ini();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}
	
	/**
	 * 初始化控件，设置控件的点击事件和数据
	 */
	private void ini(){
		loginButton = (Button)findViewById(R.id.activity_login_submit);
		loginButton.setOnClickListener(this);
		accountView = (EditText)findViewById(R.id.activity_login_account);
		passwordView = (EditText)findViewById(R.id.activity_login_password);
	}
	
	/**
	 * 登录方法 登录到MainActivity界面
	 */
	private void startLogin(){
		userRightIntent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(userRightIntent);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_login_submit:
			startLogin();
			break;

		default:
			break;
		}
	}
	
	
}
