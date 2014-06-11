package com.env.dcwater.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.ThreadPool;
import com.env.dcwater.component.ThreadPool.GetRepairTaskWorkFlow;
import com.env.dcwater.javabean.ClassRTWorkFlow;
import com.env.dcwater.util.SystemMethod;

/**
 * 整个工单的维修流程
 * @author Administrator
 *
 */
public class TaskStateWorkFlowActivity extends NfcActivity{
	
	public static final String ACTION_STRING = "com.env.dcwater.activity.TaskStateWorkFlowActivity";
	private HashMap<String, String> receivedData;
	private Intent receivedIntent;
	private String repairID;
	private ActionBar mActionBar;
	private ProgressDialog mProgressDialog;
	private ArrayList<ClassRTWorkFlow> workFlows;
	private GetRepairTaskWorkFlow getRepairTaskWorkFlow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taskstateflow);
		iniData();
		iniActionBar();
		iniView();
	}
	
	
	@SuppressWarnings("unchecked")
	private void iniData(){
		receivedIntent = getIntent();
		receivedData = (HashMap<String, String>)receivedIntent.getSerializableExtra("data");
		repairID = receivedData.get("RepairTaskID");
		getServerData();
	}
	
	private void iniActionBar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		mActionBar.setTitle(receivedData.get("FaultReportSN")+"操作历史");
	}
	
	private void iniView(){
		
		
		
		setViewData();
	}
	
	private void setViewData(){
		
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
					}
				}
				hideProgressDialog();
			}
		};
		getRepairTaskWorkFlow.execute(repairID);
	}
	
}
