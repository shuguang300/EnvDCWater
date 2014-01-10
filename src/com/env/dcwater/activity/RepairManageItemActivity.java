package com.env.dcwater.activity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;
import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;

/**
 * 单个报修工单界面，该界面可查看，编辑
 * @author sk
 *
 */
public class RepairManageItemActivity extends NfcActivity implements OnClickListener{
	private ActionBar mActionBar;
	private HashMap<String, String> receivedData;
	private Intent receivedIntent;
	private int mRequestCode;
	private TextView etName,etType,etSN,etPosition,etStartTime,etManufacture,etFaultTime,etHandleStep,etPeople,etFaultPhenomenon,etOtherStep;
	private TableRow trName,trFaultTime,trFaultPhenomenon,trHandleStep,trOtherStep,trPeople;
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repairmanageitem);
		iniData();
		iniActionbar();
		findAndIniView();
	}
	
	/**
	 * 
	 */
	private void iniActionbar(){
		mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		
		switch (mRequestCode) {
		case RepairManageActivity.REPAIRMANAGE_ADD_INTEGER:
			mActionBar.setTitle("上报故障");
			break;
		case RepairManageActivity.REPAIRMANAGE_DETAIL_INTEGER:
			mActionBar.setTitle(receivedData.get("FaultReportSN")+"详情");
			break;
		case RepairManageActivity.REPAIRMANAGE_UPDATE_INTEGER:
			mActionBar.setTitle(receivedData.get("FaultReportSN")+"修改");
			break;
		}
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void iniData(){
		receivedIntent = getIntent();
		mRequestCode = receivedIntent.getExtras().getInt("RequestCode");
		switch (mRequestCode) {
		case RepairManageActivity.REPAIRMANAGE_ADD_INTEGER:
			break;
		case RepairManageActivity.REPAIRMANAGE_UPDATE_INTEGER:
			receivedData = (HashMap<String, String>)receivedIntent.getSerializableExtra("Data");
			break;
		case RepairManageActivity.REPAIRMANAGE_DETAIL_INTEGER:
			receivedData = (HashMap<String, String>)receivedIntent.getSerializableExtra("Data");
			break;
		}
	}
	
	/**
	 * 
	 */
	private void findAndIniView(){
		
		etName = (TextView)findViewById(R.id.activity_repairmanageitem_name);
		trName = (TableRow)findViewById(R.id.activity_repairmanageitem_name_tr);
		
		etType = (TextView)findViewById(R.id.activity_repairmanageitem_type);
		
		etSN = (TextView)findViewById(R.id.activity_repairmanageitem_sn);
		
		etPosition = (TextView)findViewById(R.id.activity_repairmanageitem_position);
		
		etStartTime = (TextView)findViewById(R.id.activity_repairmanageitem_starttime);
		
		etManufacture = (TextView)findViewById(R.id.activity_repairmanageitem_manufacturer);
		
		etFaultTime = (TextView)findViewById(R.id.activity_repairmanageitem_faulttime);
		trFaultTime = (TableRow)findViewById(R.id.activity_repairmanageitem_faulttime_tr);
		
		etFaultPhenomenon = (TextView)findViewById(R.id.activity_repairmanageitem_faultphenomenon);
		trFaultPhenomenon = (TableRow)findViewById(R.id.activity_repairmanageitem_faultphenomenon_tr);
		
		etHandleStep = (TextView)findViewById(R.id.activity_repairmanageitem_handlestep);
		trHandleStep = (TableRow)findViewById(R.id.activity_repairmanageitem_handlestep_tr);
		
		etOtherStep = (TextView)findViewById(R.id.activity_repairmanageitem_otherstep);
		trOtherStep = (TableRow)findViewById(R.id.activity_repairmanageitem_otherstep_tr);
		
		etPeople = (TextView)findViewById(R.id.activity_repairmanageitem_taskpeople);
		trPeople = (TableRow)findViewById(R.id.activity_repairmanageitem_taskpeople_tr);
		
		setViewState(mRequestCode);
		fillViewData(mRequestCode);
	}
	
	/**
	 * 根据不同的状态设置各个空间的电机事件
	 * @param code
	 */
	private void setViewState( int code){
		switch (code) {
		case RepairManageActivity.REPAIRMANAGE_ADD_INTEGER:
			trName.setOnClickListener(this);
			trFaultTime.setOnClickListener(this);
			trFaultPhenomenon.setOnClickListener(this);
			trHandleStep.setOnClickListener(this);
			trOtherStep.setOnClickListener(this);
			trPeople.setOnClickListener(this);
			break;
		case RepairManageActivity.REPAIRMANAGE_UPDATE_INTEGER:
			trName.setOnClickListener(this);
			trFaultTime.setOnClickListener(this);
			trFaultPhenomenon.setOnClickListener(this);
			trHandleStep.setOnClickListener(this);
			trOtherStep.setOnClickListener(this);
			trPeople.setOnClickListener(this);
			break;
		case RepairManageActivity.REPAIRMANAGE_DETAIL_INTEGER:
			trName.setOnClickListener(null);
			trFaultTime.setOnClickListener(null);
			trFaultPhenomenon.setOnClickListener(null);
			trHandleStep.setOnClickListener(null);
			trOtherStep.setOnClickListener(null);
			trPeople.setOnClickListener(null);
			break;
		}
	}
	
	private void fillViewData(int code){
		switch (code) {
		case RepairManageActivity.REPAIRMANAGE_ADD_INTEGER:
			etName.setText("");
			etSN.setText("");
			etType.setText("");
			etPosition.setText("");
			etStartTime.setText("");
			etManufacture.setText("");
			etFaultTime.setText(new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).format(new Date()));
			etFaultPhenomenon.setText("");
			etHandleStep.setText("");
			etOtherStep.setText("");
			etPeople.setText("");
			break;
		case RepairManageActivity.REPAIRMANAGE_UPDATE_INTEGER:
			etName.setText(receivedData.get("DeviceName"));
			etFaultTime.setText(receivedData.get("AccidentOccurTime"));
			etFaultPhenomenon.setText(receivedData.get("Info"));
			break;
		case RepairManageActivity.REPAIRMANAGE_DETAIL_INTEGER:
			etName.setText(receivedData.get("DeviceName"));
			etFaultTime.setText(receivedData.get("AccidentOccurTime"));
			etFaultPhenomenon.setText(receivedData.get("Info"));
			break;
		}
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_CANCELED);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_repairmanageitem_name_tr:
			
			break;
		case R.id.activity_repairmanageitem_faulttime_tr:
			
			break;
		case R.id.activity_repairmanageitem_faultphenomenon_tr:
			
			break;
		case R.id.activity_repairmanageitem_handlestep_tr:
			
			break;
		case R.id.activity_repairmanageitem_otherstep_tr:
			
			break;
		case R.id.activity_repairmanageitem_taskpeople_tr:
			
			break;
		}
	}
}
