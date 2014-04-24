package com.env.dcwater.fragment;
import java.util.ArrayList;
import java.util.HashMap;
import com.env.dcwater.R;
import com.env.dcwater.activity.DeviceInfoListActivity;
import com.env.dcwater.activity.MainActivity;
import com.env.dcwater.activity.MaintainHistoryActivity;
import com.env.dcwater.activity.RepairManageActivity;
import com.env.dcwater.activity.UpkeepHistoryActivity;
import com.env.dcwater.javabean.EnumList;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author sk
 */
public class UserRigthGroupView extends LinearLayout implements OnItemClickListener{
	
	
	private TextView title;
	private Context mContext;
	private String mTitleStr;
	private GridView rights;
	private ArrayList<HashMap<String, String>> mRightLists;
	public UserRigthGroupView(Context context,String titleTxt,ArrayList<HashMap<String, String>> rightList){
		super(context);
		mContext = context;
		mTitleStr = titleTxt;
		mRightLists = rightList;
		LayoutInflater.from(mContext).inflate(R.layout.view_userright_group, this);
		ini();
	}
	public UserRigthGroupView(Context context,AttributeSet attr){
		super(context, attr);
	}
	private void ini(){
		title = (TextView)findViewById(R.id.view_userright_title);
		rights = (GridView)findViewById(R.id.view_userright_rights);
		title.setText(mTitleStr);
		rights.setAdapter(new UserRightGroupAdapter());
		rights.setOnItemClickListener(this);
	}
	
	private void startRightActivity(int pos){
		int code = Integer.valueOf(mRightLists.get(pos).get(EnumList.UserRight.RightCode));
		Intent intent = new Intent();
		intent.putExtra("action", MainActivity.ACTION_STRING);
		switch (code) {
		case 0:
			intent.setClass(mContext, DeviceInfoListActivity.class);
			break;
		case 1:
			intent.setClass(mContext, RepairManageActivity.class);
			break;
		case 2:
			intent.setClass(mContext, MaintainHistoryActivity.class);
			break;
		case 3:
			intent.setClass(mContext, UpkeepHistoryActivity.class);
			break;
		}
		mContext.startActivity(intent);
	}
	public void setGroupTitle(String str){
		title.setText(str);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		startRightActivity(position);
	}
	
	private class UserRightGroupAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			return mRightLists.size();
		}

		@Override
		public HashMap<String, String> getItem(int position) {
			return mRightLists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Drawable drawable= getResources().getDrawable(R.drawable.view_userright_item_bg);
			convertView = inflate(mContext, R.layout.view_userright_item, null);
			TextView view = (TextView)convertView.findViewById(R.id.view_userright_right_item);
			view.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null);
			view.setText(mRightLists.get(position).get(EnumList.UserRight.RightName));
			return convertView;
		}
		
	}
	
}
