package com.env.dcwater.fragment;

import com.env.dcwater.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ConsManage extends Fragment{
	
	private View view;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_consmanage, container, false);
		return view;
	}
}
