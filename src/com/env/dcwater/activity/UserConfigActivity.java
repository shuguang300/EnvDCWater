package com.env.dcwater.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.fragment.NaviBarAdapter;
import com.env.dcwater.util.SystemMethod;

public class UserConfigActivity extends NfcActivity{
	private DrawerLayout drawerLayout;
	private ActionBar mActionBar;
	private ListView naviListView;
	private NaviBarAdapter naviBarAdapter;
	public static final String ACTION_STRING = "com.env.dcwater.activity.UserConfigActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userconfig);
		iniData();
		iniActionBar();
		iniView();
	}
	
	private void iniData(){
		
	}
	private void iniActionBar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		mActionBar.setTitle("设置");
	}
	private void iniView(){
		drawerLayout = (DrawerLayout)findViewById(R.id.activity_userconfig_drawlayout);
		naviListView = (ListView)findViewById(R.id.activity_userconfig_navibar);
		naviBarAdapter = new NaviBarAdapter(UserConfigActivity.this,SystemParams.getInstance().getUserRightData(),ACTION_STRING){
			@Override
			public void doNothing() {
				drawerLayout.closeDrawer(Gravity.LEFT);
			}
		};
		naviListView.setAdapter(naviBarAdapter);
		naviListView.setOnItemClickListener(naviBarAdapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home :
			if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
				drawerLayout.closeDrawer(Gravity.LEFT);
			}else {
				drawerLayout.openDrawer(Gravity.LEFT);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
			drawerLayout.closeDrawer(Gravity.LEFT);
		}else {
			super.onBackPressed();
		}
	}

}
