package com.env.dcwater.fragment;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import com.env.dcwater.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

/**
 * 一个仿iphone选取时间的自定义控件 
 * @author sk
 *
 */
public class DateTimePickerView extends PopupWindow{
	public static final int MIN_YEAR_INTEGER = 1990;
	public static final int MAX_YEAR_INTEGER = 2100;
	private Context mContext;
	private View mView;
	private WheelView yearWheelView,monthWheelView,dayWheelView,hourWheelView,minuteWheelView;
	/**
	 * 构造函数
	 * @param context
	 */
	public DateTimePickerView(Context context){
		super(context);
		mContext = context;
		mView = LayoutInflater.from(mContext).inflate(R.layout.view_datetimepick, null);
		setContentView(mView);
		iniView();
	}
	
	private void iniView(){
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setAnimationStyle(R.style.popupwindow_anim);
		yearWheelView = (WheelView)mView.findViewById(R.id.view_datetimepick_year);
		monthWheelView = (WheelView)mView.findViewById(R.id.view_datetimepick_month);
		dayWheelView = (WheelView)mView.findViewById(R.id.view_datetimepick_day);
		hourWheelView = (WheelView)mView.findViewById(R.id.view_datetimepick_hour);
		minuteWheelView = (WheelView)mView.findViewById(R.id.view_datetimepick_minute);
	}
	
	public void iniWheelView(Calendar calendar){
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };
		
		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);
		
		yearWheelView.setViewAdapter(new NumericWheelAdapter(mContext,MIN_YEAR_INTEGER,MAX_YEAR_INTEGER));// 
		yearWheelView.setCyclic(true);
		yearWheelView.setCurrentItem(year - MIN_YEAR_INTEGER);
		
		monthWheelView.setViewAdapter(new NumericWheelAdapter(mContext,1, 12));
		monthWheelView.setCyclic(true);
		monthWheelView.setCurrentItem(month);
		
		dayWheelView.setCyclic(true);
		if (list_big.contains(String.valueOf(month + 1))) {
			dayWheelView.setViewAdapter(new NumericWheelAdapter(mContext,1, 31,"%02d"));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			dayWheelView.setViewAdapter(new NumericWheelAdapter(mContext,1, 30,"%02d"));
		} else {
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				dayWheelView.setViewAdapter(new NumericWheelAdapter(mContext,1, 29,"%02d"));
			else
				dayWheelView.setViewAdapter(new NumericWheelAdapter(mContext,1, 28,"%02d"));
		}
		dayWheelView.setCurrentItem(day - 1);
		
		hourWheelView.setViewAdapter(new NumericWheelAdapter(mContext,0, 23,"%02d"));
		hourWheelView.setCyclic(true);
		hourWheelView.setCurrentItem(hour);

		minuteWheelView.setViewAdapter(new NumericWheelAdapter(mContext,0,59,"%02d"));
		minuteWheelView.setCyclic(true);
		minuteWheelView.setCurrentItem(minute);
		
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + MIN_YEAR_INTEGER;
				if (list_big.contains(String.valueOf(monthWheelView.getCurrentItem() + 1))) {
					dayWheelView.setViewAdapter(new NumericWheelAdapter(mContext,1, 31,"%02d"));
				} else if (list_little.contains(String.valueOf(monthWheelView.getCurrentItem() + 1))) {
					dayWheelView.setViewAdapter(new NumericWheelAdapter(mContext,1, 30,"%02d"));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
						dayWheelView.setViewAdapter(new NumericWheelAdapter(mContext,1, 29,"%02d"));
					else
						dayWheelView.setViewAdapter(new NumericWheelAdapter(mContext,1, 28,"%02d"));
				}
			}
		};
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				if (list_big.contains(String.valueOf(month_num))) {
					dayWheelView.setViewAdapter(new NumericWheelAdapter(mContext,1, 31,"%02d"));
				} else if (list_little.contains(String.valueOf(month_num))) {
					dayWheelView.setViewAdapter(new NumericWheelAdapter(mContext,1, 30,"%02d"));
				} else {
					if (((yearWheelView.getCurrentItem() + MIN_YEAR_INTEGER) % 4 == 0 && (yearWheelView.getCurrentItem() + MIN_YEAR_INTEGER) % 100 != 0)|| (yearWheelView.getCurrentItem() + MIN_YEAR_INTEGER) % 400 == 0)
						dayWheelView.setViewAdapter(new NumericWheelAdapter(mContext,1, 29,"%02d"));
					else
						dayWheelView.setViewAdapter(new NumericWheelAdapter(mContext,1, 28,"%02d"));
				}
			}
		};
		yearWheelView.addChangingListener(wheelListener_year);
		monthWheelView.addChangingListener(wheelListener_month);
	}
}
