package com.env.dcwater.activity;
import java.util.HashMap;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.fragment.AddMediaFileView;
import com.env.dcwater.util.SystemMethod;

/**
 * 填报数据的界面
 * @author sk
 */
public class DataInputActivity extends NfcActivity {
	private Intent getedIntend;
	private HashMap<String, String> data;
	private ActionBar mActionBar;
	private EditText dataInputer;
	private RelativeLayout mainLayout;
	private AddMediaFileView addMediaFileView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_datainput);
		iniData();
		iniActionBar();
		iniView();
	}

	/**
	 * 初始化数据
	 */
	@SuppressWarnings("unchecked")
	private void iniData(){
		getedIntend = getIntent();
		data = (HashMap<String, String>)getedIntend.getSerializableExtra("data");
	}

	/**
	 * 
	 */
	private void iniActionBar(){
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
		mActionBar.setTitle(data.get("Name"));
	}

	/**
	 * 
	 */
	private void iniView(){
		mainLayout = (RelativeLayout)findViewById(R.id.activity_datainput_main);
		
		addMediaFileView = new AddMediaFileView(DataInputActivity.this);
		
		dataInputer = (EditText)findViewById(R.id.activity_datainput_data);
		dataInputer.setText(data.get("Value"));
		dataInputer.selectAll();
		
		
		
		
	}

	/**
	 * @return
	 */
	private Intent sendDataBack(){
		Intent intent = new Intent();
		data.put("Value", dataInputer.getText().toString());
		intent.putExtra("data", data);
		return intent;
	}
	
	/**
	 * 
	 */
	private void showMediaFilePanel(){
		if(!addMediaFileView.isShowing()){
			addMediaFileView.showAtLocation(mainLayout, Gravity.BOTTOM, 0, 0);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_datainput, menu);
		if(data.get("Key").equals("RequiredManHours")||data.get("Key").equals("RepairCost")){
			menu.findItem(R.id.menu_datainput_add).setVisible(false);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.menu_datainput_save:
			addMediaFileView.dismiss();
			setResult(RESULT_OK, sendDataBack());
			finish();
			break;
		case R.id.menu_datainput_add:
			SystemMethod.hideSoftInput(DataInputActivity.this);
			showMediaFilePanel();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if(addMediaFileView.isShowing()){
			addMediaFileView.dismiss();
		}else {
			setResult(RESULT_CANCELED);
			finish();
		}
		
	}

}