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
import com.env.dcwater.util.SystemMethod;

public class UpkeepSendItemActivity extends NfcActivity implements OnClickListener{

	private ActionBar mActionBar;
	private Intent receivedIntent;
	private HashMap<String, String> receivedData;
	private TextView tvDeviceName,tvInstallPos,tvMTPos,tvMTDetail,tvMTPeriod,tvMTNext,tvSendTime,tvSendPerson,tvNeedFinishTime,tvTaskDetail;
	private Button submit;
	private TableRow trNeedFinishTime,trTaskDetail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upkeepsenditem);
		iniData();
		iniActionBar();
		iniView();
	}
	
	@SuppressWarnings("unchecked")
	private void iniData(){
		receivedIntent = getIntent();
		receivedData = (HashMap<String, String>)receivedIntent.getSerializableExtra("data");
	}
	
	private void iniActionBar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		mActionBar.setTitle(receivedData.get("MaintainTaskSN")+"详情");
	}
	
	private void iniView(){
		tvDeviceName = (TextView)findViewById(R.id.activity_upkeepsenditem_devicename);
		tvInstallPos = (TextView)findViewById(R.id.activity_upkeepsenditem_installpos);
		tvMTPos = (TextView)findViewById(R.id.activity_upkeepsenditem_maintainpos);
		tvMTDetail = (TextView)findViewById(R.id.activity_upkeepsenditem_maintaindetail);
		tvMTPeriod = (TextView)findViewById(R.id.activity_upkeepsenditem_maintainperiod);
		tvMTNext = (TextView)findViewById(R.id.activity_upkeepsenditem_maintainnext);
		tvSendTime = (TextView)findViewById(R.id.activity_upkeepsenditem_sendtime);
		tvSendPerson = (TextView)findViewById(R.id.activity_upkeepsenditem_sendperson);
		tvNeedFinishTime = (TextView)findViewById(R.id.activity_upkeepsenditem_needfinishtime);
		tvTaskDetail = (TextView)findViewById(R.id.activity_upkeepsenditem_taskdetail);
		
		trNeedFinishTime = (TableRow)findViewById(R.id.activity_upkeepsenditem_needfinishtime_tr);
		trNeedFinishTime.setOnClickListener(this);
		
		trTaskDetail = (TableRow)findViewById(R.id.activity_upkeepsenditem_taskdetail_tr);
		trTaskDetail.setOnClickListener(this);
		
		submit = (Button)findViewById(R.id.activity_upkeepsenditem_send);
		submit.setOnClickListener(this);
		
		setViewData();
		
	}
	
	private void setViewData(){
		tvDeviceName.setText(receivedData.get("DeviceName"));
		tvInstallPos.setText(receivedData.get("StructureName"));
		tvMTPos.setText(receivedData.get("MaintainPosition"));
		tvMTDetail.setText(receivedData.get("MaintainSpecification"));
		tvMTPeriod.setText(receivedData.get("MaintainPeriod"));
		tvMTNext.setText("");
		tvSendTime.setText("");
		tvSendPerson.setText("");
		tvNeedFinishTime.setText("");
		tvTaskDetail.setText("");
	}
	
	private void startDataInputActivity(HashMap<String, String> data){
		Intent intent = new Intent(this,DataInputActivity.class);
		intent.putExtra("data", data);
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_upkeepsenditem_send:
			break;
		case R.id.activity_upkeepsenditem_needfinishtime_tr:
			break;
		case R.id.activity_upkeepsenditem_taskdetail_tr:
			HashMap<String, String> taskdetail= new HashMap<String, String>();
			taskdetail.put("Name", "保养要求");
			taskdetail.put("Key", "TaskDetail");
			taskdetail.put("Value", "");
			startDataInputActivity(taskdetail);
			break;
		}
	}
}
