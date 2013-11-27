package com.env.dcwater.fragment;

import com.env.dcwater.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MachineManage extends Fragment implements OnClickListener{
	private View view;
	private TextView valueName,valuePos,valueMode,valueManufacture;
	private Button btMore,btUpdate,btDelete;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_machinemanage,container, false);
		init();
		return view;
	}
	
	private void init(){
		btMore = (Button)view.findViewById(R.id.machinemanage_more);
		btUpdate = (Button)view.findViewById(R.id.machinemanage_update);
		btDelete = (Button)view.findViewById(R.id.machinemanage_delete);
		btMore.setOnClickListener(this);
		btUpdate.setOnClickListener(this);
		btDelete.setOnClickListener(this);
		valueName = (TextView)view.findViewById(R.id.machinemanage_name);
		valuePos = (TextView)view.findViewById(R.id.machinemanage_pos);
		valueMode = (TextView)view.findViewById(R.id.machinemanage_mode);
		valueManufacture = (TextView)view.findViewById(R.id.machinemanage_manufacture);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.machinemanage_more:
			break;
		case R.id.machinemanage_update:
			break;
		case R.id.machinemanage_delete:
			break;
		}
	}
	
	
}
