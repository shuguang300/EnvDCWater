package com.env.dcwater.fragment;
import java.util.ArrayList;
import java.util.HashMap;
import com.env.dcwater.R;
import com.env.dcwater.javabean.EnumList.UserRight;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NaviBarAdapter extends BaseAdapter{

	private ArrayList<HashMap<String, String>> mUserRightData;
	private Context mContext;
	
	public NaviBarAdapter (Context context,ArrayList<HashMap<String, String>> userRightData){
		mUserRightData = userRightData;
		mContext = context;
	}

	@Override
	public int getCount() {
		return mUserRightData.size();
	}

	@Override
	public Object getItem(int position) {
		return mUserRightData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_mainnavibar, null);
		}
		TextView name = (TextView)convertView.findViewById(R.id.item_mainnavibar_name);
		name.setText(mUserRightData.get(position).get(UserRight.RightName));
		return convertView;
	}

}
