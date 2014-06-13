package com.env.dcwater.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.javabean.EnumList;
import com.env.dcwater.util.SystemMethod;

public class MaintainTaskStateFlowAcivity extends NfcActivity{
	
	public static final String ACTION_STRING = "com.env.dcwater.activity.MaintainTaskStateFlowAcivity";
	private ActionBar mActionBar;
	private HashMap<String, String> receivedData;
	private Intent receivedIntent;
	private int mState;
	private ArrayList<TextView> states;
	private TextView state1,state2,state3,state5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintaintaskstateflow);
		iniData();
		iniActionBar();
		iniView();
	}
	
	@SuppressWarnings("unchecked")
	private void iniData(){
		receivedIntent = getIntent();
		receivedData = (HashMap<String, String>) receivedIntent.getExtras().getSerializable("data");
		mState = Integer.valueOf(receivedData.get("State"));
		states = new ArrayList<TextView>();
	}
	
	private void iniActionBar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		mActionBar.setTitle(receivedData.get("MaintainTaskSN")+"进度");
	}
	
	private void iniView(){
		state1 = (TextView)findViewById(R.id.activity_maintaintaskstateflow_state1);
		states.add(state1);
		state5 = (TextView)findViewById(R.id.activity_maintaintaskstateflow_state5);
		states.add(state5);
		state2 = (TextView)findViewById(R.id.activity_maintaintaskstateflow_state2);
		states.add(state2);
		state3 = (TextView)findViewById(R.id.activity_maintaintaskstateflow_state3);
		states.add(state3);
		setViewState();
	}
	
	private void setViewState(){
		int start = 0;
		switch (mState) {
		case EnumList.UpkeepHistoryState.STATE_HASBEENSEND_INT:
			start = 0;
			break;
		case EnumList.UpkeepHistoryState.STATE_HASBEENAPPROVE_INT:
			start = 3;
			break;
		case EnumList.UpkeepHistoryState.STATE_HASBEENBACK_INT:
			start = 2;
			break;
		case EnumList.UpkeepHistoryState.STATE_WAITFORSUBMIT_INT:
			start = 1;
			break;
		case EnumList.UpkeepHistoryState.STATE_NOTAPPROVE_INT:
			start = 0;
			break;
		}
		for(int i =0;i<=start;i++){
			TextView tv = states.get(i);
			tv.setTextAppearance(MaintainTaskStateFlowAcivity.this, R.style.StateFlowDone);
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
