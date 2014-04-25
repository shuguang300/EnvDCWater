package com.env.dcwater.activity;

import java.util.HashMap;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.util.SystemMethod;

/**
 * @author sk
 * 每个保养工单的详细情况
 */
public class UpkeepHistoryItemActivity extends NfcActivity {
	
	private HashMap<String, String> receivedData;
	private Intent receivedIntent;
	private ActionBar mActionBar;
	private TextView tvDeviceName,tvInstallPos,tvMTPos,tvMTDetail,tvSendTime,tvSendPerson,tvNeedHour,tvNeedFinishTime,tvMTPerson,tvBackPerson,tvActualHour,tvMTResult,tvApprovePerson,tvApproveTime,tvDDOpinion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upkeephistoryitem);
		iniData();
		iniActionBar();
		iniView();
		
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void iniData(){
		receivedIntent = getIntent();
		receivedData = (HashMap<String, String>)receivedIntent.getSerializableExtra("data");
	}
	
	/**
	 * 
	 */
	private void iniActionBar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		mActionBar.setTitle(receivedData.get("MaintainTaskSN")+"详情");
	}
	
	/**
	 * 
	 */
	private void iniView(){
		tvDeviceName = (TextView)findViewById(R.id.activity_upkeephistoryitem_devicename);
		tvInstallPos = (TextView)findViewById(R.id.activity_upkeephistoryitem_installpos);
		tvMTPos = (TextView)findViewById(R.id.activity_upkeephistoryitem_maintainpos);
		tvMTDetail = (TextView)findViewById(R.id.activity_upkeephistoryitem_maintaindetail);
		tvSendTime = (TextView)findViewById(R.id.activity_upkeephistoryitem_sendtime);
		tvSendPerson = (TextView)findViewById(R.id.activity_upkeephistoryitem_sendperson);
		tvNeedHour = (TextView)findViewById(R.id.activity_upkeephistoryitem_needhour);
		tvNeedFinishTime = (TextView)findViewById(R.id.activity_upkeephistoryitem_needfinishtime);
		tvMTPerson = (TextView)findViewById(R.id.activity_upkeephistoryitem_maintainperson);
		tvBackPerson = (TextView)findViewById(R.id.activity_upkeephistoryitem_backperson);
		tvActualHour = (TextView)findViewById(R.id.activity_upkeephistoryitem_actualhour);
		tvMTResult = (TextView)findViewById(R.id.activity_upkeephistoryitem_maintainresult);
		tvApprovePerson = (TextView)findViewById(R.id.activity_upkeephistoryitem_approveperson);
		tvApproveTime = (TextView)findViewById(R.id.activity_upkeephistoryitem_approvetime);
		tvDDOpinion = (TextView)findViewById(R.id.activity_upkeephistoryitem_ddopinion);
		setViewData();
	}
	
	/**
	 * 
	 */
	private void setViewData(){
		tvDeviceName.setText(receivedData.get("DeviceName"));
		tvInstallPos.setText(receivedData.get("StructureName"));
		tvMTPos.setText(receivedData.get("MaintainPosition"));
		tvMTDetail.setText(receivedData.get("TaskDetail"));
		tvSendTime.setText(receivedData.get("CreateTime"));
		tvSendPerson.setText(receivedData.get("CreatePerson"));
		tvNeedHour.setText(receivedData.get("RequiredManHours"));
		tvNeedFinishTime.setText(receivedData.get("NeedComplete"));
		tvMTPerson.setText(receivedData.get("MaintainPerson"));
		tvBackPerson.setText(receivedData.get("CheckPerson"));
		tvActualHour.setText(receivedData.get("ActualManHours"));
		tvMTResult.setText(receivedData.get("MaintainDetail"));
		tvApprovePerson.setText(receivedData.get("ApprovePerson"));
		tvApproveTime.setText(receivedData.get("ApproveTime"));
		tvDDOpinion.setText(receivedData.get("DDOpinion"));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
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
	
	
}
