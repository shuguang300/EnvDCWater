package com.env.dcwater.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.env.dcwater.javabean.EnumList;
import com.google.gson.JsonArray;

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
		if(PositionID == EnumList.UserRole.EQUIPMENTOPERATION.getState()){
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
	public static boolean canTaskUpdated(int rolePositionID,int taskState){
		boolean arg = false;
		switch (rolePositionID) {
		case EnumList.UserRole.USERROLEEQUIPMENTOPERATION:
			if (taskState<1) {
				arg = true;
			}
			break;
		}
		return arg;
	}
	
	/**
	 * 根据获得的userrole返回一个userrole的id
	 * @param userRole
	 * @return
	 */
	public static String getUserRole(String userRole){
		ArrayList<String> data;
		String userRoleString = "0";
		try {
			data = new ArrayList<String>();
			JSONArray jsonArray = new JSONArray(userRole);
			for (int i = 0; i < jsonArray.length(); i++) {
				data.add(jsonArray.getString(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			data = null;
		}
		if(data!=null){
			if (data.contains("ReportRepairJobInput(DepProduct)")) {
				 //UserRole:1为生产科填报权限
				userRoleString = "1";
			}else if (data.contains("ReportRepairJobInput(DepEquipment)")) {
				//userrole:2为设备科填报权限
				userRoleString = "2";
            }else if (data.contains("ReportRepairJobConfirm")) {
            	//UserRole:3为生产科长报修确认权限
            	userRoleString = "3";
            }else if (data.contains("RepairJobConfirm(ClassChief)")) {
            	//UserRole:4为生产科长维修确认权限
            	userRoleString = "4";
            }else if (data.contains("RepairJobSend")) {
            	//UserRole:5为设备科长派发工单权限
            	userRoleString = "5";
            }else if (data.contains("RepairJobCheck")) {
            	//UserRole:6为设备科长审核维修
            	userRoleString = "6";
            }else if (data.contains("RepairJobInput")) {
            	//UserRole:7为维修权限
            	userRoleString = "7";
            }else if (data.contains("RepairJobConfirm(ClassDirector)")) {
            	//UserRole:8为厂长确认权限
            	userRoleString = "8";
            }
		}
		return userRoleString;
	}
	
}
