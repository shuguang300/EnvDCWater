package com.env.dcwater.activity;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;

/**
 * 报修管理
 * @author sk
 */
public class RepairManageActivity extends NfcActivity implements IXListViewListener,OnItemClickListener{
	public static final int REPAIRMANAGE_ADD_INTEGER = 0;
	public static final int REPAIRMANAGE_UPDATE_INTEGER = 1;
	public static final int REPAIRMANAGE_DETAIL_INTEGER = 2;
	private ActionBar mActionBar;
	private PullToRefreshView mListView;
	private RepairManageItemAdapter mListViewAdapter;
	private ArrayList<HashMap<String, String>> mData;
	private AlertDialog.Builder mDeleteConfirm ;
	private Intent sendedIntent;
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
	 * 初始化数据
	 */
	private void iniData(){
		mData = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map =null;
		for (int i = 0; i < 10; i++) {
			map = new HashMap<String, String>();
			map.put("ID", i+"");
			map.put("Time", i+"");
			map.put("State", i+"");
			map.put("Name", i+"");
			map.put("Info", i+"");
			mData.add(map);
		}
	}
	
	/**
	 * 初始化view
	 */
	private void iniView(){
		mListView = (PullToRefreshView)findViewById(R.id.activity_repairmanage_info);
		mListViewAdapter = new RepairManageItemAdapter();
		mListView.setAdapter(mListViewAdapter);
		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(this);
//		mListView.setOnItemLongClickListener(this); //长按事件与上下文菜单冲突，二者只能选其一
		registerForContextMenu(mListView);
	}
	
	private void sendIntent(int code,HashMap<String, String> data){
		sendedIntent = new Intent(RepairManageActivity.this, RepairManageItemActivity.class);
		sendedIntent.putExtra("RequestCode", code);
		switch (code) {
		case REPAIRMANAGE_ADD_INTEGER:
			break;
		case REPAIRMANAGE_DETAIL_INTEGER:
			sendedIntent.putExtra("Data", data);
			break;
		case REPAIRMANAGE_UPDATE_INTEGER:
			sendedIntent.putExtra("Data", data);
			break;
		}
		startActivityForResult(sendedIntent, code);
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
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
	@Override
	protected void onDestroy() {
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
			sendIntent(REPAIRMANAGE_ADD_INTEGER,null);
			break;
		case R.id.menu_repairmanage_refresh:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		//不需要为headerview注册上下文菜单，所以进行判断
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		if(info.position!=0){
			getMenuInflater().inflate(R.menu.contextmenu_repairmanage, menu);
			menu.setHeaderTitle("更多");  
		}
	}
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		//获得contextmenu的触发控件
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		final int selectedPos = info.position-1;
		switch (item.getItemId()) {
		case R.id.contextmenu_repairmanage_update:
			sendIntent(REPAIRMANAGE_UPDATE_INTEGER,mData.get(selectedPos));
			break;
		case R.id.contextmenu_repairmanage_delete:
			if(mDeleteConfirm==null){
				mDeleteConfirm = new AlertDialog.Builder(RepairManageActivity.this);
			}
			mDeleteConfirm.setTitle("确认").setMessage("确认删除吗？"+selectedPos);
			mDeleteConfirm.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mData.remove(selectedPos);
					mListViewAdapter.notifyDataSetChanged();
				}
			}).setNegativeButton("取消", null);
			mDeleteConfirm.create();
			mDeleteConfirm.show();
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
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(position!=0){
			sendIntent(REPAIRMANAGE_DETAIL_INTEGER,mData.get(position-1));
		}
	}
	
	private class RepairManageItemAdapter extends BaseAdapter{
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
