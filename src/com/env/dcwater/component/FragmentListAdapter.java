package com.env.dcwater.component;

import java.util.HashMap;
import java.util.List;

import com.env.dcwater.R;
import com.env.dcwater.activity.MainActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 左边滑出的listview的界面和数据
 * @author Administrator
 *
 */
public class FragmentListAdapter extends BaseAdapter {

	private int mPos;
	private List<HashMap<String, String>> mdata;
	private LayoutInflater mInflater;
	
	/**
	 * 初始化listview
	 * @param fragmentID framgment的位置
	 * @param data 数据
	 */
	public FragmentListAdapter(Context context,int fragmentPos,List<HashMap<String, String>> data){
		mPos = fragmentPos;
		mdata = data;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mdata.size();
	}

	@Override
	public HashMap<String, String> getItem(int position) {
		return mdata.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.fragment_list_adapter, null);
		TextView tvName = (TextView)convertView.findViewById(R.id.fragment_list_adapter_name);
		String name = null;
		switch (mPos) {
		case MainActivity.MACHINEMANAGE:
			name = mdata.get(position).get("MachineName");
			break;
		case MainActivity.CONSMANAGE:
			name = "";
			break;
		case MainActivity.MACHINEINFO:
			name = "";
			break;
		case MainActivity.PLANMANAGE:
			name = "";
			break;
		}
		tvName.setText(name);
		return convertView;
	}
	

}
