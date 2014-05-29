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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.component.ThreadPool;
import com.env.dcwater.component.ThreadPool.GetDevicePic;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;
import com.env.dcwater.util.DataCenterHelper;
import com.env.dcwater.util.OperationMethod;
import com.env.dcwater.util.SystemMethod;

/**
 * @author sk
 *
 */
public class DeviceSelectActivity extends NfcActivity implements IXListViewListener,OnItemSelectedListener,OnItemClickListener{
	
	public static final String GET_DEVICE_STRING = "getDevice";
	public static final String GET_CONS_STRING = "getCons";
	private ActionBar mActionBar;
	private ArrayList<HashMap<String, String>> deviceDataArrayList,consDataArrayList;
	private ProgressDialog mProgressDialog;
	private GetServerDeviceData getServerDeviceData;
	private ThreadPool.GetServerConsData getServerConsData;
	private PullToRefreshView mListView;
	private Spinner spConstruction;
	private ConstructionAdapter constructionAdapter;
	private DeviceAdapter deviceAdapter;
	private String selectCons;
	private boolean spinnerIni =false;
	private int dpi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deviceselect);
		initialData();
		initialActionbar();
		initialView();
	}
	
	/**
	 * 
	 */
	private void initialActionbar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		mActionBar.setTitle("设备列表");
	};
	
	/**
	 * 
	 */
	private void initialData(){
		dpi = SystemMethod.getDpi(getWindowManager());
		deviceDataArrayList = new ArrayList<HashMap<String,String>>();
		consDataArrayList = new ArrayList<HashMap<String,String>>();
		constructionAdapter = new ConstructionAdapter();
		deviceAdapter = new DeviceAdapter();
		selectCons = "";
		
		if(SystemParams.getInstance().getConstructionList()==null){
			getServerConsList();
		}else {
			consDataArrayList = SystemParams.getInstance().getConstructionList();
			constructionAdapter.notifyDataSetChanged();
		}
		
		if(SystemParams.getInstance().getMachineList()==null){
			getServerDeviceList("",selectCons);
		}else {
			deviceDataArrayList = SystemParams.getInstance().getMachineList();
			deviceAdapter.notifyDataSetChanged();
		}
		
		
	}
	
	/**
	 * 
	 */
	private void initialView(){
		mListView = (PullToRefreshView)findViewById(R.id.activity_deviceselect_list);
		spConstruction = (Spinner)findViewById(R.id.activity_deviceselect_construction);
		
		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(deviceAdapter);
		
		spConstruction.setAdapter(constructionAdapter);
		spConstruction.setOnItemSelectedListener(this);
		
	}
	
	/**
	 * 获取设备列表
	 */
	private void getServerDeviceList(String deviceName,String consName){
		getServerDeviceData = new GetServerDeviceData();
		getServerDeviceData.execute(deviceName,consName);
	}
	/**
	 * 获取构筑物列表
	 */
	private void getServerConsList(){
		getServerConsData = new ThreadPool.GetServerConsData (){
			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				if(result!=null){
					consDataArrayList = result;
					constructionAdapter.notifyDataSetChanged();
				}
			}
			@Override
			protected void onPreExecute() {
				
			}
		};
		getServerConsData.execute("");
	}
	
	/**
	 * 提交数据时，弹出进度对话框
	 */
	private void showProgressDialog(boolean cancel){
		if(mProgressDialog==null){
			mProgressDialog = new ProgressDialog(DeviceSelectActivity.this);
			mProgressDialog.setTitle("获取数据中");
			mProgressDialog.setMessage("正在努力加载数据，请稍后");
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setCancelable(cancel);
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
	public void onBackPressed() {
		super.onBackPressed();
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
	public void onRefresh() {
		getServerDeviceList("",consDataArrayList.get(spConstruction.getSelectedItemPosition()).get("StructureName"));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Intent data = new Intent();
		data.putExtra("data", deviceDataArrayList.get(position-1));
		setResult(RESULT_OK, data);
		finish();
	}

	
	/**
	 * @author sk
	 *
	 */
	private class ConstructionAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return consDataArrayList.size();
		}

		@Override
		public Object getItem(int position) {
			return consDataArrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView = LayoutInflater.from(DeviceSelectActivity.this).inflate(R.layout.item_consselect, null);
			}
			TextView name = (TextView)convertView.findViewById(R.id.item_consselect_consname);
			name.setText(consDataArrayList.get(position).get("StructureName"));
			return convertView;
		}
		
	}
	
	
	private class DeviceViewHolder{
		TextView name = null;
		TextView cons = null;
		ImageView pic = null;
	}
	
	/**
	 * @author sk
	 *
	 */
	private class DeviceAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			return deviceDataArrayList.size();
		}
		
		@Override
		public HashMap<String, String> getItem(int position) {
			return deviceDataArrayList.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			DeviceViewHolder deviceViewHolder ;
			final HashMap<String, String> map = getItem(position);
			if(convertView ==null){
				deviceViewHolder = new DeviceViewHolder();
				convertView = LayoutInflater.from(DeviceSelectActivity.this).inflate(R.layout.item_devicelist, null);
				deviceViewHolder.name =(TextView)convertView.findViewById(R.id.item_devicelist_name);
				deviceViewHolder.cons =(TextView)convertView.findViewById(R.id.item_devicelist_cons);
				deviceViewHolder.pic = (ImageView)convertView.findViewById(R.id.item_devicelist_pic);
				convertView.setTag(deviceViewHolder);
			}else {
				deviceViewHolder = (DeviceViewHolder)convertView.getTag();
			}
			deviceViewHolder.name.setText(map.get("DeviceName").toString());
			deviceViewHolder.cons.setText(map.get("InstallPosition").toString());
			if(map.get("PicURL").equals("")){
				deviceViewHolder.pic.setImageResource(R.drawable.ic_pic_default);
			}else {
				GetDevicePic getDevicePic = new GetDevicePic(deviceViewHolder.pic,dpi,DeviceSelectActivity.this);
				getDevicePic.execute(SystemMethod.getLocalTempPath(),map.get("PicURL").toString(),DataCenterHelper.PIC_URL_STRING+"/"+map.get("PicURL").toString());
			}
			deviceViewHolder.pic.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SystemMethod.startBigImageActivity(DeviceSelectActivity.this, map.get("PicURL"));
				}
			});
			return convertView;
		}
	}
	
	/**
	 * 获取远端数据的异步方法
	 * @author sk
	 */
	private class GetServerDeviceData extends AsyncTask<String, String, ArrayList<HashMap<String, String>>>{
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
			JSONObject object = new JSONObject();
			ArrayList<HashMap<String, String>> data = null;
			try {
				object.put("PlantID",SystemParams.PLANTID_INT);
				String result = DataCenterHelper.HttpPostData("GetDeviceInfoList", object);
				if(!result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					JSONObject jsonObject = new JSONObject(result);
					data = OperationMethod.parseDeviceDataToList(jsonObject,params[0],params[1]);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				data = null;
			} catch (IOException e) {
				e.printStackTrace();
				data = null;
			} catch (JSONException e) {
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
			if(mProgressDialog!=null&&mProgressDialog.isShowing()){
				if(result!=null){
					deviceDataArrayList = result;
					deviceAdapter.notifyDataSetChanged();
					if(selectCons.equals(consDataArrayList.get(0).get("StructureName")));{
						SystemParams.getInstance().setMachineList(result);
					}
				}
				hideProgressDialog();
				mListView.stopRefresh();
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if(spinnerIni){
			selectCons = consDataArrayList.get(position).get("StructureName");
			getServerDeviceList("",selectCons);
		}
		spinnerIni = true;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
}
