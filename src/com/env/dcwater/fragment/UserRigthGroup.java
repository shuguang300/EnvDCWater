package com.env.dcwater.fragment;
import com.env.dcwater.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserRigthGroup extends LinearLayout{
	private TextView title;
	private Context mContext;
	private String mTitleStr;
	private Button testButton;
	private GridView rights;
	public UserRigthGroup(Context context,String titleTxt){
		super(context);
		mContext = context;
		mTitleStr = titleTxt;
		LayoutInflater.from(mContext).inflate(R.layout.view_userright_group, this);
		ini();
	}
	public UserRigthGroup(Context context,AttributeSet attr){
		super(context, attr);
	}
	private void ini(){
		title = (TextView)findViewById(R.id.view_userright_title);
		testButton = (Button)findViewById(R.id.view_userright_testbutton);
		rights = (GridView)findViewById(R.id.view_userright_rights);
		title.setText(mTitleStr);
	}
	public void setGroupTitle(String str){
		title.setText(str);
	}
}
