package com.env.dcwater.activity;
import java.util.HashMap;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.util.SystemMethod;

public class UserInformationAcivity extends NfcActivity implements OnClickListener{
	private ActionBar mActionBar;
	private HashMap<String, String> user;
	private TextView acc,name,role,plant;
	private ImageView rolePic;
	private Button changePsw,logout;
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
		user = SystemParams.getInstance().getLoggedUserInfo(getApplicationContext());
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
		rolePic = (ImageView)findViewById(R.id.activity_userinfor_pic);
		rolePic.setOnDragListener(new OnDragListener() {
			@Override
			public boolean onDrag(View v, DragEvent event) {
				return true;
			}
		});
		changePsw = (Button)findViewById(R.id.activity_userinfor_changepsw);
		logout = (Button)findViewById(R.id.activity_userinfor_logout);
		
		rolePic.setOnClickListener(this);
		changePsw.setOnClickListener(this);
		logout.setOnClickListener(this);
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
		finish();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_userinfor_pic:
			SystemMethod.startBigImageActivity(UserInformationAcivity.this, "");
			break;
		case R.id.activity_userinfor_changepsw:
			break;
		case R.id.activity_userinfor_logout:
			SystemMethod.logOut(UserInformationAcivity.this);
			break;
		}
		
	}
}
