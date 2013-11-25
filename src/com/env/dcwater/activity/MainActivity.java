package com.env.dcwater.activity;
import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.fragment.ConsManage;
import com.env.dcwater.fragment.MachinInfo;
import com.env.dcwater.fragment.MachineManage;
import com.env.dcwater.fragment.PlanManage;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class MainActivity extends NfcActivity{
	private FragmentTransaction ft;
	private FragmentManager fm;
	private Fragment consManage,machineManage,machineInfo,planManage;
	private RadioGroup fragmentControll;
	private FrameLayout fragmentContainer;
	private GestureDetector gestureDetector;
	private TextView drawerlayoutToggle;
	private int pos = 0;
	private final int RIGHT = 0;  
	private final int LEFT = 1;  
	private android.support.v4.widget.DrawerLayout mDrawerLayout;
	private ListView mListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		
		init();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	private void init(){
		fragmentControll = (RadioGroup)findViewById(R.id.main_activity_controll);
		fragmentContainer = (FrameLayout)findViewById(R.id.main_activity_fragment);
		mDrawerLayout = (DrawerLayout)findViewById(R.id.mdrawlayout);
		mListView = (ListView)findViewById(R.id.mdrawlayout_item);
		drawerlayoutToggle = (TextView)findViewById(R.id.drawlayout_toggle);
		
		//创建手势监听事件
		gestureDetector = new GestureDetector(MainActivity.this, onGestureListener);
		
		machineManage = new MachineManage();
		consManage = new ConsManage();
		machineInfo = new MachinInfo();
		planManage = new PlanManage();
		
		//初始化listview
		initDrawlayoutList();
		
		//设置fragment的左右滑动事件
		fragmentContainer.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				gestureDetector.onTouchEvent(event);
				return true;
			}
		});
		
		//初始化设置第一个界面
		fm = getFragmentManager();
		ft = fm.beginTransaction();
		ft.replace(R.id.main_activity_fragment, machineManage);
		ft.commit();
		
		
		//监听抽屉的打开关闭事件
		mDrawerLayout.setDrawerListener(new DrawerListener() {
			@Override
			public void onDrawerStateChanged(int arg0) {
			}
			@Override
			public void onDrawerSlide(View arg0, float arg1) {
			}
			@Override
			public void onDrawerOpened(View arg0) {
				setTextViewCompoundDrawables(drawerlayoutToggle, 0, R.drawable.activity_main_arrowleft);
			}
			@Override
			public void onDrawerClosed(View arg0) {
				setTextViewCompoundDrawables(drawerlayoutToggle, 0, R.drawable.activity_main_arrowright);
			}
		});
		
		//监听抽屉按钮事件
		drawerlayoutToggle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
					mDrawerLayout.closeDrawer(Gravity.LEFT);
				}else {
					mDrawerLayout.openDrawer(Gravity.LEFT);
				}
			}
		});
		
		
		fragmentControll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (group.getCheckedRadioButtonId()) {
				case R.id.radiogroup_machinemanage:
					if(pos!=0){
						changeFragment(0);
					}
					break;
				case R.id.radiogroup_consmanage:
					if(pos!=1){
						changeFragment(1);
					}
					break;
				case R.id.radiogroup_machineinfo:
					if(pos!=2){
						changeFragment(2);
					}
					break;
				case R.id.radiogroup_planmanage:
					if(pos!=3){
						changeFragment(3);
					}
					break;
				}
			}
		});
	}
	
	public void initDrawlayoutList(){
		mListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.test)));
	}
	
	/**
	 * 设置TextView的四周背景
	 * @param view 传入的TextView
	 * @param pos 0=left,1=top,2=right,3=bottom
	 * @param resId 设置背景色 的资源ID
	 */
	public void setTextViewCompoundDrawables(TextView view, int pos,int resId){
		Drawable drawable= getResources().getDrawable(resId);  
		switch (pos) {
		case 0:
			view.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null); 
			break;
		case 1:
			view.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null); 
			break;
		case 2:
			view.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null); 
			break;
		case 3:
			view.setCompoundDrawablesWithIntrinsicBounds(null,null,null,drawable); 
			break;
		}
		 
	}
	
	/**
	 * 替换fragment
	 * @param position 需要展示的fragment的序号
	 */
	public void changeFragment(int position){
		Fragment temp = null;
		if(position!=pos){
			switch (position) {
			case 0:
				if(machineManage==null){
					machineManage = new MachineManage();
				}
				temp = machineManage;
				break;
			case 1:
				if(consManage==null){
					consManage = new ConsManage();
				}
				temp = consManage;
				break;
			case 2:
				if(machineInfo==null){
					machineInfo = new MachinInfo();
				}
				temp = machineInfo;
				break;
			case 3:
				if(planManage==null){
					planManage = new PlanManage();
				}
				temp = planManage;
				break;
			}
			ft = fm.beginTransaction();
			ft.replace(R.id.main_activity_fragment, temp);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();
			pos = position;
		}
	}
	
	private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
			float x = e2.getX() - e1.getX();
			if (x > 0) {
				doResult(RIGHT);
			} else if (x < 0) {
				doResult(LEFT);
			}
			return true;
		}
	};
	
	
	/**
	 * 执行向左向右滑动事件
	 * @param action 0:right,1:left
	 */
	public void doResult(int action) {
		int count = 0;
		int radioId = -1; 
		switch (action) {
		case RIGHT:
			count = pos - 1;
			if(count<0){
				count = 3;
			}
			break;
		case LEFT:
			count = pos+1;
			if(count>3){
				count = 0;
			}
			break;
		}
		switch (count) {
		case 0:
			radioId = R.id.radiogroup_machinemanage;
			break;
		case 1:
			radioId = R.id.radiogroup_consmanage;
			break;
		case 2:
			radioId = R.id.radiogroup_machineinfo;
			break;
		case 3:
			radioId = R.id.radiogroup_planmanage;
			break;
		}
		fragmentControll.check(radioId);
	}
} 