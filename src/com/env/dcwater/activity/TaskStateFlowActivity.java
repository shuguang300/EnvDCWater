package com.env.dcwater.activity;

import java.util.HashMap;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.util.SystemMethod;

/**
 * 固定的维修进度图
 * @author Administrator
 *
 */
public class TaskStateFlowActivity extends NfcActivity{
	
	public static final String ACTION_STRING = "com.env.dcwater.activity.TaskStateFlowActivity";
	
	private HashMap<String, String> receivedData;
	private Intent receivedIntent;
	private ActionBar mActionBar;
	private int mState,mTaskType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iniData();
		iniActionBar();
		iniView();
	}
	
	
	@SuppressWarnings("unchecked")
	private void iniData(){
		receivedIntent = getIntent();
		receivedData = (HashMap<String, String>) receivedIntent.getExtras().getSerializable("data");
		mState = Integer.valueOf(receivedData.get("State"));
		mTaskType = Integer.valueOf(receivedData.get("RepairTaskType"));
	}
	
	private void iniActionBar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		mActionBar.setTitle(receivedData.get("FaultReportSN")+"进度");
	}
	
	private void iniView(){
		
		
		
		setViewState();
		setViewData();
	}
	
	private void setViewState(){
		
	}
	
	private void setViewData(){
		
	}
	
}
