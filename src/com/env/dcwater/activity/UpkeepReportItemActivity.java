package com.env.dcwater.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.component.ThreadPool.UpkeepTaskUpdate;
import com.env.dcwater.javabean.EnumList;
import com.env.dcwater.javabean.EnumList.UpkeepHistoryState;
import com.env.dcwater.util.DataCenterHelper;
import com.env.dcwater.util.SystemMethod;

public class UpkeepReportItemActivity extends NfcActivity implements OnClickListener{
	private ActionBar mActionBar;
	private Intent receivedIntent;
	private HashMap<String, String> receivedData;
	private Button submit;
	private TableRow trActualHour,trMTResult,trMTPerson;
	private TextView tvDeviceName,tvInstallPos,tvNeedHour,tvMTPos,tvSendTime,tvSendPerson,tvNeedFinishTime,tvTaskDetail,tvBackPerson,tvActualHour,tvMTResult,tvMTPerson;
	private boolean canUpdate;
	private TableLayout reportGroup;
	private int taskState;
	private ProgressDialog mProgressDialog;
	private AlertDialog.Builder mUpdateConfirm;
	private UpkeepTaskUpdate upkeepTaskUpdate;

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
		taskState = Integer.valueOf(receivedData.get("State").toString());
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
		
		reportGroup = (TableLayout)findViewById(R.id.activity_upkeepreportitem_reportgroup);

		trMTPerson = (TableRow)findViewById(R.id.activity_upkeepreportitem_maintainperson_tr);
		trMTPerson.setOnClickListener(canUpdate?this:null);

		trActualHour = (TableRow)findViewById(R.id.activity_upkeepreportitem_actualhour_tr);
		trActualHour.setOnClickListener(canUpdate?this:null);
		
		trMTResult = (TableRow)findViewById(R.id.activity_upkeepreportitem_maintainresult_tr);
		trMTResult.setOnClickListener(canUpdate?this:null);
		
		submit = (Button)findViewById(R.id.activity_upkeepreportitem_report);
		submit.setOnClickListener(canUpdate?this:null);
		
		
		if(!canUpdate){
			reportGroup.setVisibility(View.GONE);
			submit.setVisibility(View.GONE);
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
		
		if(taskState==UpkeepHistoryState.STATE_WAITFORSUBMIT_INT||taskState==UpkeepHistoryState.STATE_NOTAPPROVE_INT){
			tvMTPerson.setText(receivedData.get("MaintainPerson"));
			tvActualHour.setText(receivedData.get("ActualManHours"));
			tvMTResult.setText(receivedData.get("MaintainDetail"));
		}else {
			tvMTPerson.setText("");
			tvActualHour.setText("");
			tvMTResult.setText("");
		}
		tvBackPerson.setText(SystemParams.getInstance().getLoggedUserInfo().get("RealUserName"));
	}
	
	private void startDataInputActivity(HashMap<String, String> data){
		Intent intent = new Intent(this,DataInputActivity.class);
		intent.putExtra("data", data);
		startActivityForResult(intent, 0);
	}
	
	/**
	 * 获取数据时，弹出进度对话框
	 * @param cancelable 是否能被取消的操作
	 */
	private void showProgressDialog(boolean cancelable){
		if(mProgressDialog==null){
			mProgressDialog = new ProgressDialog(UpkeepReportItemActivity.this);
			mProgressDialog.setTitle("提交中");
			mProgressDialog.setMessage("正在向服务器提交，请稍后");
			mProgressDialog.setCanceledOnTouchOutside(false);
		}
		mProgressDialog.setCancelable(cancelable);
		mProgressDialog.show();
	}
	
	/**
	 * 取消时，退出对话框
	 */
	private void hideProgressDialog(){
		if(mProgressDialog!=null){
			mProgressDialog.cancel();
		}
	}
	
	private void upkeepUpdate() throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("MaintainPlanID", receivedData.get("MaintainPlanID").toString());
		jsonObject.put("MaintainTaskID",  receivedData.get("MaintainTaskID").toString());
		jsonObject.put("MaintainPerson",  tvMTPerson.getText().toString());
		jsonObject.put("CheckPersonID",  SystemParams.getInstance().getLoggedUserInfo().get("UserID"));
		jsonObject.put("ActualManHours",  tvActualHour.getText().toString());
		jsonObject.put("MaintainDetail",  tvMTResult.getText().toString());
		jsonObject.put("CheckTime",  new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING, Locale.CHINA).format(new Date()));
		upkeepTaskUpdate = new UpkeepTaskUpdate(jsonObject) {
			@Override
			public void onPreExecute() {
				showProgressDialog(false);
			}
			@Override
			public void onPostExecute(String result) {
				if(result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					Toast.makeText(UpkeepReportItemActivity.this, "提交失败,请检查您的网络设置", Toast.LENGTH_SHORT).show();
				}else {
					try {
						JSONObject jsonObject = new JSONObject(result);
						int code = jsonObject.getInt("d");
						switch (code) {
						case EnumList.DataCenterResult.CODE_SUCCESS:
							Toast.makeText(UpkeepReportItemActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
							setResult(Activity.RESULT_OK);
							finish();
							break;
						case EnumList.DataCenterResult.CODE_SERVERERRO:
							Toast.makeText(UpkeepReportItemActivity.this, "服务器数据更新失败", Toast.LENGTH_SHORT).show();
							break;
						case EnumList.DataCenterResult.CODE_OPERATIONERRO:
							Toast.makeText(UpkeepReportItemActivity.this, "工单状态已发生改变，您无权更新", Toast.LENGTH_SHORT).show();
							break;
						case EnumList.DataCenterResult.CODE_OTHERERRO:
							Toast.makeText(UpkeepReportItemActivity.this, "服务器未知错误", Toast.LENGTH_SHORT).show();
							break;
						}
					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(UpkeepReportItemActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
					}
				}
				hideProgressDialog();
			}
		};
		upkeepTaskUpdate.execute(UpkeepTaskUpdate.METHOD_REPORT_STRING);
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
				tvMTResult.setText(value);
				if(!value.isEmpty()){
					tvMTResult.setCompoundDrawables(null, null, null, null);
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
			mtresult.put("Value", tvMTResult.getText().toString());
			startDataInputActivity(mtresult);
			break;
		case  R.id.activity_upkeepreportitem_report:
			if(mUpdateConfirm==null){
				mUpdateConfirm = new AlertDialog.Builder(UpkeepReportItemActivity.this);
			}
			mUpdateConfirm.setTitle("确认").setMessage("确认提交吗？");
			mUpdateConfirm.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						upkeepUpdate();
					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(UpkeepReportItemActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
					}
				}
			}).setNegativeButton("取消", null);
			mUpdateConfirm.create();
			mUpdateConfirm.show();
			break;
		}
	}
	
	
}
