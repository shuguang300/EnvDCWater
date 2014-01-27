package com.env.dcwater.component;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 单例模式，存储系统变量
 * 比如是否登录
 * @author sk
 *
 */
public class SystemParams {
	
	public static final String STANDARDTIME_PATTERN_STRING = "yyyy-MM-dd HH:mm:ss";
	
	private static SystemParams Instance = new SystemParams();

	public static SystemParams getInstance() {
		if (Instance == null) {
			Instance = new SystemParams();
		}
		return Instance;
	}

	private SystemParams() {}
	
	
	private ArrayList<HashMap<String, String>> mMachineList;
	/**
	 * @return the mMachineList
	 */
	public ArrayList<HashMap<String, String>> getMachineList() {
		return mMachineList;
	}
	/**
	 * @param mMachineList the mMachineList to set
	 */
	public void setmMachineList(ArrayList<HashMap<String, String>> machineList) {
		mMachineList = machineList;
	}
	
}
