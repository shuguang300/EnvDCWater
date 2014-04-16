package com.env.dcwater.component;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 单例模式，存储系统变量 比如是否登录
 * 
 * @author sk
 * 
 */
public class SystemParams {
	public static final String TAG_STRING = "SystemParams";
	public static final int PLANTID_INT = 1;
	public static final String STANDARDTIME_PATTERN_STRING = "yyyy-MM-dd HH:mm:ss";
	public static final String SHORTDATE_PATTERN_STRING = "yyyy-MM-dd";

	private static SystemParams Instance = new SystemParams();

	public static SystemParams getInstance() {
		if (Instance == null) {
			Instance = new SystemParams();
		}
		return Instance;
	}

	private SystemParams() {
	}

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
	public void setMachineList(ArrayList<HashMap<String, String>> machineList) {
		mMachineList = machineList;
	}

	private HashMap<String, String> mLoggedUserInfo;

	/**
	 * @return the mLoggedUserInfo
	 */
	public HashMap<String, String> getLoggedUserInfo() {
		return mLoggedUserInfo;
	}

	/**
	 * @param mLoggedUserInfo
	 *            the mLoggedUserInfo to set
	 */
	public void setLoggedUserInfo(HashMap<String, String> LoggedUserInfo) {
		mLoggedUserInfo = LoggedUserInfo;
	}
	
	private ArrayList<HashMap<String, String>> mConstructionList;

	/**
	 * @return the constructionList
	 */
	public ArrayList<HashMap<String, String>> getConstructionList() {
		return mConstructionList;
	}

	/**
	 * @param constructionList the constructionList to set
	 */
	public void setConstructionList(ArrayList<HashMap<String, String>> constructionList) {
		mConstructionList = constructionList;
	}

}
