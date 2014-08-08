package com.env.dcwater.activity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.component.ThreadPool;
import com.env.dcwater.component.ThreadPool.GetDevicePic;
import com.env.dcwater.component.ThreadPool.GetServerConsData;
import com.env.dcwater.fragment.DataFilterView;
import com.env.dcwater.fragment.ListviewItemAdapter;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;
import com.env.dcwater.util.DataCenterHelper;
import com.env.dcwater.util.OperationMethod;
import com.env.dcwater.util.SystemMethod;

public class UpkeepSendActivity extends NfcActivity implements OnItemClickListener,IXListViewListener{
	private ActionBar mActionBar;
	private DrawerLayout drawerLayout;
	private PullToRefreshView dataListView;
	private DataFilterView dataFilterView;
	private GetServerConsData getServerConsData;
	private ArrayList<HashMap<String, String>> data;
	private UpkeepSendAdapter adapter;
	private GetUpkeepSendData getUpkeepSendData;
	private ProgressDialog mProgressDialog;
	private boolean filter = true;
	private int dpi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upkeepsend);
		iniData();
		iniActionBar();
		iniView();
		getServerData();
	}
	
	private void iniData(){
		dpi = SystemMethod.getDpi(getWindowManager());
		data = new ArrayList<HashMap<String,String>>();
		adapter = new UpkeepSendAdapter(data,UpkeepSendActivity.this);
	}	
	
	private void iniActionBar() {
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		mActionBar.setTitle("养护工单派发");
	}
	
	private void iniView(){
		drawerLayout = (DrawerLayout)findViewById(R.id.activity_upkeepsend_drawlayout);
		dataFilterView = (DataFilterView)findViewById(R.id.activity_upkeepsend_datafilter);
		dataFilterView.hideTimeSelectionPart();
		dataFilterView.setStateList(getResources().getStringArray(R.array.view_datafilter_upkeepplanstatelist), 0);
		dataFilterView.setSubmitEvent(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getUpkeepSendData();
				drawerLayout.closeDrawer(Gravity.RIGHT);
			}
		});
		
		
		
		dataListView = (PullToRefreshView)findViewById(R.id.activity_upkeepsend_list);
		dataListView.setOnItemClickListener(this);
		dataListView.setXListViewListener(this);
		dataListView.setAdapter(adapter);
		
		drawerLayout.setDrawerListener(new DrawerListener() {
			@Override
			public void onDrawerStateChanged(int arg0) {
			}
			@Override
			public void onDrawerSlide(View arg0, float arg1) {
			}
			@Override
			public void onDrawerOpened(View arg0) {
				dataListView.setEnabled(false);
				dataListView.setPullRefreshEnable(false);
			}
			@Override
			public void onDrawerClosed(View arg0) {
				dataListView.setEnabled(true);
				dataListView.setPullRefreshEnable(true);
			}
		});
	}
	
	private void getUpkeepSendData(){
		String [] selectionStrings  = dataFilterView.getSelectCondition();
		getUpkeepSendData = new GetUpkeepSendData();
		getUpkeepSendData.execute(selectionStrings[2],selectionStrings[3]);
	}
	
	private void getFilterData(){
		if(SystemParams.getInstance().getConstructionList()==null){
			getServerConsData = new ThreadPool.GetServerConsData() {
				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					if(result!=null){
						String [] posList = new String [result.size()] ;
						for(int i =0;i<result.size();i++){
							posList[i] = result.get(i).get("StructureName");
						}
						dataFilterView.setPosList(posList, 0);
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
			dataFilterView.setPosList(posList, 0);
		}
	}
	
	private void getServerData(){
		getFilterData();
		getUpkeepSendData();
	}
	
	/**
	 * 获取数据时，弹出进度对话框
	 * @param cancelable 是否能被取消的操作
	 */
	private void showProgressDialog(boolean cancelable){
		if(mProgressDialog==null){
			mProgressDialog = new ProgressDialog(UpkeepSendActivity.this);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_upkeepsend, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		drawerLayout.closeDrawer(Gravity.RIGHT);
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		drawerLayout.closeDrawer(Gravity.RIGHT);
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			onBackPressed();
		} else if (itemId == R.id.menu_upkeepsend_filter) {
			filter = ! filter;
			item.setChecked(filter);
			getUpkeepSendData();
		} else if (itemId == R.id.menu_upkeepsend_showdrawer) {
			if(drawerLayout.isDrawerOpen(Gravity.RIGHT)){
				drawerLayout.closeDrawer(Gravity.RIGHT);
			}else {
				drawerLayout.openDrawer(Gravity.RIGHT);
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class UpkeepSendAdapter extends ListviewItemAdapter{
		
		public UpkeepSendAdapter(ArrayList<HashMap<String, String>> data,Context context) {
			super(data,context);
		}
		@Override
		public void setData(ViewHolder viewHolder,final HashMap<String, String> map) {
			if (map.get("PicURL").equals("")) {
				viewHolder.pic.setImageResource(R.drawable.ic_pic_default);
			} else {
				GetDevicePic getDevicePic = new GetDevicePic(viewHolder.pic, dpi, UpkeepSendActivity.this);
				getDevicePic.execute(SystemMethod.getLocalTempPath(), map.get("PicURL").toString(), DataCenterHelper.PIC_URL_STRING + "/" + map.get("PicURL").toString());
			}
			viewHolder.lefttop.setText(map.get("DeviceName")+"("+ map.get("StructureName")+")");
			viewHolder.leftbottom.setText(Html.fromHtml(OperationMethod.getUpkeepSendContent(map)));
			viewHolder.arrow.setVisibility(map.get("CanUpdate").equalsIgnoreCase("true")?View.VISIBLE:View.GONE);
			String temp = map.get("MaintainStateDescription");
			if(temp.equals("完成")||temp.equals("已列入计划")){
				viewHolder.righttop.setText("等待派发");
			}else {
				viewHolder.righttop.setText(map.get("MaintainStateDescription"));
			}
			viewHolder.pic.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SystemMethod.startBigImageActivity(UpkeepSendActivity.this, map.get("PicURL"));
				}
			});
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			getUpkeepSendData();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(position!=0){
			Intent intent = new Intent(UpkeepSendActivity.this,UpkeepSendItemActivity.class);
			intent.putExtra("data", data.get(position-1));
			startActivityForResult(intent, 0);
		}
	}
	
	@Override
	public void onBackPressed() {
		if(drawerLayout.isDrawerOpen(Gravity.RIGHT)){
			drawerLayout.closeDrawer(Gravity.RIGHT);
		}else {
			super.onBackPressed();
			this.finish();
		}
	}
	
	/**
	 * @author sk
	 */
	private class GetUpkeepSendData extends AsyncTask<String, String, ArrayList<HashMap<String, String>>>{
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
			JSONObject param = new JSONObject();
			String result = DataCenterHelper.RESPONSE_FALSE_STRING;
			ArrayList<HashMap<String, String>> data = null;
			try {
				param.put("PlantID", SystemParams.PLANTID_INT+"");
				result = DataCenterHelper.HttpPostData("GetMaintainPlanList", param);
				if(!result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					JSONObject jsonObject = new JSONObject(result);
					data = OperationMethod.parseUpkeepSendDataList(jsonObject,filter,params[0],params[1]);
				}
			} catch (JSONException e) {
				data = null;
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				data = null;
				e.printStackTrace();
			} catch (IOException e) {
				data = null;
				e.printStackTrace();
			} catch (Exception e) {
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
				if(result!=null){
					data = result;
					adapter.datasetNotification(data);
				}
				hideProgressDialog();
				dataListView.stopRefresh();
			}
		}
	}

	@Override
	public void onRefresh() {
		getUpkeepSendData();
	}
}
