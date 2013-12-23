package com.env.dcwater.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;

public class RepairManageActivity extends NfcActivity implements IXListViewListener,OnItemClickListener,OnItemLongClickListener{
	private ActionBar mActionBar;
	private PullToRefreshView mListView;
	private RepairManageItemAdapter mListViewAdapter;
	private ArrayList<HashMap<String, String>> mData;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			mListView.stopRefresh();
			mListViewAdapter.notifyDataSetChanged();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repairmanage);
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
	 * 初始化页面数据
	 */
	private void iniData(){
		mData = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map =null;
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			map = new HashMap<String, String>();
			map.put("ID", "编号"+random.nextInt(10));
			map.put("Time", "时间"+random.nextInt(10));
			map.put("State", "已上报"+random.nextInt(10));
			map.put("Name", "位于"+random.nextInt(10));
			map.put("Info", "故障信息"+random.nextInt(10));
			mData.add(map);
		}
	}
	
	/**
	 * 初始化布局控件
	 */
	private void iniView(){
		mListView = (PullToRefreshView)findViewById(R.id.activity_repairmanage_info);
		mListViewAdapter = new RepairManageItemAdapter();
		mListView.setAdapter(mListViewAdapter);
		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(this);
//		mListView.setOnItemLongClickListener(this);
		registerForContextMenu(mListView);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_repairmanage, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.menu_repairmanage_add:
			Toast.makeText(this, "增加上报", Toast.LENGTH_SHORT).show();
			break;
		case R.id.menu_repairmanage_refresh:
			Toast.makeText(this, "刷新", Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.contextmenu_repairmanage, menu);
		menu.setHeaderTitle("更多功能");  
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		//获取触发contextmenu的listview的item的position
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		int selectedPos = info.position;
		switch (item.getItemId()) {
		case R.id.contextmenu_repairmanage_update:
			Toast.makeText(this, "修改"+selectedPos, Toast.LENGTH_SHORT).show();
			break;
		case R.id.contextmenu_repairmanage_delete:
			Toast.makeText(this, "删除"+selectedPos, Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onContextItemSelected(item);
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
					iniData();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mHandler.sendEmptyMessage(0);
			}
		}).start();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Toast.makeText(this, "详细信息", Toast.LENGTH_SHORT).show();
	}
	
	class RepairManageItemAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return mData.size();
		}
		@Override
		public HashMap<String, String> getItem(int position) {
			return mData.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView = LayoutInflater.from(RepairManageActivity.this).inflate(R.layout.item_repairmanage, null);
			}
			HashMap<String, String> map = getItem(position);
			TextView id = (TextView)convertView.findViewById(R.id.item_repairmanage_id);
			id.setText(map.get("ID"));
			TextView time = (TextView)convertView.findViewById(R.id.item_repairmanage_time);
			time.setText(map.get("Time"));
			TextView state = (TextView)convertView.findViewById(R.id.item_repairmanage_state);
			state.setText(map.get("State"));
			TextView name = (TextView)convertView.findViewById(R.id.item_repairmanage_name);
			name.setText(map.get("Name"));
			TextView info = (TextView)convertView.findViewById(R.id.item_repairmanage_info);
			info.setText(map.get("Info"));
			return convertView;
		}
	}
	
}
