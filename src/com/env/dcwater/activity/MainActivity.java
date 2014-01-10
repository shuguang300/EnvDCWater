package com.env.dcwater.activity;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.fragment.UserRigthGroupView;
import com.env.dcwater.javabean.EnumList;

/**
 * 登录后的主界面，主要包含2大区域，左边的是功能模块区
 * 右边的是app常用设置界面
 * @author sk
 */
public class MainActivity extends NfcActivity{
	
	public static final String ACTION_STRING = "MainActivity";
	
	private ViewPager viewPager;
	private ImageView imageView0,imageView1;
	private View userRigthView,configView;
	private ArrayList<View> views;
	private long lastExitTime;
	private LinearLayout userRightContainer;
	private UserRigthGroupView machineManageGroup;
	private ArrayList<HashMap<String, String>> data;
	private ActionBar mActionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iniActionBar();
		iniData();
		ini();
	}
	
	/**
	 *  初始化actionbar
	 */
	private void iniActionBar(){
		mActionBar = getActionBar();
		mActionBar.setTitle(R.string.activity_userright_title);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setHomeButtonEnabled(true);
	}
	
	/**
	 *   初始化数据
	 */
	private void iniData(){
		data = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(EnumList.UserRight.RightName, EnumList.UserRight.MACHINEINFO.getName());
		map.put(EnumList.UserRight.RightCode, EnumList.UserRight.MACHINEINFO.getCode()+"");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put(EnumList.UserRight.RightName, EnumList.UserRight.REPAIRMANAGE.getName());
		map.put(EnumList.UserRight.RightCode, EnumList.UserRight.REPAIRMANAGE.getCode()+"");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put(EnumList.UserRight.RightName, EnumList.UserRight.MAINTAINHISTORY.getName());
		map.put(EnumList.UserRight.RightCode, EnumList.UserRight.MAINTAINHISTORY.getCode()+"");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put(EnumList.UserRight.RightName, EnumList.UserRight.UPKEEPHISTORY.getName());
		map.put(EnumList.UserRight.RightCode, EnumList.UserRight.UPKEEPHISTORY.getCode()+"");
		data.add(map);
	}
	
	
	/**
	 * 初始化界面
	 */
	private void ini(){
		viewPager = (ViewPager)findViewById(R.id.activity_main_container);
		imageView0 = (ImageView)findViewById(R.id.activity_main_page0);
		imageView1 = (ImageView)findViewById(R.id.activity_main_page1);
		//注册权限界面
		userRigthView = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_userright, null);
		//注册设置界面
		configView = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_config, null);
		iniUserRight();
		iniConfig();
		views = new ArrayList<View>();
		views.add(userRigthView);
		views.add(configView);
		viewPager.setOnPageChangeListener(new MainOnPageChangeListener());
		viewPager.setAdapter(new MainPageAdapter(views));
	}
	
	/**
	 *  初始化权限控制
	 */
	private void iniUserRight(){
		userRightContainer = (LinearLayout)userRigthView.findViewById(R.id.activity_userright_container);
		machineManageGroup = new UserRigthGroupView(MainActivity.this,getResources().getString(R.string.activity_userright_group_machinemanage),data);
		userRightContainer.addView(machineManageGroup);
	}
	
	/**
	 *  初始化配置界面
	 */
	private void iniConfig(){
		
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
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		if(System.currentTimeMillis()-lastExitTime>2000){
			Toast.makeText(MainActivity.this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
		}else {
			finish();
		}
		lastExitTime = System.currentTimeMillis();	
	}
	
	
	private class MainOnPageChangeListener implements OnPageChangeListener{
		@Override
		public void onPageScrollStateChanged(int arg0) {}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				mActionBar.setTitle(R.string.activity_userright_title);
				imageView0.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				imageView1.setImageDrawable(getResources().getDrawable(R.drawable.page));
				break;
			case 1:
				mActionBar.setTitle(R.string.activity_config_title);
				imageView0.setImageDrawable(getResources().getDrawable(R.drawable.page));
				imageView1.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				break;
			}
		}
	}
	
	private class MainPageAdapter extends PagerAdapter{
		private ArrayList<View> mViews;
		public MainPageAdapter(ArrayList<View> views){
			mViews = views;
		}
		@Override
		public int getCount() {
			return mViews.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		public void destroyItem(View container, int position, Object object) {
			((ViewPager)container).removeView(views.get(position));
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager)container).addView(mViews.get(position));
			return mViews.get(position);
		}
		
	}

}
