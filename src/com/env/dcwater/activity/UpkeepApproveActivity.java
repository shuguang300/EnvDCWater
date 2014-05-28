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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
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

/**
 * @author sk
 *
 */
public class UpkeepApproveActivity extends NfcActivity implements OnItemClickListener,IXListViewListener{
	private ActionBar mActionBar;
	private DrawerLayout drawerLayout;
	private PullToRefreshView dataListView;
	private DataFilterView dataFilterView;
	private GetUpkeepApproveData getUpkeepApproveData;
	private GetServerConsData getServerConsData;
	private ArrayList<HashMap<String, String>> data;
	private ProgressDialog mProgressDialog;
	private UpkeepApproveAdapter adapter;
	private boolean filter = true;
	private int dpi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upkeepapprove);
		iniData();
		iniActionBar();
		iniView();
		getServerData();
	}
	private void iniData(){
		dpi = SystemMethod.getDpi(getWindowManager());
		data = new ArrayList<HashMap<String,String>>();
		adapter = new UpkeepApproveAdapter(data);
	}
	
	private void iniActionBar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		mActionBar.setTitle("审核工单");
	}
	
	private void iniView(){
		drawerLayout = (DrawerLayout)findViewById(R.id.activity_upkeepapprove_drawlayout);
		dataFilterView = (DataFilterView)findViewById(R.id.activity_upkeepapprove_datafilter);
		dataListView = (PullToRefreshView)findViewById(R.id.activity_upkeepapprove_list);
		
		dataListView.setOnItemClickListener(this);
		dataListView.setXListViewListener(this);
		dataListView.setAdapter(adapter);
		
		dataFilterView.hideTimeSelectionPart();
		dataFilterView.setStateList(getResources().getStringArray(R.array.view_datafilter_upkeepstatelist), 0);
		dataFilterView.setSubmitEvent(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getUpkeepApproveData();
				drawerLayout.closeDrawer(Gravity.RIGHT);
			}
		});
		
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
	
	private void getServerData(){
		getFilterData();
		getUpkeepApproveData();
	}
	
	
	private void getUpkeepApproveData(){
		String [] selectionStrings  = dataFilterView.getSelectCondition();
		getUpkeepApproveData = new GetUpkeepApproveData();
		getUpkeepApproveData.execute(selectionStrings[2],selectionStrings[3]);
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
	
	/**
	 * 获取数据时，弹出进度对话框
	 * @param cancelable 是否能被取消的操作
	 */
	private void showProgressDialog(boolean cancelable){
		if(mProgressDialog==null){
			mProgressDialog = new ProgressDialog(UpkeepApproveActivity.this);
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
		getMenuInflater().inflate(R.menu.menu_upkeepapprove,menu);
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
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.menu_upkeepapprove_filter:
			filter = ! filter;
			item.setChecked(filter);
			getUpkeepApproveData();
			break;
		case R.id.menu_upkeepapprove_showdrawer:
			if(drawerLayout.isDrawerOpen(Gravity.RIGHT)){
				drawerLayout.closeDrawer(Gravity.RIGHT);
			}else {
				drawerLayout.openDrawer(Gravity.RIGHT);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(position!=0){
			Intent intent = new Intent(UpkeepApproveActivity.this,UpkeepApproveItemActivity.class);
			intent.putExtra("data", data.get(position-1));
			startActivityForResult(intent, 0);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			getUpkeepApproveData();
		}
	}
	
	
	private class UpkeepApproveAdapter extends ListviewItemAdapter{

		public UpkeepApproveAdapter(ArrayList<HashMap<String, String>> data) {
			super(data);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final HashMap<String, String> map = getItem(position);
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(UpkeepApproveActivity.this).inflate(R.layout.item_listview, null);
				viewHolder.lefttop = (TextView) convertView.findViewById(R.id.item_listview_lefttop);
				viewHolder.righttop = (TextView) convertView.findViewById(R.id.item_listview_righttop);
				viewHolder.leftbottom = (TextView) convertView.findViewById(R.id.item_listview_leftbottom);
				viewHolder.pic = (ImageView) convertView.findViewById(R.id.item_listview_pic);
				viewHolder.arrow = (ImageView) convertView.findViewById(R.id.item_listview_rightbottom);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if(map.get("PicURL").equals("")){
				viewHolder.pic.setImageResource(R.drawable.ic_pic_default);
			}else {
				GetDevicePic getDevicePic = new GetDevicePic(viewHolder.pic,dpi,UpkeepApproveActivity.this);
				getDevicePic.execute(SystemMethod.getLocalTempPath(),map.get("PicURL").toString(),DataCenterHelper.PIC_URL_STRING+"/"+map.get("PicURL").toString());
			}
			viewHolder.lefttop.setText(map.get("DeviceName")+"("+ map.get("StructureName")+")");
			viewHolder.leftbottom.setText(OperationMethod.getUpkeepApproveContent(map));
			viewHolder.righttop.setText(map.get("StateDescription"));
			viewHolder.arrow.setVisibility(map.get("CanUpdate").equalsIgnoreCase("true")?View.VISIBLE:View.GONE);
			viewHolder.pic.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SystemMethod.startBigImageActivity(UpkeepApproveActivity.this, map.get("PicURL"));
				}
			});
			return convertView;
		}
		
	}
	
	
	private class GetUpkeepApproveData extends AsyncTask<String, String, ArrayList<HashMap<String, String>>> {

		protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
			JSONObject param = new JSONObject();
			String result = DataCenterHelper.RESPONSE_FALSE_STRING;
			ArrayList<HashMap<String, String>> data = null;
			try {
				param.put("PlantID", SystemParams.PLANTID_INT+"");
				if(filter){
					result = DataCenterHelper.HttpPostData("GetMaintainTaskApproveList", param);
				}else {
					result = DataCenterHelper.HttpPostData("GetallNotFinish", param);
				}
				if(!result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					JSONObject jsonObject = new JSONObject(result);
					data = OperationMethod.parseUpkeepApproveDataList(jsonObject,filter,params[0],params[1]);
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
		getUpkeepApproveData();
	}
}
