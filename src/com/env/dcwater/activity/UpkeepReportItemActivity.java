package com.env.dcwater.activity;

import java.util.HashMap;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.util.SystemMethod;

public class UpkeepReportItemActivity extends NfcActivity implements OnClickListener{
	private ActionBar mActionBar;
	private Intent receivedIntent;
	private HashMap<String, String> receivedData;
	private Button submit;
	private TableRow trActualHour,trMTResult,trMTPerson;
	private TextView tvDeviceName,tvInstallPos,tvNeedHour,tvMTPos,tvSendTime,tvSendPerson,tvNeedFinishTime,tvTaskDetail,tvBackPerson,tvActualHour,tvMTResult,tvMTPerson;
	private boolean canUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upkeepreportitem);
		iniData();
		iniActionBar();
		iniView();
	}
	
	@SuppressWarnings("unchecked")
	private void iniData(){
		receivedIntent = getIntent();
		receivedData = (HashMap<String, String>)receivedIntent.getSerializableExtra("data");
		canUpdate = receivedData.get("CanUpdate").equals("true")?true:false;
	}
	
	private void iniActionBar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		mActionBar.setTitle(receivedData.get("MaintainTaskSN")+"详情");
	}
	
	private void iniView(){
		tvDeviceName = (TextView)findViewById(R.id.activity_upkeepreportitem_devicename);
		tvInstallPos = (TextView)findViewById(R.id.activity_upkeepreportitem_installpos);
		tvMTPos = (TextView)findViewById(R.id.activity_upkeepreportitem_maintainpos);
		tvTaskDetail = (TextView)findViewById(R.id.activity_upkeepreportitem_taskdetail);
		
		tvSendTime = (TextView)findViewById(R.id.activity_upkeepreportitem_sendtime);
		tvSendPerson = (TextView)findViewById(R.id.activity_upkeepreportitem_sendperson);
		tvNeedHour = (TextView)findViewById(R.id.activity_upkeepreportitem_needhour);
		tvNeedFinishTime = (TextView)findViewById(R.id.activity_upkeepreportitem_needfinishtime);
		
		tvMTPerson = (TextView)findViewById(R.id.activity_upkeepreportitem_maintainperson);
		tvBackPerson = (TextView)findViewById(R.id.activity_upkeepreportitem_backperson);
		tvActualHour = (TextView)findViewById(R.id.activity_upkeepreportitem_actualhour);
		tvMTResult = (TextView)findViewById(R.id.activity_upkeepreportitem_maintainresult);

		trMTPerson = (TableRow)findViewById(R.id.activity_upkeepreportitem_maintainperson_tr);
		trMTPerson.setOnClickListener(canUpdate?this:null);

		trActualHour = (TableRow)findViewById(R.id.activity_upkeepreportitem_actualhour_tr);
		trActualHour.setOnClickListener(canUpdate?this:null);
		
		trMTResult = (TableRow)findViewById(R.id.activity_upkeepreportitem_maintainresult_tr);
		trMTResult.setOnClickListener(canUpdate?this:null);
		
		submit = (Button)findViewById(R.id.activity_upkeepreportitem_report);
		submit.setOnClickListener(canUpdate?this:null);
		submit.setVisibility(canUpdate?View.VISIBLE:View.GONE);
		
		if(!canUpdate){
			tvMTPerson.setCompoundDrawables(null, null, null, null);
			tvActualHour.setCompoundDrawables(null, null, null, null);
			tvMTResult.setCompoundDrawables(null, null, null, null);
		}
		
		setViewData();
	}
	
	private void setViewData(){
		tvDeviceName.setText(receivedData.get("DeviceName"));
		tvInstallPos.setText(receivedData.get("StructureName"));
		tvMTPos.setText(receivedData.get("MaintainPosition"));
		tvTaskDetail.setText(receivedData.get("TaskDetail"));
		
		tvSendTime.setText(receivedData.get("CreateTime"));
		tvSendPerson.setText(receivedData.get("CreatePerson"));
		tvNeedHour.setText(receivedData.get("RequiredManHours"));
		tvNeedFinishTime.setText(receivedData.get("NeedComplete"));
		
		tvMTPerson.setText("");
		tvBackPerson.setText(SystemParams.getInstance().getLoggedUserInfo().get("RealUserName"));
		tvActualHour.setText("");
		tvMTResult.setText("");
	}
	
	private void startDataInputActivity(HashMap<String, String> data){
		Intent intent = new Intent(this,DataInputActivity.class);
		intent.putExtra("data", data);
		startActivityForResult(intent, 0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			HashMap<String, String> temp = (HashMap<String, String>)data.getSerializableExtra("data");
			String value = temp.get("Value");
			String key = temp.get("Key");
			if(key.equals("MaintainPerson")){
				tvMTPerson.setText(value);
				if(!value.isEmpty()){
					tvMTPerson.setCompoundDrawables(null, null, null, null);
				}
			}else if (key.equals("ActualManHours")) {
				tvActualHour.setText(value);
				if(!value.isEmpty()){
					tvActualHour.setCompoundDrawables(null, null, null, null);
				}
			}else if (key.equals("TaskDetail")) {
				tvTaskDetail.setText(value);
				if(!value.isEmpty()){
					tvTaskDetail.setCompoundDrawables(null, null, null, null);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_upkeepreportitem_maintainperson_tr:
			HashMap<String, String> mtperson= new HashMap<String, String>();
			mtperson.put("Name", "养护人");
			mtperson.put("Key", "MaintainPerson");
			mtperson.put("Value", tvMTPerson.getText().toString());
			startDataInputActivity(mtperson);
			break;
		case R.id.activity_upkeepreportitem_actualhour_tr:
			HashMap<String, String> actualhour= new HashMap<String, String>();
			actualhour.put("Name", "实际工时");
			actualhour.put("Key", "ActualManHours");
			actualhour.put("Value", tvActualHour.getText().toString());
			startDataInputActivity(actualhour);
			break;
		case R.id.activity_upkeepreportitem_maintainresult_tr:
			HashMap<String, String> mtresult= new HashMap<String, String>();
			mtresult.put("Name", "完成情况");
			mtresult.put("Key", "TaskDetail");
			mtresult.put("Value", tvTaskDetail.getText().toString());
			startDataInputActivity(mtresult);
			break;
		case  R.id.activity_upkeepreportitem_report:
			break;
		}
		
	}
	
	
}
