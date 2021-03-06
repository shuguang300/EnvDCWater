package com.env.dcwater.activity;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.javabean.EnumList;
import com.env.dcwater.util.DataCenterHelper;
import com.env.dcwater.util.SystemMethod;

/**
 * 单个报修工单界面，该界面可查看，编辑
 * @author sk
 *
 */
public class RepairManageItemActivity extends NfcActivity{
	
	public static final String ACTION_STRING = "RepairManageItemActivity";
	private ProgressDialog mProgressDialog;
	private AlertDialog.Builder mUpdateConfirm;
	private ActionBar mActionBar;
	private HashMap<String, String> receivedData,selectedData;
	private Intent receivedIntent;
	private UpdateServerData updateServerData;
	private int mRequestCode,taskState,taskType;
	private TableLayout mGroupInfo,mGroupVerify;
	private String mOtherStep="",mHandleStep="",methodName="";
	private String [] handleStepContent = {"尝试手动启动","关闭主电源","拍下急停按钮","悬挂警示标识牌","关闭故障设备工艺段进水"};
	private boolean [] handleStepSelected = {false,false,false,false,false},tempStepSelected;
	private TextView etName,etType,etSN,etPosition,etStartTime,etManufacture,etFaultTime,etHandleStep,etPeople,etFaultPhenomenon,etOtherStep,etSendTime,etSender,etTimeCost,etContent,etResult,etFinishTime,etThing,etMoney,etVerifyPeople,etEquipmentOpinion,etProductionOpinion,etPlantOpinion;
	private TableRow trResult,trFinishTime,trThingCost,trMoneyCost,trProductionOpinion,trPlantOpinion;
	private Switch swVerifyResult;
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repairmanageitem);
		iniData();
		iniActionbar();
		findAndIniView();
	}
	
	/**
	 * 初始化actionbar
	 */
	private void iniActionbar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		switch (mRequestCode) {
		case RepairManageActivity.REPAIRMANAGE_ADD_INTEGER:
			mActionBar.setTitle("上报故障");
			break;
		case RepairManageActivity.REPAIRMANAGE_NORMAL_INTEGER:
		case RepairManageActivity.REPAIRMANAGE_HISTORY_INTEGER:
			mActionBar.setTitle(receivedData.get("FaultReportSN")+"详情");
			break;
		}
	}
	
	/**
	 * 初始化数据
	 */
	@SuppressWarnings("unchecked")
	private void iniData(){
		//获取到设备维修管理界面传过来的intent
		receivedIntent = getIntent();
		//获取请求码
		mRequestCode = receivedIntent.getExtras().getInt("RequestCode");
		switch (mRequestCode) {
		case RepairManageActivity.REPAIRMANAGE_ADD_INTEGER://新增维修单
			copyHandleStepSelected();
			taskState = -1;
			selectedData = new HashMap<String, String>();
			selectedData.put("AccidentDetail", "");
			break;
		case RepairManageActivity.REPAIRMANAGE_NORMAL_INTEGER://普通模式
		case RepairManageActivity.REPAIRMANAGE_HISTORY_INTEGER: //历史浏览模式
			receivedData = (HashMap<String, String>)receivedIntent.getSerializableExtra("Data");
			selectedData = receivedData;
			taskState = Integer.valueOf(selectedData.get("State"));
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
			} catch (Exception e) {
				mOtherStep = "";
				mHandleStep ="";
			}
			break;
		}
	}
	
	/**
	 * 初始化各个控件
	 */
	private void findAndIniView(){
		
		mGroupInfo = (TableLayout)findViewById(R.id.activity_repairmanageitem_repairgroupinfo);
		mGroupVerify = (TableLayout)findViewById(R.id.activity_repairmanageitem_repairgroupverify);
		
		etName = (TextView)findViewById(R.id.activity_repairmanageitem_name);
		
		etType = (TextView)findViewById(R.id.activity_repairmanageitem_type);
		
		etSN = (TextView)findViewById(R.id.activity_repairmanageitem_sn);
		
		etPosition = (TextView)findViewById(R.id.activity_repairmanageitem_position);
		
		etStartTime = (TextView)findViewById(R.id.activity_repairmanageitem_starttime);
		
		etManufacture = (TextView)findViewById(R.id.activity_repairmanageitem_manufacturer);
		
		etFaultTime = (TextView)findViewById(R.id.activity_repairmanageitem_faulttime);
		
		etFaultPhenomenon = (TextView)findViewById(R.id.activity_repairmanageitem_faultphenomenon);
		
		etHandleStep = (TextView)findViewById(R.id.activity_repairmanageitem_handlestep);
		
		etOtherStep = (TextView)findViewById(R.id.activity_repairmanageitem_otherstep);
		
		etPeople = (TextView)findViewById(R.id.activity_repairmanageitem_taskpeople);

		etSendTime = (TextView)findViewById(R.id.activity_repairmanageitem_repairsttime);
		
		etSender = (TextView)findViewById(R.id.activity_repairmanageitem_repairpeople);
		etTimeCost = (TextView)findViewById(R.id.activity_repairmanageitem_repairtimecost);
		
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
		
		etEquipmentOpinion = (TextView)findViewById(R.id.activity_repairmanageitem_equipmentopinion);
		
		etProductionOpinion = (TextView)findViewById(R.id.activity_repairmanageitem_productionopinion);
		trProductionOpinion = (TableRow)findViewById(R.id.activity_repairmanageitem_productionopinion_tr);
		
		trPlantOpinion = (TableRow)findViewById(R.id.activity_repairmanageitem_plantopinion_tr);
		etPlantOpinion = (TextView)findViewById(R.id.activity_repairmanageitem_plantopinion);
		
		fillViewData(mRequestCode);
		setViewState(mRequestCode);
		
	}
	
	/**
	 * 根据不同的状态设置各个空间的单击事件
	 * @param code
	 */
	private void setViewState( int code){
		int userPositionID = Integer.valueOf(SystemParams.getInstance().getLoggedUserInfo().get("PositionID"));
		if(userPositionID==EnumList.UserRole.USERROLEEQUIPMENTOPERATION){
			taskType = EnumList.RepairTaskType.EQUIPMENTSECTION.getType();
		}else if (userPositionID==EnumList.UserRole.USERROLEPRODUCTIONOPERATION) {
			taskType = EnumList.RepairTaskType.PRODUCTIONSECTION.getType();
		}else {
			try {
				taskType = Integer.valueOf(selectedData.get("RepairTaskType"));
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

//	/**
//	 * 派发维修单的界面是否显示
//	 * @param isShow
//	 */
//	private void setGroupSendTaskShow(boolean isShow) {
//		trSendTime.setVisibility(isShow?View.VISIBLE:View.GONE);
//		trSender.setVisibility(isShow?View.VISIBLE:View.GONE);
//		trTimeCost.setVisibility(isShow?View.VISIBLE:View.GONE);
//		trContent.setVisibility(isShow?View.VISIBLE:View.GONE);
//	}
	
	/**
	 * 填写维修单的界面是否显示
	 * @param isShow
	 */
	private void setGroupRepairTaskShow(boolean isShow) {
		trResult.setVisibility(isShow?View.VISIBLE:View.GONE);
		trFinishTime.setVisibility(isShow?View.VISIBLE:View.GONE);
		trThingCost.setVisibility(isShow?View.VISIBLE:View.GONE);
		trMoneyCost.setVisibility(isShow?View.VISIBLE:View.GONE);
	}
	
	/**
	 * 根据不同的code码设置表单的基本信息
	 * @param code
	 */
	private void fillViewData(int code){
		switch (code) {
		case RepairManageActivity.REPAIRMANAGE_ADD_INTEGER:
			setGroupBasicData(true);
			setGroupFaultData(true);
			break;
		case RepairManageActivity.REPAIRMANAGE_UPDATE_INTEGER:
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
			etName.setText(selectedData.get("DeviceName"));
			etType.setText(selectedData.get("Specification"));
			etSN.setText(selectedData.get("DeviceSN"));
			etStartTime.setText(selectedData.get("StartUseTime"));
			etManufacture.setText(selectedData.get("Manufacturer"));
			etPosition.setText(selectedData.get("InstallPosition"));
		}
	}
	
//	/**
//	 * 填报单的设备基本信息是否显示
//	 * @param isShow
//	 */
//	private void setGroupBasicShow(boolean isShow){
//		mGroupBasic.setVisibility(isShow?View.VISIBLE:View.GONE);
//	}
	
	
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
			etFaultTime.setText(selectedData.get("AccidentOccurTime"));
			etFaultPhenomenon.setText(selectedData.get("AccidentDetail"));
			etPeople.setText(selectedData.get("ReportPersonRealName").toString());
			etHandleStep.setText(mHandleStep);
			etOtherStep.setText(mOtherStep);
		}
	}
	
//	/**
//	 * 故障信息是否显示
//	 * @param isShow
//	 */
//	private void setGroupFaultShow(boolean isShow){
//		mGroupFault.setVisibility(isShow?View.VISIBLE:View.GONE);
//		mGroupPeople.setVisibility(isShow?View.VISIBLE:View.GONE);
//	}
	
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
				if(selectedData.get("TaskCreateTime").equals("")){
					etSendTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(new Date()));
				}else {
					etSendTime.setText(selectedData.get("TaskCreateTime"));
				}
				if(selectedData.get("CreatePersonRealName").equals("")){
					etSender.setText(SystemParams.getInstance().getLoggedUserInfo().get("RealUserName"));
				}else {
					etSender.setText(selectedData.get("CreatePersonRealName"));
				}
			}else {
				etSendTime.setText(selectedData.get("TaskCreateTime"));
				etSender.setText(selectedData.get("CreatePersonRealName"));
			}
			etTimeCost.setText(selectedData.get("RequiredManHours"));
			etContent.setText(selectedData.get("TaskDetail"));
		}
	}
	
//	/**
//	 * 维修部分是否显示数据
//	 * @param isEmpty
//	 */
//	private void setGroupRepairData (boolean isEmpty){
//		if(isEmpty){
//			etSendTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(new Date()));
//			etSender.setText(SystemParams.getInstance().getLoggedUserInfo().get("RealUserName"));
//			etTimeCost.setText("");
//			etContent.setText("");
//			etResult.setText("");
//			etFinishTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(new Date()));
//			etThing.setText("");
//			etMoney.setText("");
//		}else {
//			etSendTime.setText(selectedData.get("TaskCreateTime"));
//			etSender.setText(selectedData.get("CreatePersonRealName"));
//			etTimeCost.setText(selectedData.get("RequiredManHours"));
//			etContent.setText(selectedData.get("TaskDetail"));
//			etResult.setText(selectedData.get("RepairDetail"));
//			etFinishTime.setText(selectedData.get("RepairedTime"));
//			etThing.setText(selectedData.get("AccessoryUsed"));
//			etMoney.setText(selectedData.get("RepairCost"));
//		}
//	}
	
	/**
	 * 设置维修工单的数据
	 * @param isShow
	 */
	private void setGroupTaskRepairData(boolean isEmpty){
		if(isEmpty){
			etResult.setText("");
			etFinishTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(new Date()));
			etThing.setText("");
			etMoney.setText("");
		}else {
			if(methodName.equals(RepairManageActivity.METHOD_REPAIRTASK_STRING)){
				if(selectedData.get("RepairedTime").equals("")){
					etFinishTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(new Date()));
				}else {
					etFinishTime.setText(selectedData.get("RepairedTime"));
				}
			}else {
				etFinishTime.setText(selectedData.get("RepairedTime"));
			}
			etResult.setText(selectedData.get("RepairDetail"));
			etThing.setText(selectedData.get("AccessoryUsed"));
			etMoney.setText(selectedData.get("RepairCost"));
		}
	}
	
	/**
	 * 设置维修单维修部门是否显示
	 * @param isShow
	 */
	private void setGroupRepairShow(boolean isShow){
		mGroupInfo.setVisibility(isShow?View.VISIBLE:View.GONE);
	}
	
//	/**
//	 * 设置审核信息的数据
//	 * @param isEmpty
//	 */
//	private void setGroupVerifyData(boolean isEmpty){
//		if(isEmpty){
//			swVerifyResult.setChecked(false);
//			etVerifyPeople.setText("");
//			etEquipmentOpinion.setText("");
//			etProductionOpinion.setText("");
//			etPlantOpinion.setText("");
//		}else {
//			if(selectedData.get("ApproveResult").equals("1")){
//				swVerifyResult.setChecked(true);
//			}else {
//				swVerifyResult.setChecked(false);
//			}
//			etVerifyPeople.setText(selectedData.get("ApprovePersonRealName"));
//			etEquipmentOpinion.setText(selectedData.get("DDOpinion"));
//			etProductionOpinion.setText(selectedData.get("PDOpinion"));
//			etPlantOpinion.setText(selectedData.get("PMOpinion"));
//		}
//	}
	
	/**
	 * 设备科长审核意见的数据
	 * @param isEmpty
	 */
	private void setVerifyDDData(boolean isEmpty){
		if(isEmpty){
			swVerifyResult.setChecked(false);
			etVerifyPeople.setText("");
			etEquipmentOpinion.setText("");
		}else {
			if(methodName.equals(RepairManageActivity.METHOD_DDAPPROVE_STRING)){
				if(selectedData.get("ApprovePersonRealName").equals("")){
					etVerifyPeople.setText(SystemParams.getInstance().getLoggedUserInfo().get("RealUserName"));
				}else {
					etVerifyPeople.setText(selectedData.get("ApprovePersonRealName"));
				}
			}else {
				etVerifyPeople.setText(selectedData.get("ApprovePersonRealName"));
			}
			if(selectedData.get("ApproveResult").equals("1")){
				swVerifyResult.setChecked(true);
			}else {
				swVerifyResult.setChecked(false);
			}
			etEquipmentOpinion.setText(selectedData.get("DDOpinion"));
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
			etProductionOpinion.setText(selectedData.get("PDOpinion"));
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
			etPlantOpinion.setText(selectedData.get("PMOpinion"));
		}
	}
	/**
	 * 审核部分是否显示
	 * @param isShow
	 */
	private void setGroupVerifyShow(boolean isShow){
		mGroupVerify.setVisibility(isShow?View.VISIBLE:View.GONE);
	}
	
	/**
	 * 审核部分中厂长审核是否显示
	 * @param isShow
	 */
	private void setGroupVerifyPMShow(boolean isShow){
		trPlantOpinion.setVisibility(isShow?View.VISIBLE:View.GONE);
	}
	
//	/**
//	 * 审核部分中设备科长审核是否显示
//	 * @param isShow
//	 */
//	private void setGroupVerifyDDShow(boolean isShow){
//		trEquipmentOpinion.setVisibility(isShow?View.VISIBLE:View.GONE);
//	}
	
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
	 * 跳转 数据填报界面
	 * @param data
	 */
	private void startItemDataActivity(HashMap<String, String> data,String methodName){
		Intent intent = new Intent(this,RepairManageItemDataActivity.class);
		intent.putExtra("data", data);
		intent.putExtra("MethodName", methodName);
		startActivityForResult(intent, 0);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (mRequestCode!=RepairManageActivity.REPAIRMANAGE_HISTORY_INTEGER) {
			if(!methodName.equals(RepairManageActivity.METHOD_ADD_STRING)&&receivedData.get("CanUpdate").equals("true")){
				int positionID = Integer.valueOf(SystemParams.getInstance().getLoggedUserInfo().get("PositionID"));
				int taskState = Integer.valueOf(receivedData.get("State"));
//				int taskType = Integer.valueOf(mData.get(info.position-1).get("RepairTaskType"));
				switch (positionID) {
				case EnumList.UserRole.USERROLEPLANTER:
					getMenuInflater().inflate(R.menu.cm_rm_pm, menu);
					break;
				case EnumList.UserRole.USERROLEEQUIPMENTOPERATION:
				case EnumList.UserRole.USERROLEPRODUCTIONOPERATION:
					getMenuInflater().inflate(R.menu.cm_rm_op, menu);
					break;
				case EnumList.UserRole.USERROLEEQUIPMENTCHIEF:
					getMenuInflater().inflate(R.menu.cm_rm_dd, menu);
					if(taskState==EnumList.RepairState.STATEHASBEENCONFIRMED||
					taskState==EnumList.RepairState.STATEHASBEENREPORTED||
					taskState==EnumList.RepairState.STATEHASBEENDISTRIBUTED){
						menu.getItem(0).setVisible(true);
						menu.getItem(1).setVisible(false);
					}else {
						menu.getItem(0).setVisible(false);
						menu.getItem(1).setVisible(true);
					}
					break;
				case EnumList.UserRole.USERROLEPRODUCTIONCHIEF:
					getMenuInflater().inflate(R.menu.cm_rm_pd, menu);
					if(taskState==EnumList.RepairState.STATEHASBEENREPORTED){
						menu.getItem(0).setVisible(true);
						menu.getItem(1).setVisible(false);
					}else {
						menu.getItem(0).setVisible(false);
						menu.getItem(1).setVisible(true);
					}
					break;
				case EnumList.UserRole.USERROLEREPAIRMAN:
					getMenuInflater().inflate(R.menu.cm_rm_rm, menu);
					if(taskState==EnumList.RepairState.STATEHASBEENDISTRIBUTED){
						menu.getItem(0).setVisible(true);
						menu.getItem(1).setVisible(false);
					}else {
						menu.getItem(0).setVisible(false);
						menu.getItem(1).setVisible(true);
					}
					break;
				}
			}
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();	
			break;
		case R.id.cm_rm_op_update:
			startItemDataActivity(selectedData,RepairManageActivity.METHOD_UPDATE_STRING);
			break;
		case R.id.cm_rm_dd_send:
			startItemDataActivity(selectedData,RepairManageActivity.METHOD_SENDTASK_STRING);
			break;
		case R.id.cm_rm_dd_approve:
			startItemDataActivity(selectedData,RepairManageActivity.METHOD_DDAPPROVE_STRING);
			break;
		case R.id.cm_rm_pd_approve:
			startItemDataActivity(selectedData,RepairManageActivity.METHOD_PDAPPROVE_STRING);
			break;
		case R.id.cm_rm_rm_repair:
			startItemDataActivity(selectedData,RepairManageActivity.METHOD_REPAIRTASK_STRING);
			break;
		case R.id.cm_rm_pm_confirm:
			startItemDataActivity(selectedData,RepairManageActivity.METHOD_PMAPPROVE_STRING);
			break;
		case R.id.cm_rm_pd_confirm:
			if(mUpdateConfirm==null){
				mUpdateConfirm = new AlertDialog.Builder(RepairManageItemActivity.this);
			}
			mUpdateConfirm.setTitle("确认").setMessage("确认上报工单吗？");
			mUpdateConfirm.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					methodName = RepairManageActivity.METHOD_PDCONFIRM_STRING;
					updateServerTask(RepairManageActivity.METHOD_PDCONFIRM_STRING);
				}
			}).setNegativeButton("取消", null);
			mUpdateConfirm.create();
			mUpdateConfirm.show();
			break;
		case R.id.cm_rm_rm_receive:
			if(mUpdateConfirm==null){
				mUpdateConfirm = new AlertDialog.Builder(RepairManageItemActivity.this);
			}
			mUpdateConfirm.setTitle("确认").setMessage("确认维修吗？");
			mUpdateConfirm.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					methodName = RepairManageActivity.METHOD_RECEIVE_STRING;
					updateServerTask(RepairManageActivity.METHOD_RECEIVE_STRING);
				}
			}).setNegativeButton("取消", null);
			mUpdateConfirm.create();
			mUpdateConfirm.show();
			break;
		case R.id.cm_rm_op_delete:
			if(mUpdateConfirm==null){
				mUpdateConfirm = new AlertDialog.Builder(RepairManageItemActivity.this);
			}
			mUpdateConfirm.setTitle("确认").setMessage("确认删除吗？");
			mUpdateConfirm.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					methodName = RepairManageActivity.METHOD_DELETE_STRING;
					updateServerTask(RepairManageActivity.METHOD_DELETE_STRING);
				}
			}).setNegativeButton("取消", null);
			mUpdateConfirm.create();
			mUpdateConfirm.show();
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
	
	class UpdateServerData extends AsyncTask<String, String, String>{
		@Override
		protected String doInBackground(String... params) {
			methodName = params[0];
			JSONObject param = new JSONObject();
			String result = DataCenterHelper.RESPONSE_FALSE_STRING;
			try {
				if(methodName.equals(RepairManageActivity.METHOD_DELETE_STRING)){
					param.put("RepairTaskID", Integer.valueOf(selectedData.get("RepairTaskID")));
					result = DataCenterHelper.HttpPostData("RemoveRepairTask", param);
				}else if (methodName.equals(RepairManageActivity.METHOD_RECEIVE_STRING)) {
					param.put("RepairTaskID", Integer.valueOf(selectedData.get("RepairTaskID")));
					param.put("OldState", Integer.valueOf(selectedData.get("State")));
					result = DataCenterHelper.HttpPostData("ConfirmRepair", param);
				}else if (methodName.equals(RepairManageActivity.METHOD_PDCONFIRM_STRING)) {
					param.put("RepairTaskID", Integer.valueOf(selectedData.get("RepairTaskID")));
					param.put("CheckPerson", SystemParams.getInstance().getLoggedUserInfo().get("UserID"));
					param.put("OldState", Integer.valueOf(selectedData.get("State")));
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
						if(methodName.equals(RepairManageActivity.METHOD_DELETE_STRING)){
						}else if (methodName.equals(RepairManageActivity.METHOD_RECEIVE_STRING)) {
							selectedData.put("State",EnumList.RepairState.STATEBEENINGREPAIRED+"");
							selectedData.put("StateDescription", EnumList.RepairState.BEENINGREPAIRED.getStateDescription());
						}else if(methodName.equals(RepairManageActivity.METHOD_PDCONFIRM_STRING)){
							selectedData.put("State",EnumList.RepairState.STATEHASBEENCONFIRMED+"");
							selectedData.put("StateDescription", EnumList.RepairState.HASBEENCONFIRMED.getStateDescription());
							selectedData.put("CanUpdate", "false");
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
