package com.env.dcwater.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnConnectionPNames;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Gravity;
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
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.component.ThreadPool;
import com.env.dcwater.fragment.DataFilterView;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;
import com.env.dcwater.util.DataCenterHelper;

/**
 * 维修历史记录
 * @author sk
 */
public class MaintainHistoryActivity extends NfcActivity implements IXListViewListener,OnItemClickListener{
	
	public static final String ACTION_STRING = "MaintainHistoryActivity";
	private ThreadPool.GetServerConsData getServerConsData;
	private ActionBar mActionBar;
	private DrawerLayout mDrawerLayout;
	private DataFilterView mDataFilterView;
	private PullToRefreshView mHistoryList;
	private ArrayList<HashMap<String, String>> mHistories; 
	private MaintainHistoryItemAdapter mAdapter;
	private Intent receivedIntent;
	private String receivedAction;
	private HashMap<String, String> receivedData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintainhistory);
		iniData();
		iniActionBar();
		iniView();
		getServerData();
	}
	
	/**
	 * 初始化actionbar
	 */
	private void iniActionBar(){
		mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		//判断是否是所有设备的维修历史
		if(receivedAction.equals(MainActivity.ACTION_STRING)){
			mActionBar.setTitle("维修历史总览");
		}else if (receivedAction.equals(MachineInfoActivity.ACTION_STRING)) {
			mActionBar.setTitle(receivedData.get("DeviceName")+"维修历史");
		}
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
		mDrawerLayout = (DrawerLayout)findViewById(R.id.activity_maintainhistory_drawlayout);
		mDataFilterView = (DataFilterView)findViewById(R.id.activity_maintainhistory_datafilter);
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
	 * 初始化测试数据
	 */
	@SuppressWarnings("unchecked")
	private void iniData() {
		receivedIntent = getIntent();
		receivedAction = receivedIntent.getExtras().getString("action");
		receivedData = (HashMap<String,String>)receivedIntent.getExtras().getSerializable("data");
		mHistories = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> history = null;
		for(int i =0;i<10;i++){
			history = new HashMap<String, String>();
			history.put("Name", "test"+i);
			history.put("State", "state"+i);
			mHistories.add(history);
		}
	}
	
	/**
	 * 调用webservice的异步方法
	 */
	private void getServerData(){
		if(SystemParams.getInstance().getConstructionList()==null){
			getServerConsData = new ThreadPool.GetServerConsData() {
				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					if(result!=null){
						String [] posList = new String [result.size()] ;
						for(int i =0;i<result.size();i++){
							posList[i] = result.get(i).get("StructureName");
						}
						mDataFilterView.setPosList(posList, 0);
					}
				}
			};
			getServerConsData.execute("");
		}else {
			ArrayList<HashMap<String, String>> result = SystemParams.getInstance().getConstructionList();
			String [] posList = new String [result.size()] ;
			for(int i =0;i<result.size();i++){
				posList[i] = result.get(i).get("StructureName");
			}
			mDataFilterView.setPosList(posList, 0);
		}
//		if(!isRfresh){
//			getServerData = new GetServerData();
//			getServerData.execute("");
//		}
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
		getMenuInflater().inflate(R.menu.menu_maintainhistory, menu);
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
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
	}

	@Override
	public void onRefresh() {
		mHistoryList.stopRefresh();
	}
	
	/**
	 * 维修历史的自定义adapter
	 * @author sk
	 */
	private class MaintainHistoryItemAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return mHistories.size();
		}
		@Override
		public HashMap<String, String> getItem(int position) {
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
			HashMap<String,String> map = getItem(position);
			TextView name = (TextView)convertView.findViewById(R.id.item_maintainhistory_name);
			TextView state = (TextView)convertView.findViewById(R.id.item_maintainhistory_state);
			name.setText(map.get("Name").toString());
			state.setText(map.get("State").toString());
			return convertView;
		}
	}
	
	
	/**
	 * 获取维修历史数据的异步调用方法AsyncTask
	 * @author sk
	 */
	class GetServerTaskHistoryData extends AsyncTask<String, String, ArrayList<HashMap<String, String>>>{

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
			JSONObject param = new JSONObject();
			String result = DataCenterHelper.RESPONSE_FALSE_STRING;
			ArrayList<HashMap<String, String>> data = null;
			try {
				param.put("PlantID", params[0]);
				param.put("StartTime", params[1]);
				param.put("EndTime", params[2]);
				param.put("DeviceID", params[3]);
				result = DataCenterHelper.HttpPostData("GetReportInfoArraylist", param);
				Log.v(TAG_STRING, result);
			} catch (ClientProtocolException e) {
				data = null;
				e.printStackTrace();
			} catch (IOException e) {
				data = null;
				e.printStackTrace();
			} catch (JSONException e) {
				data = null;				
				e.printStackTrace();
			}
			return data;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			super.onPostExecute(result);
			if(result!=null){
				mAdapter.notifyDataSetChanged();
			}
		}
		
	}
}
