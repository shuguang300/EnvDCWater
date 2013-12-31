package com.env.dcwater.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;

/**
 * 保养历史记录
 * @author sk
 */
public class UpkeepHistoryActivity extends NfcActivity implements OnItemClickListener,IXListViewListener{
	private ActionBar mActionBar;
	private PullToRefreshView mHistoryList;
	private UpkeepHistoryItemAdapter mAdapter;
	private ArrayList<HashMap<String, Object>> mData;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				mHistoryList.stopRefresh();
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upkeephistory);
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
	 * 初始化界面的控件
	 */
	private void iniView(){
		mAdapter = new UpkeepHistoryItemAdapter();
		mHistoryList = (PullToRefreshView)findViewById(R.id.activity_upkeephistory_list);
		mHistoryList.setAdapter(mAdapter);
		mHistoryList.setOnItemClickListener(this);
		mHistoryList.setXListViewListener(this);
	}
	
	/**
	 * 初始化数据
	 */
	private void iniData(){
		mData = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> map = null;
		for(int i =0;i<30;i++){
			map = new HashMap<String, Object>();
			map.put("Name", "Name"+i);
			map.put("State", "State"+i);
			mData.add(map);
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
		getMenuInflater().inflate(R.menu.menu_upkeephistory, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.menu_upkeephistory_refresh:
			Toast.makeText(UpkeepHistoryActivity.this, "刷新", Toast.LENGTH_SHORT).show();
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
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mHandler.sendEmptyMessage(0);
			}
		}).start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		
	}
	
	private class UpkeepHistoryItemAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public HashMap<String, Object> getItem(int position) {
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView = LayoutInflater.from(UpkeepHistoryActivity.this).inflate(R.layout.item_upkeephistory, null);
			}
			HashMap<String, Object> map = getItem(position);
			TextView name = (TextView)convertView.findViewById(R.id.item_upkeephistory_name);
			TextView state = (TextView)convertView.findViewById(R.id.item_upkeephistory_state);
			name.setText(map.get("Name").toString());
			state.setText(map.get("State").toString());
			return convertView;
		}
	}
}
