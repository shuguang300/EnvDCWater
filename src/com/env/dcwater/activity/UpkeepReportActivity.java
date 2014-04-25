package com.env.dcwater.activity;
import android.app.ActionBar;
import android.os.Bundle;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.util.SystemMethod;


/**
 * @author sk
 *
 */
public class UpkeepReportActivity extends NfcActivity{
	private ActionBar mActionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upkeepreport);
		iniActionBar();
	}
	
	private void iniActionBar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		mActionBar.setTitle("填写工单");
	}
}
