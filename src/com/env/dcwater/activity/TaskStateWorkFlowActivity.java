package com.env.dcwater.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.ThreadPool.GetRepairTaskWorkFlow;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;
import com.env.dcwater.javabean.ClassRTWorkFlow;
import com.env.dcwater.util.OperationMethod;
import com.env.dcwater.util.SystemMethod;

/**
 * 整个工单的维修流程
 * @author Administrator
 *
 */
public class TaskStateWorkFlowActivity extends NfcActivity implements IXListViewListener{
	
	public static final String ACTION_STRING = "com.env.dcwater.activity.TaskStateWorkFlowActivity";
	private HashMap<String, String> receivedData;
	private Intent receivedIntent;
	private String repairID;
	private ActionBar mActionBar;
	private ProgressDialog mProgressDialog;
	private ArrayList<ClassRTWorkFlow> workFlows;
	private GetRepairTaskWorkFlow getRepairTaskWorkFlow;
	private PullToRefreshView listView;
	private WorkFlowAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taskstateworkflow);
		iniData();
		iniActionBar();
		iniView();
	}
	
	
	@SuppressWarnings("unchecked")
	private void iniData(){
		receivedIntent = getIntent();
		receivedData = (HashMap<String, String>)receivedIntent.getSerializableExtra("data");
		repairID = receivedData.get("RepairTaskID");
		workFlows = new ArrayList<ClassRTWorkFlow>();
		adapter = new WorkFlowAdapter();
		getServerData();
	}
	
	private void iniActionBar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		mActionBar.setTitle(receivedData.get("FaultReportSN")+"操作历史");
	}
	
	private void iniView(){
		listView = (PullToRefreshView)findViewById(R.id.activity_taskstateworkflow_list);
		listView.setAdapter(adapter);
		listView.setXListViewListener(this);
		
	}
	
	
	/**
	 * 获取数据时，弹出进度对话框
	 * 
	 * @param cancelable
	 *            是否能被取消的操作
	 */
	private void showProgressDialog(boolean cancelable) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(TaskStateWorkFlowActivity.this);
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
	private void hideProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.cancel();
		}
	}
	
	private void getServerData(){
		getRepairTaskWorkFlow = new GetRepairTaskWorkFlow() {
			
			@Override
			public void onPreExecute() {
				showProgressDialog(true);
			}
			@Override
			public void onPostExecute(ArrayList<ClassRTWorkFlow> result) {
				if(mProgressDialog!=null&&mProgressDialog.isShowing()){
					if(result!=null){
						workFlows = result;
						adapter.notifyDataSetChanged();
					}else {
						Toast.makeText(getApplicationContext(), "刷新失败", Toast.LENGTH_SHORT).show();
					}
					listView.stopRefresh();
				}
				hideProgressDialog();
			}
		};
		getRepairTaskWorkFlow.execute(repairID);
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	
	private class ViewHolder{
		public TextView left;
		public TextView right;
	}
	
	private class WorkFlowAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return workFlows.size();
		}

		@Override
		public ClassRTWorkFlow getItem(int position) {
			return workFlows.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder ;
			ClassRTWorkFlow data = workFlows.get(position);
			if(convertView == null){
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(TaskStateWorkFlowActivity.this).inflate(R.layout.item_taskstateworkflow, null);
				viewHolder.left = (TextView)convertView.findViewById(R.id.item_taskstateworkflow_left);
				viewHolder.right = (TextView)convertView.findViewById(R.id.item_taskstateworkflow_right);
				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			if(position%2==0){
				viewHolder.left.setVisibility(View.VISIBLE);
				viewHolder.right.setVisibility(View.GONE);
				viewHolder.left.setText(OperationMethod.getWorkFlowInfor(data));
			}else {
				viewHolder.right.setVisibility(View.VISIBLE);
				viewHolder.left.setVisibility(View.GONE);
				viewHolder.right.setText(OperationMethod.getWorkFlowInfor(data));
			}
			return convertView;
		}
		
	}

	@Override
	public void onRefresh() {
		getServerData();
	}
}
