package com.env.dcwater.activity;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
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

/**
 * 单个报修工单界面，该界面可查看，编辑
 * @author sk
 *
 */
public class RepairManageItemActivity extends NfcActivity implements OnClickListener,OnItemClickListener{
	
	public static final String ACTION_STRING = "RepairManageItemActivity";
	private ProgressDialog mProgressDialog;
	private ActionBar mActionBar;
	private HashMap<String, String> receivedData,selectedData;
	private Intent receivedIntent;
	private int mRequestCode;
	private Date mDate;
	private Button mSubmitButton;
	private GetServerData mGetServerData;
	private InsertTask mInsertTask;
	private DrawerLayout mDrawerLayout;
	private ListView mMachineListView;
	private Builder mHandleContent;
	private MachineListItemAdapter mMachineListAdapter;
	private ArrayList<HashMap<String, String>> mMachine;
	private TableLayout mGroupBasic,mGroupFault,mGroupPeople,mGroupInfo,mGroupVerify;
	private DateTimePickerView dateTimePickerView;
	private String mOtherStep="",mHandleStep="";
	private String [] handleStepContent = {"尝试手动启动","关闭主电源","拍下急停按钮","悬挂警示标识牌","关闭故障设备工艺段进水"};
	private boolean [] handleStepSelected = {false,false,false,false,false},tempStepSelected;
	private TextView etName,etType,etSN,etPosition,etStartTime,etManufacture,etFaultTime,etHandleStep,etPeople,etFaultPhenomenon,etOtherStep,etSendTime,etSender,etTimeCost,etContent,etResult,etFinishTime,etThing,etMoney,etVerifyResult,etVerifyPeople,etEquipmentOpinion,etPlantOpinion;
	private TableRow trName,trFaultTime,trFaultPhenomenon,trHandleStep,trOtherStep,trSendTime,trSender,trTimeCost,trContent,trResult,trFinishTime,trThingCost,trMoneyCost,trVerifyResult,trVerifyPeople,trEquipmentOpinion,trPlantOpinion;
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
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		switch (mRequestCode) {
		case RepairManageActivity.REPAIRMANAGE_ADD_INTEGER:
			mActionBar.setTitle("上报故障");
			break;
		case RepairManageActivity.REPAIRMANAGE_DETAIL_INTEGER:
			mActionBar.setTitle(receivedData.get("FaultReportSN")+"详情");
			break;
		case RepairManageActivity.REPAIRMANAGE_UPDATE_INTEGER:
			mActionBar.setTitle(receivedData.get("FaultReportSN")+"修改");
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
			mDate = new Date();
			copyHandleStepSelected();
			break;
		case RepairManageActivity.REPAIRMANAGE_UPDATE_INTEGER://修改维修单
			receivedData = (HashMap<String, String>)receivedIntent.getSerializableExtra("Data");
			selectedData = receivedData;
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
			try {
				//得到该表单的故障发生时间
				mDate = new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).parse(receivedData.get("AccidentOccurTime").toString());
			} catch (Exception e) {
				mDate = new Date();
			}
			break;
		case RepairManageActivity.REPAIRMANAGE_DETAIL_INTEGER://查看维修单的详细情况
			receivedData = (HashMap<String, String>)receivedIntent.getSerializableExtra("Data");
			selectedData = receivedData;
			String [] handle2;
			if(receivedData.get("EmergencyMeasures").equals("null")){
				handle2 = new String [0]; 
			}else {
				handle2 = receivedData.get("EmergencyMeasures").split(",");
			}
			try {
				for(int i=0;i<handle2.length;i++){
					if(handle2[i].endsWith("(an)")) {
						//应急措施和措施放在一个字段里面了，带有（an）结尾的是措施，数字的是紧急措施	
						mOtherStep = handle2[i].replace("(an)", "");
					}else {
						if(!handle2[i].equals("")){
							mHandleStep = mHandleStep + handleStepContent[Integer.parseInt(handle2[i])-1] + "\n";
							//得到初始化的 应急措施
							handleStepSelected[Integer.parseInt(handle2[i])-1] = true;
						}
					}
				}
				copyHandleStepSelected();
			} catch (Exception e) {
				mOtherStep = "";
				mHandleStep ="";
			}
			try {
				//得到该表单的故障发生时间
				mDate = new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).parse(receivedData.get("AccidentOccurTime").toString());
			} catch (Exception e) {
				mDate = new Date();
			}
			break;
		}
	}
	
	/**
	 * 初始化各个控件
	 */
	private void findAndIniView(){
		
		mDrawerLayout = (DrawerLayout)findViewById(R.id.activity_repairmanageitem_drawlayout);
		
		mMachineListView = (ListView)findViewById(R.id.activity_repairmanageitem_machinelist);
		
		mGroupBasic = (TableLayout)findViewById(R.id.activity_repairmanageitem_repairgroupbasic);
		mGroupFault = (TableLayout)findViewById(R.id.activity_repairmanageitem_repairgroupfault);
		mGroupPeople = (TableLayout)findViewById(R.id.activity_repairmanageitem_repairgrouppeople);
		mGroupInfo = (TableLayout)findViewById(R.id.activity_repairmanageitem_repairgroupinfo);
		mGroupVerify = (TableLayout)findViewById(R.id.activity_repairmanageitem_repairgroupverify);
		
		etName = (TextView)mGroupBasic.findViewById(R.id.activity_repairmanageitem_name);
		trName = (TableRow)mGroupBasic.findViewById(R.id.activity_repairmanageitem_name_tr);
		
		etType = (TextView)mGroupBasic.findViewById(R.id.activity_repairmanageitem_type);
		
		etSN = (TextView)mGroupBasic.findViewById(R.id.activity_repairmanageitem_sn);
		
		etPosition = (TextView)mGroupBasic.findViewById(R.id.activity_repairmanageitem_position);
		
		etStartTime = (TextView)mGroupBasic.findViewById(R.id.activity_repairmanageitem_starttime);
		
		etManufacture = (TextView)mGroupBasic.findViewById(R.id.activity_repairmanageitem_manufacturer);
		
		etFaultTime = (TextView)mGroupFault.findViewById(R.id.activity_repairmanageitem_faulttime);
		trFaultTime = (TableRow)mGroupFault.findViewById(R.id.activity_repairmanageitem_faulttime_tr);
		
		etFaultPhenomenon = (TextView)mGroupFault.findViewById(R.id.activity_repairmanageitem_faultphenomenon);
		trFaultPhenomenon = (TableRow)mGroupFault.findViewById(R.id.activity_repairmanageitem_faultphenomenon_tr);
		
		etHandleStep = (TextView)mGroupFault.findViewById(R.id.activity_repairmanageitem_handlestep);
		trHandleStep = (TableRow)mGroupFault.findViewById(R.id.activity_repairmanageitem_handlestep_tr);
		
		etOtherStep = (TextView)mGroupFault.findViewById(R.id.activity_repairmanageitem_otherstep);
		trOtherStep = (TableRow)mGroupFault.findViewById(R.id.activity_repairmanageitem_otherstep_tr);
		
		etPeople = (TextView)mGroupPeople.findViewById(R.id.activity_repairmanageitem_taskpeople);
//		trPeople = (TableRow)findViewById(R.id.activity_repairmanageitem_taskpeople_tr);

		trSendTime = (TableRow)mGroupInfo.findViewById(R.id.activity_repairmanageitem_repairsttime_tr);
		etSendTime = (TextView)mGroupInfo.findViewById(R.id.activity_repairmanageitem_repairsttime);
		
		trSender = (TableRow)mGroupInfo.findViewById(R.id.activity_repairmanageitem_repairpeople_tr);
		etSender = (TextView)mGroupInfo.findViewById(R.id.activity_repairmanageitem_repairpeople);
		
		trTimeCost = (TableRow)mGroupInfo.findViewById(R.id.activity_repairmanageitem_repairtimecost_tr);
		etTimeCost = (TextView)mGroupInfo.findViewById(R.id.activity_repairmanageitem_repairtimecost);
		
		trContent = (TableRow)mGroupInfo.findViewById(R.id.activity_repairmanageitem_repaircontent_tr);
		etContent = (TextView)mGroupInfo.findViewById(R.id.activity_repairmanageitem_repaircontent);
		
		trResult = (TableRow)mGroupInfo.findViewById(R.id.activity_repairmanageitem_repairresult_tr);
		etResult = (TextView)mGroupInfo.findViewById(R.id.activity_repairmanageitem_repairresult);
		
		trFinishTime = (TableRow)mGroupInfo.findViewById(R.id.activity_repairmanageitem_repairendtime_tr);
		etFinishTime = (TextView)mGroupInfo.findViewById(R.id.activity_repairmanageitem_repairendtime);
		
		trThingCost = (TableRow)mGroupInfo.findViewById(R.id.activity_repairmanageitem_repairthingcost_tr);
		etThing = (TextView)mGroupInfo.findViewById(R.id.activity_repairmanageitem_repairthingcost);
		
		trMoneyCost = (TableRow)mGroupInfo.findViewById(R.id.activity_repairmanageitem_repairmoneycost_tr);
		etMoney = (TextView)mGroupInfo.findViewById(R.id.activity_repairmanageitem_repairmoneycost);
		
		trVerifyResult = (TableRow)mGroupVerify.findViewById(R.id.activity_repairmanageitem_verifyresult_tr);
		etVerifyResult = (TextView)mGroupVerify.findViewById(R.id.activity_repairmanageitem_verifyresult);
		
		trVerifyPeople = (TableRow)mGroupVerify.findViewById(R.id.activity_repairmanageitem_verifypeople_tr);
		etVerifyPeople = (TextView)mGroupVerify.findViewById(R.id.activity_repairmanageitem_verifypeople);
		
		trEquipmentOpinion = (TableRow)mGroupVerify.findViewById(R.id.activity_repairmanageitem_equipmentopinion_tr);
		etEquipmentOpinion = (TextView)mGroupVerify.findViewById(R.id.activity_repairmanageitem_equipmentopinion);
		
		trPlantOpinion = (TableRow)mGroupVerify.findViewById(R.id.activity_repairmanageitem_plantopinion_tr);
		etPlantOpinion = (TextView)mGroupVerify.findViewById(R.id.activity_repairmanageitem_plantopinion);
		
		mSubmitButton = (Button)findViewById(R.id.activity_repairmanageitem_submit);
		mSubmitButton.setOnClickListener(this);
		
		fillViewData(mRequestCode);
		setViewState(mRequestCode);
		
	}
	
	/**
	 * 根据不同的状态设置各个空间的单击事件
	 * @param code
	 */
	private void setViewState( int code){
		mMachineListView.setOnItemClickListener(this);
		switch (code) {
		case RepairManageActivity.REPAIRMANAGE_ADD_INTEGER:
			trName.setOnClickListener(this);
			trFaultTime.setOnClickListener(this);
			trFaultPhenomenon.setOnClickListener(this);
			trHandleStep.setOnClickListener(this);
			trOtherStep.setOnClickListener(this);
			break;
		case RepairManageActivity.REPAIRMANAGE_UPDATE_INTEGER:
			trName.setOnClickListener(this);
			trFaultTime.setOnClickListener(this);
			trFaultPhenomenon.setOnClickListener(this);
			trHandleStep.setOnClickListener(this);
			trOtherStep.setOnClickListener(this);
			break;
		case RepairManageActivity.REPAIRMANAGE_DETAIL_INTEGER:
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			trName.setOnClickListener(null);
			trFaultTime.setOnClickListener(null);
			trFaultPhenomenon.setOnClickListener(null);
			trHandleStep.setOnClickListener(null);
			trOtherStep.setOnClickListener(null);
			mSubmitButton.setVisibility(View.GONE);
			break;
		}
	}
	
	/**
	 * 根据不同的code码设置表单的基本信息
	 * @param code
	 */
	private void fillViewData(int code){
		switch (code) {
		case RepairManageActivity.REPAIRMANAGE_ADD_INTEGER:
			getServerData();
			setGroupBasicData(true);
			setGroupFaultData(true);
			break;
		case RepairManageActivity.REPAIRMANAGE_UPDATE_INTEGER:
			getServerData();
			setGroupBasicData(false);
			setGroupFaultData(false);
			break;
		case RepairManageActivity.REPAIRMANAGE_DETAIL_INTEGER:
			setGroupBasicData(false);
			setGroupFaultData(false);
			break;
		}
	}
	
	/**
	 * 设置设备信息的数据
	 * @param code
	 */
	private void setGroupBasicData(boolean isEmpty){
		if(isEmpty){
			etName.setText(selectedData.get(""));
			etType.setText(selectedData.get(""));
			etSN.setText(selectedData.get(""));
			etStartTime.setText(selectedData.get(""));
			etManufacture.setText(selectedData.get(""));
			etPosition.setText(selectedData.get(""));
		}else {
			etName.setText(selectedData.get("DeviceName"));
			etType.setText(selectedData.get("Specification"));
			etSN.setText(selectedData.get("DeviceSN"));
			etStartTime.setText(selectedData.get("StartUseTime"));
			etManufacture.setText(selectedData.get("Manufacturer"));
			etPosition.setText(selectedData.get("InstallPosition"));
		}
	}
	
	/**
	 * 设置故障信息的数据
	 * @param code
	 */
	private void setGroupFaultData(boolean isEmpty){
		if(isEmpty){
			etFaultTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(mDate));
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
	
	/**
	 * 设置维修信息的数据
	 */
	private void setGroupRepairData(boolean isEmpty){
		if(isEmpty){
			etSendTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(mDate));
			etSender.setText(SystemParams.getInstance().getLoggedUserInfo().get("RealUserName"));
			etTimeCost.setText("");
			etContent.setText("");
		}else {
			
		}
	}
	
	
	/**
	 * 设置审核信息的数据
	 */
	private void setGroupVerifyData(boolean isEmpty){
		if(isEmpty){
			
		}else {
			
		}
	}
	
	/**
	 * 使用asynctask异步获取服务器上的数据
	 */
	private void getServerData(){
		mGetServerData = new GetServerData();
		mGetServerData.execute("");
	}
	/**
	 * 使用asynctask异步提交工单
	 */
	private void insertServerTask(){
		if(selectedData==null){
			Toast.makeText(this, "未选择设备", Toast.LENGTH_SHORT).show();
		}else {
			mInsertTask = new InsertTask();
			mInsertTask.execute("");
		}
		
	}
	
	/**
	 * 提交数据时，弹出进度对话框
	 */
	private void showProgressDialog(){
		if(mProgressDialog==null){
			mProgressDialog = new ProgressDialog(RepairManageItemActivity.this);
			mProgressDialog.setTitle("获取数据中");
			mProgressDialog.setMessage("正在努力加载数据，请稍后");
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setCancelable(false);
		}
		mProgressDialog.show();
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
	
	@Override
	public void onBackPressed() {
		if(dateTimePickerView!=null&&dateTimePickerView.isShowing()){
			dateTimePickerView.dismiss();
		}else {
			super.onBackPressed();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_repairmanageitem, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
				mDrawerLayout.closeDrawer(Gravity.LEFT);
			}else {
				setResult(RESULT_CANCELED);
				finish();
			}
			break;
//		case R.id.menu_repairmanageitem_refresh:
//			getServerData();
//			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v) {
 		switch (v.getId()) {
		case R.id.activity_repairmanageitem_name_tr:
			mDrawerLayout.openDrawer(Gravity.LEFT);
			break;
		case R.id.activity_repairmanageitem_faulttime_tr:
			if(dateTimePickerView==null){
				dateTimePickerView = new DateTimePickerView(RepairManageItemActivity.this);
				dateTimePickerView.setButtonClickEvent(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mDate = dateTimePickerView.getSelectedDate();
						etFaultTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(mDate));
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
						calendar.setTime(mDate);
						dateTimePickerView.iniWheelView(calendar);
					}
				});
			}
			Calendar calendar = Calendar.getInstance(Locale.CHINA);
			calendar.setTime(mDate);
			dateTimePickerView.iniWheelView(calendar);
			dateTimePickerView.showAtLocation(findViewById(R.id.activity_repairmanageitem_main), Gravity.BOTTOM, 0, 0);
			break;
		case R.id.activity_repairmanageitem_faultphenomenon_tr:
			Toast.makeText(RepairManageItemActivity.this, "故障现象", Toast.LENGTH_SHORT).show();
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
			
			break;
		case R.id.activity_repairmanageitem_submit:
			insertServerTask();
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mDrawerLayout.closeDrawer(Gravity.LEFT);
		selectedData = mMachine.get(position);
		etName.setText(selectedData.get("DeviceName"));
		etType.setText(selectedData.get("Specification"));
		etSN.setText(selectedData.get("DeviceSN"));
		etStartTime.setText(selectedData.get("StartUseTime"));
		etManufacture.setText(selectedData.get("Manufacturer"));
		etPosition.setText(selectedData.get("InstallPosition"));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 自定义的设备列表的adapter
	 * @author sk
	 */
	private class MachineListItemAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mMachine.size();
		}
		@Override
		public HashMap<String, String> getItem(int position) {
			return mMachine.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView ==null){
				convertView = LayoutInflater.from(RepairManageItemActivity.this).inflate(R.layout.item_machinelist, null);
			}
			TextView nameTextView = (TextView)convertView.findViewById(R.id.item_machinelist_name);
			nameTextView.setText(mMachine.get(position).get("DeviceName").toString());
			return convertView;
		}
	}
	
	/**
	 * 获取远端数据的异步方法
	 * @author sk
	 */
	class GetServerData extends AsyncTask<String, String, ArrayList<HashMap<String, String>>>{
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
			JSONObject object = new JSONObject();
			ArrayList<HashMap<String, String>> data = null;
			try {
				object.put("PlantID", 1);
				String result = DataCenterHelper.HttpPostData("GetDeviceInfoList", object);
				if(!result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					JSONObject jsonObject = new JSONObject(result);
					JSONArray jsonArray = new JSONArray(jsonObject.getString("d").toString());
					JSONObject device = null;
					HashMap<String, String> map = null;
					data = new ArrayList<HashMap<String,String>>();
					for(int i =0;i<jsonArray.length();i++){
						device = jsonArray.getJSONObject(i);
						map = new HashMap<String, String>();
						map.put("DeviceID", device.get("DeviceID").toString());
						map.put("DeviceSN", device.get("DeviceSN").toString());
						map.put("DeviceName", device.get("DeviceName").toString());
						map.put("FixedAssets", device.get("FixedAssets").toString());
						map.put("InstallPosition", device.get("InstallPosition").toString());
						map.put("Price", device.get("Price").toString());
						map.put("FilingTime", device.get("FilingTime").toString().replace("T", " "));
						map.put("InstallTime", device.get("InstallTime").toString().replace("T", " "));
						map.put("StartUseTime", device.get("StartUseTime").toString().replace("T", " "));
						map.put("StopUseTime", device.get("StopUseTime").toString().replace("T", " "));
						map.put("ScrapTime", device.get("ScrapTime").toString().replace("T", " "));
						map.put("DepreciationPeriod", device.get("DepreciationPeriod").toString());
						map.put("DeviceClassType", device.get("DeviceClassType").toString());
						map.put("Department", device.get("Department").toString());
						map.put("Specification", device.get("Specification").toString());
						map.put("Manufacturer", device.get("Manufacturer").toString());
						map.put("Quality", device.get("Quality").toString());
						map.put("AccessoryInfo", device.get("AccessoryInfo").toString());
						data.add(map);
					}
					mMachine = data;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				data = null;
			} catch (IOException e) {
				e.printStackTrace();
				data = null;
			} catch (JSONException e) {
				e.printStackTrace();
				data = null;
			}
			return data;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			super.onPostExecute(result);
			if(result!=null){
				mMachineListAdapter = new MachineListItemAdapter();
				mMachineListView.setAdapter(mMachineListAdapter);
			}
		}
	}
	
	/**
	 * 填报一个单子
	 * @author sk
	 */
	class InsertTask extends AsyncTask<String, String, String>{
		@Override
		protected String doInBackground(String... params) {
			JSONObject param = new JSONObject();
			JSONObject repairDataString = new JSONObject();
			String result = DataCenterHelper.RESPONSE_FALSE_STRING;
			try {
				param.put("PlantID", SystemParams.PLANTID_INT);
				
				//是否有RepairTaskID，如果有，则添加，视为更新，没有的话则是插入
				if(mRequestCode==RepairManageActivity.REPAIRMANAGE_UPDATE_INTEGER){
					param.put("RepairTaskID", Integer.valueOf(selectedData.get("RepairTaskID")));
				}else {
					param.put("RepairTaskID","");
				}
				
				//填报工单时，根据登录的用户，选择该工单是何种类型的工单
				int postionID = Integer.valueOf(SystemParams.getInstance().getLoggedUserInfo().get("PositionID"));
				if(postionID==EnumList.UserRole.PRODUCTIONOPERATION.getState()){
					repairDataString.put("RepairTaskType", EnumList.RepairTaskType.PRODUCTIONSECTION.getType());
				}else if (postionID==EnumList.UserRole.EQUIPMENTOPERATION.getState()) {
					repairDataString.put("RepairTaskType", EnumList.RepairTaskType.EQUIPMENTSECTION.getType());
				}
				
				//填报工单时，应急措施和其他措施的组合，应急措施为 1,2,3,4,5,其他措施为......(an)结尾，2种措施用,分割开来
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
				
				repairDataString.put("DeviceID", selectedData.get("DeviceID"));
				repairDataString.put("EmergencyMeasures", EmergencyMeasures);
				repairDataString.put("AccidentOccurTime", etFaultTime.getText().toString());
				repairDataString.put("AccidentDetail",etFaultPhenomenon.getText().toString());
				repairDataString.put("ReportPerson", SystemParams.getInstance().getLoggedUserInfo().get("UserID"));
				
				param.put("RepairDataString", repairDataString.toString());
				
				result = DataCenterHelper.HttpPostData(DataCenterHelper.METHOD_INSERTTASK_STRING, param);
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
			mProgressDialog.cancel();
			if(result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
				Toast.makeText(RepairManageItemActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
			}else {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if(jsonObject.getBoolean("d")){
						setResult(Activity.RESULT_OK);
						finish();
					}else {
						Toast.makeText(RepairManageItemActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
