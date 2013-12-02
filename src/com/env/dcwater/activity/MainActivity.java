package com.env.dcwater.activity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.env.dcwater.R;
import com.env.dcwater.component.FragmentListAdapter;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.fragment.ConsManage;
import com.env.dcwater.fragment.MachineInfo;
import com.env.dcwater.fragment.MachineManage;
import com.env.dcwater.fragment.PlanManage;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.telephony.TelephonyManager;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ������
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
	private FragmentListAdapter mlistAdapter;
	private ListView mListView;
	private DrawerLayout mDrawerLayout;
	private List<HashMap<String, String>> fragmentListData;
	private int pos = 0;
	private final int RIGHT = 0;  
	private final int LEFT = 1;  
	public static final int MACHINEMANAGE = 0;
	public static final int CONSMANAGE = 1;
	public static final int MACHINEINFO = 2;
	public static final int PLANMANAGE = 3;
	
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
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	private void init(){
		fragmentControll = (RadioGroup)findViewById(R.id.main_activity_controll);
		fragmentContainer = (FrameLayout)findViewById(R.id.main_activity_fragment);
		mDrawerLayout = (DrawerLayout)findViewById(R.id.mdrawlayout);
		mListView = (ListView)findViewById(R.id.mdrawlayout_item);
		drawerlayoutToggle = (TextView)findViewById(R.id.drawlayout_toggle);
		
		//�������Ƽ����¼�
		gestureDetector = new GestureDetector(MainActivity.this, onGestureListener);
		
		machineManage = new MachineManage();
		consManage = new ConsManage();
		machineInfo = new MachineInfo();
		planManage = new PlanManage();
		
		//��ʼ��listview
		initDrawlayoutList(pos);
		
		//����fragment�����һ����¼�
		fragmentContainer.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				gestureDetector.onTouchEvent(event);
				return true;
			}
		});
		
		//��ʼ�����õ�һ������
		fm = getFragmentManager();
		ft = fm.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putSerializable("Data", fragmentListData.get(0));
		machineManage.setArguments(bundle);
		ft.replace(R.id.main_activity_fragment, machineManage);
		ft.commit();
		
		//��������Ĵ򿪹ر��¼�
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
		
		//����listview�ĵ����¼�
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				mlistOnItemClick(position);
			}
		});
		
		
		//�������밴ť�¼�
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
		
		//�����ײ�����radiogroup�л�
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
	 * ִ��list�ĵ����¼�
	 * @param position
	 */
	public void mlistOnItemClick(int position){
		switch (pos) {
		case MACHINEMANAGE:
			((MachineManage)machineManage).setData(fragmentListData.get(position));
			break;
		case CONSMANAGE:
			break;
		case MACHINEINFO:
			break;
		case PLANMANAGE:
			break;
		}
		mDrawerLayout.closeDrawer(Gravity.LEFT);
	}
	
	/**
	 * ��ʼ��drawerlayout��list����
	 * @param position
	 */
	public void initDrawlayoutList(int position){
		fragmentListData = new ArrayList<HashMap<String,String>>();
		mlistAdapter = new FragmentListAdapter(this, position, fragmentListData);
		for(int i =0;i<5;i++){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("MachineName", "MachineName"+i);
			map.put("MachinePos", "MachinePos"+i);
			map.put("MachineMode", "MachineMode"+i);
			map.put("MachineManufacture", "MachineManufacture"+i);
			fragmentListData.add(map);
		}
		switch (position) {
		case MACHINEMANAGE:
			break;
		case CONSMANAGE:
			break;
		case MACHINEINFO:
			break;
		case PLANMANAGE:
			break;
		}
 		mListView.setAdapter(mlistAdapter);
	}
	
	/**
	 * ����TextView�����ܱ���
	 * @param view �����TextView
	 * @param position 0=left,1=top,2=right,3=bottom
	 * @param resId ���ñ���ɫ ����ԴID
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
	 * �滻fragment,��ʹ��hide��show��������fragment��״̬
	 * @param position ��Ҫչʾ��fragment�����
	 */
	public void changeFragment(int position){
		Fragment temp = null;
		Bundle bundle = new Bundle();
		if(position!=pos){
			initDrawlayoutList(position);
			switch (position) {
			case MACHINEMANAGE:
				if(machineManage==null){
					machineManage = new MachineManage();
					bundle.putSerializable("Data", fragmentListData.get(0));
					machineManage.setArguments(bundle);
				}
				temp = machineManage;
				break;
			case CONSMANAGE:
				if(consManage==null){
					consManage = new ConsManage();
					bundle.putSerializable("Data", fragmentListData.get(0));
					consManage.setArguments(bundle);
				}
				temp = consManage;
				break;
			case MACHINEINFO:
				if(machineInfo==null){
					machineInfo = new MachineInfo();
					bundle.putSerializable("Data", fragmentListData.get(0));
					machineInfo.setArguments(bundle);
				}
				temp = machineInfo;
				break;
			case PLANMANAGE:
				if(planManage==null){
					planManage = new PlanManage();
					bundle.putSerializable("Data", fragmentListData.get(0));
					planManage.setArguments(bundle);
				}
				temp = planManage;
				break;
			}
			ft = fm.beginTransaction();
			switch (pos) {
			case MACHINEMANAGE:
				ft.hide(machineManage);
				break;
			case CONSMANAGE:
				ft.hide(consManage);
				break;
			case MACHINEINFO:
				ft.hide(machineInfo);
				break;
			case PLANMANAGE:
				ft.hide(planManage);
				break;
			}
			if(temp.isAdded()){
				ft.show(temp);
			}else {
				ft.add(temp, position+"");
			}
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();
			pos = position;
			
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
	 * ִ���������һ����¼�
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