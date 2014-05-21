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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
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

public class DeviceInfoListActivity extends NfcActivity implements OnQueryTextListener,OnItemClickListener,OnItemSelectedListener, IXListViewListener{
	
	public static final String ACTION_STRING = "DeviceInfoListActivity";
	private ProgressDialog mProgressDialog;
	private ActionBar actionBar;
	private Spinner consList;
	private ArrayList<HashMap<String, String>> deviceArrayList,consArrayList;
	private GetServerDeviceData getServerDeviceData;
	private PullToRefreshView deviceListView;
	private ConstructionAdapter constructionAdapter;
	private DeviceListAdapter deviceListAdapter;
	private ThreadPool.GetServerConsData getServerConsData;
	private SearchView mSearchView;
	private ImageView mSearchHintIcon;
	private String selectCons = "";
	private boolean spinnerIni = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deviceinfolist);
		iniData();
		iniActionbar();
		iniView();
		getServerData();
	}
	
	/**
	 * 
	 */
	private void iniData(){
		deviceArrayList = new ArrayList<HashMap<String,String>>();
		consArrayList = new ArrayList<HashMap<String,String>>();
		constructionAdapter = new ConstructionAdapter();
		deviceListAdapter = new DeviceListAdapter(deviceArrayList);
	}
	
	/**
	 * 
	 */
	private void iniActionbar(){
		actionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, actionBar);
		actionBar.setTitle("设备列表");
	}
	
	/**
	 * 
	 */
	private void iniView(){
		consList = (Spinner)findViewById(R.id.activity_deviceinfolist_conslist);
		
		deviceListView = (PullToRefreshView)findViewById(R.id.activity_deviceinfolist_devicelist);
		
		deviceListView.setXListViewListener(this);
		
		deviceListView.setOnItemClickListener(this);
		
		deviceListView.setTextFilterEnabled(false);
		
		consList.setOnItemSelectedListener(this);
		
		deviceListView.setAdapter(deviceListAdapter);
		consList.setAdapter(constructionAdapter);
	}
	
	private void iniSearchView(){
		mSearchView.setQueryHint("请输入设备关键词查找");
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode==KeyEvent.KEYCODE_BACK){
					mSearchView.setIconified(true);
				}
				return false;
			}
		});
		final EditText editText = (EditText)mSearchView.findViewById(getResources().getIdentifier("android:id/search_src_text", null, null));
//		editText.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.small));
		editText.setTextColor(getResources().getColor(R.color.white));
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ( actionId == EditorInfo.IME_ACTION_SEARCH) {
					SystemMethod.hideSoftInput(DeviceInfoListActivity.this);
					mSearchView.setIconified(true);
				}
				return false;
			}
		});
		mSearchHintIcon = (ImageView)mSearchView.findViewById(getResources().getIdentifier("android:id/search_button",null,null));
		mSearchHintIcon.setImageResource(R.drawable.ic_menu_search);
	}
	
	/**
	 * 
	 */
	private void getServerData(){
		getConsList();
		getDeviceList();
		
	}
	
	/**
	 * 
	 */
	private void getConsList(){
		if(SystemParams.getInstance().getConstructionList()==null){
			getServerConsList();
		}else {
			consArrayList = SystemParams.getInstance().getConstructionList();
			constructionAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * 
	 */
	private void getServerConsList(){
		getServerConsData = new ThreadPool.GetServerConsData() {
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				if(result!=null){
					consArrayList = result;
					constructionAdapter.notifyDataSetChanged();
				}
			}

			@Override
			protected void onPreExecute() {
				
			}
		};
		getServerConsData.execute("");
	}
	
	
	private void getDeviceList(){
		if(SystemParams.getInstance().getMachineList()==null){
			getServerDeviceList("");
		}else {
			deviceArrayList = SystemParams.getInstance().getMachineList();
			deviceListAdapter = new DeviceListAdapter(deviceArrayList);
			deviceListView.setAdapter(deviceListAdapter);
		}
	}
	
	/**
	 * 
	 */
	private void getServerDeviceList(String consName){
		getServerDeviceData = new GetServerDeviceData();
		getServerDeviceData.execute(consName);
	}
	
	
	/**
	 * 获取数据时，弹出进度对话框
	 * @param cancelable 是否能被取消的操作
	 */
	private void showProgressDialog(boolean cancelable){
		if(mProgressDialog==null){
			mProgressDialog = new ProgressDialog(DeviceInfoListActivity.this);
			mProgressDialog.setTitle("提交中");
			mProgressDialog.setMessage("正在向服务器提交，请稍后");
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
	
	/**
	 * 跳转到维修历史
	 */
	private void startMaintainHistoryActivity(){
		Intent intent = new Intent(this, MaintainHistoryActivity.class);
		intent.putExtra("action", ACTION_STRING);
		startActivity(intent);
	}

	/**
	 * 跳转到保养历史
	 */
	private void startUpkeepHistoryActivity(){
		Intent intent = new Intent(this, UpkeepHistoryActivity.class);
		intent.putExtra("action", ACTION_STRING);
		startActivity(intent);
	}
	
	/**
	 * 搜索与输入内容有关的数据
	 * @param searchContent
	 * @return
	 */
	private ArrayList<HashMap<String, String>> getSearchData(String searchContent){
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		for(int i =0;i<deviceArrayList.size();i++){
			int index = deviceArrayList.get(i).get("DeviceName").indexOf(searchContent);
			if(index!=-1){
				data.add(deviceArrayList.get(i));
			}
		}
		return data;
	}
	
	
	/**
	 * 更新listview的显示
	 * @param data
	 */
	private void updateSearchResult(ArrayList<HashMap<String, String>> data){
		deviceListAdapter = new DeviceListAdapter(data);
		deviceListView.setAdapter(deviceListAdapter);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_deviceinfolist, menu);
		mSearchView = (SearchView)menu.getItem(0).getActionView();
		iniSearchView();
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.menu_deviceinfolist_maintainhistory:
			startMaintainHistoryActivity();
			break;
		case R.id.menu_deviceinfolist_upkeephistory:
			startUpkeepHistoryActivity();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	/**
	 * @author sk
	 *
	 */
	private class ConstructionAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return consArrayList.size();
		}

		@Override
		public Object getItem(int position) {
			return consArrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView = LayoutInflater.from(DeviceInfoListActivity.this).inflate(R.layout.item_consselect, null);
			}
			TextView name = (TextView)convertView.findViewById(R.id.item_consselect_consname);
			name.setText(consArrayList.get(position).get("StructureName"));
			return convertView;
		}
		
	}
	
	private class DeviceViewHolder{
		TextView name = null;
		TextView cons = null;
		ImageView pic = null;
	}
	
	/**
	 * 自定义设备列表的adapter
	 * @author sk
	 */
	private class DeviceListAdapter extends BaseAdapter {
		
		private ArrayList<HashMap<String, String>> mData;
		
		public DeviceListAdapter (ArrayList<HashMap<String, String>> data){
			mData = data;
		}
		
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
			DeviceViewHolder deviceViewHolder ;
			HashMap<String, String> map = mData.get(position);
			if(convertView ==null){
				deviceViewHolder = new DeviceViewHolder();
				convertView = LayoutInflater.from(DeviceInfoListActivity.this).inflate(R.layout.item_devicelist, null);
				deviceViewHolder.name =(TextView)convertView.findViewById(R.id.item_devicelist_name);
				deviceViewHolder.cons =(TextView)convertView.findViewById(R.id.item_devicelist_cons);
				deviceViewHolder.pic = (ImageView)convertView.findViewById(R.id.item_devicelist_pic);
				convertView.setTag(deviceViewHolder);
			}else {
				deviceViewHolder = (DeviceViewHolder)convertView.getTag();
			}
			deviceViewHolder.name.setText(map.get("DeviceName").toString());
			deviceViewHolder.cons.setText(map.get("InstallPosition").toString());
			if(!map.get("PicURL").equals("")){
				GetDevicePic getDevicePic = new GetDevicePic(deviceViewHolder.pic);
				getDevicePic.execute(SystemMethod.getLocalTempPath(),map.get("PicURL").toString(),DataCenterHelper.PIC_URL_STRING+"/"+map.get("PicURL").toString());
			}
			return convertView;
		}
	}
	
	/**
	 * 获取服务器段的设备列表
	 * @author sk
	 */
	private class GetServerDeviceData extends AsyncTask<String, String, ArrayList<HashMap<String, String>>>{
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
			JSONObject object = new JSONObject();
			ArrayList<HashMap<String, String>> data = null;
			try {
				object.put("PlantID", SystemParams.PLANTID_INT);
				String result = DataCenterHelper.HttpPostData("GetDeviceInfoList", object);
				if(!result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					JSONObject jsonObject = new JSONObject(result);
					data = OperationMethod.parseDeviceDataToList(jsonObject,"",params[0]);
					if(data!=null&&(params[0].equals("")||params[0].equals("全部"))){
						deviceArrayList = data;
						SystemParams.getInstance().setMachineList(data);
					}
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
					deviceArrayList = result;
					deviceListAdapter = new DeviceListAdapter(deviceArrayList);
					deviceListView.setAdapter(deviceListAdapter);
				}
				deviceListView.stopRefresh();
				hideProgressDialog();
			}
		}
	}
	
	
	@Override
	public boolean onQueryTextSubmit(String query) {
		
        return false;  
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		updateSearchResult(getSearchData(newText));
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(position!=0){
			Intent intent = new Intent(DeviceInfoListActivity.this,DeviceInfoItemActivity.class);
			intent.putExtra("data", deviceListAdapter.getItem(position-1));
			startActivity(intent);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if(spinnerIni){
			selectCons = consArrayList.get(position).get("StructureName");
			getServerDeviceList(selectCons);
		}
		spinnerIni = true;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}

	@Override
	public void onRefresh() {
		getServerDeviceList(selectCons);
	}
}
