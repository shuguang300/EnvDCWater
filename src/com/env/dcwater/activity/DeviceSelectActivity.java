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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.component.ThreadPool;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;
import com.env.dcwater.util.DataCenterHelper;
import com.env.dcwater.util.OperationMethod;
import com.env.dcwater.util.SystemMethod;

/**
 * @author sk
 *
 */
public class DeviceSelectActivity extends NfcActivity implements IXListViewListener,OnItemClickListener,OnClickListener{
	
	public static final String GET_DEVICE_STRING = "getDevice";
	public static final String GET_CONS_STRING = "getCons";
	private ActionBar mActionBar;
	private ArrayList<HashMap<String, String>> deviceDataArrayList,consDataArrayList;
	private ProgressDialog mProgressDialog;
	private GetServerDeviceData getServerDeviceData;
	private ThreadPool.GetServerConsData getServerConsData;
	private PullToRefreshView mListView;
	private EditText etDeviceName;
	private Spinner spConstruction;
	private Button btSearch;
	private ImageView btClear;
	private ConstructionAdapter constructionAdapter;
	private DeviceAdapter deviceAdapter;
	
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
		deviceDataArrayList = new ArrayList<HashMap<String,String>>();
		consDataArrayList = new ArrayList<HashMap<String,String>>();
		constructionAdapter = new ConstructionAdapter();
		deviceAdapter = new DeviceAdapter();
		if(SystemParams.getInstance().getMachineList()==null){
			getServerDeviceList("","");
		}else {
			deviceDataArrayList = SystemParams.getInstance().getMachineList();
			
		}
		if(SystemParams.getInstance().getConstructionList()==null){
			getServerConsList();
		}else {
			consDataArrayList = SystemParams.getInstance().getConstructionList();
		}
		
	}
	
	/**
	 * 
	 */
	private void initialView(){
		mListView = (PullToRefreshView)findViewById(R.id.activity_deviceselect_list);
		etDeviceName = (EditText)findViewById(R.id.activity_deviceselect_devicename);
		spConstruction = (Spinner)findViewById(R.id.activity_deviceselect_construction);
		btSearch = (Button)findViewById(R.id.activity_deviceselect_submit);
		btClear = (ImageView)findViewById(R.id.activity_deviceselect_devicename_clear);
		
		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(this);
		
		btSearch.setOnClickListener(this);
		btClear.setOnClickListener(this);
		
		mListView.setAdapter(deviceAdapter);
		spConstruction.setAdapter(constructionAdapter);
		etDeviceName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				int count = etDeviceName.getText().toString().length();
				if(count==0){
					btClear.setVisibility(View.GONE);
				}else {
					btClear.setVisibility(View.VISIBLE);
				}
			}
		});
		
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
				// TODO Auto-generated method stub
				
			}
		};
		getServerConsData.execute("");
	}
	
	/**
	 * 提交数据时，弹出进度对话框
	 */
	private void showProgressDialog(){
		if(mProgressDialog==null){
			mProgressDialog = new ProgressDialog(DeviceSelectActivity.this);
			mProgressDialog.setTitle("获取数据中");
			mProgressDialog.setMessage("正在努力加载数据，请稍后");
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setCancelable(false);
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
		getServerDeviceList(etDeviceName.getText().toString(),consDataArrayList.get(spConstruction.getSelectedItemPosition()).get("StructureName"));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Intent data = new Intent();
		data.putExtra("data", deviceDataArrayList.get(position-1));
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_deviceselect_devicename_clear:
			etDeviceName.setText("");
			SystemMethod.hideSoftInput(DeviceSelectActivity.this);
			break;
		case R.id.activity_deviceselect_submit:
			onRefresh();
			break;
		}
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
		public Object getItem(int position) {
			return deviceDataArrayList.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView = LayoutInflater.from(DeviceSelectActivity.this).inflate(R.layout.item_devicelist, null);
			}
			TextView nameTextView = (TextView)convertView.findViewById(R.id.item_devicelist_name);
			nameTextView.setText(deviceDataArrayList.get(position).get("DeviceName").toString());
			TextView consTextView = (TextView)convertView.findViewById(R.id.item_devicelist_cons);
			consTextView.setText(deviceDataArrayList.get(position).get("InstallPosition").toString());
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
				object.put("PlantID", 1);
				String result = DataCenterHelper.HttpPostData("GetDeviceInfoList", object);
				if(!result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					JSONObject jsonObject = new JSONObject(result);
					data = OperationMethod.parseDeviceDataToList(jsonObject,params[0],params[1]);
					deviceDataArrayList = data;
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
			showProgressDialog();
		}
		
		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			super.onPostExecute(result);
			if(result!=null){
				deviceAdapter.notifyDataSetChanged();
			}
			hideProgressDialog();
			mListView.stopRefresh();
		}
	}
}
