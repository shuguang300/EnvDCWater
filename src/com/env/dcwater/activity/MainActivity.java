package com.env.dcwater.activity;
import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.fragment.ConsManage;
import com.env.dcwater.fragment.MachineInfo;
import com.env.dcwater.fragment.MachineManage;
import com.env.dcwater.fragment.PlanManage;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * 主界面
 * @author Administrator
 */
public class MainActivity extends NfcActivity{
	private FragmentTransaction ft;
	private FragmentManager fm;
	private Fragment consManage,machineManage,machineInfo,planManage;
	private RadioGroup fragmentControll;
	private FrameLayout fragmentContainer;
	private GestureDetector gestureDetector;
	private TextView drawerlayoutToggle;
	private ArrayAdapter<String> mlistAdapter;
	private int pos = 0;
	private final int RIGHT = 0;  
	private final int LEFT = 1;  
	public final int MACHINEMANAGE = 0;
	public final int CONSMANAGE = 1;
	public final int MACHINEINFO = 2;
	public final int PLANMANAGE = 3;
	private android.support.v4.widget.DrawerLayout mDrawerLayout;
	private ListView mListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		machineInfo = new MachineInfo();
		planManage = new PlanManage();
		
		//初始化listview
		initDrawlayoutList(pos);
		
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
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				mlistOnItemClick(position);
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
		
		//监听底部导航radiogroup切换
		fragmentControll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (group.getCheckedRadioButtonId()) {
				case R.id.radiogroup_machinemanage:
					if(pos!=MACHINEMANAGE){
						changeFragment(MACHINEMANAGE);
					}
					break;
				case R.id.radiogroup_consmanage:
					if(pos!=CONSMANAGE){
						changeFragment(CONSMANAGE);
					}
					break;
				case R.id.radiogroup_machineinfo:
					if(pos!=MACHINEINFO){
						changeFragment(MACHINEINFO);
					}
					break;
				case R.id.radiogroup_planmanage:
					if(pos!=PLANMANAGE){
						changeFragment(PLANMANAGE);
					}
					break;
				}
			}
		});
	}
	
	/**
	 * 执行list的单击事件
	 * @param position
	 */
	public void mlistOnItemClick(int position){
		switch (pos) {
		case MACHINEMANAGE:
			break;
		case CONSMANAGE:
			break;
		case MACHINEINFO:
			break;
		case PLANMANAGE:
			break;
		}
//		drawerlayoutToggle.setText(mlistAdapter.getItem(position));
		mDrawerLayout.closeDrawer(Gravity.LEFT);
	}
	
	/**
	 * 初始化drawerlayout的list数据
	 * @param position
	 */
	public void initDrawlayoutList(int position){
		String[] data = null;
		switch (position) {
		case MACHINEMANAGE:
			data = getResources().getStringArray(R.array.machinemanage);
			break;
		case CONSMANAGE:
			data = getResources().getStringArray(R.array.consmanage);
			break;
		case MACHINEINFO:
			data = getResources().getStringArray(R.array.machineinfo);
			break;
		case PLANMANAGE:
			data = getResources().getStringArray(R.array.planmanage);
			break;
		}
		mlistAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,data);
 		mListView.setAdapter(mlistAdapter);
// 		drawerlayoutToggle.setText(mlistAdapter.getItem(0));
	}
	
	/**
	 * 设置TextView的四周背景
	 * @param view 传入的TextView
	 * @param position 0=left,1=top,2=right,3=bottom
	 * @param resId 设置背景色 的资源ID
	 */
	public void setTextViewCompoundDrawables(TextView view, int position,int resId){
		Drawable drawable= getResources().getDrawable(resId);  
		switch (position) {
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
			case MACHINEMANAGE:
				if(machineManage==null){
					machineManage = new MachineManage();
				}
				temp = machineManage;
				break;
			case CONSMANAGE:
				if(consManage==null){
					consManage = new ConsManage();
				}
				temp = consManage;
				break;
			case MACHINEINFO:
				if(machineInfo==null){
					machineInfo = new MachineInfo();
				}
				temp = machineInfo;
				break;
			case PLANMANAGE:
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
			initDrawlayoutList(pos);
		}
	}
	
	private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
			float x = e2.getX() - e1.getX();
			if (x > 100) {
				doResult(RIGHT);
			} else if (x < -100) {
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
		case MACHINEMANAGE:
			radioId = R.id.radiogroup_machinemanage;
			break;
		case CONSMANAGE:
			radioId = R.id.radiogroup_consmanage;
			break;
		case MACHINEINFO:
			radioId = R.id.radiogroup_machineinfo;
			break;
		case PLANMANAGE:
			radioId = R.id.radiogroup_planmanage;
			break;
		}
		fragmentControll.check(radioId);
	}
} 