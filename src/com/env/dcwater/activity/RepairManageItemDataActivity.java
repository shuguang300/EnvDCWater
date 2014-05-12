package com.env.dcwater.activity;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.fragment.DateTimePickerView;
import com.env.dcwater.javabean.EnumList;
import com.env.dcwater.util.DataCenterHelper;
import com.env.dcwater.util.SystemMethod;

/**
 * 填写工单数据的一个页面
 * @author sk
 *
 */
public class RepairManageItemDataActivity extends NfcActivity implements OnClickListener{
	private AlertDialog.Builder mUpdateConfirm;
	private Intent mIntent;
	private AlertDialog.Builder mHandleContent;
	private HashMap<String, String> repairData;
	private DateTimePickerView dateTimePickerView;
	private Date mFaultTime,mFinishTime;
	private ProgressDialog mProgressDialog;
	private ActionBar mActionBar;
	private UpdateTask mUpdateTask;
	private TableLayout taskBasicGroup,taskSendGroup,taskRepairGroup,taskDDVerifyGroup,taskPDVerifyGroup,taskPMVerifyGroup;
	private TextView etName,etFaultTime,etHandleStep,etPeople,etFaultPhenomenon,etOtherStep,etSendTime,etSender,etTimeCost,etContent,etResult,etFinishTime,etThing,etMoney,etVerifyPeople,etEquipmentOpinion,etProductionOpinion,etPlantOpinion;
	private TableRow trName,trFaultTime,trFaultPhenomenon,trHandleStep,trOtherStep,trTimeCost,trContent,trResult,trFinishTime,trThingCost,trMoneyCost,trEquipmentOpinion,trProductionOpinion,trPlantOpinion;
	private Switch swVerifyResult;
	private boolean [] handleStepSelected = {false,false,false,false,false},tempStepSelected;
	private String mOtherStep="",mHandleStep="",methodName="";
	private String [] handleStepContent = {"尝试手动启动","关闭主电源","拍下急停按钮","悬挂警示标识牌","关闭故障设备工艺段进水"};
	private Button submit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repairmanageitemdata);
		initialData();
		iniActionbar();
		initialView();
		setViewStateAndData();
	}
	
	/**
	 *   初始化数据
	 */
	@SuppressWarnings("unchecked")
	private void initialData(){
		mIntent = getIntent();
		methodName = mIntent.getStringExtra("MethodName");
		if(methodName.equals(RepairManageActivity.METHOD_ADD_STRING)){
			mFaultTime = new Date();
			repairData = new HashMap<String, String>();
			copyHandleStepSelected();
		}else {
			repairData = (HashMap<String, String>)mIntent.getSerializableExtra("data");
			if(methodName.equals(RepairManageActivity.METHOD_UPDATE_STRING)){
				String [] handle1;
				if(repairData.get("EmergencyMeasures").equals("null")){ 
					handle1 = new String [0]; 
				}else {
					handle1 = repairData.get("EmergencyMeasures").split(",");
				}
				try {
					for(int i=0;i<handle1.length;i++){
						if(handle1[i].endsWith("(an)")){ 
							//应急措施和措施放在一个字段里面了，带有（an）结尾的是措施，数字的是紧急措施
							mOtherStep = handle1[i].replace("(an)", "");
						} else {
							if(!handle1[i].equals("")){
								mHandleStep = mHandleStep + handleStepContent[Integer.parseInt(handle1[i])-1] + "\n";
								//得到初始化的 应急措施
								handleStepSelected[Integer.parseInt(handle1[i])-1] = true;
							}
						}
					}
					copyHandleStepSelected();
					mFaultTime = new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).parse(repairData.get("AccidentOccurTime").toString());
				} catch (Exception e) {
					mOtherStep = "";
					mHandleStep ="";
					mFaultTime = new Date();
				}
			}
		}
	}
	
	
	/**
	 * 初始化actionbar
	 */
	private void iniActionbar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		if(methodName.equals(RepairManageActivity.METHOD_ADD_STRING)){
			mActionBar.setTitle("新增故障上报");
		}else if (methodName.equals(RepairManageActivity.METHOD_UPDATE_STRING)) {
			mActionBar.setTitle("修改工单");
		}else if (methodName.equals(RepairManageActivity.METHOD_SENDTASK_STRING)) {
			mActionBar.setTitle("派发工单");
		}else if (methodName.equals(RepairManageActivity.METHOD_REPAIRTASK_STRING)) {
			mActionBar.setTitle("填写工单");
		}else if (methodName.equals(RepairManageActivity.METHOD_PDAPPROVE_STRING)) {
			mActionBar.setTitle("生产科长审核");
		}else if (methodName.equals(RepairManageActivity.METHOD_PMAPPROVE_STRING)) {
			mActionBar.setTitle("厂长审核");
		}else if (methodName.equals(RepairManageActivity.METHOD_DDAPPROVE_STRING)) {
			mActionBar.setTitle("设备科长审核");
		}
		
		
	}
	
	/**
	 * 寻找到控件
	 */
	private void initialView(){
		taskBasicGroup = (TableLayout)findViewById(R.id.activity_repairmanageitem_repairgroupfault);
		taskSendGroup = (TableLayout)findViewById(R.id.activity_repairmanageitem_tasksendgroup);
		taskRepairGroup = (TableLayout)findViewById(R.id.activity_repairmanageitem_taskrepairgroup);
		taskDDVerifyGroup = (TableLayout)findViewById(R.id.activity_repairmanageitem_ddverifygroup);
		taskPDVerifyGroup = (TableLayout)findViewById(R.id.activity_repairmanageitem_pdverifygroup);
		taskPMVerifyGroup = (TableLayout)findViewById(R.id.activity_repairmanageitem_pmverifygroup);
		
		
		etName = (TextView)findViewById(R.id.activity_repairmanageitem_name);
		trName = (TableRow)findViewById(R.id.activity_repairmanageitem_name_tr);
		
		etFaultTime = (TextView)findViewById(R.id.activity_repairmanageitem_faulttime);
		trFaultTime = (TableRow)findViewById(R.id.activity_repairmanageitem_faulttime_tr);
		
		etFaultPhenomenon = (TextView)findViewById(R.id.activity_repairmanageitem_faultphenomenon);
		trFaultPhenomenon = (TableRow)findViewById(R.id.activity_repairmanageitem_faultphenomenon_tr);
		
		etHandleStep = (TextView)findViewById(R.id.activity_repairmanageitem_handlestep);
		trHandleStep = (TableRow)findViewById(R.id.activity_repairmanageitem_handlestep_tr);
		
		etOtherStep = (TextView)findViewById(R.id.activity_repairmanageitem_otherstep);
		trOtherStep = (TableRow)findViewById(R.id.activity_repairmanageitem_otherstep_tr);
		
		etPeople = (TextView)findViewById(R.id.activity_repairmanageitem_taskpeople);

		etSendTime = (TextView)findViewById(R.id.activity_repairmanageitem_repairsttime);
		
		etSender = (TextView)findViewById(R.id.activity_repairmanageitem_repairpeople);
		
		trTimeCost = (TableRow)findViewById(R.id.activity_repairmanageitem_repairtimecost_tr);
		etTimeCost = (TextView)findViewById(R.id.activity_repairmanageitem_repairtimecost);
		
		trContent = (TableRow)findViewById(R.id.activity_repairmanageitem_repaircontent_tr);
		etContent = (TextView)findViewById(R.id.activity_repairmanageitem_repaircontent);
		
		trResult = (TableRow)findViewById(R.id.activity_repairmanageitem_repairresult_tr);
		etResult = (TextView)findViewById(R.id.activity_repairmanageitem_repairresult);
		
		trFinishTime = (TableRow)findViewById(R.id.activity_repairmanageitem_repairendtime_tr);
		etFinishTime = (TextView)findViewById(R.id.activity_repairmanageitem_repairendtime);
		
		trThingCost = (TableRow)findViewById(R.id.activity_repairmanageitem_repairthingcost_tr);
		etThing = (TextView)findViewById(R.id.activity_repairmanageitem_repairthingcost);
		
		trMoneyCost = (TableRow)findViewById(R.id.activity_repairmanageitem_repairmoneycost_tr);
		etMoney = (TextView)findViewById(R.id.activity_repairmanageitem_repairmoneycost);
		
		swVerifyResult = (Switch)findViewById(R.id.activity_repairmanageitem_verifyresult);
		
		etVerifyPeople = (TextView)findViewById(R.id.activity_repairmanageitem_verifypeople);
		
		trEquipmentOpinion = (TableRow)findViewById(R.id.activity_repairmanageitem_equipmentopinion_tr);
		etEquipmentOpinion = (TextView)findViewById(R.id.activity_repairmanageitem_equipmentopinion);
		
		etProductionOpinion = (TextView)findViewById(R.id.activity_repairmanageitem_productionopinion);
		trProductionOpinion = (TableRow)findViewById(R.id.activity_repairmanageitem_productionopinion_tr);
		
		trPlantOpinion = (TableRow)findViewById(R.id.activity_repairmanageitem_plantopinion_tr);
		etPlantOpinion = (TextView)findViewById(R.id.activity_repairmanageitem_plantopinion);
		
		submit = (Button)findViewById(R.id.activity_repairmanageitem_submit);
	}
	
	/**
	 * 给控制设置各种状态
	 */
	private void setViewStateAndData(){
		trName.setOnClickListener(this);
		trFaultTime.setOnClickListener(this);
		trFaultPhenomenon.setOnClickListener(this);
		trHandleStep.setOnClickListener(this);
		trOtherStep.setOnClickListener(this);
		trTimeCost.setOnClickListener(this);
		trContent.setOnClickListener(this);
		trResult.setOnClickListener(this);
		trFinishTime.setOnClickListener(this);
		trThingCost.setOnClickListener(this);
		trMoneyCost.setOnClickListener(this);
		trEquipmentOpinion.setOnClickListener(this);
		trProductionOpinion.setOnClickListener(this);
		trPlantOpinion.setOnClickListener(this);
		submit.setOnClickListener(this);
		if(methodName.equals(RepairManageActivity.METHOD_ADD_STRING)){
			taskBasicGroup.setVisibility(View.VISIBLE);
			etFaultTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(mFaultTime));
			etPeople.setText(SystemParams.getInstance().getLoggedUserInfo().get("RealUserName"));
			
		}else if (methodName.equals(RepairManageActivity.METHOD_UPDATE_STRING)) {
			taskBasicGroup.setVisibility(View.VISIBLE);
			etName.setText(repairData.get("DeviceName"));
			etFaultTime.setText(repairData.get("AccidentOccurTime"));
			etFaultPhenomenon.setText(repairData.get("AccidentDetail"));
			etPeople.setText(repairData.get("ReportPersonRealName").toString());
			etHandleStep.setText(mHandleStep);
			etOtherStep.setText(mOtherStep);
			
		}else if (methodName.equals(RepairManageActivity.METHOD_SENDTASK_STRING)) {
			taskSendGroup.setVisibility(View.VISIBLE);
			if(methodName.equals(RepairManageActivity.METHOD_SENDTASK_STRING)){
				if(repairData.get("TaskCreateTime").equals("")){
					etSendTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(new Date()));
				}else {
					etSendTime.setText(repairData.get("TaskCreateTime"));
				}
				if(repairData.get("CreatePersonRealName").equals("")){
					etSender.setText(SystemParams.getInstance().getLoggedUserInfo().get("RealUserName"));
				}else {
					etSender.setText(repairData.get("CreatePersonRealName"));
				}
			}else {
				etSendTime.setText(repairData.get("TaskCreateTime"));
				etSender.setText(repairData.get("CreatePersonRealName"));
			}
			etTimeCost.setText(repairData.get("RequiredManHours"));
			etContent.setText(repairData.get("TaskDetail"));
			
		}else if (methodName.equals(RepairManageActivity.METHOD_REPAIRTASK_STRING)) {
			taskRepairGroup.setVisibility(View.VISIBLE);
			if(repairData.get("RepairedTime").equals("")){
				etFinishTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(new Date()));
			}else {
				etFinishTime.setText(repairData.get("RepairedTime"));
			}
			etResult.setText(repairData.get("RepairDetail"));
			etThing.setText(repairData.get("AccessoryUsed"));
			etMoney.setText(repairData.get("RepairCost"));
			
		}else if (methodName.equals(RepairManageActivity.METHOD_PDAPPROVE_STRING)) {
			taskPDVerifyGroup.setVisibility(View.VISIBLE);
			etProductionOpinion.setText(repairData.get("PDOpinion"));
			
		}else if (methodName.equals(RepairManageActivity.METHOD_PMAPPROVE_STRING)) {
			taskPMVerifyGroup.setVisibility(View.VISIBLE);
			etPlantOpinion.setText(repairData.get("PMOpinion"));
			
		}else if (methodName.equals(RepairManageActivity.METHOD_DDAPPROVE_STRING)) {
			taskDDVerifyGroup.setVisibility(View.VISIBLE);
			if(repairData.get("ApprovePersonRealName").equals("")){
				etVerifyPeople.setText(SystemParams.getInstance().getLoggedUserInfo().get("RealUserName"));
			}else {
				etVerifyPeople.setText(repairData.get("ApprovePersonRealName"));
			}
			if(repairData.get("ApproveResult").equals("1")){
				swVerifyResult.setChecked(true);
			}else {
				swVerifyResult.setChecked(false);
			}
			etEquipmentOpinion.setText(repairData.get("DDOpinion"));
		}
		submit.setText(mActionBar.getTitle());
	}
	
	
	/**
	 * 创建一个缓存对象，并且把初始选择的 应急措施 存储到该对象
	 */
	private void copyHandleStepSelected(){
		tempStepSelected = new boolean [5];
		for(int i = 0;i<handleStepSelected.length;i++){
			tempStepSelected[i] = handleStepSelected[i];
		}
	}
	/**
	 * 如果选择取消，则将初始的 应急措施 重新赋值给 非缓存对象
	 */
	private void copyTempStepSelected(){
		for(int i = 0;i<tempStepSelected.length;i++){
			handleStepSelected[i] = tempStepSelected[i];
		}
	}
	
	/**
	 * 跳转 数据填报界面
	 * @param data
	 */
	private void startDataInputActivity(HashMap<String, String> data){
		Intent intent = new Intent(this,DataInputActivity.class);
		intent.putExtra("data", data);
		startActivityForResult(intent, 0);
	}
	
	/**
	 * 跳转到选取设备列表的界面
	 */
	private void gotoSelectDevice(HashMap<String, String> data){
		
	}
	
	/**
	 * 完成紧急措施和其他措施的合并
	 * @return
	 */
	private String combineEmergencyMeasures() {
		String EmergencyMeasures = "";
		for(int i = 0;i<handleStepSelected.length;i++){
			if (handleStepSelected[i]) {
				EmergencyMeasures = EmergencyMeasures + String.valueOf(i+1) + ",";
			}
		}
		if(EmergencyMeasures.equals("")&&!etOtherStep.getText().toString().equals("")){
			EmergencyMeasures = "," + etOtherStep.getText().toString()+"(an)";
		}else if (!EmergencyMeasures.equals("")&&!etOtherStep.getText().toString().equals("")) {
			EmergencyMeasures = EmergencyMeasures + etOtherStep.getText().toString()+"(an)";
		}
		return EmergencyMeasures;
	}
	
	/**
	 * 提交数据时，弹出进度对话框
	 */
	private void showProgressDialog(){
		if(mProgressDialog==null){
			mProgressDialog = new ProgressDialog(RepairManageItemDataActivity.this);
			mProgressDialog.setTitle("获取数据中");
			mProgressDialog.setMessage("正在努力加载数据，请稍后");
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setCancelable(false);
		}
		mProgressDialog.show();
	}
	
	/**
	 * 使用asynctask异步提交工单
	 */
	private void updateServerTask(String arg0){
		if(arg0.equals(RepairManageActivity.METHOD_ADD_STRING)){
			if(repairData==null){
				Toast.makeText(this, "未选择设备", Toast.LENGTH_SHORT).show();
			}else {
				mUpdateTask = new UpdateTask();
				mUpdateTask.execute(arg0);
			}
		}else {
			mUpdateTask = new UpdateTask();
			mUpdateTask.execute(arg0);
		}
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_repairmanageitem_name_tr:
			gotoSelectDevice(repairData);
			break;
		case R.id.activity_repairmanageitem_faulttime_tr:
			if(dateTimePickerView==null){
				dateTimePickerView = new DateTimePickerView(RepairManageItemDataActivity.this);
			}
			dateTimePickerView.setButtonClickEvent(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mFaultTime = dateTimePickerView.getSelectedDate();
					etFaultTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(mFaultTime));
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
					calendar.setTime(mFaultTime);
					dateTimePickerView.iniWheelView(calendar);
				}
			});
			Calendar fualtTimeCL = Calendar.getInstance(Locale.CHINA);
			fualtTimeCL.setTime(mFaultTime);
			dateTimePickerView.iniWheelView(fualtTimeCL);
			dateTimePickerView.showAtLocation(findViewById(R.id.activity_repairmanageitem_main), Gravity.BOTTOM, 0, 0);
			break;
		case R.id.activity_repairmanageitem_repairendtime_tr:
			if(!etFinishTime.getText().toString().equals("")){
				try {
					mFinishTime = new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).parse(etFinishTime.getText().toString());
				} catch (ParseException e) {
					e.printStackTrace();
					mFinishTime = new Date();
				}
				if(dateTimePickerView==null){
					dateTimePickerView = new DateTimePickerView(RepairManageItemDataActivity.this);
				}
				dateTimePickerView.setButtonClickEvent(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mFinishTime = dateTimePickerView.getSelectedDate();
						etFinishTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(mFinishTime));
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
						calendar.setTime(mFinishTime);
						dateTimePickerView.iniWheelView(calendar);
					}
				});
				Calendar finishTimeCl = Calendar.getInstance(Locale.CHINA);
				finishTimeCl.setTime(mFinishTime);
				dateTimePickerView.iniWheelView(finishTimeCl);
				dateTimePickerView.showAtLocation(findViewById(R.id.activity_repairmanageitem_main), Gravity.BOTTOM, 0, 0);
			}
			break;
		case R.id.activity_repairmanageitem_faultphenomenon_tr:
			HashMap<String, String> faultphenomenon = new HashMap<String, String>();
			faultphenomenon.put("Key", "AccidentDetail");
			faultphenomenon.put("Name", "故障现象");
			faultphenomenon.put("Value", repairData.get("AccidentDetail"));
			startDataInputActivity(faultphenomenon);
			break;
		case R.id.activity_repairmanageitem_handlestep_tr:
			if(mHandleContent == null){
				mHandleContent = new Builder(RepairManageItemDataActivity.this);
				mHandleContent.setTitle("应急措施选择").setCancelable(false);
				mHandleContent.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						copyHandleStepSelected();
					}
				}).setNegativeButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						copyTempStepSelected();
						mHandleStep = "";
						for(int i = 0;i<handleStepSelected.length;i++){
							if(handleStepSelected[i]){
								mHandleStep = mHandleStep + handleStepContent[i] + "\n";
							}
						}
						etHandleStep.setText(mHandleStep);
					}
				});
			}
			mHandleContent.setMultiChoiceItems(handleStepContent, tempStepSelected, 
					new OnMultiChoiceClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				}
			});
			mHandleContent.create();
			mHandleContent.show();
			break;
		case R.id.activity_repairmanageitem_otherstep_tr:
			HashMap<String, String> otherstep = new HashMap<String, String>();
			otherstep.put("Key", "Otherstep");
			otherstep.put("Name", "其他措施");
			otherstep.put("Value", etOtherStep.getText().toString());
			startDataInputActivity(otherstep);
			break;
		case R.id.activity_repairmanageitem_repairtimecost_tr:
			HashMap<String, String> repairtimecost = new HashMap<String, String>();
			repairtimecost.put("Key", "RequiredManHours");
			repairtimecost.put("Name", "所需工时");
			repairtimecost.put("Value", repairData.get("RequiredManHours"));
			startDataInputActivity(repairtimecost);
			break;
		case R.id.activity_repairmanageitem_repaircontent_tr:
			HashMap<String, String> repaircontent = new HashMap<String, String>();
			repaircontent.put("Key", "TaskDetail");
			repaircontent.put("Name", "维修内容及要求");
			repaircontent.put("Value", repairData.get("TaskDetail"));
			startDataInputActivity(repaircontent);
			break;
		case R.id.activity_repairmanageitem_repairresult_tr:
			HashMap<String, String> repairresult = new HashMap<String, String>();
			repairresult.put("Key", "RepairDetail");
			repairresult.put("Name", "工作完成情况及处理措施");
			repairresult.put("Value", repairData.get("RepairDetail"));
			startDataInputActivity(repairresult);
			break;
		case R.id.activity_repairmanageitem_repairthingcost_tr:
			HashMap<String, String> repairthingcost = new HashMap<String, String>();
			repairthingcost.put("Key", "AccessoryUsed");
			repairthingcost.put("Name", "物品备件使用情况");
			repairthingcost.put("Value", repairData.get("AccessoryUsed"));
			startDataInputActivity(repairthingcost);
			break;
		case R.id.activity_repairmanageitem_repairmoneycost_tr:
			HashMap<String, String> repairmoneycost = new HashMap<String, String>();
			repairmoneycost.put("Key", "RepairCost");
			repairmoneycost.put("Name", "维修金额");
			repairmoneycost.put("Value", repairData.get("RepairCost"));
			startDataInputActivity(repairmoneycost);
			break;
		case R.id.activity_repairmanageitem_equipmentopinion_tr:
			HashMap<String, String> equipmentopinion = new HashMap<String, String>();
			equipmentopinion.put("Key", "DDOpinion");
			equipmentopinion.put("Name", "设备科意见");
			equipmentopinion.put("Value", repairData.get("DDOpinion"));
			startDataInputActivity(equipmentopinion);
			break;
		case R.id.activity_repairmanageitem_productionopinion_tr:
			HashMap<String, String> productionopinion = new HashMap<String, String>();
			productionopinion.put("Key", "PDOpinion");
			productionopinion.put("Name", "生产科意见");
			productionopinion.put("Value", repairData.get("PDOpinion"));
			startDataInputActivity(productionopinion);
			break;
		case R.id.activity_repairmanageitem_plantopinion_tr:
			HashMap<String, String> plantopinion = new HashMap<String, String>();
			plantopinion.put("Key", "PMOpinion");
			plantopinion.put("Name", "厂领导意见");
			plantopinion.put("Value", repairData.get("PMOpinion"));
			startDataInputActivity(plantopinion);
			break;
		case R.id.activity_repairmanageitem_submit:
			if(mUpdateConfirm==null){
				mUpdateConfirm = new AlertDialog.Builder(RepairManageItemDataActivity.this);
			}
			mUpdateConfirm.setTitle("确认").setMessage("确认提交吗？");
			mUpdateConfirm.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					updateServerTask(methodName);
				}
			}).setNegativeButton("取消", null);
			mUpdateConfirm.create();
			mUpdateConfirm.show();
			break;
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
		if(dateTimePickerView!=null&&dateTimePickerView.isShowing()){
			dateTimePickerView.dismiss();
		}else {
			super.onBackPressed();
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==0&&resultCode==RESULT_OK){
			@SuppressWarnings("unchecked")
			HashMap<String, String> temp = (HashMap<String, String>)data.getSerializableExtra("data");
			String key = temp.get("Key");
			String value = temp.get("Value");
			if (key.equals("AccidentDetail")) {
				etFaultPhenomenon.setText(value);
			}else if(key.equals("Otherstep")){
				etOtherStep.setText(value);
				repairData.put("EmergencyMeasures", combineEmergencyMeasures());
			}else if (key.equals("RequiredManHours")) {
				etTimeCost.setText(value);
			}else if (key.equals("TaskDetail")) {
				etContent.setText(value);
			}else if (key.equals("RepairDetail")){
				etResult.setText(value);
			}else if (key.equals("AccessoryUsed")) {
				etThing.setText(value);
			}else if (key.equals("RepairCost")) {
				etMoney.setText(value);
			}else if (key.equals("DDOpinion")) {
				etEquipmentOpinion.setText(value);
			}else if (key.equals("PDOpinion")) {
				etProductionOpinion.setText(value);
			}else if (key.equals("PMOpinion")) {
				etPlantOpinion.setText(value);
			}
			repairData.put(key, value);
		}else if (requestCode==1&&resultCode==RESULT_OK) {
			@SuppressWarnings("unchecked")
			HashMap<String, String> temp = (HashMap<String, String>)data.getSerializableExtra("data");
			repairData.put("DeviceID", temp.get("DeviceID"));
			repairData.put("DeviceName", temp.get("DeviceName"));
			etName.setText(repairData.get("DeviceName"));
		}
	}
	
	
	/**
	 * 填报一个单子
	 * @author sk
	 */
	class UpdateTask extends AsyncTask<String, String, String>{
		@Override
		protected String doInBackground(String... params) {
			methodName = params[0];
			JSONObject param = new JSONObject();
			String result = DataCenterHelper.RESPONSE_FALSE_STRING;
			try {
				if(methodName.equals(RepairManageActivity.METHOD_ADD_STRING)){
					param.put("PlantID", SystemParams.PLANTID_INT);
					param.put("RepairTaskID","");
					JSONObject repairDataString = new JSONObject();
					int postionID = Integer.valueOf(SystemParams.getInstance().getLoggedUserInfo().get("PositionID"));
					if(postionID==EnumList.UserRole.PRODUCTIONOPERATION.getState()){
						repairDataString.put("RepairTaskType", EnumList.RepairTaskType.PRODUCTIONSECTION.getType());
					}else if (postionID==EnumList.UserRole.EQUIPMENTOPERATION.getState()) {
						repairDataString.put("RepairTaskType", EnumList.RepairTaskType.EQUIPMENTSECTION.getType());
					}
					String EmergencyMeasures = combineEmergencyMeasures();
					repairDataString.put("DeviceID", repairData.get("DeviceID"));
					repairDataString.put("EmergencyMeasures", EmergencyMeasures);
					repairDataString.put("AccidentOccurTime", etFaultTime.getText().toString());
					repairDataString.put("AccidentDetail",etFaultPhenomenon.getText().toString());
					repairDataString.put("ReportPerson", SystemParams.getInstance().getLoggedUserInfo().get("UserID"));
					param.put("RepairDataString", repairDataString.toString());
					param.put("OldState", -1);
					result = DataCenterHelper.HttpPostData("InsertRepairTaskData", param);
				}else if (methodName.equals(RepairManageActivity.METHOD_UPDATE_STRING)) {
					param.put("PlantID", SystemParams.PLANTID_INT);
					param.put("RepairTaskID", Integer.valueOf(repairData.get("RepairTaskID")));
					JSONObject repairDataString = new JSONObject();
					String EmergencyMeasures = combineEmergencyMeasures();
					repairDataString.put("RepairTaskType", repairData.get("RepairTaskType"));
					repairDataString.put("DeviceID", repairData.get("DeviceID"));
					repairDataString.put("EmergencyMeasures", EmergencyMeasures);
					repairDataString.put("AccidentOccurTime", etFaultTime.getText().toString());
					repairDataString.put("AccidentDetail",etFaultPhenomenon.getText().toString());
					if(repairData.get("ReportPersonID").equals("")){
						repairDataString.put("ReportPerson", SystemParams.getInstance().getLoggedUserInfo().get("UserID"));
					}else {
						repairDataString.put("ReportPerson", repairData.get("ReportPersonID"));
					}
					param.put("RepairDataString", repairDataString.toString());
					param.put("OldState", repairData.get("State"));
					result = DataCenterHelper.HttpPostData("InsertRepairTaskData", param);
				}else if (methodName.equals(RepairManageActivity.METHOD_SENDTASK_STRING)) {
					param.put("PlantID", SystemParams.PLANTID_INT);
					param.put("RepairTaskID", Integer.valueOf(repairData.get("RepairTaskID")));
					JSONObject repairDataString = new JSONObject();
					repairDataString.put("TaskCreateTime", etSendTime.getText().toString());
					if(repairData.get("CreatePersonID").equals("")){
						repairDataString.put("CreatePerson", SystemParams.getInstance().getLoggedUserInfo().get("UserID"));
					}else {
						repairDataString.put("CreatePerson",repairData.get("CreatePersonID"));
					}
					repairDataString.put("RequiredManHours", etTimeCost.getText().toString());
					repairDataString.put("TaskDetail", etContent.getText().toString());
					param.put("RepairDataString", repairDataString.toString());
					param.put("State", EnumList.RepairState.STATEHASBEENDISTRIBUTED);
					param.put("OldState", repairData.get("State"));
					result = DataCenterHelper.HttpPostData("UpdateRepairDataforEdit", param);
				}else if (methodName.equals(RepairManageActivity.METHOD_REPAIRTASK_STRING)) {
					param.put("PlantID", SystemParams.PLANTID_INT);
					param.put("RepairTaskID", Integer.valueOf(repairData.get("RepairTaskID")));
					JSONObject repairDataString = new JSONObject();
					repairDataString.put("RepairDetail", etResult.getText().toString());
					repairDataString.put("RepairedTime", etFinishTime.getText().toString());
					repairDataString.put("AccessoryUsed", etThing.getText().toString());
					repairDataString.put("RepairCost", etMoney.getText().toString());
					if(repairData.get("RepairPersonID").equals("")){
						repairDataString.put("RepairPerson", SystemParams.getInstance().getLoggedUserInfo().get("UserID"));
					}else {
						repairDataString.put("RepairPerson", repairData.get("RepairPersonID"));
					}
					param.put("RepairDataString", repairDataString.toString());
					param.put("State", EnumList.RepairState.STATEHASBEENREPAIRED);
					param.put("OldState", repairData.get("State"));
					result = DataCenterHelper.HttpPostData("UpdateRepairDataforWrite", param);
				}else if (methodName.equals(RepairManageActivity.METHOD_PDAPPROVE_STRING)) {
					param.put("RepairTaskID", Integer.valueOf(repairData.get("RepairTaskID")));
					JSONObject repairDataString = new JSONObject();
					repairDataString.put("PDOpinion", etProductionOpinion.getText().toString());
					param.put("RepairDataString", repairDataString.toString());
					param.put("State", EnumList.RepairState.STATEPRODUCTIONTHROUGH);
					param.put("OldState", repairData.get("State"));
					result = DataCenterHelper.HttpPostData("UpdateAuditingRepairDataforUser2", param);
				}else if (methodName.equals(RepairManageActivity.METHOD_PMAPPROVE_STRING)) {
					param.put("RepairTaskID", Integer.valueOf(repairData.get("RepairTaskID")));
					JSONObject repairDataString = new JSONObject();
					repairDataString.put("PMOpinion", etPlantOpinion.getText().toString());
					param.put("RepairDataString", repairDataString.toString());
					param.put("State", EnumList.RepairState.STATEDIRECTORTHROUGH);
					param.put("OldState", repairData.get("State"));
					result = DataCenterHelper.HttpPostData("UpdateAuditingRepairDataforUser3", param);
				}else if (methodName.equals(RepairManageActivity.METHOD_DDAPPROVE_STRING)) {
					param.put("RepairTaskID", Integer.valueOf(repairData.get("RepairTaskID")));
					JSONObject repairDataString = new JSONObject();
					repairDataString.put("ApproveResult", swVerifyResult.isChecked()?"1":"2");
					if(repairData.get("ApprovePersonID").equals("")){
						repairDataString.put("ApprovePerson", SystemParams.getInstance().getLoggedUserInfo().get("UserID"));
					}else {
						repairDataString.put("ApprovePerson", repairData.get("ApprovePersonID"));
					}
					repairDataString.put("DDOpinion", etEquipmentOpinion.getText().toString());
					param.put("RepairDataString", repairDataString.toString());
					param.put("State", swVerifyResult.isChecked()?EnumList.RepairState.STATEDEVICETHROUGH:EnumList.RepairState.STATEFORCORRECTION);
					param.put("OldState", repairData.get("State"));
					result = DataCenterHelper.HttpPostData("UpdateAuditingRepairDataforUser7", param);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				result = DataCenterHelper.RESPONSE_FALSE_STRING;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				result = DataCenterHelper.RESPONSE_FALSE_STRING;
			} catch (IOException e) {
				e.printStackTrace();
				result = DataCenterHelper.RESPONSE_FALSE_STRING;
			}
			return result;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog();
		}
		
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
				Toast.makeText(RepairManageItemDataActivity.this, "提交失败,请检查您的网络设置", Toast.LENGTH_SHORT).show();
			}else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					int code = jsonObject.getInt("d");
					switch (code) {
					case EnumList.DataCenterResult.CODE_SUCCESS:
						Toast.makeText(RepairManageItemDataActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
						setResult(Activity.RESULT_OK);
						finish();
						break;
					case EnumList.DataCenterResult.CODE_SERVERERRO:
						Toast.makeText(RepairManageItemDataActivity.this, "服务器数据更新失败", Toast.LENGTH_SHORT).show();
						break;
					case EnumList.DataCenterResult.CODE_OPERATIONERRO:
						Toast.makeText(RepairManageItemDataActivity.this, "工单状态已发生改变，您无权更新", Toast.LENGTH_SHORT).show();
						break;
					case EnumList.DataCenterResult.CODE_OTHERERRO:
						Toast.makeText(RepairManageItemDataActivity.this, "服务器未知错误", Toast.LENGTH_SHORT).show();
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(RepairManageItemDataActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
				}
			}
			mProgressDialog.cancel();
		}
		
	}
	
}
