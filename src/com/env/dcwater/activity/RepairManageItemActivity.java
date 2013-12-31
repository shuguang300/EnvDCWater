package com.env.dcwater.activity;

import java.util.HashMap;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;

public class RepairManageItemActivity extends NfcActivity{
	private ActionBar mActionBar;
	private HashMap<String, String> receivedData;
	private Intent receivedIntent;
	private TextView mStateTextView;
	private int mRequestCode;
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repairmanageitem);
		iniData();
		iniActionbar();
		findAndIniView();
	}
	
	private void iniActionbar(){
		mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		if(mRequestCode==RepairManageActivity.REPAIRMANAGE_ADD_INTEGER){
			mActionBar.setTitle("新增");
		}else {
			mActionBar.setTitle(receivedData.get("ID"));
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void iniData(){
		receivedIntent = getIntent();
		mRequestCode = receivedIntent.getExtras().getInt("RequestCode");
		switch (mRequestCode) {
		case RepairManageActivity.REPAIRMANAGE_ADD_INTEGER:
			break;
		case RepairManageActivity.REPAIRMANAGE_UPDATE_INTEGER:
			receivedData = (HashMap<String, String>)receivedIntent.getSerializableExtra("Data");
			break;
		case RepairManageActivity.REPAIRMANAGE_DETAIL_INTEGER:
			receivedData = (HashMap<String, String>)receivedIntent.getSerializableExtra("Data");
			break;
		}
	}
	
	private void findAndIniView(){
		mStateTextView = (TextView)findViewById(R.id.activity_repairmanageitem_state);
		
		setView(mRequestCode);
	}
	
	private void setView( int code){
		switch (code) {
		case RepairManageActivity.REPAIRMANAGE_ADD_INTEGER:
			mStateTextView.setText("新增模式");
			break;
		case RepairManageActivity.REPAIRMANAGE_UPDATE_INTEGER:
			mStateTextView.setText("编辑模式");
			break;
		case RepairManageActivity.REPAIRMANAGE_DETAIL_INTEGER:
			mStateTextView.setText("浏览模式");
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_CANCELED);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
