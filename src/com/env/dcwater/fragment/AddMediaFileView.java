package com.env.dcwater.fragment;

import com.env.dcwater.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * 在数据录入界面中，可以输入文本，语音，图像
 * @author sk
 */
public class AddMediaFileView extends PopupWindow implements OnClickListener{
	
	private View mView;
	private Context mContext;
	private Button buttonVoice,buttonImg,buttonLocation;
	
	/**
	 * 构造函数
	 * @param context
	 */
	public AddMediaFileView(Context context){
		super(context);
		mContext = context;
		mView = LayoutInflater.from(context).inflate(R.layout.view_addmediafile, null);
		iniView();
	}
	
	public AddMediaFileView(Context context,AttributeSet attr){
		super(context, attr);
	}
	
	/**
	 * 初始化控件
	 */
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
			Toast.makeText(mContext, "添加语音", Toast.LENGTH_SHORT).show();
			break;
		case R.id.view_addmediafile_img:
			Toast.makeText(mContext, "添加图片", Toast.LENGTH_SHORT).show();
			break;
		case R.id.view_addmediafile_location:
			Toast.makeText(mContext, "添加位置信息", Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
}
