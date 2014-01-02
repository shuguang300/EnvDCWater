package com.env.dcwater.activity;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;

/**
 * 设备信息查看
 * @author sk
 */
public class MachineInfoActivity extends NfcActivity implements OnItemClickListener,IXListViewListener{
	
	public static final String ACTION_STRING = "MachineInfoActivity";
	
	private ArrayAdapter<String> deviceAdapter;
	private DrawerLayout mDrawerLayout;
	private ListView mListView;
	private PullToRefreshView infoListView;
	private ArrayList<HashMap<String, String>> mMachine;
	private String [] machineArray;
	private MachineInfoItemAdapter infoListViewAdapter;
	private ActionBar mActionBar;
	private int mSelectedPosition =0;
	private Handler mHandler =  new Handler(){
		public void handleMessage(android.os.Message msg) {
			infoListView.stopRefresh();
			infoListViewAdapter.notifyDataSetChanged();
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_machineinfo);
		machineArray = getResources().getStringArray(R.array.machineinfo);
		iniActionBar();
		setData();
		iniView();
	}
	
	/**
	 * 初始化actionbar
	 */
	private void iniActionBar(){
		mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		mActionBar.setTitle(machineArray[mSelectedPosition]);
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
	 * 设置数据
	 */
	private void setData(){
		mMachine = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map=null;
		for(int i=0;i<10;i++){
			map = new HashMap<String, String>();
			map.put("Key",machineArray[mSelectedPosition]+"参数"+i++);
			map.put("Value","值是"+Math.random()*10);
			mMachine.add(map);
		}
	}
	
	
	/**
	 * 初始化 listview
	 */
	private void iniListView(){
		deviceAdapter = new ArrayAdapter<String>(MachineInfoActivity.this, android.R.layout.simple_list_item_1,machineArray);
		mListView.setAdapter(deviceAdapter);
		mListView.setOnItemClickListener(this);
		
		infoListViewAdapter =  new MachineInfoItemAdapter();
		infoListView.setAdapter(infoListViewAdapter);
		infoListView.setXListViewListener(this);
		infoListView.setOnItemClickListener(this);
	}
	
	/**
	 * 跳转到维修历史
	 */
	private void startMaintainHistoryActivity(){
		Intent intent = new Intent(this, MaintainHistoryActivity.class);
		intent.putExtra("action", ACTION_STRING);
		intent.putExtra("data",machineArray[mSelectedPosition]);
		startActivity(intent);
	}
	/**
	 * 跳转到保养历史
	 */
	private void startUpkeepHistoryActivity(){
		Intent intent = new Intent(this, UpkeepHistoryActivity.class);
		intent.putExtra("action", ACTION_STRING);
		intent.putExtra("data",machineArray[mSelectedPosition]);
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
		if(parent.equals(mListView)){
			mDrawerLayout.closeDrawer(Gravity.LEFT);
			mSelectedPosition = position;
			mActionBar.setTitle(machineArray[position]);
			setData();
			infoListViewAdapter.notifyDataSetChanged();
		}else if (parent.equals(infoListView)) {
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
		case R.id.menu_machineinfo_refresh:
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
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					setData();
					mHandler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private class MachineInfoItemAdapter extends BaseAdapter{
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
				convertView = LayoutInflater.from(MachineInfoActivity.this).inflate(R.layout.item_machineinfo, null);
			}
			HashMap<String, String> map = getItem(position);
			TextView keyTextView = (TextView)convertView.findViewById(R.id.item_machineinfo_key);
			TextView valueTextView = (TextView)convertView.findViewById(R.id.item_machineinfo_value);
			keyTextView.setText(map.get("Key"));
			valueTextView.setText(map.get("Value"));
			return convertView;
		}
	}
}
