package com.env.dcwater.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.env.dcwater.javabean.EnumList;
import com.env.dcwater.util.DataCenterHelper;

/**
 * 设备维修管理
 * @author sk
 */
public class RepairManageActivity extends NfcActivity implements IXListViewListener,OnItemClickListener{
	/**
	 * 新增模式
	 */
	public static final int REPAIRMANAGE_ADD_INTEGER = 0;
	/**
	 * 修改模式
	 */
	public static final int REPAIRMANAGE_UPDATE_INTEGER = 1;
	/**
	 * 浏览模式
	 */
	public static final int REPAIRMANAGE_DETAIL_INTEGER = 2;
	
	private ActionBar mActionBar;
	private PullToRefreshView mListView;
	private RepairManageItemAdapter mListViewAdapter;
	private ArrayList<HashMap<String, String>> mData;
	private AlertDialog.Builder mDeleteConfirm ;
	private Intent sendedIntent;
	private GetServerData getServerData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repairmanage);
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
	}
	
	/**
	 * 初始化数据
	 */
	private void iniData(){
		mData = new ArrayList<HashMap<String,String>>();
	}
	
	private void getServerData(){
		getServerData = new GetServerData();
		getServerData.execute("");
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
	
	/**
	 * 根据code进行activity的跳转，并携带该报修单的信息
	 * @param code
	 * @param data
	 */
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
		getServerData();
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
			id.setText(map.get("FaultReportSN"));
			TextView time = (TextView)convertView.findViewById(R.id.item_repairmanage_time);
			time.setText(map.get("AccidentOccurTime"));
			TextView state = (TextView)convertView.findViewById(R.id.item_repairmanage_state);
			state.setText(map.get("StateDescription"));
			TextView name = (TextView)convertView.findViewById(R.id.item_repairmanage_name);
			name.setText(map.get("DeviceName"));
			TextView info = (TextView)convertView.findViewById(R.id.item_repairmanage_info);
			info.setText(map.get("AccidentDetail"));
			return convertView;
		}
	}
	
	class GetServerData extends AsyncTask<String, String, ArrayList<HashMap<String, String>>>{

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
			JSONObject object = new JSONObject();
			ArrayList<HashMap<String, String>> data = null;
			try {
				object.put("PlantID", 1);
				String result = DataCenterHelper.HttpPostData("GetReportInfoList", object);
				if(!result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					JSONObject jsonObject = new JSONObject(result);
					JSONArray jsonArray = new JSONArray(jsonObject.getString("d").toString());
					JSONObject report = null;
					HashMap<String, String> map = null;
					data = new ArrayList<HashMap<String,String>>();
					for(int i =0;i<jsonArray.length();i++){
						report = jsonArray.getJSONObject(i);
						map = new HashMap<String, String>();
						if(Integer.valueOf(report.get("State").toString())!=EnumList.RepairState.HASBEENREPORTED.getState()){
							continue;
						}
						map.put("RepairTaskID", report.get("RepairTaskID").toString());
						map.put("FaultReportSN", report.get("FaultReportSN").toString());
						map.put("AccidentOccurTime", report.get("AccidentOccurTime").toString().replace("T", " "));
						map.put("DeviceName", report.get("DeviceName").toString());
						map.put("InstallPosition", report.get("InstallPosition").toString());
						map.put("RepairedTime", report.get("RepairedTime").toString().replace("T", " "));
						map.put("ReportPerson", report.get("ReportPerson").toString());
						map.put("State", report.get("State").toString());
						map.put("StateDescription", EnumList.RepairState.getEnumRepairState(Integer.valueOf(report.get("State").toString())).getStateDescription());
						map.put("AccidentDetail", report.get("AccidentDetail").toString());
						data.add(map);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				data = null;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				data = null;
			} catch (IOException e) {
				e.printStackTrace();
				data = null;
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
				mData = result;
				mListViewAdapter.notifyDataSetChanged();
			}
			mListView.stopRefresh();
		}
		
	}
	
}
