package com.env.dcwater.activity;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
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
import com.env.dcwater.util.OperationMethod;
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
	private AlertDialog.Builder mUpdateConfirm,mHandleContent,stateListDialog;
	private ActionBar mActionBar;
	private HashMap<String, String> receivedData;
	private String [] backStates;
	private Intent receivedIntent;
	private Date mFaultTime,mFinishTime;
	private UpdateServerData updateServerData;
	private int mRequestCode,taskState,taskType,selectState;
	private TableLayout mGroupInfo,mGroupVerify;
	private ViewStub viewStub;
	private String mOtherStep="",mHandleStep="",methodName="",methodDesc="",confirmMessage;
	private String [] handleStepContent = {"尝试手动启动","关闭主电源","拍下急停按钮","悬挂警示标识牌","关闭故障设备工艺段进水"};
	private boolean [] handleStepSelected = {false,false,false,false,false},tempStepSelected;
	private TextView etName,etType,etSN,etPosition,etStartTime,etManufacture,etFaultTime,etHandleStep,etPeople,etFaultPhenomenon,etOtherStep,etSendTime,etSender,etTimeCost,etContent,etResult,etFinishTime,etThing,etMoney,etVerifyPeople,etEquipmentOpinion,etProductionOpinion,etPlantOpinion;
	private TableRow trName,trFaultTime,trFaultPhenomenon,trHandleStep,trOtherStep,trResult,trFinishTime,trThingCost,trMoneyCost;
	private TextView EPtvMoney,EPtvVerifyPerson,EPtvDDOpinion,EPtvPDOpinion,EPtvPMOpinion,EPtvResult,EPtvFinishTime,EPtvThing,EPtvSendTime,EPtvSender,EPtvTime,EPtvContent,EPtvBackState;
	private TableRow EPtrMoney,EPtrDDOpinion,EPtrPDOpinion,EPtrPMOpinion,EPtrResult,EPtrFinishTime,EPtrThing,EPtrTime,EPtrContent,EPtrBackState;
	private Switch swVerifyResult,EPswVerify,swPMResult,EPswResult;
	private Button confirmSubmit,inputSubmit;
	private LinearLayout pmGroup,pdGroup;
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
					int positionID = Integer.valueOf(SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("PositionID"));
					int taskState = Integer.valueOf(receivedData.get("State"));
					switch (positionID) {
					case EnumList.UserRole.USERROLEPLANTER:
						methodName = RepairManageActivity.METHOD_PMAPPROVE_STRING;
						GetAllowStateListByPosition getAllowStateListByPosition = new GetAllowStateListByPosition();
						getAllowStateListByPosition.execute("");
						break;
					case EnumList.UserRole.USERROLEEQUIPMENTOPERATION:
					case EnumList.UserRole.USERROLEPRODUCTIONOPERATION:
						methodName = RepairManageActivity.METHOD_ADD_STRING;
						break;
					case EnumList.UserRole.USERROLEEQUIPMENTCHIEF:
						if(taskState==EnumList.RepairState.STATEHASBEENCONFIRMED||
						taskState==EnumList.RepairState.STATEHASBEENREPORTED){
							methodName = RepairManageActivity.METHOD_SENDTASK_STRING;
						}else if (taskState==EnumList.RepairState.STATEHASBEENREPAIRED) {
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
			confirmMessage = "确定将故障信息上报吗？";
		}else if (methodName.equals(RepairManageActivity.METHOD_HISTORY_STRING)) {
			mActionBar.setTitle(receivedData.get("FaultReportSN")+"详情");
		}else if (methodName.equals(RepairManageActivity.METHOD_DDAPPROVE_STRING)) {
			mActionBar.setTitle("设备科长审核");
			methodDesc = "提交审核";
			confirmMessage = "确定提交设备科长审核信息吗？";
		}else if (methodName.equals(RepairManageActivity.METHOD_PDAPPROVE_STRING)) {
			mActionBar.setTitle("生产科长审核");
			methodDesc = "提交审核";
			confirmMessage = "确定提交生产科长审核信息吗？";
		}else if (methodName.equals(RepairManageActivity.METHOD_PDCONFIRM_STRING)) {
			mActionBar.setTitle("确认报修单");
			methodDesc = "确认报修单";
			confirmMessage = "确认该报修单吗？";
		}else if (methodName.equals(RepairManageActivity.METHOD_PMAPPROVE_STRING)) {
			mActionBar.setTitle("厂长审核");
			methodDesc = "提交审核";
			confirmMessage = "确定提交长生审核信息吗？";
		}else if (methodName.equals(RepairManageActivity.METHOD_RECEIVE_STRING)) {
			mActionBar.setTitle("确认维修单");
			methodDesc = "确认维修单";
			confirmMessage = "确定接收该维修单吗？";
		}else if (methodName.equals(RepairManageActivity.METHOD_REPAIRTASK_STRING)) {
			mActionBar.setTitle("填写维修单");
			methodDesc = "填写维修单";
			confirmMessage = "确定提交填写的维修单信息吗？";
		}else if (methodName.equals(RepairManageActivity.METHOD_REREPAIRTASK_STRING)) {
			mActionBar.setTitle("修改维修单");
			methodDesc = "修改维修单";
			confirmMessage = "确定提交修改的维修单信息吗？";
		}else if (methodName.equals(RepairManageActivity.METHOD_SENDTASK_STRING)) {
			mActionBar.setTitle("派发维修单");
			methodDesc = "派发维修单";
			confirmMessage = "确定派发维修单吗？";
		}
	}
	
	
	/**
	 * 初始化各个控件
	 */
	private void findAndIniView() {

		etName = (TextView) findViewById(R.id.activity_repairmanageitem_name);
		etFaultTime = (TextView) findViewById(R.id.activity_repairmanageitem_faulttime);
		etFaultPhenomenon = (TextView) findViewById(R.id.activity_repairmanageitem_faultphenomenon);
		etHandleStep = (TextView) findViewById(R.id.activity_repairmanageitem_handlestep);
		etOtherStep = (TextView) findViewById(R.id.activity_repairmanageitem_otherstep);
		etPeople = (TextView) findViewById(R.id.activity_repairmanageitem_taskpeople);

		mGroupInfo = (TableLayout) findViewById(R.id.activity_repairmanageitem_repairgroupinfo);
		mGroupVerify = (TableLayout) findViewById(R.id.activity_repairmanageitem_repairgroupverify);

		etType = (TextView) findViewById(R.id.activity_repairmanageitem_type);

		etSN = (TextView) findViewById(R.id.activity_repairmanageitem_sn);

		etPosition = (TextView) findViewById(R.id.activity_repairmanageitem_position);

		etStartTime = (TextView) findViewById(R.id.activity_repairmanageitem_starttime);

		etManufacture = (TextView) findViewById(R.id.activity_repairmanageitem_manufacturer);

		etSendTime = (TextView) findViewById(R.id.activity_repairmanageitem_repairsttime);

		etSender = (TextView) findViewById(R.id.activity_repairmanageitem_repairpeople);
		etTimeCost = (TextView) findViewById(R.id.activity_repairmanageitem_repairtimecost);

		etContent = (TextView) findViewById(R.id.activity_repairmanageitem_repaircontent);

		trResult = (TableRow) findViewById(R.id.activity_repairmanageitem_repairresult_tr);
		etResult = (TextView) findViewById(R.id.activity_repairmanageitem_repairresult);

		trFinishTime = (TableRow) findViewById(R.id.activity_repairmanageitem_repairendtime_tr);
		etFinishTime = (TextView) findViewById(R.id.activity_repairmanageitem_repairendtime);

		trThingCost = (TableRow) findViewById(R.id.activity_repairmanageitem_repairthingcost_tr);
		etThing = (TextView) findViewById(R.id.activity_repairmanageitem_repairthingcost);

		trMoneyCost = (TableRow) findViewById(R.id.activity_repairmanageitem_repairmoneycost_tr);
		etMoney = (TextView) findViewById(R.id.activity_repairmanageitem_repairmoneycost);

		swVerifyResult = (Switch) findViewById(R.id.activity_repairmanageitem_verifyresult);
		swPMResult = (Switch)findViewById(R.id.activity_repairmanageitem_plantverify);
		
		etVerifyPeople = (TextView) findViewById(R.id.activity_repairmanageitem_verifypeople);

		etEquipmentOpinion = (TextView) findViewById(R.id.activity_repairmanageitem_equipmentopinion);

		etProductionOpinion = (TextView) findViewById(R.id.activity_repairmanageitem_productionopinion);

		etPlantOpinion = (TextView) findViewById(R.id.activity_repairmanageitem_plantopinion);
		
		pmGroup = (LinearLayout)findViewById(R.id.activity_repairmanageitem_plantgroup);
		
		pdGroup = (LinearLayout)findViewById(R.id.activity_repairmanageitem_productiongroup);

		if (methodName.equals(RepairManageActivity.METHOD_DETAIL_STRING) || methodName.equals(RepairManageActivity.METHOD_HISTORY_STRING)) {

		} else {


			if (methodName.equals(RepairManageActivity.METHOD_ADD_STRING)) {

				trName = (TableRow) findViewById(R.id.activity_repairmanageitem_name_tr);
				trFaultTime = (TableRow) findViewById(R.id.activity_repairmanageitem_faulttime_tr);
				trFaultPhenomenon = (TableRow) findViewById(R.id.activity_repairmanageitem_faultphenomenon_tr);
				trHandleStep = (TableRow) findViewById(R.id.activity_repairmanageitem_handlestep_tr);
				trOtherStep = (TableRow) findViewById(R.id.activity_repairmanageitem_otherstep_tr);
				inputSubmit = (Button)findViewById(R.id.activity_repairmanageitem_submit);
				inputSubmit.setText(methodDesc);
				trName.setOnClickListener(this);
				trFaultTime.setOnClickListener(this);
				trFaultPhenomenon.setOnClickListener(this);
				trHandleStep.setOnClickListener(this);
				trOtherStep.setOnClickListener(this);
				inputSubmit.setOnClickListener(this);

			} else {
				
				viewStub = (ViewStub) findViewById(R.id.activity_repairmanageitem_editpart);
				
				if (methodName.equals(RepairManageActivity.METHOD_SENDTASK_STRING)) {
					viewStub.setLayoutResource(R.layout.view_repair_send);
					viewStub.inflate();

					EPtvSendTime = (TextView) findViewById(R.id.view_repair_send_repairsttime);
					EPtvSendTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING, Locale.CHINA).format(new Date()));
					EPtvSender = (TextView) findViewById(R.id.view_repair_send_repairpeople);
					EPtvSender.setText(SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("RealUserName"));
					EPtvTime = (TextView) findViewById(R.id.view_repair_send_repairtimecost);
					EPtvTime.setText(receivedData.get("RequiredManHours"));
					EPtvContent = (TextView) findViewById(R.id.view_repair_send_repaircontent);
					EPtvContent.setText(receivedData.get("TaskDetail"));

					EPtrTime = (TableRow) findViewById(R.id.view_repair_send_repairtimecost_tr);
					EPtrContent = (TableRow) findViewById(R.id.view_repair_send_repaircontent_tr);

					EPtrTime.setOnClickListener(this);
					EPtrContent.setOnClickListener(this);

				} else if (methodName.equals(RepairManageActivity.METHOD_RECEIVE_STRING) || methodName.equals(RepairManageActivity.METHOD_PDCONFIRM_STRING)) {
					confirmSubmit = (Button) findViewById(R.id.activity_repairmanageitem_submit);
					confirmSubmit.setText(methodDesc);
					confirmSubmit.setOnClickListener(this);
					confirmSubmit.setVisibility(View.VISIBLE);

				} else if (methodName.equals(RepairManageActivity.METHOD_PMAPPROVE_STRING)) {
					viewStub.setLayoutResource(R.layout.view_repair_pmopinion);
					viewStub.inflate();
					
					EPswResult = (Switch)findViewById(R.id.view_repair_pmopinion_pmverify);
					
					EPtrBackState = (TableRow)findViewById(R.id.view_repair_pmopinion_state_tr);
					EPtrBackState.setOnClickListener(this);
					EPtvBackState = (TextView)findViewById(R.id.view_repair_pmopinion_state);

					EPtvPMOpinion = (TextView) findViewById(R.id.view_repair_pmopinion_opinion);
					EPtvPMOpinion.setText(receivedData.get("PMOpinion"));

					EPtrPMOpinion = (TableRow) findViewById(R.id.view_repair_pmopinion_opinion_tr);

					EPtrPMOpinion.setOnClickListener(this);
					EPswResult.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							EPtrBackState.setVisibility(isChecked?View.GONE:View.VISIBLE);
						}
					});
					EPtvBackState.setText(getResources().getStringArray(R.array.pm_allow_state)[0]);

				} else if (methodName.equals(RepairManageActivity.METHOD_PDAPPROVE_STRING)) {
					viewStub.setLayoutResource(R.layout.view_repair_pdopinion);
					viewStub.inflate();

					EPtvPDOpinion = (TextView) findViewById(R.id.view_repair_pdopinion_opinion);
					EPtvPDOpinion.setText(receivedData.get("PDOpinion"));

					EPtrPDOpinion = (TableRow) findViewById(R.id.view_repair_pdopinion_opinion_tr);

					EPtrPDOpinion.setOnClickListener(this);

				} else if (methodName.equals(RepairManageActivity.METHOD_DDAPPROVE_STRING)) {
					viewStub.setLayoutResource(R.layout.view_repair_ddopinion);
					viewStub.inflate();

					EPtvMoney = (TextView) findViewById(R.id.view_repair_ddopinion_money);
					EPtvMoney.setText(receivedData.get("RepairCost"));
					EPswVerify = (Switch) findViewById(R.id.view_repair_ddopinion_verifyresult);
					EPswVerify.setChecked(receivedData.get("ApproveResult").equals("1") ? true : false);
					EPtvVerifyPerson = (TextView) findViewById(R.id.view_repair_ddopinion_verifypeople);
					EPtvVerifyPerson.setText(SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("RealUserName"));
					EPtvDDOpinion = (TextView) findViewById(R.id.view_repair_ddopinion_opinion);
					EPtvDDOpinion.setText(receivedData.get("DDOpinion"));

					EPtrMoney = (TableRow) findViewById(R.id.view_repair_ddopinion_money_tr);
					EPtrDDOpinion = (TableRow) findViewById(R.id.view_repair_ddopinion_opinion_tr);

					EPtrMoney.setOnClickListener(this);
					EPtrDDOpinion.setOnClickListener(this);

				} else if (methodName.equals(RepairManageActivity.METHOD_REREPAIRTASK_STRING) || methodName.equals(RepairManageActivity.METHOD_REPAIRTASK_STRING)) {
					viewStub.setLayoutResource(R.layout.view_repair_report);
					viewStub.inflate();

					EPtvResult = (TextView) findViewById(R.id.view_repair_report_result);
					EPtvResult.setText(receivedData.get("RepairDetail"));
					EPtvFinishTime = (TextView) findViewById(R.id.view_repair_report_finishtime);
					if (receivedData.get("RepairedTime").equals("")) {
						EPtvFinishTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING, Locale.CHINA).format(new Date()));
					} else {
						EPtvFinishTime.setText(receivedData.get("RepairedTime"));
					}
					EPtvThing = (TextView) findViewById(R.id.view_repair_report_thing);
					EPtvThing.setText(receivedData.get("AccessoryUsed"));

					EPtrResult = (TableRow) findViewById(R.id.view_repair_report_result_tr);
					EPtrFinishTime = (TableRow) findViewById(R.id.view_repair_report_finishtime_tr);
					EPtrThing = (TableRow) findViewById(R.id.view_repair_report_thing_tr);

					EPtrResult.setOnClickListener(this);
					EPtrFinishTime.setOnClickListener(this);
					EPtrThing.setOnClickListener(this);
				}
				if(!methodName.equals(RepairManageActivity.METHOD_RECEIVE_STRING) && !methodName.equals(RepairManageActivity.METHOD_PDCONFIRM_STRING)){
					inputSubmit = (Button) findViewById(R.id.view_repair_submit);
					inputSubmit.setText(methodDesc);
					inputSubmit.setOnClickListener(this);
				}
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
			int userPositionID = Integer.valueOf(SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("PositionID"));
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
				setGroupVerifyPDShow(false);
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
		etPeople.setText(SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("RealUserName"));
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
			etPeople.setText(SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("RealUserName"));
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
			etSender.setText(SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("RealUserName"));
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
					etSender.setText(SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("RealUserName"));
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
					etVerifyPeople.setText(SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("RealUserName"));
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
			swPMResult.setChecked(true);
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
		pmGroup.setVisibility(isShow?View.VISIBLE:View.GONE);
	}
	
	
	/**
	 * 审核部分中生产科审核是否显示
	 * @param isShow
	 */
	private void setGroupVerifyPDShow(boolean isShow){
		pdGroup.setVisibility(isShow?View.VISIBLE:View.GONE);
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
			mProgressDialog.setCancelable(cancelable);
		}
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
		getMenuInflater().inflate(R.menu.menu_repairmanageitem, menu);
		if(mRequestCode==RepairManageActivity.REPAIRMANAGE_HISTORY_INTEGER){
			menu.findItem(R.id.menu_repairmanageitem_flow).setVisible(false);
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();	
			break;
		case R.id.menu_repairmanageitem_flow:
			Intent flow = new Intent(TaskStateFlowActivity.ACTION_STRING);
			flow.putExtra("data", receivedData);
			startActivityForResult(flow, 0);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.menu_repairmanageitem_workflow:
			Intent workflow = new Intent(TaskStateWorkFlowActivity.ACTION_STRING);
			workflow.putExtra("data", receivedData);
			startActivityForResult(workflow, 0);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
		if(requestCode==0&&resultCode==RESULT_OK){
			@SuppressWarnings("unchecked")
			HashMap<String, String> temp = (HashMap<String, String>)data.getSerializableExtra("data");
			String key = temp.get("Key");
			String value = temp.get("Value");
			if (key.equals("AccidentDetail")) {
				etFaultPhenomenon.setText(value);
				if(!value.equals("")){
					etFaultPhenomenon.setCompoundDrawables(null, null, null, null);
				}
			}else if(key.equals("Otherstep")){
				etOtherStep.setText(value);
				receivedData.put("EmergencyMeasures", combineEmergencyMeasures());
				if(!value.equals("")){
					etOtherStep.setCompoundDrawables(null, null, null, null);
				}
			}else if (key.equals("RequiredManHours")) {
				try {
					Integer.parseInt(value);
					EPtvTime.setCompoundDrawables(null, null, null, null);
					EPtvTime.setText(value);
				} catch (Exception e) {
				}
			}else if (key.equals("TaskDetail")) {
				EPtvContent.setText(value);
				if(!value.equals("")){
					EPtvContent.setCompoundDrawables(null, null, null, null);
				}
			}else if (key.equals("RepairDetail")){
				EPtvResult.setText(value);
				if(!value.equals("")){
					EPtvResult.setCompoundDrawables(null, null, null, null);
				}
			}else if (key.equals("AccessoryUsed")) {
				EPtvThing.setText(value);
				if(!value.equals("")){
					EPtvThing.setCompoundDrawables(null, null, null, null);
				}
			}else if (key.equals("RepairCost")) {
				try {
					Integer.parseInt(value);
					EPtvMoney.setCompoundDrawables(null, null, null, null);
					EPtvMoney.setText(value);
				} catch (Exception e) {
					
				}
			}else if (key.equals("DDOpinion")) {
				EPtvDDOpinion.setText(value);
				if(!value.equals("")){
					EPtvDDOpinion.setCompoundDrawables(null, null, null, null);
				}
			}else if (key.equals("PDOpinion")) {
				EPtvPDOpinion.setText(value);
				if(!value.equals("")){
					EPtvPDOpinion.setCompoundDrawables(null, null, null, null);
				}
			}else if (key.equals("PMOpinion")) {
				EPtvPMOpinion.setText(value);
				if(!value.equals("")){
					EPtvPMOpinion.setCompoundDrawables(null, null, null, null);
				}
			}
			receivedData.put(key, value);
		}else if (requestCode==1&&resultCode==RESULT_OK) {
			@SuppressWarnings("unchecked")
			HashMap<String, String> temp = (HashMap<String, String>)data.getSerializableExtra("data");
			receivedData.put("DeviceID", temp.get("DeviceID"));
			receivedData.put("DeviceName", temp.get("DeviceName"));
			etName.setText(receivedData.get("DeviceName"));
			if(!etName.getText().equals("")){
				etName.setCompoundDrawables(null, null, null, null);
			}
		}
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
				etFaultTime.setCompoundDrawables(null, null, null, null);
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
			case R.id.view_repair_report_finishtime_tr:
				EPtvFinishTime.setCompoundDrawables(null, null, null, null);
				if(!EPtvFinishTime.getText().toString().equals("")){
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
							EPtvFinishTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(mFinishTime));
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
							etHandleStep.setCompoundDrawables(null, null, null, null);
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
			case R.id.view_repair_send_repairtimecost_tr:
				HashMap<String, String> repairtimecost = new HashMap<String, String>();
				repairtimecost.put("Key", "RequiredManHours");
				repairtimecost.put("Name", "所需工时");
				repairtimecost.put("Value", receivedData.get("RequiredManHours"));
				startDataInputActivity(repairtimecost);
				break;
			case R.id.view_repair_send_repaircontent_tr:
				HashMap<String, String> repaircontent = new HashMap<String, String>();
				repaircontent.put("Key", "TaskDetail");
				repaircontent.put("Name", "维修内容及要求");
				repaircontent.put("Value", receivedData.get("TaskDetail"));
				startDataInputActivity(repaircontent);
				break;
			case R.id.view_repair_report_result_tr:
				HashMap<String, String> repairresult = new HashMap<String, String>();
				repairresult.put("Key", "RepairDetail");
				repairresult.put("Name", "工作完成情况及处理措施");
				repairresult.put("Value", receivedData.get("RepairDetail"));
				startDataInputActivity(repairresult);
				break;
			case R.id.view_repair_report_thing_tr:
				HashMap<String, String> repairthingcost = new HashMap<String, String>();
				repairthingcost.put("Key", "AccessoryUsed");
				repairthingcost.put("Name", "物品备件使用情况");
				repairthingcost.put("Value", receivedData.get("AccessoryUsed"));
				startDataInputActivity(repairthingcost);
				break;
			case R.id.view_repair_ddopinion_money_tr:
				HashMap<String, String> repairmoneycost = new HashMap<String, String>();
				repairmoneycost.put("Key", "RepairCost");
				repairmoneycost.put("Name", "维修金额");
				repairmoneycost.put("Value", receivedData.get("RepairCost"));
				startDataInputActivity(repairmoneycost);
				break;
			case R.id.view_repair_ddopinion_opinion_tr:
				HashMap<String, String> equipmentopinion = new HashMap<String, String>();
				equipmentopinion.put("Key", "DDOpinion");
				equipmentopinion.put("Name", "设备科意见");
				equipmentopinion.put("Value", receivedData.get("DDOpinion"));
				startDataInputActivity(equipmentopinion);
				break;
			case R.id.view_repair_pdopinion_opinion_tr:
				HashMap<String, String> productionopinion = new HashMap<String, String>();
				productionopinion.put("Key", "PDOpinion");
				productionopinion.put("Name", "生产科意见");
				productionopinion.put("Value", receivedData.get("PDOpinion"));
				startDataInputActivity(productionopinion);
				break;
			case R.id.view_repair_pmopinion_opinion_tr:
				HashMap<String, String> plantopinion = new HashMap<String, String>();
				plantopinion.put("Key", "PMOpinion");
				plantopinion.put("Name", "厂领导意见");
				plantopinion.put("Value", receivedData.get("PMOpinion"));
				startDataInputActivity(plantopinion);
				break;
			case R.id.view_repair_pmopinion_state_tr:
				if(backStates==null){
					backStates = getResources().getStringArray(R.array.pm_allow_state);
				}
				if(stateListDialog==null){
					stateListDialog = new AlertDialog.Builder(RepairManageItemActivity.this);
					stateListDialog.setTitle("请选择返回的工单状态").setCancelable(true);
				}
				stateListDialog.setSingleChoiceItems(backStates, selectState, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectState = which;
						EPtvBackState.setText(backStates[selectState]);
						EPtvBackState.setCompoundDrawables(null,null,null,null);
						dialog.dismiss();
					}
				});
				stateListDialog.show();
				break;
			case R.id.activity_repairmanageitem_submit:
			case R.id.view_repair_submit:
				if(mUpdateConfirm==null){
					mUpdateConfirm = new AlertDialog.Builder(RepairManageItemActivity.this);
				}
				mUpdateConfirm.setTitle(methodDesc).setMessage(confirmMessage);
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
					JSONObject repairConfirm = new JSONObject();
					repairConfirm.put("RepairConfirmPerson", SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("UserID"));
					repairConfirm.put("RepairConfirmTime", new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING, Locale.CHINA).format(new Date()));
					param.put("RepairDataString", repairConfirm.toString());
					param.put("State", EnumList.RepairState.STATEBEENINGREPAIRED);
					param.put("OldState", Integer.valueOf(receivedData.get("State")));
					result = DataCenterHelper.HttpPostData("ConfirmRepair", param);
				}else if (methodName.equals(RepairManageActivity.METHOD_PDCONFIRM_STRING)) {
					param.put("RepairTaskID", Integer.valueOf(receivedData.get("RepairTaskID")));
					JSONObject PDConfirm = new JSONObject();
					PDConfirm.put("PDConfirmPerson", SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("UserID"));
					PDConfirm.put("PDConfirmTime", new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING, Locale.CHINA).format(new Date()));
					param.put("RepairDataString", PDConfirm.toString());
					param.put("CheckPerson", SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("UserID"));
					param.put("OldState", Integer.valueOf(receivedData.get("State")));
					result = DataCenterHelper.HttpPostData("ValidationReport", param);
				}else if(methodName.equals(RepairManageActivity.METHOD_ADD_STRING)){
					param.put("PlantID", SystemParams.PLANTID_INT);
					param.put("RepairTaskID","");
					JSONObject repairDataString = new JSONObject();
					int postionID = Integer.valueOf(SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("PositionID"));
					if(postionID==EnumList.UserRole.REPAIRMAN.getState()){
						repairDataString.put("RepairTaskType", EnumList.RepairTaskType.EQUIPMENTSECTION.getType());
					}else if (postionID==EnumList.UserRole.PRODUCTIONOPERATION.getState()) {
						repairDataString.put("RepairTaskType", EnumList.RepairTaskType.PRODUCTIONSECTION.getType());
					}
					String EmergencyMeasures = combineEmergencyMeasures();
					repairDataString.put("DeviceID", receivedData.get("DeviceID"));
					repairDataString.put("EmergencyMeasures", EmergencyMeasures);
					repairDataString.put("AccidentOccurTime", etFaultTime.getText().toString());
					repairDataString.put("AccidentDetail",etFaultPhenomenon.getText().toString());
					repairDataString.put("ReportPerson", SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("UserID"));
					param.put("RepairDataString", repairDataString.toString());
					param.put("OldState", -1);
					result = DataCenterHelper.HttpPostData("InsertRepairTaskData", param);
				}else if (methodName.equals(RepairManageActivity.METHOD_UPDATE_STRING)) {
					param.put("PlantID", SystemParams.PLANTID_INT);
					param.put("RepairTaskID", Integer.valueOf(receivedData.get("RepairTaskID")));
					JSONObject repairDataString = new JSONObject();
					String EmergencyMeasures = combineEmergencyMeasures();
					repairDataString.put("RepairTaskType", receivedData.get("RepairTaskType"));
					repairDataString.put("DeviceID", receivedData.get("DeviceID"));
					repairDataString.put("EmergencyMeasures", EmergencyMeasures);
					repairDataString.put("AccidentOccurTime", etFaultTime.getText().toString());
					repairDataString.put("AccidentDetail",etFaultPhenomenon.getText().toString());
					repairDataString.put("ReportPerson", SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("UserID"));
					param.put("RepairDataString", repairDataString.toString());
					param.put("OldState", receivedData.get("State"));
					result = DataCenterHelper.HttpPostData("InsertRepairTaskData", param);
				}else if (methodName.equals(RepairManageActivity.METHOD_SENDTASK_STRING)) {
					param.put("PlantID", SystemParams.PLANTID_INT);
					param.put("RepairTaskID", Integer.valueOf(receivedData.get("RepairTaskID")));
					JSONObject repairDataString = new JSONObject();
					repairDataString.put("TaskCreateTime", EPtvSendTime.getText().toString());
					repairDataString.put("CreatePerson", SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("UserID"));
					repairDataString.put("RequiredManHours", EPtvTime.getText().toString());
					repairDataString.put("TaskDetail", EPtvContent.getText().toString());
					param.put("RepairDataString", repairDataString.toString());
					param.put("State", EnumList.RepairState.STATEHASBEENDISTRIBUTED);
					param.put("OldState", receivedData.get("State"));
					result = DataCenterHelper.HttpPostData("UpdateRepairDataforEdit", param);
				}else if (methodName.equals(RepairManageActivity.METHOD_REPAIRTASK_STRING)) {
					param.put("PlantID", SystemParams.PLANTID_INT);
					param.put("RepairTaskID", Integer.valueOf(receivedData.get("RepairTaskID")));
					JSONObject repairDataString = new JSONObject();
					repairDataString.put("RepairDetail", EPtvResult.getText().toString());
					repairDataString.put("RepairedTime", EPtvFinishTime.getText().toString());
					repairDataString.put("AccessoryUsed", EPtvThing.getText().toString());
					repairDataString.put("RepairPerson", SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("UserID"));
					param.put("RepairDataString", repairDataString.toString());
					param.put("State", EnumList.RepairState.STATEHASBEENREPAIRED);
					param.put("OldState", receivedData.get("State"));
					result = DataCenterHelper.HttpPostData("UpdateRepairDataforWrite", param);
				}else if (methodName.equals(RepairManageActivity.METHOD_PDAPPROVE_STRING)) {
					param.put("RepairTaskID", Integer.valueOf(receivedData.get("RepairTaskID")));
					JSONObject repairDataString = new JSONObject();
					repairDataString.put("PDOpinion", EPtvPDOpinion.getText().toString());
					repairDataString.put("RepairCost", EPtvMoney.getText().toString());
					repairDataString.put("PDApprovePerson", SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("UserID"));
					repairDataString.put("PDApproveTime", new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING, Locale.CHINA).format(new Date()));
					param.put("RepairDataString", repairDataString.toString());
					param.put("State", EnumList.RepairState.STATEPRODUCTIONTHROUGH);
					param.put("OldState", receivedData.get("State"));
					result = DataCenterHelper.HttpPostData("UpdateAuditingRepairDataforUser2", param);
				}else if (methodName.equals(RepairManageActivity.METHOD_PMAPPROVE_STRING)) {
					param.put("RepairTaskID", Integer.valueOf(receivedData.get("RepairTaskID")));
					JSONObject repairDataString = new JSONObject();
					repairDataString.put("PMOpinion", EPtvPMOpinion.getText().toString());
					repairDataString.put("PMApprovePerson", SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("UserID"));
					repairDataString.put("PMApproveTime", new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING, Locale.CHINA).format(new Date()));
					param.put("RepairDataString", repairDataString.toString());
					if(EPswResult.isChecked()){
						param.put("State", EnumList.RepairState.STATEDIRECTORTHROUGH);
					}else {
						param.put("State", OperationMethod.getTaskStateByStateName(EPtvBackState.getText().toString()));
					}
					param.put("OldState", receivedData.get("State"));
					result = DataCenterHelper.HttpPostData("UpdateAuditingRepairDataforUser3", param);
				}else if (methodName.equals(RepairManageActivity.METHOD_DDAPPROVE_STRING)) {
					param.put("RepairTaskID", Integer.valueOf(receivedData.get("RepairTaskID")));
					JSONObject repairDataString = new JSONObject();
					repairDataString.put("ApproveResult", EPswVerify.isChecked()?"1":"2");
					repairDataString.put("ApprovePerson", SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("UserID"));
					repairDataString.put("ApproveTime", new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING, Locale.CHINA).format(new Date()));
					repairDataString.put("DDOpinion", EPtvDDOpinion.getText().toString());
					param.put("RepairDataString", repairDataString.toString());
					param.put("State", EPswVerify.isChecked()?EnumList.RepairState.STATEDEVICETHROUGH:EnumList.RepairState.STATEFORCORRECTION);
					param.put("OldState", receivedData.get("State"));
					result = DataCenterHelper.HttpPostData("UpdateAuditingRepairDataforUser7", param);
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
	
	private class GetAllowStateListByPosition extends AsyncTask<String, Integer, String[]>{

		@Override
		protected String[] doInBackground(String... params) {
			JSONObject param = new JSONObject();
			String result = DataCenterHelper.RESPONSE_FALSE_STRING;
			String [] states = null;
			try {
				param.put("PositionID", SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("PositionID"));
				result = DataCenterHelper.HttpPostData("GetAllowStateListByPosition", param);
				if(!result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					JSONObject jsonObject = new JSONObject(result);
					JSONArray array = new JSONArray(jsonObject.toString());
					states = new String[array.length()];
					for(int i = 0;i<array.length();i++){
						JSONObject temp  = array.getJSONObject(i);
						states[i] = temp.getString("StateText");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return states;
		}
		
		@Override
		protected void onPostExecute(String[] result) {
			super.onPostExecute(result);
			if(result!=null){
				backStates = result;
			}
		}

		
	}
}
