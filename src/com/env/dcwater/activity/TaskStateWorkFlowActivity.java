package com.env.dcwater.activity;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;

import com.env.dcwater.component.NfcActivity;

/**
 * 整个工单的维修流程
 * @author Administrator
 *
 */
public class TaskStateWorkFlowActivity extends NfcActivity{
	
	private HashMap<String, String> receivedData;
	private Intent receivedIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iniData();
		iniActionBar();
		iniView();
	}
	
	
	private void iniData(){
		receivedIntent = getIntent();
	}
	
	private void iniActionBar(){
		
	}
	
	private void iniView(){
		setViewData();
	}
	
	private void setViewData(){
		
	}
}
