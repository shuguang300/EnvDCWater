package com.env.dcwater.fragment;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.env.dcwater.R;
import com.env.dcwater.javabean.ClassTaskWorkFlow;

public abstract class WorkFlowAdapter extends BaseAdapter implements PullToRefreshAdapterInterface{
	private ArrayList<ClassTaskWorkFlow> workFlows;
	private Context context;
	public class ViewHolder{
		public TextView left;
		public TextView right;
	}
	public WorkFlowAdapter (Context context,ArrayList<ClassTaskWorkFlow> data){
		this.context = context;
		workFlows = data;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> void datasetNotification(List<T> data) {
		workFlows = (ArrayList<ClassTaskWorkFlow>) data;
		notifyDataSetChanged();
	}
	

	@Override
	public int getCount() {
		return workFlows.size();
	}

	@Override
	public ClassTaskWorkFlow getItem(int position) {
		return workFlows.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder ;
		ClassTaskWorkFlow data = workFlows.get(position);
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_taskstateworkflow, null);
			viewHolder.left = (TextView)convertView.findViewById(R.id.item_taskstateworkflow_left);
			viewHolder.right = (TextView)convertView.findViewById(R.id.item_taskstateworkflow_right);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		setData(viewHolder, data,position);
		return convertView;
	}
	
	public abstract void setData(ViewHolder viewHolder,ClassTaskWorkFlow data,int position);
	
}
