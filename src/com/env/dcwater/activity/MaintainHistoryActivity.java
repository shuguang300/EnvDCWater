package com.env.dcwater.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;

/**
 * 维修历史记录
 * @author sk
 */
public class MaintainHistoryActivity extends NfcActivity implements IXListViewListener,OnItemClickListener{
	private ActionBar mActionBar;
	private PullToRefreshView mHistoryList;
	private ArrayList<HashMap<String, Object>> mHistories; 
	private MaintainHistoryItemAdapter mAdapter;
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mHistoryList.stopRefresh();
				mAdapter.notifyDataSetChanged();
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintainhistory);
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
	 * 初始化控件
	 */
	private void iniView(){
		mAdapter = new MaintainHistoryItemAdapter();
		mHistoryList = (PullToRefreshView)findViewById(R.id.activity_maintainhistory_list);
		mHistoryList.setAdapter(mAdapter);
		mHistoryList.setOnItemClickListener(this);
		mHistoryList.setXListViewListener(this);
	}
	
	
	/**
	 * 初始化测试数据
	 */
	private void iniData() {
		mHistories = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> history = null;
		for(int i =0;i<10;i++){
			history = new HashMap<String, Object>();
			history.put("Name", "test"+i);
			history.put("State", "state"+i);
			mHistories.add(history);
		}
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
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
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
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
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		
	}

	@Override
	public void onRefresh() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mHandler.sendEmptyMessage(0);
			}
		}).start();
	}
	
	private class MaintainHistoryItemAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mHistories.size();
		}

		@Override
		public HashMap<String, Object> getItem(int position) {
			return mHistories.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = LayoutInflater.from(MaintainHistoryActivity.this).inflate(R.layout.item_maintainhistory, null);
			}
			HashMap<String,Object> map = getItem(position);
			TextView name = (TextView)convertView.findViewById(R.id.item_maintainhistory_name);
			TextView state = (TextView)convertView.findViewById(R.id.item_maintainhistory_state);
			name.setText(map.get("Name").toString());
			state.setText(map.get("State").toString());
			return convertView;
		}
		
	}
}
