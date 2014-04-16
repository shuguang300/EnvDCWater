package com.env.dcwater.fragment;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.env.dcwater.R;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.util.LogicMethod;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 表单筛选控件，可使用时间，设备位置，工单状态进行过滤
 * @author sk
 */
public class DataFilterView extends LinearLayout{
	
	public static final String TAG_STRING = "DataFilterView";
	
	private Context mContext;
	private View mView;
	private TableRow trFastTime,trStartTime,trEndTime,trState,trPos;
	private TableRowClickListen mClickListen;
	private DateTimePickerView dateTimePickerView;
	private Builder mBuilder;
	private Button mSubmit;
	private Calendar mStartTime,mEndTime;
	private TextView tvFastTime,tvStartTime,tvEndTime,tvState,tvPos;
	private int selectFastTimePos,selectStatePos,selectPosPos;
	private String [] mStateList,mPosList,mFastTimeList;
	private String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
	private String[] months_little = { "4", "6", "9", "11" };
	final List<String> list_big = Arrays.asList(months_big);
	final List<String> list_little = Arrays.asList(months_little);
	/**
	 * @param context
	 */
	public DataFilterView (Context context){
		super(context);
		mContext = context;
		mView = LayoutInflater.from(mContext).inflate(R.layout.view_datafilter, this);
		iniView();
		
	}

	public DataFilterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mView = LayoutInflater.from(mContext).inflate(R.layout.view_datafilter, this);
		iniView();
	}
	
	/**
	 * 初始化条件过滤器
	 */
	private void iniView(){
		mClickListen = new TableRowClickListen();
		
		//初始化数据
		mFastTimeList = mContext.getResources().getStringArray(R.array.view_datafilter_fasttimelist);
		selectFastTimePos = 0;
		selectStatePos = 0;
		selectPosPos = 0;
		mStartTime = Calendar.getInstance(Locale.CHINA);
		mEndTime = Calendar.getInstance(Locale.CHINA);
		mStartTime.add(Calendar.DAY_OF_YEAR, -7);
		
		
		//查找控件
		trFastTime = (TableRow)mView.findViewById(R.id.view_datafilter_tablerow_fasttime);
		trStartTime = (TableRow)mView.findViewById(R.id.view_datafilter_tablerow_starttime);
		trEndTime = (TableRow)mView.findViewById(R.id.view_datafilter_tablerow_endtime);
		trState = (TableRow)mView.findViewById(R.id.view_datafilter_tablerow_state);
		trPos = (TableRow)mView.findViewById(R.id.view_datafilter_tablerow_pos);
		
		tvFastTime = (TextView)mView.findViewById(R.id.view_datafilter_tv_fasttime);
		tvStartTime = (TextView)mView.findViewById(R.id.view_datafilter_tv_starttime);
		tvEndTime = (TextView)mView.findViewById(R.id.view_datafilter_tv_endtime);
		tvState = (TextView)mView.findViewById(R.id.view_datafilter_tv_state);
		tvPos = (TextView)mView.findViewById(R.id.view_datafilter_tv_pos);
		
		mSubmit = (Button)mView.findViewById(R.id.view_datafilter_submit);
		
		//初始化控件的内容
		tvFastTime.setText(mFastTimeList[selectFastTimePos].toString());
		tvStartTime.setText(new SimpleDateFormat(SystemParams.SHORTDATE_PATTERN_STRING, Locale.CHINA).format(mStartTime.getTime()));
		tvEndTime.setText(new SimpleDateFormat(SystemParams.SHORTDATE_PATTERN_STRING, Locale.CHINA).format(mEndTime.getTime()));
		
		
		//设置控件的单击事件
		trFastTime.setOnClickListener(mClickListen);
		trStartTime.setOnClickListener(mClickListen);
		trEndTime.setOnClickListener(mClickListen);
		trState.setOnClickListener(mClickListen);
		trPos.setOnClickListener(mClickListen);
	}
	
	
	/**
	 * 设置当选择时间快速选择时，帮助用户自动设定时间范围
	 * @param pos
	 */
	private void setTimeText(int pos){
		if(selectFastTimePos!=pos){
			mStartTime = Calendar.getInstance(Locale.CHINA);
			mEndTime = Calendar.getInstance(Locale.CHINA);
			switch (pos) {
			case 0://请选择
				mStartTime.add(Calendar.DAY_OF_YEAR, -7);
				break;
			case 1://今天
				break;
			case 2://本月
				mStartTime.set(Calendar.DAY_OF_MONTH, 1);
				break;
			case 3://上月
				mStartTime.add(Calendar.MONTH, -1);
				mEndTime.add(Calendar.MONTH, -1);
				mStartTime.set(Calendar.DAY_OF_MONTH, 1);
				if(list_big.contains(mEndTime.get(Calendar.MONDAY)+1)){
					mEndTime.set(Calendar.DAY_OF_MONTH, 31);
				}else if (list_little.contains(mEndTime.get(Calendar.MONDAY)+1)) {
					mEndTime.set(Calendar.DAY_OF_MONTH, 30);
				}else {
					if(LogicMethod.isLeapYear(mEndTime.get(Calendar.YEAR))){
						mEndTime.set(Calendar.DAY_OF_MONTH, 29);
					}else {
						mEndTime.set(Calendar.DAY_OF_MONTH, 28);
					}
				}
				break;
			case 4://今年
				mStartTime.set(Calendar.DAY_OF_YEAR, 1);
				break;
			case 5://去年
				mStartTime.add(Calendar.YEAR, -1);
				mEndTime.add(Calendar.YEAR, -1);
				mStartTime.set(Calendar.DAY_OF_YEAR, 1);
				if(LogicMethod.isLeapYear(mEndTime.get(Calendar.YEAR))){
					mEndTime.set(Calendar.DAY_OF_YEAR, 366);
				}else {
					mEndTime.set(Calendar.DAY_OF_YEAR, 365);
				}
				break;
			case 6://最近7天
				mStartTime.add(Calendar.DAY_OF_YEAR, -7);
				break;
			case 7://最近1月
				mStartTime.add(Calendar.DAY_OF_YEAR, -30);
				break;
			}
			tvStartTime.setText(new SimpleDateFormat(SystemParams.SHORTDATE_PATTERN_STRING, Locale.CHINA).format(mStartTime.getTime()));
			tvEndTime.setText(new SimpleDateFormat(SystemParams.SHORTDATE_PATTERN_STRING, Locale.CHINA).format(mEndTime.getTime()));
			tvFastTime.setText(mFastTimeList[pos]);
		}
		
	}
	
	/**
	 * 判断日期选择控件是否出现
	 * @return
	 */
	public boolean isDateTimePickerShowing(){
		if(dateTimePickerView!=null&&dateTimePickerView.isShowing()){
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 隐藏日期选择控件
	 */
	public void hideDateTimePicker(){
		if(dateTimePickerView!=null){
			dateTimePickerView.dismiss();
		}
	}
	
	/**
	 * 设置工单状态列表
	 * @param stateList 工单状态列表
	 * @param defaultPos 默认值
	 */
	public void setStateList(String [] stateList, int defaultPos){
		mStateList = stateList;
		selectStatePos  =defaultPos ;
		tvState.setText(mStateList[selectStatePos]);
	}
	
	/**
	 * 设置设备位置列表
	 * @param stateList 工单状态列表
	 * @param defaultPos 默认值
	 */
	public void setPosList(String [] posList,int defaultPos){
		mPosList = posList;
		selectPosPos = defaultPos;
		tvPos.setText(mPosList[selectPosPos]);
	}
	
	/**
	 * 设定 提交 按钮的单击事件
	 */
	public void setSubmitEvent(OnClickListener onClickListener){
		mSubmit.setOnClickListener(onClickListener);
	}
	
	/**
	 * 隐藏时间选择，有可能不需要
	 */
	public void hideTimeSelectionPart(){
		trFastTime.setVisibility(View.GONE);
		trStartTime.setVisibility(View.GONE);
		trEndTime.setVisibility(View.GONE);
	}
	
	/**
	 * 隐藏设备安装位置过滤条件，有可能不需要
	 */
	public void hideDevicePositionPicker(){
		trPos.setVisibility(View.GONE);
	}
	
	/**
	 * 隐藏工单状态过滤额条件，有可能不需要
	 */
	public void hideTaskStatePicker(){
		trState.setVisibility(View.GONE);
	}
	
	/**
	 * 获取条件过滤控件中用户所选取的条件
	 * @return String Array 
	 * 0:开始时间;1:结束时间;2:安装位置;3:工单状态
	 */
	public String [] getSelectCondition(){
		String [] conditions = new String [4];
		conditions[0] = tvStartTime.getText().toString();
		conditions[1] = tvEndTime.getText().toString();
		conditions[2] = tvPos.getText().toString();
		conditions[3] = tvState.getText().toString();
		return conditions;
	}
	
	private class TableRowClickListen implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.view_datafilter_tablerow_fasttime:
				if(mBuilder==null){
					mBuilder = new Builder(mContext);
					mBuilder.setCancelable(true);
				}
				mBuilder.setTitle("快速选择时间").setSingleChoiceItems(mFastTimeList, selectFastTimePos,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						setTimeText(which);
						selectFastTimePos = which;
						dialog.dismiss();
					}
				});
				mBuilder.create().show();
				break;
			case R.id.view_datafilter_tablerow_starttime:
				if(dateTimePickerView==null){
					dateTimePickerView = new DateTimePickerView(mContext);
					dateTimePickerView.setShortDateView();
					dateTimePickerView.hideResetButton();
				}
				dateTimePickerView.setButtonClickEvent(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mStartTime.setTime(dateTimePickerView.getSelectedDate());
						tvStartTime.setText(new SimpleDateFormat(SystemParams.SHORTDATE_PATTERN_STRING, Locale.CHINA).format(mStartTime.getTime()));
						dateTimePickerView.dismiss();
					}
				}, new OnClickListener() {
					@Override
					public void onClick(View v) {
						
					}
				}, null);
				dateTimePickerView.iniWheelView(mStartTime);
				dateTimePickerView.showAtLocation(mView.findViewById(R.id.view_datafilter_tablerow_starttime), Gravity.BOTTOM, 0, 0);
				break;
			case R.id.view_datafilter_tablerow_endtime:
				if(dateTimePickerView==null){
					dateTimePickerView = new DateTimePickerView(mContext);
					dateTimePickerView.setShortDateView();
					dateTimePickerView.hideResetButton();
				}
				dateTimePickerView.setButtonClickEvent(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mEndTime.setTime(dateTimePickerView.getSelectedDate());
						tvEndTime.setText(new SimpleDateFormat(SystemParams.SHORTDATE_PATTERN_STRING, Locale.CHINA).format(mEndTime.getTime()));
						dateTimePickerView.dismiss();
					}
				}, new OnClickListener() {
					@Override
					public void onClick(View v) {
						dateTimePickerView.dismiss();
					}
				}, null);
				dateTimePickerView.iniWheelView(mEndTime);
				dateTimePickerView.showAtLocation(mView.findViewById(R.id.view_datafilter_tablerow_starttime), Gravity.BOTTOM, 0, 0);
				break;
			case R.id.view_datafilter_tablerow_state:
				if(mStateList!=null){
					if (mBuilder == null) {
						mBuilder = new Builder(mContext);
						mBuilder.setCancelable(true);
					}
					mBuilder.setTitle("状态").setSingleChoiceItems(mStateList,selectStatePos,new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which) {
							tvState.setText(mStateList[which]);
							selectStatePos = which;
							dialog.dismiss();
						}
					});
					mBuilder.create().show();
				}
				break;
			case R.id.view_datafilter_tablerow_pos:
				if (mPosList != null) {
					if (mBuilder == null) {
						mBuilder = new Builder(mContext);
						mBuilder.setCancelable(true);
					}
					mBuilder.setTitle("安装位置").setSingleChoiceItems(mPosList,selectPosPos,new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which) {
							tvPos.setText(mPosList[which]);
							selectPosPos = which;
							dialog.dismiss();
						}
					});
					mBuilder.create().show();
				}
				break;
			}
		}
	}
}
