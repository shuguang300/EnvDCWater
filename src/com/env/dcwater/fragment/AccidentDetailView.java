package com.env.dcwater.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 自定义填报故障现象的控件，包括添加图片，文本，录音
 * 类似于新浪微博的模式
 * @author sk
 *
 */
public class AccidentDetailView extends RelativeLayout{
	
	public static final String TAG_STRING = "AccidentDetailView";
	
	private View mView;
	
	public AccidentDetailView (Context context){
		super(context);
	}
	
	public AccidentDetailView(Context context, AttributeSet attrs){
		super(context, attrs);
	}

}
