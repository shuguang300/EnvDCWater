package com.env.dcwater.activity;
import java.util.HashMap;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.util.SystemMethod;

public class UserInformationAcivity extends NfcActivity {
	private ActionBar mActionBar;
	private HashMap<String, String> user;
	private TextView acc,name,role,plant;
	public static final String ACTION_STRING = "com.env.dcwater.activity.UserInformationAcivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinformation);
		iniData();
		iniActionBar();
		iniView();
		setViewData();
	}
	private void iniData(){
		user = SystemParams.getInstance().getLoggedUserInfo();
	}
	private void iniActionBar() {
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		mActionBar.setTitle("个人信息");
	}
	
	private void iniView(){
		acc = (TextView)findViewById(R.id.activity_userinfor_acc);
		name = (TextView)findViewById(R.id.activity_userinfor_name);
		role = (TextView)findViewById(R.id.activity_userinfor_role);
		plant = (TextView)findViewById(R.id.activity_userinfor_plant);
	}
	
	private void setViewData(){
		acc.setText(user.get("UserName"));
		name.setText(user.get("RealUserName"));
		role.setText(user.get("PositionName"));
		plant.setText(user.get("PlantName"));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home :
			onBackPressed();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
