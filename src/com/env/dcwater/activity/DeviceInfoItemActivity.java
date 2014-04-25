package com.env.dcwater.activity;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.ThreadPool;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;
import com.env.dcwater.util.SystemMethod;

/**
 * 设备信息查看
 * @author sk
 */
public class DeviceInfoItemActivity extends NfcActivity implements IXListViewListener{
	
	public static final String ACTION_STRING = "DeviceInfoItemActivity";
	
	private Intent receivedIntent;
	private HashMap<String, String> receivedDevice;
	private PullToRefreshView infoListView;
	private ArrayList<HashMap<String, String>> deviceParams;
	private DeviceInfoAdapter deviceInfoAdapter;
	private ActionBar mActionBar;
	private ProgressDialog mProgressDialog;
	private ThreadPool.GetDeviceDetailData getDeviceDetailData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deviceinfoitem);
		iniData();
 		iniActionBar();
		iniView();
	}
	
	/**
	 * 初始化actionbar
	 */
	private void iniActionBar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		mActionBar.setTitle(receivedDevice.get("DeviceName"));
	}
	
	/**
	 * 初始化view
	 */
	private void iniView(){
		infoListView = (PullToRefreshView)findViewById(R.id.activity_deviceinfoitem_info);
		
		deviceInfoAdapter =  new DeviceInfoAdapter();
		infoListView.setAdapter(deviceInfoAdapter);
		infoListView.setXListViewListener(this);
	}
	
	/**
	 * 初始化数据
	 */
	@SuppressWarnings("unchecked")
	private void iniData(){
		receivedIntent = getIntent();
		receivedDevice = (HashMap<String, String>) receivedIntent.getExtras().getSerializable("data");
		setDeviceParams(receivedDevice);
		
	}
	
	private void getServerDeviceData(){
		getDeviceDetailData = new ThreadPool.GetDeviceDetailData() {
			@Override
			protected void onPreExecute() {
				showProgressDialog(false);
			}
			@Override
			protected void onPostExecute(HashMap<String, String> result) {
				if(result!=null){
					setDeviceParams(result);
					deviceInfoAdapter.notifyDataSetChanged();
					Toast.makeText(DeviceInfoItemActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(DeviceInfoItemActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
				}
				hideProgressDialog();
				infoListView.stopRefresh();
			}
		};
		getDeviceDetailData.execute(receivedDevice.get("DeviceID"));
	}
	
	/**
	 * 构造参数列表
	 * @param selectedMachine
	 */
	private void setDeviceParams(HashMap<String, String> selectedMachine){
		deviceParams = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("Name", "设备名称");
		map.put("Value", selectedMachine.get("DeviceName"));
		deviceParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "建档时间");
		map.put("Value", selectedMachine.get("FilingTime"));
		deviceParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "固定资产编号");
		map.put("Value", selectedMachine.get("FixedAssets"));
		deviceParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "安装位置");
		map.put("Value", selectedMachine.get("InstallPosition"));
		deviceParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "生产厂家");
		map.put("Value", selectedMachine.get("Manufacturer"));
		deviceParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "开始使用时间");
		map.put("Value", selectedMachine.get("StartUseTime"));
		deviceParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "设备报废时间");
		map.put("Value", selectedMachine.get("ScrapTime"));
		deviceParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "设备质量");
		map.put("Value", selectedMachine.get("Quality"));
		deviceParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "设备类型");
		map.put("Value", selectedMachine.get("DeviceClassType"));
		deviceParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "设备编号");
		map.put("Value", selectedMachine.get("DeviceSN"));
		deviceParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "型号（规格）");
		map.put("Value", selectedMachine.get("Specification"));
		deviceParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "使用部门");
		map.put("Value", selectedMachine.get("Department"));
		deviceParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "安装试车时间");
		map.put("Value", selectedMachine.get("InstallTime"));
		deviceParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "开始停用时间");
		map.put("Value", selectedMachine.get("StopUseTime"));
		deviceParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "设备折旧年限");
		map.put("Value", selectedMachine.get("DepreciationPeriod"));
		deviceParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "设备价格");
		map.put("Value", selectedMachine.get("Price"));
		deviceParams.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "随机附件及数量");
		map.put("Value", selectedMachine.get("AccessoryInfo"));
		deviceParams.add(map);
	}
	
	/**
	 * 获取数据时，弹出进度对话框
	 * @param cancelable 是否能被取消的操作
	 */
	private void showProgressDialog(boolean cancelable){
		if(mProgressDialog==null){
			mProgressDialog = new ProgressDialog(DeviceInfoItemActivity.this);
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
	
	/**
	 * 跳转到维修历史
	 */
	private void startMaintainHistoryActivity(){
		Intent intent = new Intent(this, MaintainHistoryActivity.class);
		intent.putExtra("action", ACTION_STRING);
		intent.putExtra("data",receivedDevice);
		startActivity(intent);
	}
	/**
	 * 跳转到保养历史
	 */
	private void startUpkeepHistoryActivity(){
		Intent intent = new Intent(this, UpkeepHistoryActivity.class);
		intent.putExtra("action", ACTION_STRING);
		intent.putExtra("data",receivedDevice);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_deviceinfoitem, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.menu_deviceinfoitem_maintainhistory:
			startMaintainHistoryActivity();
			break;
		case R.id.menu_deviceinfoitem_upkeephistory:
			startUpkeepHistoryActivity();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	@Override
	public void onRefresh() {
		getServerDeviceData();
	}
	
	/**
	 * 自定义设备参数列表的adapter
	 * @author sk
	 */
	private class DeviceInfoAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return deviceParams.size();
		}
		@Override
		public HashMap<String, String> getItem(int position) {
			return deviceParams.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView ==null){
				convertView = LayoutInflater.from(DeviceInfoItemActivity.this).inflate(R.layout.item_deviceinfo, null);
			}
			HashMap<String, String> map = getItem(position);
			TextView keyTextView = (TextView)convertView.findViewById(R.id.item_deviceinfo_key);
			TextView valueTextView = (TextView)convertView.findViewById(R.id.item_deviceinfo_value);
			keyTextView.setText(map.get("Name"));
			valueTextView.setText(map.get("Value"));
			return convertView;
		}
	}
	
}
