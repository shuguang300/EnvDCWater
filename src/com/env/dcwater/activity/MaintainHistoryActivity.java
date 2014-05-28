package com.env.dcwater.activity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.component.ThreadPool;
import com.env.dcwater.component.ThreadPool.GetDevicePic;
import com.env.dcwater.fragment.DataFilterView;
import com.env.dcwater.fragment.ListviewItemAdapter;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;
import com.env.dcwater.util.DataCenterHelper;
import com.env.dcwater.util.OperationMethod;
import com.env.dcwater.util.SystemMethod;

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
	private ProgressDialog mProgressDialog;
	private GetServerTaskHistoryData getServerTaskHistoryData;
	private ArrayList<HashMap<String, String>> mHistoryArrayList; 
	private MaintainHistoryAdapter mAdapter;
	private Intent receivedIntent;
	private String receivedAction;
	private HashMap<String, String> receivedData;
	private TextView titleMessage;
	private int dpi;
	
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
	 * 初始化测试数据
	 */
	@SuppressWarnings("unchecked")
	private void iniData() {
		dpi = SystemMethod.getDpi(getWindowManager());
		receivedIntent = getIntent();
		receivedAction = receivedIntent.getExtras().getString("action");
		receivedData = (HashMap<String,String>)receivedIntent.getExtras().getSerializable("data");
		mHistoryArrayList = new ArrayList<HashMap<String,String>>();
	}
	
	/**
	 * 初始化actionbar
	 */
	private void iniActionBar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		//判断是否是所有设备的维修历史
		if(receivedAction.equals(MainActivity.ACTION_STRING)||receivedAction.equals(DeviceInfoListActivity.ACTION_STRING)){
			mActionBar.setTitle("维修历史总览");
		}else if (receivedAction.equals(DeviceInfoItemActivity.ACTION_STRING)) {
			mActionBar.setTitle(receivedData.get("DeviceName")+"维修历史");
		}
	}
	
	/**
	 * 初始化控件
	 */
	private void iniView(){
		mAdapter = new MaintainHistoryAdapter(mHistoryArrayList);
		mHistoryList = (PullToRefreshView)findViewById(R.id.activity_maintainhistory_list);
		mHistoryList.setAdapter(mAdapter);
		mHistoryList.setOnItemClickListener(this);
		mHistoryList.setXListViewListener(this);
		mDrawerLayout = (DrawerLayout)findViewById(R.id.activity_maintainhistory_drawlayout);
		mDataFilterView = (DataFilterView)findViewById(R.id.activity_maintainhistory_datafilter);
		mDataFilterView.hideTaskStatePicker();
		mDataFilterView.setFastTimeTitle("故障发生时间");
		mDataFilterView.setSubmitEvent(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDrawerLayout.closeDrawer(Gravity.RIGHT);
				getServerHistoryData();
			}
		});
		
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
	
	private void getServerData(){
		getServerConsData();
		getServerHistoryData();
	}
	
	/**
	 * 获取构筑物
	 */
	private void getServerConsData(){
		if(SystemParams.getInstance().getConstructionList()==null){
			getServerConsData = new ThreadPool.GetServerConsData() {
				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					if(result!=null){
						SystemParams.getInstance().setConstructionList(result);
						String [] posList = new String [result.size()] ;
						for(int i =0;i<result.size();i++){
							posList[i] = result.get(i).get("StructureName");
						}
						mDataFilterView.setPosList(posList, 0);
					}
				}

				@Override
				protected void onPreExecute() {
					
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
	}
	
	/**
	 * 获取维修历史的数据
	 */
	private void getServerHistoryData(){
		String [] filterData = mDataFilterView.getSelectCondition();
		String startTime = filterData[0]+" 00:00:00";
		String endTime = filterData[1]+" 23:59:59";
		String consName = filterData[2];
		String plantID = SystemParams.PLANTID_INT+"";
		getServerTaskHistoryData = new GetServerTaskHistoryData();
		if(receivedAction.equals(MainActivity.ACTION_STRING)||receivedAction.equals(DeviceInfoListActivity.ACTION_STRING)){
			getServerTaskHistoryData.execute(plantID,startTime,endTime,"",consName);
		}else if (receivedAction.equals(DeviceInfoItemActivity.ACTION_STRING)) {
			getServerTaskHistoryData.execute(plantID,startTime,endTime,receivedData.get("DeviceID"),consName);
		}
	}
	
	/**
	 * 获取数据时，弹出进度对话框
	 * @param cancelable 是否能被取消的操作
	 */
	private void showProgressDialog(boolean cancelable){
		if(mProgressDialog==null){
			mProgressDialog = new ProgressDialog(MaintainHistoryActivity.this);
			mProgressDialog.setTitle("获取数据中");
			mProgressDialog.setMessage("正在努力加载数据，请稍后");
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setCancelable(cancelable);
		}
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
	
	private void startCheckDetailActivity(HashMap<String, String> data){
		Intent intent = new Intent(this,RepairManageItemActivity.class);
		intent.putExtra("RequestCode", RepairManageActivity.REPAIRMANAGE_HISTORY_INTEGER);
		intent.putExtra("Data", data);
		startActivity(intent);
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
		titleMessage = (TextView)menu.getItem(0).getActionView();
		titleMessage.setTextColor(getResources().getColor(R.color.white));
//		titleMessage.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.small));
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.menu_maintainhistory_showdraw:
			if(mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
				mDrawerLayout.closeDrawer(Gravity.RIGHT);
			}else {
				mDrawerLayout.openDrawer(Gravity.RIGHT);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		mDrawerLayout.closeDrawer(Gravity.RIGHT);
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public void onBackPressed() {
		if(mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
			mDrawerLayout.closeDrawer(Gravity.RIGHT);
		}else {
			super.onBackPressed();
			this.finish();
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(position!=0){
			startCheckDetailActivity(mHistoryArrayList.get(position-1));
		}
	}

	@Override
	public void onRefresh() {
		getServerHistoryData();
	}
	
	private class MaintainHistoryAdapter extends ListviewItemAdapter{

		public MaintainHistoryAdapter(ArrayList<HashMap<String, String>> data) {
			super(data);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final HashMap<String, String> map = getItem(position);
			ViewHolder viewHolder ;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(MaintainHistoryActivity.this).inflate(R.layout.item_listview, null);
				viewHolder.lefttop = (TextView) convertView.findViewById(R.id.item_listview_lefttop);
				viewHolder.righttop = (TextView) convertView.findViewById(R.id.item_listview_righttop);
				viewHolder.leftbottom = (TextView) convertView.findViewById(R.id.item_listview_leftbottom);
				viewHolder.pic = (ImageView) convertView.findViewById(R.id.item_listview_pic);
				viewHolder.arrow = (ImageView) convertView.findViewById(R.id.item_listview_rightbottom);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.lefttop.setText(map.get("DeviceName")+"("+ map.get("InstallPosition")+")");
			viewHolder.leftbottom.setText(OperationMethod.getMaintainHistoryContent(map));
			viewHolder.righttop.setText("");
			if(map.get("PicURL").equals("")){
				viewHolder.pic.setImageResource(R.drawable.ic_pic_default);
			}else {
				GetDevicePic getDevicePic = new GetDevicePic(viewHolder.pic,dpi,MaintainHistoryActivity.this);
				getDevicePic.execute(SystemMethod.getLocalTempPath(),map.get("PicURL").toString(),DataCenterHelper.PIC_URL_STRING+"/"+map.get("PicURL").toString());
			}
			viewHolder.pic.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SystemMethod.startBigImageActivity(MaintainHistoryActivity.this, map.get("PicURL"));
				}
			});
			return convertView;
		}
		
	}
	
	
	
	/**
	 * 获取维修历史数据的异步调用方法AsyncTask
	 * @author sk
	 * @param 0 PlantID to WebService
	 * @param 1 StartTime to WebService
	 * @param 2 EndTime to WebService
	 * @param 3 DeviceID to WebService
	 * @param 4 ConsName to Filter Data
	 */
	private class GetServerTaskHistoryData extends AsyncTask<String, String, ArrayList<HashMap<String, String>>>{

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
				if(!result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					JSONObject jsonObject = new JSONObject(result);
					data = OperationMethod.parseRepairHistoryDataToList(jsonObject,params[4]);
				}
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
			showProgressDialog(true);
		}
		
		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			super.onPostExecute(result);
			if(mProgressDialog!=null&&mProgressDialog.isShowing()){
				int count = 0;
				if(result!=null){
					mHistoryArrayList = result;
					mAdapter.datasetNotification(mHistoryArrayList);
					count = mHistoryArrayList.size();
				}
				hideProgressDialog();
				mHistoryList.stopRefresh();
				titleMessage.setText("当前共有"+count+"条记录");
			}
		}
		
	}
}
