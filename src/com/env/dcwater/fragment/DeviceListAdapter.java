package com.env.dcwater.fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.env.dcwater.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class DeviceListAdapter extends BaseAdapter implements PullToRefreshAdapterInterface{
	private ArrayList<HashMap<String, String>> mData;
	private Context mContext;
	public class ViewHodler{
		public TextView name = null;
		public TextView cons = null;
		public TextView time = null;
		public ImageView pic = null;
	}
	
	public DeviceListAdapter (ArrayList<HashMap<String, String>> data,Context context){
		mData = data;
		mContext = context;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> void datasetNotification(List<T> data){
		mData = (ArrayList<HashMap<String, String>>)data;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public HashMap<String, String> getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHodler viewHolder ;
		final HashMap<String, String> map = mData.get(position);
		if(convertView ==null){
			viewHolder = new ViewHodler();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_devicelist, null);
			viewHolder.name =(TextView)convertView.findViewById(R.id.item_devicelist_name);
			viewHolder.cons =(TextView)convertView.findViewById(R.id.item_devicelist_cons);
			viewHolder.time =(TextView)convertView.findViewById(R.id.item_devicelist_time);
			viewHolder.pic = (ImageView)convertView.findViewById(R.id.item_devicelist_pic);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHodler)convertView.getTag();
		}
		setData(viewHolder, map);
		return convertView;
	}
	
	public abstract void setData(ViewHodler viewHodler, HashMap<String, String> map);
	
	
}
