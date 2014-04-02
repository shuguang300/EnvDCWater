package com.env.dcwater.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.fragment.DataFilterView;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;

/**
 * 保养历史记录
 * @author sk
 */
public class UpkeepHistoryActivity extends NfcActivity implements OnItemClickListener,IXListViewListener{
	
	public static final String TAG_STRING = "UpkeepHistoryActivity";
	
	private ActionBar mActionBar;
	private PullToRefreshView mHistoryList;
	private UpkeepHistoryItemAdapter mAdapter;
	private DrawerLayout mDrawerLayout;
	private DataFilterView mDataFilterView;
	private ArrayList<HashMap<String, String>> mData;
	private Intent receivedIntent;
	private String receivedAction;
	private HashMap<String, String> receivedData;
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
		iniData();
		iniActionBar();
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
		if(receivedAction.equals(MainActivity.ACTION_STRING)){
			mActionBar.setTitle("保养历史总览");
		}else if (receivedAction.equals(MachineInfoActivity.ACTION_STRING)) {
			mActionBar.setTitle(receivedData.get("DeviceName")+"保养历史");
		}
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
		mDrawerLayout = (DrawerLayout)findViewById(R.id.activity_upkeephistory_drawlayout);
		mDataFilterView =(DataFilterView)findViewById(R.id.activity_upkeephistory_datafilter);
		mDataFilterView.setStateList(getResources().getStringArray(R.array.view_datafilter_statelist),0);
		mDataFilterView.setPosList(getResources().getStringArray(R.array.view_datafilter_poslist),0);
		mDrawerLayout.setDrawerListener(new DrawerListener() {
			@Override
			public void onDrawerStateChanged(int arg0) {
			}
			@Override
			public void onDrawerSlide(View arg0, float arg1) {
			}
			@Override
			public void onDrawerOpened(View arg0) {
				mHistoryList.setEnabled(false);
				mHistoryList.setPullRefreshEnable(false);
			}
			@Override
			public void onDrawerClosed(View arg0) {
				mHistoryList.setEnabled(true);
				mHistoryList.setPullRefreshEnable(true);
				if(mDataFilterView.isDateTimePickerShowing()){
					mDataFilterView.hideDateTimePicker();
				}
			}
		});
	}
	
	/**
	 * 初始化数据
	 */
	@SuppressWarnings("unchecked")
	private void iniData(){
		receivedIntent = getIntent();
		receivedAction = receivedIntent.getExtras().getString("action");
		receivedData = (HashMap<String, String>)receivedIntent.getExtras().getSerializable("data");
		mData = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = null;
		for(int i =0;i<30;i++){
			map = new HashMap<String, String>();
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
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed() {
		if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		}else {
			super.onBackPressed();
			this.finish();
		}
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
	
	/**
	 * 保养记录的自定义adapter 
	 * @author sk
	 */
	private class UpkeepHistoryItemAdapter extends BaseAdapter{
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
				convertView = LayoutInflater.from(UpkeepHistoryActivity.this).inflate(R.layout.item_upkeephistory, null);
			}
			HashMap<String, String> map = getItem(position);
			TextView name = (TextView)convertView.findViewById(R.id.item_upkeephistory_name);
			TextView state = (TextView)convertView.findViewById(R.id.item_upkeephistory_state);
			name.setText(map.get("Name").toString());
			state.setText(map.get("State").toString());
			return convertView;
		}
	}
}
