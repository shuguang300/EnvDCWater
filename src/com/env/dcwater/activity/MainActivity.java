package com.env.dcwater.activity;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.fragment.UserRigthGroupView;
import com.env.dcwater.javabean.EnumList;

public class MainActivity extends NfcActivity{
	
	private ViewPager viewPager;
	private ImageView imageView0,imageView1;
	private View userRigthView,configView;
	private ArrayList<View> views;
	private long lastExitTime;
	private LinearLayout userRightContainer;
	private UserRigthGroupView machineManageGroup;
	private ArrayList<HashMap<String, String>> data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iniData();
		ini();
	}
	
	/**
	 *   测试数据
	 */
	private void iniData(){
		data = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(EnumList.EnumUserRight.RightName, EnumList.EnumUserRight.MACHINEINFO.getName());
		map.put(EnumList.EnumUserRight.RightCode, EnumList.EnumUserRight.MACHINEINFO.getCode()+"");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put(EnumList.EnumUserRight.RightName, EnumList.EnumUserRight.REPAIRMANAGE.getName());
		map.put(EnumList.EnumUserRight.RightCode, EnumList.EnumUserRight.REPAIRMANAGE.getCode()+"");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put(EnumList.EnumUserRight.RightName, EnumList.EnumUserRight.MAINTAINHISTORY.getName());
		map.put(EnumList.EnumUserRight.RightCode, EnumList.EnumUserRight.MAINTAINHISTORY.getCode()+"");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put(EnumList.EnumUserRight.RightName, EnumList.EnumUserRight.UPKEEPHISTORY.getName());
		map.put(EnumList.EnumUserRight.RightCode, EnumList.EnumUserRight.UPKEEPHISTORY.getCode()+"");
		data.add(map);
	}
	
	
	/**
	 * 初始化布局控件
	 */
	private void ini(){
		viewPager = (ViewPager)findViewById(R.id.activity_main_container);
		imageView0 = (ImageView)findViewById(R.id.activity_main_page0);
		imageView1 = (ImageView)findViewById(R.id.activity_main_page1);
		//注册用户权限界面
		userRigthView = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_userright, null);
		//注册用户设置界面
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
	 *  初始化用户权限控制界面
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
	public void onBackPressed() {
		if(System.currentTimeMillis()-lastExitTime>2000){
			Toast.makeText(MainActivity.this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
		}else {
			finish();
		}
		lastExitTime = System.currentTimeMillis();	
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
