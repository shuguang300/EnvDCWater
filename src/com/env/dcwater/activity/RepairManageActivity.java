package com.env.dcwater.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
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
import android.widget.Toast;
import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.fragment.DataFilterView;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;
import com.env.dcwater.javabean.EnumList;
import com.env.dcwater.util.DataCenterHelper;
import com.env.dcwater.util.OperationMethod;

/**
 * 设备维修管理
 * @author sk
 */
public class RepairManageActivity extends NfcActivity implements IXListViewListener,OnItemClickListener{
	
	public static final String TAG_STRING = "RepairManageActivity";
	
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
	private DrawerLayout mDrawerLayout;
	private DataFilterView mDataFilterView;
	private RepairManageItemAdapter mListViewAdapter;
	private ArrayList<HashMap<String, String>> mData;
	private AlertDialog.Builder mDeleteConfirm ;
	private Intent sendedIntent;
	private GetServerData getServerData;
	private boolean isFilter = true,isRfresh = false;
	private ProgressDialog mProgressDialog;
	private DeleteServerData deleteServerData;
	private int selectedPos;
	private String [] dateFilters;
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
	
	/**
	 * 从服务器上获取数据
	 */
	private void getServerData(){
		if(!isRfresh){
			getServerData = new GetServerData();
			getServerData.execute("");
		}
	}
	
	/**
	 * 删除工单
	 */
	private void deleteServerData(){
		deleteServerData = new DeleteServerData();
		deleteServerData.execute("");
	}
	
	/**
	 * 获取数据时，弹出进度对话框
	 * @param cancelable 是否能被取消的操作
	 */
	private void showProgressDialog(boolean cancelable){
		if(mProgressDialog==null){
			mProgressDialog = new ProgressDialog(RepairManageActivity.this);
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
	 * 初始化view
	 */
	private void iniView(){
		mListView = (PullToRefreshView)findViewById(R.id.activity_repairmanage_info);
		mDrawerLayout = (DrawerLayout)findViewById(R.id.activity_repairmanage_drawlayout);
		mDataFilterView = (DataFilterView)findViewById(R.id.activity_repairmanage_datafilter);
		
		mDataFilterView.setSubmitEvent(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getServerData();
			}
		});
		
		mDataFilterView.hideTimeSelectionPart();
		
		mListViewAdapter = new RepairManageItemAdapter();
		mListView.setAdapter(mListViewAdapter);
		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(this);
		
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
				mListView.setEnabled(false);
				mListView.setPullRefreshEnable(false);
			}
			@Override
			public void onDrawerClosed(View arg0) {
				mListView.setEnabled(true);
				mListView.setPullRefreshEnable(true);
				if(mDataFilterView.isDateTimePickerShowing()){
					mDataFilterView.hideDateTimePicker();
				}
			}
		});
		
		
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK){
			getServerData();
		}
		
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
		int positionID = Integer.valueOf(SystemParams.getInstance().getLoggedUserInfo().get("PositionID"));
		if(positionID == EnumList.UserRole.USERROLEEQUIPMENTOPERATION||positionID==EnumList.UserRole.USERROLEPRODUCTIONOPERATION){
			menu.getItem(0).setVisible(true);
		}else {
			menu.getItem(0).setVisible(false);
		}
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
		case R.id.menu_repairmanage_filter:
			isFilter = !isFilter;
			if (isFilter) {
				item.setTitle(getResources().getString(R.string.menu_repairmanage_filter));
			}else {
				item.setTitle(getResources().getString(R.string.menu_repairmanage_nofilter));
			}
			getServerData();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		//不需要为headerview注册上下文菜单，所以进行判断
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		if(info.position!=0&&mData.get(info.position-1).get("CanUpdate").equals("true")){
			getMenuInflater().inflate(R.menu.contextmenu_repairmanage, menu);
			menu.setHeaderTitle("更多");  
		}
	}
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		//获得contextmenu的触发控件
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		selectedPos = info.position-1;
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
					deleteServerData();
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
		if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		}else {
			this.finish();
		}
		
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
	
	/**
	 * 获取服务器端的数据，并将数据放入 arraylist中
	 * @author sk
	 */
	class GetServerData extends AsyncTask<String, String, ArrayList<HashMap<String, String>>>{

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
			isRfresh = true;
			JSONObject object = new JSONObject();
			ArrayList<HashMap<String, String>> data = null;
			try {
				object.put("PlantID", SystemParams.PLANTID_INT);
				object.put("UserRole", Integer.valueOf(SystemParams.getInstance().getLoggedUserInfo().get("UserRole")));
				String result = DataCenterHelper.HttpPostData("GetReportInfoList", object);
				if(!result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					JSONObject jsonObject = new JSONObject(result);
					int rolePositionID = Integer.valueOf(SystemParams.getInstance().getLoggedUserInfo().get("PositionID"));
					dateFilters = mDataFilterView.getSelectCondition();
					int taskState = OperationMethod.getTaskStateByStateName(dateFilters[3]);
					data = OperationMethod.parseRepairTaskToArray(rolePositionID, jsonObject, taskState, isFilter);
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
			showProgressDialog(true);
		}
		
		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			super.onPostExecute(result);
			if(result!=null){
				mData = result;
				mListViewAdapter.notifyDataSetChanged();
			}else {
				Toast.makeText(RepairManageActivity.this, "获取数据失败，请重试", Toast.LENGTH_SHORT).show();
			}
			mListView.stopRefresh();
			isRfresh = false;
			hideProgressDialog();
		}
		
	}
	
	class DeleteServerData extends AsyncTask<String, String, String>{
		@Override
		protected String doInBackground(String... params) {
			JSONObject param = new JSONObject();
			String result = DataCenterHelper.RESPONSE_FALSE_STRING;
			try {
				param.put("RepairTaskID", Integer.valueOf(mData.get(selectedPos).get("RepairTaskID")));
				result = DataCenterHelper.HttpPostData(DataCenterHelper.METHOD_DELETETASK_STRING, param);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(false);
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			hideProgressDialog();
			if(result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
				Toast.makeText(RepairManageActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
			}else {
				JSONObject object;
				try {
					object = new JSONObject(result);
					if(object.getBoolean("d")){
						mData.remove(selectedPos);
						mListViewAdapter.notifyDataSetChanged();
					}else {
						Toast.makeText(RepairManageActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(RepairManageActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
}
