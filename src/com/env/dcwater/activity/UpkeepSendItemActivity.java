package com.env.dcwater.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.fragment.DateTimePickerView;
import com.env.dcwater.util.SystemMethod;

public class UpkeepSendItemActivity extends NfcActivity implements OnClickListener{

	private ActionBar mActionBar;
	private Intent receivedIntent;
	private HashMap<String, String> receivedData;
	private TextView tvDeviceName,tvInstallPos,tvMTPos,tvMTDetail,tvMTPeriod,tvMTNext,tvSendTime,tvSendPerson,tvNeedFinishTime,tvTaskDetail;
	private Button submit;
	private TableRow trNeedFinishTime,trTaskDetail;
	private Date date;
	private DateTimePickerView dateTimePickerView;
	private boolean canUpdate;
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
		date = new Date();
		canUpdate = receivedData.get("CanUpdate").equals("true")?true:false;
	}
	
	private void iniActionBar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		mActionBar.setTitle("派发工单");
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
		trNeedFinishTime.setOnClickListener(canUpdate?this:null);
		
		trTaskDetail = (TableRow)findViewById(R.id.activity_upkeepsenditem_taskdetail_tr);
		trTaskDetail.setOnClickListener(canUpdate?this:null);
		
		submit = (Button)findViewById(R.id.activity_upkeepsenditem_send);
		submit.setOnClickListener(canUpdate?this:null);
		submit.setVisibility(canUpdate?View.VISIBLE:View.GONE);
		
		if(!canUpdate){
			tvNeedFinishTime.setCompoundDrawables(null, null, null, null);
			tvTaskDetail.setCompoundDrawables(null, null, null, null);
		}
		
		setViewData();
		
	}
	
	private void setViewData(){
		tvDeviceName.setText(receivedData.get("DeviceName"));
		tvInstallPos.setText(receivedData.get("StructureName"));
		tvMTPos.setText(receivedData.get("MaintainPosition"));
		tvMTDetail.setText(receivedData.get("MaintainSpecification"));
		tvMTPeriod.setText(receivedData.get("MaintainPeriod"));
		tvMTNext.setText(receivedData.get("Maintaintimenext"));
		tvSendTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING, Locale.CHINA).format(date));
		tvSendPerson.setText(SystemParams.getInstance().getLoggedUserInfo().get("RealUserName"));
		tvNeedFinishTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING, Locale.CHINA).format(date));
		tvTaskDetail.setText("");
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
			tvTaskDetail.setText(temp.get("Value"));
			if(!temp.get("Value").toString().trim().equals("")){
				tvTaskDetail.setCompoundDrawables(null, null, null, null);
			}
		}
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
		setResult(RESULT_CANCELED);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_upkeepsenditem_send:
			break;
		case R.id.activity_upkeepsenditem_needfinishtime_tr:
			if(dateTimePickerView==null){
				dateTimePickerView = new DateTimePickerView(UpkeepSendItemActivity.this);
			}
			dateTimePickerView.setButtonClickEvent(new OnClickListener() {
				@Override
				public void onClick(View v) {
					date = dateTimePickerView.getSelectedDate();
					tvNeedFinishTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(date));
					dateTimePickerView.dismiss();
				}
			}, new OnClickListener() {
				@Override
				public void onClick(View v) {
					dateTimePickerView.dismiss();
				}
			}, new OnClickListener() {
				@Override
				public void onClick(View v) {
					Calendar calendar = Calendar.getInstance(Locale.CHINA);
					calendar.setTime(date);
					dateTimePickerView.iniWheelView(calendar);
				}
			});
			Calendar fualtTimeCL = Calendar.getInstance(Locale.CHINA);
			fualtTimeCL.setTime(date);
			dateTimePickerView.iniWheelView(fualtTimeCL);
			dateTimePickerView.showAtLocation(findViewById(R.id.activity_upkeepsenditem_main), Gravity.BOTTOM, 0, 0);
			tvNeedFinishTime.setCompoundDrawables(null, null, null, null);
			break;
		case R.id.activity_upkeepsenditem_taskdetail_tr:
			HashMap<String, String> taskdetail= new HashMap<String, String>();
			taskdetail.put("Name", "保养要求");
			taskdetail.put("Key", "TaskDetail");
			taskdetail.put("Value", tvTaskDetail.getText().toString());
			startDataInputActivity(taskdetail);
			break;
		}
	}
}
