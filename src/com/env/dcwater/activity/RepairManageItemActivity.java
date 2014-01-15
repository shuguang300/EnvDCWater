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
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.fragment.DateTimePickerView;
import com.env.dcwater.util.DataCenterHelper;

/**
 * 单个报修工单界面，该界面可查看，编辑
 * @author sk
 *
 */
public class RepairManageItemActivity extends NfcActivity implements OnClickListener,OnItemClickListener{
	
	public static final String ACTION_STRING = "RepairManageItemActivity";
	
	private ActionBar mActionBar;
	private HashMap<String, String> receivedData;
	private Intent receivedIntent;
	private int mRequestCode;
	private Date mDate;
	private GetServerData mGetServerData;
	private DrawerLayout mDrawerLayout;
	private ListView mMachineListView;
	private MachineListItemAdapter mMachineListAdapter;
	private ArrayList<HashMap<String, String>> mMachine;
	private DateTimePickerView dateTimePickerView;
	private String [] handleStepContent = {"尝试手动启动","关闭主电源","拍下急停按钮","悬挂警示标识牌","关闭故障设备工艺段进水"};
	private TextView etName,etType,etSN,etPosition,etStartTime,etManufacture,etFaultTime,etHandleStep,etPeople,etFaultPhenomenon,etOtherStep;
	private TableRow trName,trFaultTime,trFaultPhenomenon,trHandleStep,trOtherStep,trPeople;
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
		receivedIntent = getIntent();
		mRequestCode = receivedIntent.getExtras().getInt("RequestCode");
		switch (mRequestCode) {
		case RepairManageActivity.REPAIRMANAGE_ADD_INTEGER:
			mDate = new Date();
			break;
		case RepairManageActivity.REPAIRMANAGE_UPDATE_INTEGER:
			receivedData = (HashMap<String, String>)receivedIntent.getSerializableExtra("Data");
			try {
				mDate = new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).parse(receivedData.get("AccidentOccurTime").toString());
			} catch (Exception e) {
				mDate = new Date();
			}
			break;
		case RepairManageActivity.REPAIRMANAGE_DETAIL_INTEGER:
			receivedData = (HashMap<String, String>)receivedIntent.getSerializableExtra("Data");
			try {
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
		
		etName = (TextView)findViewById(R.id.activity_repairmanageitem_name);
		trName = (TableRow)findViewById(R.id.activity_repairmanageitem_name_tr);
		
		etType = (TextView)findViewById(R.id.activity_repairmanageitem_type);
		
		etSN = (TextView)findViewById(R.id.activity_repairmanageitem_sn);
		
		etPosition = (TextView)findViewById(R.id.activity_repairmanageitem_position);
		
		etStartTime = (TextView)findViewById(R.id.activity_repairmanageitem_starttime);
		
		etManufacture = (TextView)findViewById(R.id.activity_repairmanageitem_manufacturer);
		
		etFaultTime = (TextView)findViewById(R.id.activity_repairmanageitem_faulttime);
		trFaultTime = (TableRow)findViewById(R.id.activity_repairmanageitem_faulttime_tr);
		
		etFaultPhenomenon = (TextView)findViewById(R.id.activity_repairmanageitem_faultphenomenon);
		trFaultPhenomenon = (TableRow)findViewById(R.id.activity_repairmanageitem_faultphenomenon_tr);
		
		etHandleStep = (TextView)findViewById(R.id.activity_repairmanageitem_handlestep);
		trHandleStep = (TableRow)findViewById(R.id.activity_repairmanageitem_handlestep_tr);
		
		etOtherStep = (TextView)findViewById(R.id.activity_repairmanageitem_otherstep);
		trOtherStep = (TableRow)findViewById(R.id.activity_repairmanageitem_otherstep_tr);
		
		etPeople = (TextView)findViewById(R.id.activity_repairmanageitem_taskpeople);
		trPeople = (TableRow)findViewById(R.id.activity_repairmanageitem_taskpeople_tr);
		
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
			trPeople.setOnClickListener(this);
			break;
		case RepairManageActivity.REPAIRMANAGE_UPDATE_INTEGER:
			trName.setOnClickListener(this);
			trFaultTime.setOnClickListener(this);
			trFaultPhenomenon.setOnClickListener(this);
			trHandleStep.setOnClickListener(this);
			trOtherStep.setOnClickListener(this);
			trPeople.setOnClickListener(this);
			break;
		case RepairManageActivity.REPAIRMANAGE_DETAIL_INTEGER:
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			trName.setOnClickListener(null);
			trFaultTime.setOnClickListener(null);
			trFaultPhenomenon.setOnClickListener(null);
			trHandleStep.setOnClickListener(null);
			trOtherStep.setOnClickListener(null);
			trPeople.setOnClickListener(null);
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
			etName.setText("");
			etSN.setText("");
			etType.setText("");
			etPosition.setText("");
			etStartTime.setText("");
			etManufacture.setText("");
			etFaultTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(mDate));
			etFaultPhenomenon.setText("");
			etHandleStep.setText("");
			etOtherStep.setText("");
			etPeople.setText("");
			break;
		case RepairManageActivity.REPAIRMANAGE_UPDATE_INTEGER:
			getServerData();
			etName.setText(receivedData.get("DeviceName"));
			etPosition.setText(receivedData.get("InstallPosition"));
			etFaultTime.setText(receivedData.get("AccidentOccurTime"));
			etFaultPhenomenon.setText(receivedData.get("AccidentDetail"));
			etPeople.setText(receivedData.get("ReportPerson").toString());
			break;
		case RepairManageActivity.REPAIRMANAGE_DETAIL_INTEGER:
			etName.setText(receivedData.get("DeviceName"));
			etPosition.setText(receivedData.get("InstallPosition"));
			etFaultTime.setText(receivedData.get("AccidentOccurTime"));
			etFaultPhenomenon.setText(receivedData.get("AccidentDetail"));
			etPeople.setText(receivedData.get("ReportPerson").toString());
			break;
		}
	}
	
	
	/**
	 * 使用asynctask异步获取服务器上的数据
	 */
	private void getServerData(){
		mGetServerData = new GetServerData();
		mGetServerData.execute("");
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
		case R.id.menu_repairmanageitem_refresh:
			getServerData();
			break;
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
			Toast.makeText(RepairManageItemActivity.this, "应急措施", Toast.LENGTH_SHORT).show();
			break;
		case R.id.activity_repairmanageitem_otherstep_tr:
			Toast.makeText(RepairManageItemActivity.this, "其他措施", Toast.LENGTH_SHORT).show();
			break;
		case R.id.activity_repairmanageitem_taskpeople_tr:
			Toast.makeText(RepairManageItemActivity.this, "巡检人员", Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mDrawerLayout.closeDrawer(Gravity.LEFT);
	}
	
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
	
	private class GetServerData extends AsyncTask<String, String, ArrayList<HashMap<String, String>>>{
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

}
