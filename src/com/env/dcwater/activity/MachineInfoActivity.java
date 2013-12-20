package com.env.dcwater.activity;
import java.util.ArrayList;
import java.util.HashMap;

import android.R.anim;
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
import android.widget.Toast;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;
import com.env.dcwater.util.CustomMethod;

public class MachineInfoActivity extends NfcActivity implements OnItemClickListener,IXListViewListener{
	private DrawerLayout mDrawerLayout;
	private ListView mListView;
	private PullToRefreshView infoListView;
	private ArrayList<HashMap<String, String>> mMachine;
	private String [] machineArray;
	private String mSelectedMachine;
	private MachineInfoItemAdapter infoListViewAdapter;
	private ActionBar mActionBar;
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
		iniActionBar();
		iniData();
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
	 * 初始化测试用的数据
	 */
	private void iniData(){
		machineArray = getResources().getStringArray(R.array.machineinfo);
		mSelectedMachine = machineArray[0];
		mActionBar.setTitle(mSelectedMachine);
		mMachine = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map=null;
		for(int i=0;i<10;i++){
			map = new HashMap<String, String>();
			map.put("Key",mSelectedMachine+"参数"+i);
			map.put("Value","值:"+Math.random()*10);
			mMachine.add(map);
		}
	}
	
	/**
	 * 设置新的测试用的数据
	 */
	private void setData(){
		mMachine = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map=null;
		for(int i=0;i<10;i++){
			map = new HashMap<String, String>();
			map.put("Key",mSelectedMachine+"参数"+i);
			map.put("Value","值:"+Math.random()*10);
			mMachine.add(map);
		}
	}
	
	
	/**
	 * 填充2个list的数据s
	 */
	private void iniListView(){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(MachineInfoActivity.this, android.R.layout.simple_list_item_1,machineArray);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
		
		infoListViewAdapter =  new MachineInfoItemAdapter();
		infoListView.setAdapter(infoListViewAdapter);
		infoListView.setXListViewListener(this);
	}
	
	/**
	 * 点击查看维修历史
	 */
	private void startMaintainHistoryActivity(){
		Intent intent = new Intent(this, MaintainHistoryActivity.class);
		startActivity(intent);
	}
	/**
	 * 点击查看保养历史
	 */
	private void startUpkeepHistoryActivity(){
		Intent intent = new Intent(this, UpkeepHistoryActivity.class);
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
		mDrawerLayout.closeDrawer(Gravity.LEFT);
		mSelectedMachine = machineArray[position];
		mActionBar.setTitle(mSelectedMachine);
		setData();
		infoListViewAdapter.notifyDataSetChanged();
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
			Toast.makeText(this, "刷新", Toast.LENGTH_SHORT).show();
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
	
	class MachineInfoItemAdapter extends BaseAdapter{
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
			TextView keyTextView = (TextView)convertView.findViewById(R.id.item_machineinfo_key);
			TextView valueTextView = (TextView)convertView.findViewById(R.id.item_machineinfo_value);
			keyTextView.setText(getItem(position).get("Key"));
			valueTextView.setText(getItem(position).get("Value"));
			return convertView;
		}
	}
}
