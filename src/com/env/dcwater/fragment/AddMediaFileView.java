package com.env.dcwater.fragment;

import com.env.dcwater.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;

/**
 * 在数据录入界面中，可以输入文本，语音，图像
 * @author sk
 */
public class AddMediaFileView extends PopupWindow implements OnClickListener{
	
	private View mView;
	private Context mContext;
	private Button buttonVoice,buttonImg,buttonLocation;
	
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
		buttonVoice = (Button)mView.findViewById(R.id.view_addmediafile_voice);
		buttonImg = (Button)mView.findViewById(R.id.view_addmediafile_img);
		buttonLocation = (Button)mView.findViewById(R.id.view_addmediafile_location);
		buttonVoice.setOnClickListener(this);
		buttonImg.setOnClickListener(this);
		buttonLocation.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_addmediafile_voice:
			
			break;
		case R.id.view_addmediafile_img:
			
			break;
		case R.id.view_addmediafile_location:
			
			break;
		}
	}
	
}
