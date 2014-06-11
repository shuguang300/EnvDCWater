package com.env.dcwater.activity;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.javabean.EnumList;
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
	private LinearLayout state1Group,state7Group;
	private TextView state0,state1,state2,state3,state4,state6,state7,state8;
	private ArrayList<TextView> states;
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
		receivedData = (HashMap<String, String>) receivedIntent.getExtras().getSerializable("data");
		mState = Integer.valueOf(receivedData.get("State"));
		mTaskType = Integer.valueOf(receivedData.get("RepairTaskType"));
		states = new ArrayList<TextView>();
	}
	
	private void iniActionBar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		mActionBar.setTitle(receivedData.get("FaultReportSN")+"进度");
	}
	
	private void iniView(){
		
		state0 = (TextView)findViewById(R.id.activity_taskstateflow_state0);
		states.add(state0);
		state1 = (TextView)findViewById(R.id.activity_taskstateflow_state1);
		states.add(state1);
		state2 = (TextView)findViewById(R.id.activity_taskstateflow_state2);
		states.add(state2);
		state3 = (TextView)findViewById(R.id.activity_taskstateflow_state3);
		states.add(state3);
		state4 = (TextView)findViewById(R.id.activity_taskstateflow_state4);
		states.add(state4);
		state6 = (TextView)findViewById(R.id.activity_taskstateflow_state6);
		states.add(state6);
		state7 = (TextView)findViewById(R.id.activity_taskstateflow_state7);
		states.add(state7);
		state8 = (TextView)findViewById(R.id.activity_taskstateflow_state8);
		states.add(state8);
		state1Group = (LinearLayout)findViewById(R.id.activity_taskstateflow_state1group);
		state7Group = (LinearLayout)findViewById(R.id.activity_taskstateflow_state7group);
		
		setViewState();
		
	}
	
	private void setViewState(){
		switch (mTaskType) {
		case EnumList.RepairTaskType.TASKTYPE_EQUIPMENT:
			state1Group.setVisibility(View.GONE);
			state7Group.setVisibility(View.GONE);
			break;
		}
		int start = 0;
		switch (mState) {
		case 0:
			start = 0;
			break;
		case 1:
			start = 1;
			break;
		case 2:
			start = 2;
			break;
		case 3:
			start = 3;
			break;
		case 4:
			start = 4;
			break;
		case 5:
			start = 3;
			break;
		case 6:
			start = 5;
			break;
		case 7:
			start = 6;
			break;
		case 8:
			start = 7;
			break;
		}
		for(int i =0;i<=start;i++){
			TextView tv = states.get(i);
			tv.setTextAppearance(TaskStateFlowActivity.this, R.style.StateFlowDone);
			tv.setBackgroundResource(R.drawable.stateflow_tv_bg_done);
		}
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
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
}
