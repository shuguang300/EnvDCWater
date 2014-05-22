package com.env.dcwater.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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
import com.env.dcwater.fragment.DateTimePickerView;
import com.env.dcwater.javabean.EnumList;
import com.env.dcwater.javabean.EnumList.UpkeepHistoryPlanState;
import com.env.dcwater.util.DataCenterHelper;
import com.env.dcwater.util.SystemMethod;

public class UpkeepSendItemActivity extends NfcActivity implements OnClickListener{

	private ActionBar mActionBar;
	private Intent receivedIntent;
	private HashMap<String, String> receivedData;
	private TextView tvDeviceName,tvInstallPos,tvMTPos,tvMTDetail,tvMTPeriod,tvMTNext,tvSendTime,tvSendPerson,tvNeedFinishTime,tvTaskDetail;
	private Button submit;
	private TableRow trNeedFinishTime,trTaskDetail;
	private Date finishDate,sendDate;
	private DateTimePickerView dateTimePickerView;
	private boolean canUpdate;
	private TableLayout approveGroup;
	private UpkeepTaskUpdate upkeepTaskUpdate;
	private AlertDialog.Builder mUpdateConfirm;
	private ProgressDialog mProgressDialog;
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
		finishDate = new Date();
		sendDate = new Date();
		canUpdate = receivedData.get("CanUpdate").equals("true")?true:false;
		
	}
	
	private void iniActionBar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		if(canUpdate){
			mActionBar.setTitle("派发工单");
		}else {
			mActionBar.setTitle("计划详情");
		}
		
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
		
		approveGroup = (TableLayout)findViewById(R.id.activity_upkeepsenditem_approvegroup);
		
		trNeedFinishTime = (TableRow)findViewById(R.id.activity_upkeepsenditem_needfinishtime_tr);
		trNeedFinishTime.setOnClickListener(canUpdate?this:null);
		
		trTaskDetail = (TableRow)findViewById(R.id.activity_upkeepsenditem_taskdetail_tr);
		trTaskDetail.setOnClickListener(canUpdate?this:null);
		
		submit = (Button)findViewById(R.id.activity_upkeepsenditem_send);
		submit.setOnClickListener(canUpdate?this:null);
		
		
		if(!canUpdate){
			approveGroup.setVisibility(View.GONE);
			submit.setVisibility(View.GONE);
		}
		
		setViewData();
		
	}
	
	private void setViewData(){
		tvDeviceName.setText(receivedData.get("DeviceName"));
		tvInstallPos.setText(receivedData.get("StructureName"));
		tvMTPos.setText(receivedData.get("MaintainPosition"));
		tvMTDetail.setText(receivedData.get("MaintainSpecification"));
		tvMTPeriod.setText(receivedData.get("MaintainPeriod")+"小时");
		tvMTNext.setText(receivedData.get("Maintaintimenext"));
		
		tvSendTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING, Locale.CHINA).format(sendDate));
		tvSendPerson.setText(SystemParams.getInstance().getLoggedUserInfo().get("RealUserName"));
		tvNeedFinishTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING, Locale.CHINA).format(finishDate));
		tvTaskDetail.setText("");
	}
	
	private void startDataInputActivity(HashMap<String, String> data){
		Intent intent = new Intent(this,DataInputActivity.class);
		intent.putExtra("data", data);
		startActivityForResult(intent, 0);
	}
	
	@SuppressWarnings("deprecation")
	private void upkeepUpdate() throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("MaintainPlanID", receivedData.get("MaintainPlanID").toString());
		jsonObject.put("MaintainfinishTime", new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING, Locale.CHINA).format(finishDate));
		jsonObject.put("PlantID",  receivedData.get("PlantID").toString());
		jsonObject.put("State",  UpkeepHistoryPlanState.STATE_HASBEENPLAN_INT);
		jsonObject.put("CreateTime",  new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING, Locale.CHINA).format(sendDate));
		jsonObject.put("CreatePersonID",  SystemParams.getInstance().getLoggedUserInfo().get("UserID").toString());
		jsonObject.put("TaskDetail",  tvTaskDetail.getText().toString());
		jsonObject.put("month",  sendDate.getMonth()+1);
		jsonObject.put("year",  sendDate.getYear());
		upkeepTaskUpdate = new UpkeepTaskUpdate(jsonObject) {
			@Override
			public void onPreExecute() {
				showProgressDialog(false);
			}
			@Override
			public void onPostExecute(String result) {
				if(result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					Toast.makeText(UpkeepSendItemActivity.this, "提交失败,请检查您的网络设置", Toast.LENGTH_SHORT).show();
				}else {
					try {
						JSONObject jsonObject = new JSONObject(result);
						int code = jsonObject.getInt("d");
						switch (code) {
						case EnumList.DataCenterResult.CODE_SUCCESS:
							setResult(Activity.RESULT_OK);
							finish();
							break;
						case EnumList.DataCenterResult.CODE_SERVERERRO:
							Toast.makeText(UpkeepSendItemActivity.this, "服务器数据更新失败", Toast.LENGTH_SHORT).show();
							break;
						case EnumList.DataCenterResult.CODE_OPERATIONERRO:
							Toast.makeText(UpkeepSendItemActivity.this, "工单状态已发生改变，您无权更新", Toast.LENGTH_SHORT).show();
							break;
						case EnumList.DataCenterResult.CODE_OTHERERRO:
							Toast.makeText(UpkeepSendItemActivity.this, "服务器未知错误", Toast.LENGTH_SHORT).show();
							break;
						}
					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(UpkeepSendItemActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
					}
				}
				hideProgressDialog();
			}
		};
		upkeepTaskUpdate.execute(UpkeepTaskUpdate.METHOD_SEND_STRING);
	}
	
	/**
	 * 获取数据时，弹出进度对话框
	 * @param cancelable 是否能被取消的操作
	 */
	private void showProgressDialog(boolean cancelable){
		if(mProgressDialog==null){
			mProgressDialog = new ProgressDialog(UpkeepSendItemActivity.this);
			mProgressDialog.setTitle("提交中");
			mProgressDialog.setMessage("正在向服务器提交，请稍后");
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setCancelable(cancelable);
		}
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
			if(mUpdateConfirm==null){
				mUpdateConfirm = new AlertDialog.Builder(UpkeepSendItemActivity.this);
			}
			mUpdateConfirm.setTitle("确认").setMessage("确认提交吗？");
			mUpdateConfirm.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						upkeepUpdate();
					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(UpkeepSendItemActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
					}
				}
			}).setNegativeButton("取消", null);
			mUpdateConfirm.create();
			mUpdateConfirm.show();
			break;
		case R.id.activity_upkeepsenditem_needfinishtime_tr:
			if(dateTimePickerView==null){
				dateTimePickerView = new DateTimePickerView(UpkeepSendItemActivity.this);
			}
			dateTimePickerView.setButtonClickEvent(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finishDate = dateTimePickerView.getSelectedDate();
					tvNeedFinishTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(finishDate));
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
					calendar.setTime(finishDate);
					dateTimePickerView.iniWheelView(calendar);
				}
			});
			Calendar fualtTimeCL = Calendar.getInstance(Locale.CHINA);
			fualtTimeCL.setTime(finishDate);
			dateTimePickerView.iniWheelView(fualtTimeCL);
			dateTimePickerView.showAtLocation(findViewById(R.id.activity_upkeepsenditem_main), Gravity.BOTTOM, 0, 0);
			tvNeedFinishTime.setCompoundDrawables(null, null, null, null);
			break;
		case R.id.activity_upkeepsenditem_taskdetail_tr:
			HashMap<String, String> taskdetail= new HashMap<String, String>();
			taskdetail.put("Name", "备注");
			taskdetail.put("Key", "TaskDetail");
			taskdetail.put("Value", tvTaskDetail.getText().toString());
			startDataInputActivity(taskdetail);
			break;
		}
	}
}
