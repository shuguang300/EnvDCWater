package com.env.dcwater.fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.env.dcwater.R;
import com.env.dcwater.activity.MainActivity;
import com.env.dcwater.javabean.EnumList.UserRight;
import com.env.dcwater.util.SystemMethod;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class NaviBarAdapter extends BaseAdapter implements OnItemClickListener,PullToRefreshAdapterInterface{

	private ArrayList<HashMap<String, String>> mUserRightData;
	private Context mContext;
	private String mAction;
	
	public NaviBarAdapter (Context context,ArrayList<HashMap<String, String>> userRightData,String action){
		mUserRightData = userRightData;
		mContext = context;
		mAction = action;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> void datasetNotification(List<T> data){
		mUserRightData = (ArrayList<HashMap<String, String>>)data;
		notifyDataSetChanged();
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

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_mainnavibar, null);
		}
		HashMap<String, String> map = mUserRightData.get(position);
		TextView name = (TextView)convertView.findViewById(R.id.item_mainnavibar_name);
		TextView count = (TextView)convertView.findViewById(R.id.item_mainnavibar_taskcount);
		name.setText(map.get(UserRight.RightName));
		name.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(Integer.valueOf(map.get(UserRight.RightResourceID))), null, null, null);
		count.setText(map.get(UserRight.RightTaskCount));
		if(map.get(UserRight.RightTaskCount).length()>0){
			count.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_item_notification));
		}else {
			count.setBackgroundDrawable(null);
		}
		return convertView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(position==mUserRightData.size()){
			SystemMethod.logOut(mContext);
		}else if(mUserRightData.get(position-1).get(UserRight.RightAction).equals(mAction)){
			doNothing();
		}else {
//			if(mUserRightData.get(position).get(UserRight.RightName).equals(UserRight.REPAIRMANAGE.getName())){
//				mUserRightData.get(position).put(UserRight.RightTaskCount, "");
//			}else if(mUserRightData.get(position).get(UserRight.RightName).equals(UserRight.UPKEEPAPPROVE.getName())){
//				mUserRightData.get(position).put(UserRight.RightTaskCount, "");
//			}else if (mUserRightData.get(position).get(UserRight.RightName).equals(UserRight.UPKEEPREPORT.getName())) {
//				mUserRightData.get(position).put(UserRight.RightTaskCount, "");
//			}else if (mUserRightData.get(position).get(UserRight.RightName).equals(UserRight.UPKEEPSEND.getName())) {
//				mUserRightData.get(position).put(UserRight.RightTaskCount, "");
//			}
			Intent intent = new Intent(mUserRightData.get(position-1).get(UserRight.RightAction));
			intent.putExtra("action", MainActivity.ACTION_STRING);
			mContext.startActivity(intent);
		}
	}
	
	public abstract void doNothing();
}
