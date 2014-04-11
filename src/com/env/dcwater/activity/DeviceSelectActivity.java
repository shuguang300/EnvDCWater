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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;
import com.env.dcwater.util.DataCenterHelper;
import com.env.dcwater.util.OperationMethod;

/**
 * @author sk
 *
 */
public class DeviceSelectActivity extends NfcActivity implements IXListViewListener,OnItemClickListener{
	
	private ActionBar mActionBar;
	private ArrayList<HashMap<String, String>> deviceDataArrayList;
	private ProgressDialog mProgressDialog;
	private GetServerDeviceData getServerDeviceData;
	private PullToRefreshView mListView;
	private EditText etDeviceName;
	private Spinner spConstruction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deviceselect);
		initialData();
		initialActionbar();
		initialView();
	}
	
	private void initialActionbar(){
		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		mActionBar.setTitle("设备列表");
	};
	
	private void initialData(){
		
	}
	
	private void initialView(){
		mListView = (PullToRefreshView)findViewById(R.id.activity_deviceselect_list);
		etDeviceName = (EditText)findViewById(R.id.activity_deviceselect_devicename);
		spConstruction = (Spinner)findViewById(R.id.activity_deviceselect_construction);
		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.view_datafilter_poslist));
		mListView.setAdapter(adapter);
	}
	
	private void getServerDeviceList(){
		getServerDeviceData = new GetServerDeviceData();
		getServerDeviceData.execute("");
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
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 获取远端数据的异步方法
	 * @author sk
	 */
	class GetServerDeviceData extends AsyncTask<String, String, ArrayList<HashMap<String, String>>>{
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
			JSONObject object = new JSONObject();
			ArrayList<HashMap<String, String>> data = null;
			try {
				object.put("PlantID", 1);
				String result = DataCenterHelper.HttpPostData("GetDeviceInfoList", object);
				if(!result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					JSONObject jsonObject = new JSONObject(result);
					data = OperationMethod.parseDeviceListToArray(jsonObject);
					SystemParams.getInstance().setMachineList(data);
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
			}
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void onRefresh() {
		getServerDeviceList();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Intent data = new Intent();
		data.putExtra("data", deviceDataArrayList.get(position));
		setResult(RESULT_OK, data);
		finish();
	}
}
