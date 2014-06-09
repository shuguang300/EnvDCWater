package com.env.dcwater.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * 维修流程的工作流
 * @author sk
 *
 */
public class RepairWorkFlowView extends View{
	
	private ArrayList<HashMap<String, String>> data;
	
	/**
	 * @return the data
	 */
	public ArrayList<HashMap<String, String>> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(ArrayList<HashMap<String, String>> data) {
		this.data = data;
	}

	public RepairWorkFlowView (Context context){
		super(context);
	}
	
	public RepairWorkFlowView (Context context,ArrayList<HashMap<String, String>> flowData){
		super(context);
		data = flowData;
	}

	public RepairWorkFlowView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
}
