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
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.env.dcwater.R;
import com.env.dcwater.R.id;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.fragment.DateTimePickerView;
import com.env.dcwater.javabean.EnumList;
import com.env.dcwater.util.DataCenterHelper;
import com.env.dcwater.util.SystemMethod;

/**
 * 单个报修工单界面，该界面可查看，编辑
 * @author sk
 *
 */
public class RepairManageItemActivity extends NfcActivity implements OnClickListener{
	
	public static final String ACTION_STRING = "RepairManageItemActivity";
	private ProgressDialog mProgressDialog;
	private DateTimePickerView dateTimePickerView;
	private AlertDialog.Builder mUpdateConfirm;
	private AlertDialog.Builder mHandleContent;
	private ActionBar mActionBar;
	private HashMap<String, String> receivedData;
	private Intent receivedIntent;
	private Date mFaultTime,mFinishTime;
	private UpdateServerData updateServerData;
	private int mRequestCode,taskState,taskType;
	private TableLayout mGroupInfo,mGroupVerify;
	private ViewStub viewStub;
	private String mOtherStep="",mHandleStep="",methodName="",methodDesc="";
	private String [] handleStepContent = {"尝试手动启动","关闭主电源","拍下急停按钮","悬挂警示标识牌","关闭故障设备工艺段进水"};
	private boolean [] handleStepSelected = {false,false,false,false,false},tempStepSelected;
	private TextView etName,etType,etSN,etPosition,etStartTime,etManufacture,etFaultTime,etHandleStep,etPeople,etFaultPhenomenon,etOtherStep,etSendTime,etSender,etTimeCost,etContent,etResult,etFinishTime,etThing,etMoney,etVerifyPeople,etEquipmentOpinion,etProductionOpinion,etPlantOpinion;
	private TableRow trName,trFaultTime,trFaultPhenomenon,trHandleStep,trOtherStep,trResult,trFinishTime,trThingCost,trMoneyCost,trEquipmentOpinion,trProductionOpinion,trPlantOpinion,trTimeCost,trContent;
	private Switch swVerifyResult;
	private Button submit;
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iniData();
		iniActionbar();
		findAndIniView();
	}
	
	/**
	 * 初始化数据
	 */
	@SuppressWarnings("unchecked")
	private void iniData(){
		receivedIntent = getIntent();
		mRequestCode = receivedIntent.getExtras().getInt("RequestCode");
		switch (mRequestCode) {
		case RepairManageActivity.REPAIRMANAGE_ADD_INTEGER:
			setContentView(R.layout.view_repair_add);
			methodName = RepairManageActivity.METHOD_ADD_STRING;
			mFaultTime = new Date();
			copyHandleStepSelected();
			taskState = -1;
			receivedData = new HashMap<String, String>();
			receivedData.put("AccidentDetail", "");
			break;
		case RepairManageActivity.REPAIRMANAGE_NORMAL_INTEGER:
		case RepairManageActivity.REPAIRMANAGE_HISTORY_INTEGER:
			setContentView(R.layout.activity_repairmanageitem);
			receivedData = (HashMap<String, String>)receivedIntent.getSerializableExtra("Data");
			taskState = Integer.valueOf(receivedData.get("State"));
			if(mRequestCode==RepairManageActivity.REPAIRMANAGE_NORMAL_INTEGER){
				if(receivedData.get("CanUpdate").equals("true")){
					int positionID = Integer.valueOf(SystemParams.getInstance().getLoggedUserInfo().get("PositionID"));
					int taskState = Integer.valueOf(receivedData.get("State"));
					switch (positionID) {
					case EnumList.UserRole.USERROLEPLANTER:
						methodName = RepairManageActivity.METHOD_PMAPPROVE_STRING;
						break;
					case EnumList.UserRole.USERROLEEQUIPMENTOPERATION:
					case EnumList.UserRole.USERROLEPRODUCTIONOPERATION:
						methodName = RepairManageActivity.METHOD_ADD_STRING;
						break;
					case EnumList.UserRole.USERROLEEQUIPMENTCHIEF:
						if(taskState==EnumList.RepairState.STATEHASBEENCONFIRMED||
						taskState==EnumList.RepairState.STATEHASBEENREPORTED){
							methodName = RepairManageActivity.METHOD_SENDTASK_STRING;
						}else if (taskState==EnumList.RepairState.STATEHASBEENREPORTED) {
							methodName = RepairManageActivity.METHOD_DDAPPROVE_STRING;
						}
						break;
					case EnumList.UserRole.USERROLEPRODUCTIONCHIEF:
						if(taskState==EnumList.RepairState.STATEHASBEENREPORTED){
							methodName = RepairManageActivity.METHOD_PDCONFIRM_STRING;
						}else if(taskState==EnumList.RepairState.STATEDEVICETHROUGH){
							methodName = RepairManageActivity.METHOD_PDAPPROVE_STRING;
						}
						break;
					case EnumList.UserRole.USERROLEREPAIRMAN:
						if(taskState==EnumList.RepairState.STATEHASBEENDISTRIBUTED){
							methodName = RepairManageActivity.METHOD_RECEIVE_STRING;
						}else if(taskState==EnumList.RepairState.STATEBEENINGREPAIRED){
							methodName = RepairManageActivity.METHOD_REPAIRTASK_STRING;
						}
						break;
					}
				}else {
					methodName = RepairManageActivity.METHOD_DETAIL_STRING;
				}
			}else if (mRequestCode==RepairManageActivity.REPAIRMANAGE_HISTORY_INTEGER) {
				methodName = RepairManageActivity.METHOD_HISTORY_STRING;
			}
			String [] handle1;
			if(receivedData.get("EmergencyMeasures").equals("null")){ 
				handle1 = new String [0]; 
			}else {
				handle1 = receivedData.get("EmergencyMeasures").split(",");
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
				mFaultTime = new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).parse(receivedData.get("AccidentOccurTime").toString());
			} catch (Exception e) {
				mOtherStep = "";
				mHandleStep ="";
				mFaultTime = new Date();
			}
			break;
		}
	}
	
	
	/**
	 * 初始化actionbar
	 */
	private void iniActionbar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		if(methodName.equals(RepairManageActivity.METHOD_ADD_STRING)){
			mActionBar.setTitle("上报故障");
			methodDesc = "上报故障";
		}else if (methodName.equals(RepairManageActivity.METHOD_HISTORY_STRING)) {
			mActionBar.setTitle(receivedData.get("FaultReportSN")+"详情");
		}else if (methodName.equals(RepairManageActivity.METHOD_DDAPPROVE_STRING)) {
			mActionBar.setTitle("设备科长审核");
			methodDesc = "提交审核";
		}else if (methodName.equals(RepairManageActivity.METHOD_PDAPPROVE_STRING)) {
			mActionBar.setTitle("生产科长审核");
			methodDesc = "提交审核";
		}else if (methodName.equals(RepairManageActivity.METHOD_PDCONFIRM_STRING)) {
			mActionBar.setTitle("确认报修单");
			methodDesc = "确认报修单";
		}else if (methodName.equals(RepairManageActivity.METHOD_PMAPPROVE_STRING)) {
			mActionBar.setTitle("厂长审核");
			methodDesc = "提交审核";
		}else if (methodName.equals(RepairManageActivity.METHOD_RECEIVE_STRING)) {
			mActionBar.setTitle("确认维修单");
			methodDesc = "确认维修单";
		}else if (methodName.equals(RepairManageActivity.METHOD_REPAIRTASK_STRING)) {
			mActionBar.setTitle("填写维修单");
			methodDesc = "填写维修单";
		}else if (methodName.equals(RepairManageActivity.METHOD_REREPAIRTASK_STRING)) {
			mActionBar.setTitle("修改维修单");
			methodDesc = "修改维修单";
		}else if (methodName.equals(RepairManageActivity.METHOD_SENDTASK_STRING)) {
			mActionBar.setTitle("发送维修单");
			methodDesc = "发送维修单";
		}
	}
	
	
	/**
	 * 初始化各个控件
	 */
	private void findAndIniView(){
		
		etName = (TextView)findViewById(R.id.activity_repairmanageitem_name);
		etFaultTime = (TextView)findViewById(R.id.activity_repairmanageitem_faulttime);
		etFaultPhenomenon = (TextView)findViewById(R.id.activity_repairmanageitem_faultphenomenon);
		etHandleStep = (TextView)findViewById(R.id.activity_repairmanageitem_handlestep);
		etOtherStep = (TextView)findViewById(R.id.activity_repairmanageitem_otherstep);
		etPeople = (TextView)findViewById(R.id.activity_repairmanageitem_taskpeople);
		
		if(methodName.equals(RepairManageActivity.METHOD_DETAIL_STRING)||methodName.equals(RepairManageActivity.METHOD_HISTORY_STRING)){
			
		}else {
//			submit = (Button)findViewById(R.id.activity_repairmanageitem_submit);
//			submit.setText(methodDesc);
//			submit.setOnClickListener(this);
			
			viewStub = (ViewStub)findViewById(R.id.activity_repairmanageitem_editpart);
			
			if(methodName.equals(RepairManageActivity.METHOD_ADD_STRING)){
				
				trName = (TableRow)findViewById(R.id.activity_repairmanageitem_name_tr);
				trFaultTime = (TableRow)findViewById(R.id.activity_repairmanageitem_faulttime_tr);
				trFaultPhenomenon = (TableRow)findViewById(R.id.activity_repairmanageitem_faultphenomenon_tr);
				trHandleStep = (TableRow)findViewById(R.id.activity_repairmanageitem_handlestep_tr);
				trOtherStep = (TableRow)findViewById(R.id.activity_repairmanageitem_otherstep_tr);
				
				trName.setOnClickListener(this);
				trFaultTime.setOnClickListener(this);
				trFaultPhenomenon.setOnClickListener(this);
				trHandleStep.setOnClickListener(this);
				trOtherStep.setOnClickListener(this);
				
			}else {
				
				mGroupInfo = (TableLayout)findViewById(R.id.activity_repairmanageitem_repairgroupinfo);
				mGroupVerify = (TableLayout)findViewById(R.id.activity_repairmanageitem_repairgroupverify);
				
				etType = (TextView)findViewById(R.id.activity_repairmanageitem_type);
				
				etSN = (TextView)findViewById(R.id.activity_repairmanageitem_sn);
				
				etPosition = (TextView)findViewById(R.id.activity_repairmanageitem_position);
				
				etStartTime = (TextView)findViewById(R.id.activity_repairmanageitem_starttime);
				
				etManufacture = (TextView)findViewById(R.id.activity_repairmanageitem_manufacturer);
				
				etSendTime = (TextView)findViewById(R.id.activity_repairmanageitem_repairsttime);
				
				etSender = (TextView)findViewById(R.id.activity_repairmanageitem_repairpeople);
				etTimeCost = (TextView)findViewById(R.id.activity_repairmanageitem_repairtimecost);
				trTimeCost = (TableRow)findViewById(R.id.activity_repairmanageitem_repairtimecost_tr);
				
				etContent = (TextView)findViewById(R.id.activity_repairmanageitem_repaircontent);
				trContent = (TableRow)findViewById(R.id.activity_repairmanageitem_repaircontent_tr);
				
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
				
				etEquipmentOpinion = (TextView)findViewById(R.id.activity_repairmanageitem_equipmentopinion);
				trEquipmentOpinion = (TableRow)findViewById(R.id.activity_repairmanageitem_equipmentopinion_tr);
				
				etProductionOpinion = (TextView)findViewById(R.id.activity_repairmanageitem_productionopinion);
				trProductionOpinion = (TableRow)findViewById(R.id.activity_repairmanageitem_productionopinion_tr);
				
				trPlantOpinion = (TableRow)findViewById(R.id.activity_repairmanageitem_plantopinion_tr);
				etPlantOpinion = (TextView)findViewById(R.id.activity_repairmanageitem_plantopinion);
				
				if(methodName.equals(RepairManageActivity.METHOD_SENDTASK_STRING)){
					viewStub.setLayoutResource(R.layout.view_repair_send);
					viewStub.inflate();
//					etSendTime = (TextView)findViewById(R.id.view_repair_send_repairsttime);
//					etSender = (TextView)findViewById(R.id.view_repair_send_repairpeople);
//					etTimeCost = (TextView)findViewById(R.id.view_repair_send_repairsttime);
//					etContent = (TextView)findViewById(R.id.view_repair_send_repaircontent);
//					
//					trTimeCost = (TableRow)findViewById(R.id.view_repair_send_repairsttime_tr);
//					trContent = (TableRow)findViewById(R.id.view_repair_send_repaircontent_tr);
					
					
					
				}else if (methodName.equals(RepairManageActivity.METHOD_RECEIVE_STRING)) {
					
				}else if (methodName.equals(RepairManageActivity.METHOD_PDCONFIRM_STRING)) {
					
				}else if (methodName.equals(RepairManageActivity.METHOD_PMAPPROVE_STRING)) {
					
				}else if (methodName.equals(RepairManageActivity.METHOD_PDAPPROVE_STRING)) {
					
				}else if (methodName.equals(RepairManageActivity.METHOD_REPAIRTASK_STRING)) {
					
				}else if (methodName.equals(RepairManageActivity.METHOD_DDAPPROVE_STRING)) {
					
				}else if (methodName.equals(RepairManageActivity.METHOD_REREPAIRTASK_STRING)) {
					
				}
				
				
				trTimeCost.setOnClickListener(this);
				trContent.setOnClickListener(this);
				trResult.setOnClickListener(this);
				trFinishTime.setOnClickListener(this);
				trThingCost.setOnClickListener(this);
				trMoneyCost.setOnClickListener(this);
				trEquipmentOpinion.setOnClickListener(this);
				trProductionOpinion.setOnClickListener(this);
				trPlantOpinion.setOnClickListener(this);
			}
			
			
			
		}
		
		
		fillViewData(mRequestCode);
		setViewState(mRequestCode);
		
	}
	
	/**
	 * 根据不同的状态设置各个空间的单击事件
	 * @param code
	 */
	private void setViewState( int code){
		
		if(code==RepairManageActivity.REPAIRMANAGE_ADD_INTEGER){
			
		}else {
			int userPositionID = Integer.valueOf(SystemParams.getInstance().getLoggedUserInfo().get("PositionID"));
			if(userPositionID==EnumList.UserRole.USERROLEEQUIPMENTOPERATION){
				taskType = EnumList.RepairTaskType.EQUIPMENTSECTION.getType();
			}else if (userPositionID==EnumList.UserRole.USERROLEPRODUCTIONOPERATION) {
				taskType = EnumList.RepairTaskType.PRODUCTIONSECTION.getType();
			}else {
				try {
					taskType = Integer.valueOf(receivedData.get("RepairTaskType"));
				} catch (Exception e) {
					taskType = 0;
				}
			}
			
			switch (taskType) {
			case 0:
			case EnumList.RepairTaskType.TASKTYPE_EQUIPMENT:
				setGroupVerifyPDShow(false);
				break;
			case EnumList.RepairTaskType.TASKTYPE_PRODUCTION:
				setGroupVerifyPDShow(true);
				break;
			}	
			
			switch (taskState) {
			case -1:
				setGroupRepairShow(false);
				setGroupVerifyShow(false);
				break;
			case EnumList.RepairState.STATEHASBEENREPORTED:
			case EnumList.RepairState.STATEHASBEENCONFIRMED:
				setGroupRepairShow(false);
				setGroupVerifyShow(false);
				break;
			case EnumList.RepairState.STATEHASBEENDISTRIBUTED:
				setGroupRepairTaskShow(false);
				setGroupVerifyShow(false);
				break;
			case EnumList.RepairState.STATEBEENINGREPAIRED:
				setGroupRepairTaskShow(false);
			case EnumList.RepairState.STATEHASBEENREPAIRED:
				setGroupVerifyShow(false);
				break;
			case EnumList.RepairState.STATEFORCORRECTION:
				setGroupVerifyPDShow(false);
				setGroupVerifyPMShow(false);
				break;
			case EnumList.RepairState.STATEDEVICETHROUGH:
			case EnumList.RepairState.STATEPRODUCTIONTHROUGH:
				setGroupVerifyPMShow(false);
				break;
			case EnumList.RepairState.STATEDIRECTORTHROUGH:
				break;
			}
		}
	}
	
	/**
	 * 填写维修单的界面是否显示
	 * @param isShow
	 */
	private void setGroupRepairTaskShow(boolean isShow) {
		trResult.setVisibility(isShow?View.VISIBLE:View.GONE);
		trFinishTime.setVisibility(isShow?View.VISIBLE:View.GONE);
		trThingCost.setVisibility(isShow?View.VISIBLE:View.GONE);
	}
	
	/**
	 * 根据不同的code码设置表单的基本信息
	 * @param code
	 */
	private void fillViewData(int code){
		switch (code) {
		case RepairManageActivity.REPAIRMANAGE_ADD_INTEGER:
			setGroupAddData();
			break;
		case RepairManageActivity.REPAIRMANAGE_NORMAL_INTEGER:
		case RepairManageActivity.REPAIRMANAGE_HISTORY_INTEGER:
			setGroupBasicData(false);
			setGroupFaultData(false);
			setGroupTaskSendData(false);
			setGroupTaskRepairData(false);
			setVerifyDDData(false);
			setVerifyPDData(false);
			setVerifyPMData(false);
			break;
		}
	}
	
	private void setGroupAddData(){
		etFaultTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING, Locale.CHINA).format(mFaultTime));
		etPeople.setText(SystemParams.getInstance().getLoggedUserInfo().get("RealUserName"));
	}
	
	/**
	 * 设置设备信息的数据
	 * @param isEmpty
	 */
	private void setGroupBasicData(boolean isEmpty){
		if(isEmpty){
			etName.setText("");
			etType.setText("");
			etSN.setText("");
			etStartTime.setText("");
			etManufacture.setText("");
			etPosition.setText("");
		}else {
			etName.setText(receivedData.get("DeviceName"));
			etType.setText(receivedData.get("Specification"));
			etSN.setText(receivedData.get("DeviceSN"));
			etStartTime.setText(receivedData.get("StartUseTime"));
			etManufacture.setText(receivedData.get("Manufacturer"));
			etPosition.setText(receivedData.get("InstallPosition"));
		}
	}
	
	
	/**
	 * 设置故障信息的数据
	 * @param isEmpty
	 */
	private void setGroupFaultData(boolean isEmpty){
		if(isEmpty){
			etFaultTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(new Date()));
			etFaultPhenomenon.setText("");
			etHandleStep.setText("");
			etOtherStep.setText("");
			etPeople.setText(SystemParams.getInstance().getLoggedUserInfo().get("RealUserName"));
		}else {
			etFaultTime.setText(receivedData.get("AccidentOccurTime"));
			etFaultPhenomenon.setText(receivedData.get("AccidentDetail"));
			etPeople.setText(receivedData.get("ReportPersonRealName").toString());
			etHandleStep.setText(mHandleStep);
			etOtherStep.setText(mOtherStep);
		}
	}
	
	
	/**
	 * 设置派发工单的数据
	 * @param isShow
	 */
	private void setGroupTaskSendData(boolean isEmpty){
		if(isEmpty){
			etSendTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(new Date()));
			etSender.setText(SystemParams.getInstance().getLoggedUserInfo().get("RealUserName"));
			etTimeCost.setText("");
			etContent.setText("");
		}else {
			if(methodName.equals(RepairManageActivity.METHOD_SENDTASK_STRING)){
				if(receivedData.get("TaskCreateTime").equals("")){
					etSendTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(new Date()));
				}else {
					etSendTime.setText(receivedData.get("TaskCreateTime"));
				}
				if(receivedData.get("CreatePersonRealName").equals("")){
					etSender.setText(SystemParams.getInstance().getLoggedUserInfo().get("RealUserName"));
				}else {
					etSender.setText(receivedData.get("CreatePersonRealName"));
				}
			}else {
				etSendTime.setText(receivedData.get("TaskCreateTime"));
				etSender.setText(receivedData.get("CreatePersonRealName"));
			}
			etTimeCost.setText(receivedData.get("RequiredManHours"));
			etContent.setText(receivedData.get("TaskDetail"));
		}
	}
	
	
	/**
	 * 设置维修工单的数据
	 * @param isShow
	 */
	private void setGroupTaskRepairData(boolean isEmpty){
		if(isEmpty){
			etResult.setText("");
			etFinishTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(new Date()));
			etThing.setText("");
		}else {
			if(methodName.equals(RepairManageActivity.METHOD_REPAIRTASK_STRING)){
				if(receivedData.get("RepairedTime").equals("")){
					etFinishTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(new Date()));
				}else {
					etFinishTime.setText(receivedData.get("RepairedTime"));
				}
			}else {
				etFinishTime.setText(receivedData.get("RepairedTime"));
			}
			etResult.setText(receivedData.get("RepairDetail"));
			etThing.setText(receivedData.get("AccessoryUsed"));
		}
	}
	
	/**
	 * 设置维修单维修部门是否显示
	 * @param isShow
	 */
	private void setGroupRepairShow(boolean isShow){
		mGroupInfo.setVisibility(isShow?View.VISIBLE:View.GONE);
	}
	
	
	/**
	 * 设备科长审核意见的数据
	 * @param isEmpty
	 */
	private void setVerifyDDData(boolean isEmpty){
		if(isEmpty){
			swVerifyResult.setChecked(false);
			etVerifyPeople.setText("");
			etEquipmentOpinion.setText("");
			etMoney.setText("");
		}else {
			if(methodName.equals(RepairManageActivity.METHOD_DDAPPROVE_STRING)){
				if(receivedData.get("ApprovePersonRealName").equals("")){
					etVerifyPeople.setText(SystemParams.getInstance().getLoggedUserInfo().get("RealUserName"));
				}else {
					etVerifyPeople.setText(receivedData.get("ApprovePersonRealName"));
				}
			}else {
				etVerifyPeople.setText(receivedData.get("ApprovePersonRealName"));
			}
			if(receivedData.get("ApproveResult").equals("1")){
				swVerifyResult.setChecked(true);
			}else {
				swVerifyResult.setChecked(false);
			}
			etMoney.setText(receivedData.get("RepairCost"));
			etEquipmentOpinion.setText(receivedData.get("DDOpinion"));
		}
	}
	
	/**
	 * 生产科长的审核意见
	 * @param isEmpty
	 */
	private void setVerifyPDData(boolean isEmpty){
		if(isEmpty){
			etProductionOpinion.setText("");
		}else {
			etProductionOpinion.setText(receivedData.get("PDOpinion"));
		}
	}
	
	/**
	 * 厂长的审核意见
	 * @param isEmpty
	 */
	private void setVerifyPMData(boolean isEmpty){
		if(isEmpty){
			etPlantOpinion.setText("");
		}else {
			etPlantOpinion.setText(receivedData.get("PMOpinion"));
		}
	}
	/**
	 * 审核部分是否显示
	 * @param isShow
	 */
	private void setGroupVerifyShow(boolean isShow){
		trMoneyCost.setVisibility(isShow?View.VISIBLE:View.GONE);
		mGroupVerify.setVisibility(isShow?View.VISIBLE:View.GONE);
	}
	
	/**
	 * 审核部分中厂长审核是否显示
	 * @param isShow
	 */
	private void setGroupVerifyPMShow(boolean isShow){
		trPlantOpinion.setVisibility(isShow?View.VISIBLE:View.GONE);
	}
	
	
	/**
	 * 审核部分中生产科审核是否显示
	 * @param isShow
	 */
	private void setGroupVerifyPDShow(boolean isShow){
		trProductionOpinion.setVisibility(isShow?View.VISIBLE:View.GONE);
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
	 * 获取数据时，弹出进度对话框
	 * @param cancelable 是否能被取消的操作
	 */
	private void showProgressDialog(boolean cancelable){
		if(mProgressDialog==null){
			mProgressDialog = new ProgressDialog(RepairManageItemActivity.this);
			mProgressDialog.setTitle("提交中");
			mProgressDialog.setMessage("正在向服务器提交，请稍后");
			mProgressDialog.setCanceledOnTouchOutside(false);
		}
		mProgressDialog.setCancelable(cancelable);
		mProgressDialog.show();
	}
	
	/**
	 * 使用asynctask异步提交工单
	 */
	private void updateServerTask(String methodName) {
		updateServerData = new UpdateServerData();
		updateServerData.execute(methodName);
	}
	
	/**
	 * 取消时，退出对话框
	 */
	private void hideProgressDialog(){
		if(mProgressDialog!=null){
			mProgressDialog.cancel();
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
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			setResult(RESULT_OK);
			finish();
		}
	}
	
	@Override
	public void onClick(View v) {
		if(methodName.equals(RepairManageActivity.METHOD_HISTORY_STRING)||methodName.equals(RepairManageActivity.METHOD_DETAIL_STRING)){
			
		}else {
			switch (v.getId()) {
			case R.id.activity_repairmanageitem_name_tr:
				Intent intent = new Intent(this, DeviceSelectActivity.class);
				intent.putExtra("data", receivedData);
				startActivityForResult(intent, 1);
				break;
			case R.id.activity_repairmanageitem_faulttime_tr:
				if(dateTimePickerView==null){
					dateTimePickerView = new DateTimePickerView(RepairManageItemActivity.this);
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
						dateTimePickerView = new DateTimePickerView(RepairManageItemActivity.this);
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
				faultphenomenon.put("Value", receivedData.get("AccidentDetail"));
				startDataInputActivity(faultphenomenon);
				break;
			case R.id.activity_repairmanageitem_handlestep_tr:
				if(mHandleContent == null){
					mHandleContent = new Builder(RepairManageItemActivity.this);
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
				repairtimecost.put("Value", receivedData.get("RequiredManHours"));
				startDataInputActivity(repairtimecost);
				break;
			case R.id.activity_repairmanageitem_repaircontent_tr:
				HashMap<String, String> repaircontent = new HashMap<String, String>();
				repaircontent.put("Key", "TaskDetail");
				repaircontent.put("Name", "维修内容及要求");
				repaircontent.put("Value", receivedData.get("TaskDetail"));
				startDataInputActivity(repaircontent);
				break;
			case R.id.activity_repairmanageitem_repairresult_tr:
				HashMap<String, String> repairresult = new HashMap<String, String>();
				repairresult.put("Key", "RepairDetail");
				repairresult.put("Name", "工作完成情况及处理措施");
				repairresult.put("Value", receivedData.get("RepairDetail"));
				startDataInputActivity(repairresult);
				break;
			case R.id.activity_repairmanageitem_repairthingcost_tr:
				HashMap<String, String> repairthingcost = new HashMap<String, String>();
				repairthingcost.put("Key", "AccessoryUsed");
				repairthingcost.put("Name", "物品备件使用情况");
				repairthingcost.put("Value", receivedData.get("AccessoryUsed"));
				startDataInputActivity(repairthingcost);
				break;
			case R.id.activity_repairmanageitem_repairmoneycost_tr:
				HashMap<String, String> repairmoneycost = new HashMap<String, String>();
				repairmoneycost.put("Key", "RepairCost");
				repairmoneycost.put("Name", "维修金额");
				repairmoneycost.put("Value", receivedData.get("RepairCost"));
				startDataInputActivity(repairmoneycost);
				break;
			case R.id.activity_repairmanageitem_equipmentopinion_tr:
				HashMap<String, String> equipmentopinion = new HashMap<String, String>();
				equipmentopinion.put("Key", "DDOpinion");
				equipmentopinion.put("Name", "设备科意见");
				equipmentopinion.put("Value", receivedData.get("DDOpinion"));
				startDataInputActivity(equipmentopinion);
				break;
			case R.id.activity_repairmanageitem_productionopinion_tr:
				HashMap<String, String> productionopinion = new HashMap<String, String>();
				productionopinion.put("Key", "PDOpinion");
				productionopinion.put("Name", "生产科意见");
				productionopinion.put("Value", receivedData.get("PDOpinion"));
				startDataInputActivity(productionopinion);
				break;
			case R.id.activity_repairmanageitem_plantopinion_tr:
				HashMap<String, String> plantopinion = new HashMap<String, String>();
				plantopinion.put("Key", "PMOpinion");
				plantopinion.put("Name", "厂领导意见");
				plantopinion.put("Value", receivedData.get("PMOpinion"));
				startDataInputActivity(plantopinion);
				break;
			case R.id.activity_repairmanageitem_submit:
				if(mUpdateConfirm==null){
					mUpdateConfirm = new AlertDialog.Builder(RepairManageItemActivity.this);
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
		
	
		
	}
	
	private class UpdateServerData extends AsyncTask<String, String, String>{
		@Override
		protected String doInBackground(String... params) {
			methodName = params[0];
			JSONObject param = new JSONObject();
			String result = DataCenterHelper.RESPONSE_FALSE_STRING;
			try {
				if (methodName.equals(RepairManageActivity.METHOD_RECEIVE_STRING)) {
					param.put("RepairTaskID", Integer.valueOf(receivedData.get("RepairTaskID")));
					param.put("OldState", Integer.valueOf(receivedData.get("State")));
					result = DataCenterHelper.HttpPostData("ConfirmRepair", param);
				}else if (methodName.equals(RepairManageActivity.METHOD_PDCONFIRM_STRING)) {
					param.put("RepairTaskID", Integer.valueOf(receivedData.get("RepairTaskID")));
					param.put("CheckPerson", SystemParams.getInstance().getLoggedUserInfo().get("UserID"));
					param.put("OldState", Integer.valueOf(receivedData.get("State")));
					result = DataCenterHelper.HttpPostData("ValidationReport", param);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(false);
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
				Toast.makeText(RepairManageItemActivity.this, "提交失败,请检查您的网络设置", Toast.LENGTH_SHORT).show();
			}else {
				JSONObject object;
				try {
					object = new JSONObject(result);
					int code =object.getInt("d");
					switch (code) {
					case EnumList.DataCenterResult.CODE_SUCCESS:
						Toast.makeText(RepairManageItemActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
						if (methodName.equals(RepairManageActivity.METHOD_RECEIVE_STRING)) {
							receivedData.put("State",EnumList.RepairState.STATEBEENINGREPAIRED+"");
							receivedData.put("StateDescription", EnumList.RepairState.BEENINGREPAIRED.getStateDescription());
						}else if(methodName.equals(RepairManageActivity.METHOD_PDCONFIRM_STRING)){
							receivedData.put("State",EnumList.RepairState.STATEHASBEENCONFIRMED+"");
							receivedData.put("StateDescription", EnumList.RepairState.HASBEENCONFIRMED.getStateDescription());
							receivedData.put("CanUpdate", "false");
						}
						setResult(RESULT_OK);
						finish();
						break;
					case EnumList.DataCenterResult.CODE_SERVERERRO:
						Toast.makeText(RepairManageItemActivity.this, "服务器数据更新失败", Toast.LENGTH_SHORT).show();
						break;
					case EnumList.DataCenterResult.CODE_OPERATIONERRO:
						Toast.makeText(RepairManageItemActivity.this, "工单状态已发生改变，您无权更新", Toast.LENGTH_SHORT).show();
						break;
					case EnumList.DataCenterResult.CODE_OTHERERRO:
						Toast.makeText(RepairManageItemActivity.this, "服务器未知错误", Toast.LENGTH_SHORT).show();
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(RepairManageItemActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
				}
			}
			hideProgressDialog();
		}
	}
}
