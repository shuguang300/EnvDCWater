package com.env.dcwater.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;

public class LoginActivity extends NfcActivity implements OnClickListener{
	private Button loginButton;
	private Intent userRightIntent;
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
	
	private void ini(){
		loginButton = (Button)findViewById(R.id.activity_login_submit);
		loginButton.setOnClickListener(this);
	}
	
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
