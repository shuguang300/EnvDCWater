package com.env.dcwater.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.util.SystemMethod;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;

public class UpkeepApproveItemActivity extends NfcActivity implements OnClickListener{
	private HashMap<String, String> receivedData;
	private Intent receivedIntent;
	private ActionBar mActionBar;
	private TextView tvDeviceName,tvInstallPos,tvMTPos,tvTaskDetail,tvSendTime,tvSendPerson,tvNeedHour,tvNeedFinishTime,tvMTPerson,tvBackPerson,tvActualHour,tvMTResult,tvApprovePerson,tvApproveTime,tvDDOpinion;
	private Switch tvApproveSwitch;
	private TableRow trDDOpinion;
	private Button submit;
	private boolean canUpdate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upkeepapproveitem);
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
		canUpdate = receivedData.get("CanUpdate").equals("true")?true:false;
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
		tvDeviceName = (TextView)findViewById(R.id.activity_upkeepapproveitem_devicename);
		tvInstallPos = (TextView)findViewById(R.id.activity_upkeepapproveitem_installpos);
		tvMTPos = (TextView)findViewById(R.id.activity_upkeepapproveitem_maintainpos);
		tvTaskDetail = (TextView)findViewById(R.id.activity_upkeepapproveitem_taskdetail);
		
		tvSendTime = (TextView)findViewById(R.id.activity_upkeepapproveitem_sendtime);
		tvSendPerson = (TextView)findViewById(R.id.activity_upkeepapproveitem_sendperson);
		tvNeedHour = (TextView)findViewById(R.id.activity_upkeepapproveitem_needhour);
		tvNeedFinishTime = (TextView)findViewById(R.id.activity_upkeepapproveitem_needfinishtime);
		
		tvMTPerson = (TextView)findViewById(R.id.activity_upkeepapproveitem_maintainperson);
		tvBackPerson = (TextView)findViewById(R.id.activity_upkeepapproveitem_backperson);
		tvActualHour = (TextView)findViewById(R.id.activity_upkeepapproveitem_actualhour);
		tvMTResult = (TextView)findViewById(R.id.activity_upkeepapproveitem_maintainresult);
		
		tvApproveSwitch = (Switch)findViewById(R.id.activity_upkeepapproveitem_approveresult);
		tvApprovePerson = (TextView)findViewById(R.id.activity_upkeepapproveitem_approveperson);
		tvApproveTime = (TextView)findViewById(R.id.activity_upkeepapproveitem_approvetime);
		tvDDOpinion = (TextView)findViewById(R.id.activity_upkeepapproveitem_ddopinion);
		
		trDDOpinion = (TableRow)findViewById(R.id.activity_upkeepapproveitem_ddopinion_tr);
		trDDOpinion.setOnClickListener(canUpdate?this:null);
		
		submit = (Button)findViewById(R.id.activity_upkeepapproveitem_approve);
		submit.setOnClickListener(canUpdate?this:null);
		submit.setVisibility(canUpdate?View.VISIBLE:View.GONE);
		
		if(!canUpdate){
			tvDDOpinion.setCompoundDrawables(null, null, null, null);
			tvApproveSwitch.setCompoundDrawables(null, null, null, null);
			tvApproveSwitch.setEnabled(false);
		}
		
//		tvApproveSwitch.setOnDragListener(l);
		tvApproveSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				tvApproveSwitch.setCompoundDrawables(null, null, null, null);
			}
		});
		
		setViewData();
	}
	
	/**
	 * 
	 */
	private void setViewData(){
		tvDeviceName.setText(receivedData.get("DeviceName"));
		tvInstallPos.setText(receivedData.get("StructureName"));
		tvMTPos.setText(receivedData.get("MaintainPosition"));
		tvTaskDetail.setText(receivedData.get("TaskDetail"));
		
		tvSendTime.setText(receivedData.get("CreateTime"));
		tvSendPerson.setText(receivedData.get("CreatePerson"));
		tvNeedHour.setText(receivedData.get("RequiredManHours"));
		tvNeedFinishTime.setText(receivedData.get("NeedComplete"));
		
		tvMTPerson.setText(receivedData.get("MaintainPerson"));
		tvBackPerson.setText(receivedData.get("CheckPerson"));
		tvActualHour.setText(receivedData.get("ActualManHours"));
		tvMTResult.setText(receivedData.get("MaintainDetail"));
		
		tvApproveSwitch.setChecked(false);
		tvApprovePerson.setText(SystemParams.getInstance().getLoggedUserInfo().get("RealUserName"));
		tvApproveTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING, Locale.CHINA).format(new Date()));
		tvDDOpinion.setText("");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_upkeepapproveitem_ddopinion_tr:
			HashMap<String, String> data= new HashMap<String, String>();
			data.put("Name", "审核意见");
			data.put("Key", "DDOpinion");
			data.put("Value", tvDDOpinion.getText().toString());
			Intent intent = new Intent(this,DataInputActivity.class);
			intent.putExtra("data", data);
			startActivityForResult(intent, 0);
			break;
		case R.id.activity_upkeepapproveitem_approve:
			break;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			HashMap<String, String> temp = (HashMap<String, String>)data.getSerializableExtra("data");
			String value = temp.get("Value");
			String key = temp.get("Key");
			if(key.equals("DDOpinion")){
				tvDDOpinion.setText(value);
				if(!value.isEmpty()){
					tvDDOpinion.setCompoundDrawables(null, null, null, null);
				}
			}
		}
	}
}
