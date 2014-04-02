package com.env.dcwater.fragment;

import com.env.dcwater.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;

/**
 * 在数据录入界面中，可以输入文本，语音，图像
 * @author sk
 */
public class AddMediaFileView extends PopupWindow implements OnClickListener{
	
	private View mView;
	private Context mContext;
	
	public AddMediaFileView(Context context){
		super(context);
		mContext = context;
		mView = LayoutInflater.from(context).inflate(R.layout.view_addmediafile, null);
		iniView();
	}
	
	public AddMediaFileView(Context context,AttributeSet attr){
		super(context, attr);
	}
	
	private void iniView(){
		
	}

	@Override
	public void onClick(View v) {
		
	}
}
