package com.env.dcwater.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class ListviewItemAdapter extends BaseAdapter{
	
	private ArrayList<HashMap<String, String>> mData;

	public ListviewItemAdapter (ArrayList<HashMap<String, String>> data){
		mData = data;
	}
	
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
	public abstract View getView(int position, View convertView, ViewGroup parent);
	
	public class ViewHolder{
		public TextView lefttop = null;
		public TextView righttop = null;
		public TextView leftbottom = null;
		public ImageView pic = null;
		public ImageView arrow = null;
	}

}
