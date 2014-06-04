package com.env.dcwater.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import com.env.dcwater.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class ListviewItemAdapter extends BaseAdapter implements PullToRefreshAdapter{
	
	private ArrayList<HashMap<String, String>> mData;
	private Context mContext;
	
	public  class ViewHolder{
		public TextView lefttop = null;
		public TextView righttop = null;
		public TextView leftbottom = null;
		public ImageView pic = null;
		public ImageView arrow = null;
	}

	public ListviewItemAdapter (ArrayList<HashMap<String, String>> data,Context context){
		mData = data;
		mContext = context;
	}
	
	@Override
	public void datasetNotification(ArrayList<HashMap<String, String>> data){
		mData = data;
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
	public  View getView(int position, View convertView, ViewGroup parent){
		final HashMap<String, String> map = getItem(position);
		ViewHolder viewHolder ;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listview, null);
			viewHolder.lefttop = (TextView) convertView.findViewById(R.id.item_listview_lefttop);
			viewHolder.righttop = (TextView) convertView.findViewById(R.id.item_listview_righttop);
			viewHolder.leftbottom = (TextView) convertView.findViewById(R.id.item_listview_leftbottom);
			viewHolder.pic = (ImageView) convertView.findViewById(R.id.item_listview_pic);
			viewHolder.arrow = (ImageView) convertView.findViewById(R.id.item_listview_rightbottom);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		setData(viewHolder, map);
		return convertView;
	}
	
	public abstract void setData(ViewHolder viewHolder,HashMap<String, String> map);

}
