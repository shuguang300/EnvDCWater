package com.env.dcwater.activity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;
import com.env.dcwater.util.DataCenterHelper;

/**
 * 设备信息查看
 * @author sk
 */
public class MachineInfoActivity extends NfcActivity implements OnItemClickListener,IXListViewListener{
	
	public static final String ACTION_STRING = "MachineInfoActivity";
	
	private DrawerLayout mDrawerLayout;
	private ListView mListView;
	private PullToRefreshView infoListView;
	private ArrayList<HashMap<String, String>> mMachine;
	private ArrayList<HashMap<String, String>> mMachineParams;
	private GetServerData getServerData;
	private MachineInfoItemAdapter infoListViewAdapter;
	private MachineListItemAdapter machineListViewAdapter;
	private ActionBar mActionBar;
	private int mSelectedPosition = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_machineinfo);
		iniData();
 		iniActionBar();
		iniView();
		getServerData();
	}
	
	/**
	 * 初始化actionbar
	 */
	private void iniActionBar(){
		mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
	}
	
	/**
	 * 初始化view
	 */
	private void iniView(){
		infoListView = (PullToRefreshView)findViewById(R.id.activity_machineinfo_info);
		mDrawerLayout = (DrawerLayout)findViewById(R.id.activity_machineinfo_drawlayout);
		mListView = (ListView)findViewById(R.id.activity_machineinfo_listview);
		iniListView();
	}
	
	/**
	 * 初始化数据
	 */
	private void iniData(){
		mMachine = new ArrayList<HashMap<String,String>>();
		mMachineParams = new ArrayList<HashMap<String,String>>();
	}
	
	/**
	 * 使用asynctask异步获取服务器上的数据
	 */
	private void getServerData(){
		getServerData = new GetServerData();
		getServerData.execute("");
	}
	
	/**
	 * 构造参数列表
	 * @param selectedMachine
	 */
	private void setMachineParams(HashMap<String, String> selectedMachine){
		mMachineParams = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("Name", "设备名称");
		map.put("Value", selectedMachine.get("DeviceName"));
		mMachineParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "建档时间");
		map.put("Value", selectedMachine.get("FilingTime"));
		mMachineParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "固定资产编号");
		map.put("Value", selectedMachine.get("FixedAssets"));
		mMachineParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "安装位置");
		map.put("Value", selectedMachine.get("InstallPosition"));
		mMachineParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "生产厂家");
		map.put("Value", selectedMachine.get("Manufacturer"));
		mMachineParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "开始使用时间");
		map.put("Value", selectedMachine.get("StartUseTime"));
		mMachineParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "设备报废时间");
		map.put("Value", selectedMachine.get("ScrapTime"));
		mMachineParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "设备质量");
		map.put("Value", selectedMachine.get("Quality"));
		mMachineParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "设备类型");
		map.put("Value", selectedMachine.get("DeviceClassType"));
		mMachineParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "设备编号");
		map.put("Value", selectedMachine.get("DeviceSN"));
		mMachineParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "型号（规格）");
		map.put("Value", selectedMachine.get("Specification"));
		mMachineParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "使用部门");
		map.put("Value", selectedMachine.get("Department"));
		mMachineParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "安装试车时间");
		map.put("Value", selectedMachine.get("InstallTime"));
		mMachineParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "开始停用时间");
		map.put("Value", selectedMachine.get("StopUseTime"));
		mMachineParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "设备折旧年限");
		map.put("Value", selectedMachine.get("DepreciationPeriod"));
		mMachineParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "设备价格");
		map.put("Value", selectedMachine.get("Price"));
		mMachineParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "随机附件及数量");
		map.put("Value", selectedMachine.get("AccessoryInfo"));
		mMachineParams.add(map);
	}
	
	
	/**
	 * 初始化 listview
	 */
	private void iniListView(){
		machineListViewAdapter = new MachineListItemAdapter();
		mListView.setAdapter(machineListViewAdapter);
		mListView.setOnItemClickListener(this);
		
		infoListViewAdapter =  new MachineInfoItemAdapter();
		infoListView.setAdapter(infoListViewAdapter);
		infoListView.setXListViewListener(this);
	}
	
	/**
	 * 跳转到维修历史
	 */
	private void startMaintainHistoryActivity(){
		Intent intent = new Intent(this, MaintainHistoryActivity.class);
		intent.putExtra("action", ACTION_STRING);
		intent.putExtra("data",mMachine.get(mSelectedPosition));
		startActivity(intent);
	}
	/**
	 * 跳转到保养历史
	 */
	private void startUpkeepHistoryActivity(){
		Intent intent = new Intent(this, UpkeepHistoryActivity.class);
		intent.putExtra("action", ACTION_STRING);
		intent.putExtra("data",mMachine.get(mSelectedPosition));
		startActivity(intent);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(parent.equals(mListView)){ //选取设备的 listview
			mDrawerLayout.closeDrawer(Gravity.LEFT);
			mSelectedPosition = position;
			mActionBar.setTitle(mMachine.get(mSelectedPosition).get("DeviceName"));
			setMachineParams(mMachine.get(mSelectedPosition));
			infoListViewAdapter.notifyDataSetChanged();
		}else if (parent.equals(infoListView)) {//设备参数的listview
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_machineinfo, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.menu_machineinfo_maintainhistory:
			startMaintainHistoryActivity();
			break;
		case R.id.menu_machineinfo_upkeephistory:
			startUpkeepHistoryActivity();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}
	
	@Override
	public void onRefresh() {
		getServerData();
	}
	
	private class MachineInfoItemAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return mMachineParams.size();
		}
		@Override
		public HashMap<String, String> getItem(int position) {
			return mMachineParams.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView ==null){
				convertView = LayoutInflater.from(MachineInfoActivity.this).inflate(R.layout.item_machineinfo, null);
			}
			HashMap<String, String> map = getItem(position);
			TextView keyTextView = (TextView)convertView.findViewById(R.id.item_machineinfo_key);
			TextView valueTextView = (TextView)convertView.findViewById(R.id.item_machineinfo_value);
			keyTextView.setText(map.get("Name"));
			valueTextView.setText(map.get("Value"));
			return convertView;
		}
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
				convertView = LayoutInflater.from(MachineInfoActivity.this).inflate(R.layout.item_machinelist, null);
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
					SystemParams.getInstance().setmMachineList(mMachine);
					setMachineParams(mMachine.get(mSelectedPosition));
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
				mActionBar.setTitle(mMachine.get(mSelectedPosition).get("DeviceName"));
				machineListViewAdapter.notifyDataSetChanged();
				infoListViewAdapter.notifyDataSetChanged();
			}
			infoListView.stopRefresh();
		}
	}
}
