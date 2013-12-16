package com.env.dcwater.activity;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.fragment.UserRigthGroup;

public class MainActivity extends NfcActivity implements OnClickListener{
	
	private ViewPager viewPager;
	private ImageView imageView0,imageView1;
	private View userRigthView,configView;
	private ArrayList<View> views;
	private long lastExitTime;
	private LinearLayout userRightContainer;
	private UserRigthGroup machineManageGroup,performanceGroup,analysisGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ini();
	}
	
	private void ini(){
		viewPager = (ViewPager)findViewById(R.id.activity_main_container);
		imageView0 = (ImageView)findViewById(R.id.activity_main_page0);
		imageView1 = (ImageView)findViewById(R.id.activity_main_page1);
		userRigthView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_userright, null);
		iniUserRight();
		configView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_config, null);
		iniConfig();
		views = new ArrayList<View>();
		views.add(userRigthView);
		views.add(configView);
		viewPager.setOnPageChangeListener(new MainOnPageChangeListener());
		viewPager.setAdapter(new MainPageAdapter(views));
	}
	
	/**
	 *  初始化用户权限控制界面
	 */
	private void iniUserRight(){
		userRightContainer = (LinearLayout)userRigthView.findViewById(R.id.activity_userright_container);
		machineManageGroup = new UserRigthGroup(MainActivity.this,getResources().getString(R.string.activity_userright_group_machinemanage));
		performanceGroup = new UserRigthGroup(MainActivity.this,getResources().getString(R.string.activity_userright_group_performance));
		analysisGroup = new UserRigthGroup(MainActivity.this,getResources().getString(R.string.activity_userright_group_analysis));
		userRightContainer.addView(machineManageGroup);
		userRightContainer.addView(performanceGroup);
		userRightContainer.addView(analysisGroup);
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
	public void onBackPressed() {
		if(System.currentTimeMillis()-lastExitTime>2000){
			Toast.makeText(MainActivity.this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
		}else {
			finish();
		}
		lastExitTime = System.currentTimeMillis();	
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.test:
			break;
		default:
			break;
		}
	}
	
	class MainOnPageChangeListener implements OnPageChangeListener{
		@Override
		public void onPageScrollStateChanged(int arg0) {}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				imageView0.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				imageView1.setImageDrawable(getResources().getDrawable(R.drawable.page));
				break;
			case 1:
				imageView0.setImageDrawable(getResources().getDrawable(R.drawable.page));
				imageView1.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				break;
			}
		}
	}
	
	class MainPageAdapter extends PagerAdapter{
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
