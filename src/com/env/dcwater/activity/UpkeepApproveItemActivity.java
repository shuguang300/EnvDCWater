package com.env.dcwater.activity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.component.ThreadPool.UpkeepTaskUpdate;
import com.env.dcwater.javabean.EnumList;
import com.env.dcwater.util.DataCenterHelper;
import com.env.dcwater.util.SystemMethod;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class UpkeepApproveItemActivity extends NfcActivity implements OnClickListener{
	private HashMap<String, String> receivedData;
	private Intent receivedIntent;
	private ActionBar mActionBar;
	private TextView tvDeviceName,tvInstallPos,tvMTPos,tvTaskDetail,tvSendTime,tvSendPerson,tvNeedHour,tvNeedFinishTime,tvMTPerson,tvBackPerson,tvActualHour,tvMTResult,tvApprovePerson,tvApproveTime,tvDDOpinion;
	private Switch tvApproveSwitch;
	private TableRow trDDOpinion;
	private Button submit;
	private boolean canUpdate;
	private TableLayout approveGroup;
	private UpkeepTaskUpdate upkeepTaskUpdate;
	private AlertDialog.Builder mUpdateConfirm;
	private ProgressDialog mProgressDialog;
	
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
		
		approveGroup = (TableLayout)findViewById(R.id.activity_upkeepapproveitem_approvegroup);
		
		trDDOpinion = (TableRow)findViewById(R.id.activity_upkeepapproveitem_ddopinion_tr);
		trDDOpinion.setOnClickListener(canUpdate?this:null);
		
		submit = (Button)findViewById(R.id.activity_upkeepapproveitem_approve);
		submit.setOnClickListener(canUpdate?this:null);
		
		if(!canUpdate){
			approveGroup.setVisibility(View.GONE);
			submit.setVisibility(View.GONE);
		}
		
		tvApproveSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				tvApproveSwitch.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
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
	
	
	private void upkeepUpdate() throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("MaintainPlanID", receivedData.get("MaintainPlanID").toString());
		jsonObject.put("MaintainTaskID",  receivedData.get("MaintainTaskID").toString());
		jsonObject.put("ApprovePersonID",  SystemParams.getInstance().getLoggedUserInfo().get("UserID"));
		jsonObject.put("ApproveTime",  tvApproveTime.getText().toString());
		jsonObject.put("DDOpinion",  tvDDOpinion.getText().toString());
		if(tvApproveSwitch.isChecked()){
			jsonObject.put("CheckTime",  receivedData.get("CheckTime").toString());
		}
		upkeepTaskUpdate = new UpkeepTaskUpdate(jsonObject) {
			@Override
			public void onPreExecute() {
				showProgressDialog(false);
			}
			@Override
			public void onPostExecute(String result) {
				if(result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					Toast.makeText(UpkeepApproveItemActivity.this, "提交失败,请检查您的网络设置", Toast.LENGTH_SHORT).show();
				}else {
					try {
						JSONObject jsonObject = new JSONObject(result);
						int code = jsonObject.getInt("d");
						switch (code) {
						case EnumList.DataCenterResult.CODE_SUCCESS:
							Toast.makeText(UpkeepApproveItemActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
							setResult(Activity.RESULT_OK);
							finish();
							break;
						case EnumList.DataCenterResult.CODE_SERVERERRO:
							Toast.makeText(UpkeepApproveItemActivity.this, "服务器数据更新失败", Toast.LENGTH_SHORT).show();
							break;
						case EnumList.DataCenterResult.CODE_OPERATIONERRO:
							Toast.makeText(UpkeepApproveItemActivity.this, "工单状态已发生改变，您无权更新", Toast.LENGTH_SHORT).show();
							break;
						case EnumList.DataCenterResult.CODE_OTHERERRO:
							Toast.makeText(UpkeepApproveItemActivity.this, "服务器未知错误", Toast.LENGTH_SHORT).show();
							break;
						}
					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(UpkeepApproveItemActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
					}
				}
				hideProgressDialog();
			}
		};
		if(tvApproveSwitch.isChecked()){
			upkeepTaskUpdate.execute(UpkeepTaskUpdate.METHOD_APPROVE_STRING);
		}else {
			upkeepTaskUpdate.execute(UpkeepTaskUpdate.METHOD_NOTAPPROVE_STRING);
		}
		
	}
	
	/**
	 * 获取数据时，弹出进度对话框
	 * @param cancelable 是否能被取消的操作
	 */
	private void showProgressDialog(boolean cancelable){
		if(mProgressDialog==null){
			mProgressDialog = new ProgressDialog(UpkeepApproveItemActivity.this);
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
			if(mUpdateConfirm==null){
				mUpdateConfirm = new AlertDialog.Builder(UpkeepApproveItemActivity.this);
			}
			mUpdateConfirm.setTitle("确认").setMessage("确认提交吗？");
			mUpdateConfirm.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						upkeepUpdate();
					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(UpkeepApproveItemActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
					}
				}
			}).setNegativeButton("取消", null);
			mUpdateConfirm.create();
			mUpdateConfirm.show();
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
