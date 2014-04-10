package com.env.dcwater.activity;
import java.util.HashMap;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.util.SystemMethod;

/**
 * 填报数据的界面
 * @author sk
 */
public class DataInputActivity extends NfcActivity implements OnClickListener {
	private Intent getedIntend;
	private HashMap<String, String> data;
	private ActionBar mActionBar;
	private EditText dataInputer;
	private Button buttonAddMedia;

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
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		mActionBar.setTitle(data.get("Name"));
	}

	/**
	 * 
	 */
	private void iniView(){
		dataInputer = (EditText)findViewById(R.id.activity_datainput_data);
		dataInputer.setText(data.get("Value"));
		dataInputer.selectAll();
		buttonAddMedia = (Button)findViewById(R.id.activity_datainput_add);
		buttonAddMedia.setOnClickListener(this);
		if(data.get("Key").equals("RequiredManHours")||data.get("Key").equals("RepairCost")){
			buttonAddMedia.setVisibility(View.GONE);
		}
	}

	/**
	 * @return
	 */
	private Intent sendDataToRepairItem(){
		Intent intent = new Intent(this,RepairManageItemActivity.class);
		data.put("Value", dataInputer.getText().toString());
		intent.putExtra("data", data);
		return intent;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_datainput, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_CANCELED);
			break;
		case R.id.menu_datainput_save:
			setResult(RESULT_OK, sendDataToRepairItem());
			break;
		}
		finish();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		setResult(RESULT_CANCELED);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_datainput_add:
			SystemMethod.hideSoftInput(DataInputActivity.this);
			break;
		}
	}

}