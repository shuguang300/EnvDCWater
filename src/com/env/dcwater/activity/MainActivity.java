package com.env.dcwater.activity;
import com.env.dcwater.R;
import com.env.dcwater.fragment.ConsManage;
import com.env.dcwater.fragment.MachinInfo;
import com.env.dcwater.fragment.MachineManage;
import com.env.dcwater.fragment.PlanManage;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends Activity{
	private FragmentTransaction ft;
	private FragmentManager fm;
	private Fragment consManage,machineManage,machineInfo,planManage;
	private RadioGroup fragmentControll;
	private LinearLayout fragmentContainer;
	private GestureDetector gestureDetector;
	private int pos = 0;
	private final int RIGHT = 0;  
	private final int LEFT = 1;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		init();
	}
	
	private void init(){
		fragmentControll = (RadioGroup)findViewById(R.id.main_activity_controll);
		fragmentContainer = (LinearLayout)findViewById(R.id.main_activity_fragment);
		gestureDetector = new GestureDetector(MainActivity.this, onGestureListener);
		
		machineManage = new MachineManage();
		consManage = new ConsManage();
		machineInfo = new MachinInfo();
		planManage = new PlanManage();
		
		fm = getFragmentManager();
		ft = fm.beginTransaction();
		ft.replace(R.id.main_activity_fragment, machineManage);
		pos = 0;
		ft.commit();
		
		fragmentControll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
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
				case R.id.radiogroup_planmanage:
					if(pos!=2){
						changeFragment(2);
					}
					break;
				case R.id.radiogroup_machineinfo:
					if(pos!=3){
						changeFragment(3);
					}
					break;
				}
				
			}
		});
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		return true;
	}

	public void changeFragment(int position){
		Fragment temp = null;
		if(position!=pos){
			switch (position) {
			case 0:
				temp = machineManage;
				break;
			case 1:
				temp = consManage;
				break;
			case 2:
				temp = machineInfo;
				break;
			case 3:
				temp = planManage;
				break;
			}
			ft = fm.beginTransaction();
			ft.replace(R.id.main_activity_fragment, temp);
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
	public void doResult(int action) {
		int count = 0;
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
		changeFragment(count);
	}
} 