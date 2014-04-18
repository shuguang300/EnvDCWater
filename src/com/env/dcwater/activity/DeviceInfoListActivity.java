package com.env.dcwater.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
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
import android.widget.SearchView;
import android.widget.TextView;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.component.ThreadPool;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.util.DataCenterHelper;
import com.env.dcwater.util.OperationMethod;

public class DeviceInfoListActivity extends NfcActivity {
	
	public static final String ACTION_STRING = "DeviceInfoListActivity";
	private ProgressDialog mProgressDialog;
	private ActionBar actionBar;
	private ArrayList<HashMap<String, String>> deviceArrayList,consArrayList;
	private GetServerDeviceData getServerDeviceData;
	private DrawerLayout drawerLayout;
	private PullToRefreshView deviceListView,consListView;
	private ConstructionAdapter constructionAdapter;
	private DeviceListAdapter deviceListAdapter;
	private ThreadPool.GetServerConsData getServerConsData;
	private SearchView searchView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deviceinfolist);
		iniData();
		iniActionbar();
		iniView();
		getServerData();
	}
	
	/**
	 * 
	 */
	private void iniData(){
		deviceArrayList = new ArrayList<HashMap<String,String>>();
		consArrayList = new ArrayList<HashMap<String,String>>();
		constructionAdapter = new ConstructionAdapter();
		deviceListAdapter = new DeviceListAdapter();
	}
	
	/**
	 * 
	 */
	private void iniActionbar(){
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("设备列表");
	}
	
	/**
	 * 
	 */
	private void iniView(){
		drawerLayout = (DrawerLayout)findViewById(R.id.activity_deviceinfolist_drawlayout);
		deviceListView = (PullToRefreshView)findViewById(R.id.activity_deviceinfolist_devicelist);
		consListView = (PullToRefreshView)findViewById(R.id.activity_deviceinfolist_conslist);
		consListView.setXListViewListener(new PullToRefreshView.IXListViewListener(){
			@Override
			public void onRefresh() {
				getServerConsList();
			}
		});
		
		consListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});
		
		deviceListView.setXListViewListener(new PullToRefreshView.IXListViewListener() {
			@Override
			public void onRefresh() {
				getServerDeviceList();
			}
		});
		
		deviceListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(DeviceInfoListActivity.this,DeviceInfoItemActivity.class);
				intent.putExtra("data", deviceArrayList.get(position-1));
				startActivity(intent);
			}
		});
		
		deviceListView.setAdapter(deviceListAdapter);
		consListView.setAdapter(constructionAdapter);
	}
	
	/**
	 * 
	 */
	private void getServerData(){
		getConsList();
		getDeviceList();
		
	}
	
	/**
	 * 
	 */
	private void getConsList(){
		if(SystemParams.getInstance().getConstructionList()==null){
			getServerConsList();
		}else {
			consArrayList = SystemParams.getInstance().getConstructionList();
		}
	}
	
	/**
	 * 
	 */
	private void getServerConsList(){
		getServerConsData = new ThreadPool.GetServerConsData() {
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				if(result!=null){
					consArrayList = result;
					constructionAdapter.notifyDataSetChanged();
				}
				consListView.stopRefresh();
			}
		};
		getServerConsData.execute("");
	}
	
	private void getDeviceList(){
		if(SystemParams.getInstance().getMachineList()==null){
			getServerDeviceList();
		}else {
			deviceArrayList = SystemParams.getInstance().getMachineList();
		}
	}
	
	/**
	 * 
	 */
	private void getServerDeviceList(){
		getServerDeviceData = new GetServerDeviceData();
		getServerDeviceData.execute("");
	}
	
	
	/**
	 * 获取数据时，弹出进度对话框
	 * @param cancelable 是否能被取消的操作
	 */
	private void showProgressDialog(boolean cancelable){
		if(mProgressDialog==null){
			mProgressDialog = new ProgressDialog(DeviceInfoListActivity.this);
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
		startActivity(intent);
	}
	/**
	 * 跳转到保养历史
	 */
	private void startUpkeepHistoryActivity(){
		Intent intent = new Intent(this, UpkeepHistoryActivity.class);
		intent.putExtra("action", ACTION_STRING);
		startActivity(intent);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_deviceinfolist, menu);
		searchView = (SearchView)menu.getItem(0).getActionView();
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.menu_deviceinfolist_maintainhistory:
			startMaintainHistoryActivity();
			break;
		case R.id.menu_deviceinfolist_upkeephistory:
			startUpkeepHistoryActivity();
			break;
		case R.id.menu_deviceinfolist_showcons:
			if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
				drawerLayout.closeDrawer(Gravity.LEFT);
			}else {
				drawerLayout.openDrawer(Gravity.LEFT);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
			drawerLayout.closeDrawer(Gravity.LEFT);
		}else {
			super.onBackPressed();
		}
	}
	
	/**
	 * @author sk
	 *
	 */
	private class ConstructionAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return consArrayList.size();
		}

		@Override
		public Object getItem(int position) {
			return consArrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView = LayoutInflater.from(DeviceInfoListActivity.this).inflate(R.layout.item_consselect, null);
			}
			TextView name = (TextView)convertView.findViewById(R.id.item_consselect_consname);
			name.setText(consArrayList.get(position).get("StructureName"));
			return convertView;
		}
		
	}
	
	/**
	 * 自定义设备列表的adapter
	 * @author sk
	 */
	private class DeviceListAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return deviceArrayList.size();
		}
		@Override
		public HashMap<String, String> getItem(int position) {
			return deviceArrayList.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView ==null){
				convertView = LayoutInflater.from(DeviceInfoListActivity.this).inflate(R.layout.item_devicelist, null);
			}
			TextView nameTextView = (TextView)convertView.findViewById(R.id.item_devicelist_name);
			nameTextView.setText(deviceArrayList.get(position).get("DeviceName").toString());
			return convertView;
		}
	}
	
	/**
	 * 获取服务器段的设备列表
	 * @author sk
	 */
	private class GetServerDeviceData extends AsyncTask<String, String, ArrayList<HashMap<String, String>>>{
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
			JSONObject object = new JSONObject();
			ArrayList<HashMap<String, String>> data = null;
			try {
				object.put("PlantID", 1);
				String result = DataCenterHelper.HttpPostData("GetDeviceInfoList", object);
				if(!result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					JSONObject jsonObject = new JSONObject(result);
					data = OperationMethod.parseDeviceDataToList(jsonObject,"","");
					if(data!=null){
						deviceArrayList = data;
					}
					//获取到设备列表后，将设备列表数据暂存到程序变量中，方便其他地方调用
					
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
			showProgressDialog(false);
		}
		
		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			super.onPostExecute(result);
			if(result!=null){
				result = deviceArrayList;
				deviceListAdapter.notifyDataSetChanged();
			}
			deviceListView.stopRefresh();
			hideProgressDialog();
		}
	}
}