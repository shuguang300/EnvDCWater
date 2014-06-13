package com.env.dcwater.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.ThreadPool.GetTaskWorkFlow;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;
import com.env.dcwater.fragment.WorkFlowAdapter;
import com.env.dcwater.javabean.ClassTaskWorkFlow;
import com.env.dcwater.javabean.EnumList;
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
	private String repairID,maintainID;
	private ActionBar mActionBar;
	private ProgressDialog mProgressDialog;
	private ArrayList<ClassTaskWorkFlow> workFlows;
	private GetTaskWorkFlow getTaskWorkFlow;
	private PullToRefreshView listView;
	private MyAdapter adapter;
	private int taskType;
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
		taskType = receivedIntent.getExtras().getInt("TaskType");
		if(taskType==EnumList.TaskType.TYPE_MAINTAIN_INT){
			maintainID = receivedData.get("MaintainTaskID");
		}else {
			repairID = receivedData.get("RepairTaskID");
		}
		workFlows = new ArrayList<ClassTaskWorkFlow>();
		adapter = new MyAdapter(TaskStateWorkFlowActivity.this,workFlows);
		getServerData();
	}
	
	private void iniActionBar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		if(taskType==EnumList.TaskType.TYPE_MAINTAIN_INT){
			mActionBar.setTitle(receivedData.get("MaintainTaskSN")+"操作历史");
		}else {
			mActionBar.setTitle(receivedData.get("FaultReportSN")+"操作历史");
		}
		
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
		getTaskWorkFlow = new GetTaskWorkFlow() {
			
			@Override
			public void onPreExecute() {
				showProgressDialog(true);
			}
			@Override
			public void onPostExecute(ArrayList<ClassTaskWorkFlow> result) {
				if(mProgressDialog!=null&&mProgressDialog.isShowing()){
					if(result!=null){
						workFlows = result;
						adapter.datasetNotification(workFlows);
					}else {
						Toast.makeText(getApplicationContext(), "刷新失败", Toast.LENGTH_SHORT).show();
					}
					listView.stopRefresh();
				}
				hideProgressDialog();
			}
		};
		if(taskType==EnumList.TaskType.TYPE_MAINTAIN_INT){
			getTaskWorkFlow.execute(maintainID,taskType+"");
		}else {
			getTaskWorkFlow.execute(repairID,taskType+"");
		}
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	@Override
	public void onRefresh() {
		getServerData();
	}
	
	private class MyAdapter extends WorkFlowAdapter{

		public MyAdapter(Context context, ArrayList<ClassTaskWorkFlow> data) {
			super(context, data);
		}
		
		@Override
		public void setData(ViewHolder viewHolder, ClassTaskWorkFlow data,int position) {
			if(position%2==0){
				viewHolder.left.setVisibility(View.VISIBLE);
				viewHolder.right.setVisibility(View.GONE);
				viewHolder.left.setText(OperationMethod.getWorkFlowInfor(data));
			}else {
				viewHolder.right.setVisibility(View.VISIBLE);
				viewHolder.left.setVisibility(View.GONE);
				viewHolder.right.setText(OperationMethod.getWorkFlowInfor(data));
			}
		}
		
	}
}
