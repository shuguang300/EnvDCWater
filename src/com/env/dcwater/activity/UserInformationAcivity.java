package com.env.dcwater.activity;
import java.util.HashMap;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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
		rolePic = (ImageView)findViewById(R.id.activity_userinfor_pic);
		rolePic.setOnClickListener(this);
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_userinfor_pic:
			SystemMethod.startBigImageActivity(UserInformationAcivity.this, "");
			break;
		}
		
	}
}
