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
import com.env.dcwater.javabean.EnumList;
import com.env.dcwater.util.SystemMethod;

/**
 * @author sk
 * 每个保养工单的详细情况
 */
public class UpkeepHistoryItemActivity extends NfcActivity {
	
	private HashMap<String, String> receivedData;
	private Intent receivedIntent;
	private ActionBar mActionBar;
	private TextView tvCheckTime,tvDeviceName,tvInstallPos,tvMTPos,tvMTDetail,tvSendTime,tvSendPerson,tvNeedHour,tvNeedFinishTime,tvMTPerson,tvBackPerson,tvActualHour,tvMTResult,tvApprovePerson,tvApproveTime,tvDDOpinion;
	
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
		tvCheckTime = (TextView)findViewById(R.id.activity_upkeephistoryitem_checktime);
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
		tvCheckTime.setText(receivedData.get("CheckTime"));
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
		getMenuInflater().inflate(R.menu.contextmenu_taskstate, menu);
		menu.findItem(R.id.contextmenu_taskstate_flow).setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			onBackPressed();
		} else if (itemId == R.id.contextmenu_taskstate_flow) {
			Intent flow = new Intent(MaintainTaskStateFlowAcivity.ACTION_STRING);
			flow.putExtra("data", receivedData);
			flow.putExtra("TaskType", EnumList.TaskType.TYPE_MAINTAIN_INT);
			startActivityForResult(flow, 0);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		} else if (itemId == R.id.contextmenu_taskstate_workflow) {
			Intent workflow = new Intent(TaskStateWorkFlowActivity.ACTION_STRING);
			workflow.putExtra("data", receivedData);
			workflow.putExtra("TaskType", EnumList.TaskType.TYPE_MAINTAIN_INT);
			startActivityForResult(workflow, 0);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	
	
}
