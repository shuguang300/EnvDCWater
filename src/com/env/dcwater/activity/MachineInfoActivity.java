package com.env.dcwater.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.fragment.PullToRefreshView;

public class MachineInfoActivity extends NfcActivity implements OnItemClickListener{
	private DrawerLayout mDrawerLayout;
	private ListView mListView;
	private PullToRefreshView infoListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_machineinfo);
		ini();
	}
	private void ini(){
		infoListView = (PullToRefreshView)findViewById(R.id.activity_machineinfo_info);
		mDrawerLayout = (DrawerLayout)findViewById(R.id.activity_machineinfo_drawlayout);
		mListView = (ListView)findViewById(R.id.activity_machineinfo_listview);
		iniListView();
	}
	
	/**
	 * 测试用的假数据
	 */
	private void iniListView(){
		String [] mData = getResources().getStringArray(R.array.machineinfo);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(MachineInfoActivity.this, android.R.layout.simple_list_item_1,mData);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
		infoListView.setAdapter(adapter);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		mDrawerLayout.closeDrawer(Gravity.LEFT);
	}
}
