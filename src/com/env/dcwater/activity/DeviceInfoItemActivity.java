package com.env.dcwater.activity;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.ThreadPool;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;
import com.env.dcwater.util.OperationMethod;
import com.env.dcwater.util.SystemMethod;

/**
 * 设备信息查看
 * @author sk
 */
public class DeviceInfoItemActivity extends NfcActivity implements IXListViewListener,OnPageChangeListener,OnCheckedChangeListener{
	
	static class FileViewHolder{
		public TextView code = null;  
        public TextView name = null;  
        public Button dl = null;  
        public Button pre = null; 
	}
	static class ParamViewHoler{
		public TextView code = null;  
        public TextView name = null;  
        public TextView value = null;  
        public TextView remark = null; 
	}
	
	public static final String ACTION_STRING = "DeviceInfoItemActivity";
	
	private Intent receivedIntent;
	private HashMap<String, String> receivedDevice;
	private PullToRefreshView property,params,files;
	private HashMap<String, ArrayList<HashMap<String, String>>> detailData;
	private ArrayList<HashMap<String, String>> deviceFiles,deviceProperty,deviceParams;
	private ViewPager viewPager;
	private ArrayList<View> views;
	private View propertyView,paramsView,filesView;
	private DevicePropertyAdapter devicePropertyAdapter;
	private DeviceParamAdapter deviceParamAdapter;
	private DeviceFilesAdapter deviceFilesAdapter;
	private ActionBar mActionBar;
	private DevicePagerAdapter devicePagerAdapter;
	private ProgressDialog mProgressDialog;
	private ThreadPool.GetDeviceDetailData getDeviceDetailData;
	private String [] titles = {"基本参数","技术参数","技术文档"};
	private RadioGroup titlegGroup;
	private RadioButton fileButton,paramsButton,propertyButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deviceinfoitem);
		iniData();
 		iniActionBar();
		iniView();
	}
	
	/**
	 * 初始化数据
	 */
	@SuppressWarnings("unchecked")
	private void iniData(){
		receivedIntent = getIntent();
		receivedDevice = (HashMap<String, String>) receivedIntent.getExtras().getSerializable("data");
		getServerDeviceData();
		deviceProperty = OperationMethod.parseDevicePropertyToList(receivedDevice);
		deviceParams = new ArrayList<HashMap<String,String>>();
		deviceFiles = new ArrayList<HashMap<String,String>>();
		views = new ArrayList<View>();
	}
	
	/**
	 * 初始化actionbar
	 */
	private void iniActionBar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		mActionBar.setTitle(receivedDevice.get("DeviceName")+titles[0]);
	}
	
	/**
	 * 初始化view
	 */
	private void iniView(){
		titlegGroup = (RadioGroup)findViewById(R.id.activity_deviceinfoitem_title);
		
		fileButton = (RadioButton)findViewById(R.id.activity_deviceinfoitem_title_file);
		fileButton.setText(titles[2]);
		
		propertyButton = (RadioButton)findViewById(R.id.activity_deviceinfoitem_title_property);
		propertyButton.setText(titles[0]);
		
		paramsButton = (RadioButton)findViewById(R.id.activity_deviceinfoitem_title_params);
		paramsButton.setText(titles[1]);
		
		viewPager =  (ViewPager)findViewById(R.id.activity_deviceinfoitem);
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		
		propertyView = layoutInflater.inflate(R.layout.view_device_property, null);
		filesView = layoutInflater.inflate(R.layout.view_device_files, null);
		paramsView = layoutInflater.inflate(R.layout.view_device_params, null);
		
		views.add(propertyView);
		views.add(paramsView);
		views.add(filesView);
		
		
		
		devicePagerAdapter = new DevicePagerAdapter();
		viewPager.setAdapter(devicePagerAdapter);
		
		property = (PullToRefreshView)propertyView.findViewById(R.id.view_device_property);
		files = (PullToRefreshView)filesView.findViewById(R.id.view_device_files);
		params = (PullToRefreshView)paramsView.findViewById(R.id.view_device_params);
		
		
		devicePropertyAdapter = new DevicePropertyAdapter();
		deviceParamAdapter = new DeviceParamAdapter();
		deviceFilesAdapter = new DeviceFilesAdapter();
		
		property.setAdapter(devicePropertyAdapter);
		property.setXListViewListener(this);
		
		params.setAdapter(deviceParamAdapter);
		params.setXListViewListener(this);
		
		files.setAdapter(deviceFilesAdapter);
		files.setXListViewListener(this);
		
		viewPager.setOnPageChangeListener(this);
		titlegGroup.setOnCheckedChangeListener(this);
	}
	
	
	private void getServerDeviceData(){
		getDeviceDetailData = new ThreadPool.GetDeviceDetailData() {
			@Override
			protected void onPreExecute() {
				showProgressDialog(false);
			}
			@Override
			protected void onPostExecute(HashMap<String, ArrayList<HashMap<String, String>>> result) {
				if(result!=null){
					detailData = result;
					deviceFiles = detailData.get("DeviceFile");
					deviceParams = detailData.get("DeviceParam");
					deviceProperty = detailData.get("DeviceProperty");
					int i =viewPager.getCurrentItem();
					switch (i) {
					case 0:
						devicePropertyAdapter.notifyDataSetChanged();
						break;
					case 1:
						deviceParamAdapter.notifyDataSetChanged();
						break;
					case 2:
						deviceFilesAdapter.notifyDataSetChanged();
						break;
					}
					Toast.makeText(DeviceInfoItemActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(DeviceInfoItemActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
				}
				hideProgressDialog();
				property.stopRefresh();
				params.stopRefresh();
				files.stopRefresh();
			}
		};
		getDeviceDetailData.execute(receivedDevice.get("DeviceID"));
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
	private class DevicePropertyAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return deviceProperty.size();
		}
		@Override
		public HashMap<String, String> getItem(int position) {
			return deviceProperty.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView ==null){
				convertView = LayoutInflater.from(DeviceInfoItemActivity.this).inflate(R.layout.item_deviceproperty, null);
			}
			HashMap<String, String> map = getItem(position);
			TextView keyTextView = (TextView)convertView.findViewById(R.id.item_deviceproperty_key);
			TextView valueTextView = (TextView)convertView.findViewById(R.id.item_deviceproperty_value);
			keyTextView.setText(map.get("Name"));
			valueTextView.setText(map.get("Value"));
			return convertView;
		}
	}
	/**
	 * 自定义设备参数列表的adapter
	 * @author sk
	 */
	private class DeviceParamAdapter extends BaseAdapter{
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
			ParamViewHoler viewHoler ;
			HashMap<String, String> map = getItem(position);
			if(convertView ==null){
				convertView = LayoutInflater.from(DeviceInfoItemActivity.this).inflate(R.layout.item_deviceparam, null);
				viewHoler = new ParamViewHoler();
				viewHoler.code = (TextView)convertView.findViewById(R.id.item_deviceparam_code);
				viewHoler.name = (TextView)convertView.findViewById(R.id.item_deviceparam_name);
				viewHoler.value = (TextView)convertView.findViewById(R.id.item_deviceparam_value);
				viewHoler.remark = (TextView)convertView.findViewById(R.id.item_deviceparam_remark);
				convertView.setTag(viewHoler);
			}else {
				viewHoler = (ParamViewHoler)convertView.getTag();
			}
			viewHoler.code.setText(position+1+"");
			viewHoler.name.setText(map.get("ParameterName"));
			viewHoler.value.setText(map.get("ParameterValue"));
			viewHoler.remark.setText(map.get("Remark"));
			return convertView;
		}
	}
	/**
	 * 自定义设备参数列表的adapter
	 * @author sk
	 */
	private class DeviceFilesAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return deviceFiles.size();
		}
		@Override
		public HashMap<String, String> getItem(int position) {
			return deviceFiles.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FileViewHolder viewHolder;
			HashMap<String, String> map = getItem(position);
			if(convertView ==null){
				convertView = LayoutInflater.from(DeviceInfoItemActivity.this).inflate(R.layout.item_devicefile, null);
				viewHolder = new FileViewHolder();
				viewHolder.code = (TextView)convertView.findViewById(R.id.item_devicefiles_code);
				viewHolder.name = (TextView)convertView.findViewById(R.id.item_devicefiles_name);
				viewHolder.dl = (Button)convertView.findViewById(R.id.item_devicefiles_dl);
				viewHolder.pre = (Button)convertView.findViewById(R.id.item_devicefiles_pre);
				convertView.setTag(viewHolder);
			}else {
				viewHolder = (FileViewHolder)convertView.getTag();
			}
			viewHolder.code.setText(position+1+"");
			viewHolder.name.setText(map.get("TechnicalData"));
			viewHolder.dl.setVisibility(map.get("WhetherDownload").equals("true")?View.VISIBLE:View.GONE);
			return convertView;
		}
	}
	
	private class DevicePagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager)container).removeView(views.get(position));
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position].toString();
		}
		
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager)container).addView(views.get(position));
			return views.get(position);
		}
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		switch (arg0) {
		case 0:
			titlegGroup.check(R.id.activity_deviceinfoitem_title_property);
			devicePropertyAdapter.notifyDataSetChanged();
			break;
		case 1:
			titlegGroup.check(R.id.activity_deviceinfoitem_title_params);
			deviceParamAdapter.notifyDataSetChanged();
			break;
		case 2:
			titlegGroup.check(R.id.activity_deviceinfoitem_title_file);
			deviceFilesAdapter.notifyDataSetChanged();
			break;
		}
		mActionBar.setTitle(receivedDevice.get("DeviceName")+titles[arg0]);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.activity_deviceinfoitem_title_property:
			viewPager.setCurrentItem(0);
			break;
		case R.id.activity_deviceinfoitem_title_params:
			viewPager.setCurrentItem(1);
			break;
		case R.id.activity_deviceinfoitem_title_file:
			viewPager.setCurrentItem(2);
			break;
		}
	}
	
}
