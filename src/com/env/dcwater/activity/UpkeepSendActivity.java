package com.env.dcwater.activity;

import android.app.ActionBar;
import android.os.Bundle;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.util.SystemMethod;

public class UpkeepSendActivity extends NfcActivity{
	private ActionBar mActionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upkeepsend);
		iniData();
		iniActionBar();
	}
	
	private void iniData(){
		
	}	
	
	private void iniActionBar() {
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		mActionBar.setTitle("派发工单");
	}
	
	
}
