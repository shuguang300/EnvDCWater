package com.env.dcwater.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;

import com.env.dcwater.javabean.EnumList;

/**
 * 一个用于存储 污水厂业务逻辑方法 的类
 * @author sk
 */
public class OperationMethod {
	
	/**
	 * 根据用户的userroleid来动态生成权限界面
	 * @param PositionID
	 * @return
	 */
	public static ArrayList<HashMap<String,String>> getViewByUserRole ( int PositionID ){
		ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		if(PositionID == EnumList.UserRole.MACHINEOPERATION.getState()){
			map.put(EnumList.UserRight.RightName, EnumList.UserRight.REPAIRMANAGE.getName());
			map.put(EnumList.UserRight.RightCode, EnumList.UserRight.REPAIRMANAGE.getCode()+"");
			data.add(map);
			
			map = new HashMap<String, String>();
			map.put(EnumList.UserRight.RightName, EnumList.UserRight.MAINTAINHISTORY.getName());
			map.put(EnumList.UserRight.RightCode, EnumList.UserRight.MAINTAINHISTORY.getCode()+"");
			data.add(map);
			
			map = new HashMap<String, String>();
			map.put(EnumList.UserRight.RightName, EnumList.UserRight.UPKEEPHISTORY.getName());
			map.put(EnumList.UserRight.RightCode, EnumList.UserRight.UPKEEPHISTORY.getCode()+"");
			data.add(map);
		}
		return data;
		
	}
	
	/**
	 * 根据当前用户的角色id以及工单的状态，判断该角色是否有权利对工单进行修改
	 * @param rolePositionID
	 * @param taskState
	 * @return
	 */
	public static boolean CanTaskUpdated(int rolePositionID,int taskState){
		boolean arg = false;
		switch (rolePositionID) {
		case EnumList.UserRole.USERROLEMACHINEOPERATION:
			if (taskState<1) {
				arg = true;
			}
			break;
		}
		return arg;
	}
	
}
