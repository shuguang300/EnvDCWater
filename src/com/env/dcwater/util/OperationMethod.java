package com.env.dcwater.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.env.dcwater.javabean.EnumList;

/**
 * 一个用于存储 污水厂业务逻辑方法 的类
 * @author sk
 */
public class OperationMethod {
	public static final String TAG_STRING = "OperationMethod";
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
			
		}else if (PositionID == EnumList.UserRole.PRODUCTIONOPERATION.getState()) {
			map.put(EnumList.UserRight.RightName, EnumList.UserRight.MACHINEINFO.getName());
			map.put(EnumList.UserRight.RightCode, EnumList.UserRight.MACHINEINFO.getCode()+"");
			data.add(map);
			
			map = new HashMap<String, String>();
			map.put(EnumList.UserRight.RightName, EnumList.UserRight.REPAIRMANAGE.getName());
			map.put(EnumList.UserRight.RightCode, EnumList.UserRight.REPAIRMANAGE.getCode()+"");
			data.add(map);
		}else if (PositionID == EnumList.UserRole.EQUIPMENTCHIEF.getState()){
			map.put(EnumList.UserRight.RightName, EnumList.UserRight.MACHINEINFO.getName());
			map.put(EnumList.UserRight.RightCode, EnumList.UserRight.MACHINEINFO.getCode()+"");
			data.add(map);
			
			map = new HashMap<String, String>();
			map.put(EnumList.UserRight.RightName, EnumList.UserRight.REPAIRMANAGE.getName());
			map.put(EnumList.UserRight.RightCode, EnumList.UserRight.REPAIRMANAGE.getCode()+"");
			data.add(map);
			
			
		}
		
		map = new HashMap<String, String>();
		map.put(EnumList.UserRight.RightName, EnumList.UserRight.MAINTAINHISTORY.getName());
		map.put(EnumList.UserRight.RightCode, EnumList.UserRight.MAINTAINHISTORY.getCode()+"");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put(EnumList.UserRight.RightName, EnumList.UserRight.UPKEEPHISTORY.getName());
		map.put(EnumList.UserRight.RightCode, EnumList.UserRight.UPKEEPHISTORY.getCode()+"");
		data.add(map);
		return data;
		
	}
	
	/**
	 * 根据维修单状态的名称获得状态代码
	 * @param stateName
	 * @return
	 */
	public static int getTaskStateByStateName(String stateName){
		if(stateName.equals(EnumList.RepairState.HASBEENREPORTED.getStateDescription())){
			return EnumList.RepairState.HASBEENREPORTED.getState();
		}else if (stateName.equals(EnumList.RepairState.HASBEENCONFIRMED.getStateDescription())) {
			return EnumList.RepairState.HASBEENCONFIRMED.getState();
		}else if(stateName.equals(EnumList.RepairState.HASBEENDISTRIBUTED.getStateDescription())){
			return EnumList.RepairState.HASBEENDISTRIBUTED.getState();
		}else if(stateName.equals(EnumList.RepairState.BEENINGREPAIRED.getStateDescription())){
			return EnumList.RepairState.BEENINGREPAIRED.getState();
		}else if(stateName.equals(EnumList.RepairState.HASBEENREPAIRED.getStateDescription())){
			return EnumList.RepairState.HASBEENREPAIRED.getState();
		}else if(stateName.equals(EnumList.RepairState.FORCORRECTION.getStateDescription())){
			return EnumList.RepairState.FORCORRECTION.getState();
		}else if(stateName.equals(EnumList.RepairState.DEVICETHROUGH.getStateDescription())){
			return EnumList.RepairState.DEVICETHROUGH.getState();
		}else if(stateName.equals(EnumList.RepairState.PRODUCTIONTHROUGH.getStateDescription())){
			return EnumList.RepairState.PRODUCTIONTHROUGH.getState();
		}else if(stateName.equals(EnumList.RepairState.DIRECTORTHROUGH.getStateDescription())){
			return EnumList.RepairState.DIRECTORTHROUGH.getState();
		}else {
			return -1;
		}
		
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
			if (taskState==EnumList.RepairState.HASBEENREPORTED.getState()) {
				arg = true;
			}
			break;
		case EnumList.UserRole.USERROLEPRODUCTIONOPERATION:
			if(taskState==EnumList.RepairState.HASBEENREPORTED.getState()){
				arg = true;
			}
			break;
		case EnumList.UserRole.USERROLEEQUIPMENTCHIEF:
			if(taskState==EnumList.RepairState.HASBEENREPORTED.getState()||
			taskState==EnumList.RepairState.HASBEENDISTRIBUTED.getState()||
			taskState==EnumList.RepairState.HASBEENREPAIRED.getState()||
			taskState==EnumList.RepairState.HASBEENCONFIRMED.getState()){
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
	
	
	public static ArrayList<HashMap<String, String>> parseDeviceListToArray(JSONObject jsonObject) throws JSONException{
		JSONArray jsonArray = new JSONArray(jsonObject.getString("d").toString());
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		JSONObject device = null;
		HashMap<String, String> map = null;
		for(int i =0;i<jsonArray.length();i++){
			device = jsonArray.getJSONObject(i);
			map = new HashMap<String, String>();
			
			map.put("DeviceID", device.get("DeviceID").toString());
			
			map.put("DeviceSN", device.get("DeviceSN").toString());
			
			map.put("DeviceName", device.get("DeviceName").toString());
			
			map.put("PlantID", device.get("PlantID").toString());
			
			map.put("Department", LogicMethod.getRightString(device.get("Department").toString()));
			
			map.put("FixedAssets", LogicMethod.getRightString(device.get("FixedAssets").toString()));
			
			map.put("InstallPosition", LogicMethod.getRightString(device.get("InstallPosition").toString()));
			
			map.put("TechnicalParameter", LogicMethod.getRightString(device.get("TechnicalParameter").toString()));
			
			map.put("Manufacturer", LogicMethod.getRightString(device.get("Manufacturer").toString()));
			
			map.put("Price", LogicMethod.getRightString(device.get("Price").toString()));
			
			map.put("FilingTime", LogicMethod.getRightString(device.get("FilingTime").toString().replace("T", " ")));
			
			map.put("InstallTime", LogicMethod.getRightString(device.get("InstallTime").toString().replace("T", " ")));
			
			map.put("StartUseTime", LogicMethod.getRightString(device.get("StartUseTime").toString().replace("T", " ")));
			
			map.put("StopUseTime", LogicMethod.getRightString(device.get("StopUseTime").toString().replace("T", " ")));
			
			map.put("ScrapTime", LogicMethod.getRightString(device.get("ScrapTime").toString().replace("T", " ")));
			
			map.put("DepreciationPeriod", LogicMethod.getRightString(device.get("DepreciationPeriod").toString()));
			
			map.put("Quality", LogicMethod.getRightString(device.get("Quality").toString()));
			
			map.put("ReMark", LogicMethod.getRightString(device.get("ReMark").toString()));
			
			map.put("StandardNorOperation", LogicMethod.getRightString(device.get("StandardNorOperation").toString()));
			
			map.put("OperatManagAndOperatPoint", LogicMethod.getRightString(device.get("OperatManagAndOperatPoint").toString()));
			
			map.put("ComProbAndSolutions", LogicMethod.getRightString(device.get("ComProbAndSolutions").toString()));
			
			map.put("DeviceClassType", LogicMethod.getRightString(device.get("DeviceClassType").toString()));
			
			map.put("AccessoryInfo", LogicMethod.getRightString(device.get("AccessoryInfo").toString()));
			
			data.add(map);
		}
		return data;
	}
	
	/**
	 * 将得到的维修任务的json字符串解析为arraylist
	 * @param rolePosition 
	 * @param jsonObject
	 * @param taskState
	 * @param isFilter
	 * @return
	 * @throws JSONException
	 */
	public static ArrayList<HashMap<String, String>> parseRepairTaskToArray(int rolePosition,JSONObject jsonObject,int taskState,boolean isFilter) throws JSONException{
		JSONArray jsonArray = new JSONArray(jsonObject.getString("d").toString());
		JSONObject report = null;
		HashMap<String, String> map = null;
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		boolean canUpdate = false;
		for(int i =0;i<jsonArray.length();i++){
			report = jsonArray.getJSONObject(i);
			map = new HashMap<String, String>();
			canUpdate = canTaskUpdated(rolePosition, Integer.valueOf(report.get("State").toString()));
			if(isFilter&&!canUpdate){
				continue;
			}
			if(taskState!=-1&&taskState!=Integer.valueOf(report.get("State").toString())){
				continue;
			}
			//是否能够编辑
			map.put("CanUpdate", canUpdate?"true":"false");
			//保修单的ID
			map.put("RepairTaskID", report.get("RepairTaskID").toString());
			//厂名
			map.put("PlantID", report.get("PlantID").toString());
			//维修措施（紧急措施和其他措施）
			map.put("EmergencyMeasures",LogicMethod.getRightString(report.get("EmergencyMeasures").toString()));
			//设备安装位置
			map.put("InstallPosition", LogicMethod.getRightString(report.get("InstallPosition").toString()));
			//设备安装位置ID
			map.put("InstallPositionID", LogicMethod.getRightString(report.get("InstallPositionID").toString()));
			//故障信息
			map.put("AccidentDetail",LogicMethod.getRightString(report.get("AccidentDetail").toString()));
			//故障发生时间
			map.put("AccidentOccurTime", report.get("AccidentOccurTime").toString().replace("T", " "));
			//维修时间
			map.put("RepairedTime", LogicMethod.getRightString(report.get("RepairedTime").toString().replace("T", " ")));
			//故障单填报人ID	
			map.put("ReportPersonID", report.get("ReportPersonID").toString());
			//故障单填报人名
			map.put("ReportPersonRealName", report.get("ReportPersonRealName").toString());
			//故障单接收人ID
			map.put("ReceiverID", LogicMethod.getRightString(report.get("ReceiverID").toString()));
			//故障单接收人名
			map.put("ReceiverRealName", LogicMethod.getRightString(report.get("ReceiverRealName").toString()));
			//故障维修人ID
			map.put("RepairPersonID", LogicMethod.getRightString(report.get("RepairPersonID").toString()));
			//故障维修人名
			map.put("RepairPersonRealName", LogicMethod.getRightString(report.get("RepairPersonRealName").toString()));
			//所需工时
			map.put("RequiredManHours", LogicMethod.getRightString(report.get("RequiredManHours").toString()));
			//维修内容及要求
			map.put("TaskDetail",LogicMethod.getRightString(report.get("TaskDetail").toString()));
			//维修结果
			map.put("RepairDetail", LogicMethod.getRightString(report.get("RepairDetail").toString()));
			//维修物品消耗
			map.put("AccessoryUsed", LogicMethod.getRightString(report.get("AccessoryUsed").toString()));
			//金钱消耗
			map.put("RepairCost", LogicMethod.getRightString(report.get("RepairCost").toString()));
			//审核结果
			map.put("ApproveResult", LogicMethod.getRightString(report.get("ApproveResult").toString()));
			//审核人ID
			map.put("ApprovePersonID", LogicMethod.getRightString(report.get("ApprovePersonID").toString()));
			//审核人名
			map.put("ApprovePersonRealName", LogicMethod.getRightString(report.get("ApprovePersonID").toString()));
			//设备科意见
			map.put("DDOpinion", LogicMethod.getRightString(report.get("DDOpinion").toString()));
			//生产科意见
			map.put("PDOpinion", LogicMethod.getRightString(report.get("PDOpinion").toString()));
			//厂长意见
			map.put("PMOpinion", LogicMethod.getRightString(report.get("PMOpinion").toString()));
			//工单派发时间
			map.put("TaskCreateTime", LogicMethod.getRightString(report.get("TaskCreateTime").toString().replace("T", " ")));
			//工单派发人ID
			map.put("CreatePersonID", LogicMethod.getRightString(report.get("CreatePersonID").toString()));
			//工单派发人名
			map.put("CreatePersonRealName", LogicMethod.getRightString(report.get("CreatePersonRealName").toString()));
			//工单状态ID
			map.put("State", LogicMethod.getRightString(report.get("State").toString()));
			//工单状态描述
			map.put("StateDescription", EnumList.RepairState.getEnumRepairState(Integer.valueOf(report.get("State").toString())).getStateDescription());
			//设备ID
			map.put("DeviceID", report.get("DeviceID").toString());
			//设备型号
			map.put("Specification", LogicMethod.getRightString(report.get("Specification").toString()));
			//设备编码
			map.put("DeviceSN", LogicMethod.getRightString(report.get("DeviceSN").toString()));
			//开始使用时间
			map.put("StartUseTime",LogicMethod.getRightString(report.get("StartUseTime").toString().toString().replace("T", " ")));
			//生产产商
			map.put("Manufacturer", LogicMethod.getRightString(report.get("Manufacturer").toString()));
			//设备名称
			map.put("DeviceName", LogicMethod.getRightString(report.get("DeviceName").toString()));
			//工单编码
			map.put("FaultReportSN", report.get("FaultReportSN").toString());
			//使用部门
			map.put("Department", LogicMethod.getRightString(report.get("Department").toString()));
			//设备类型
			map.put("DeviceClassType", LogicMethod.getRightString(report.get("DeviceClassType").toString()));
			
			data.add(map);
		}
		return data;
	}
	
	/**
	 * 将得到的用户信息的jsonObject解析成为hashmap
	 * @param jsonObject
	 * @param mAccount
	 * @param mPassword
	 * @return
	 */
	public static HashMap<String, String> parseSoapObject (JSONObject jsonObject,String mAccount,String mPassword){
		HashMap<String, String> map = null;
		try {
			map = new HashMap<String, String>();
			map.put("UserName", mAccount);
			map.put("UserPassword", mPassword);
			map.put("UserRole", getUserRole(jsonObject.getString("UserRole")));
			map.put("UserID", jsonObject.getString("UserID"));
			map.put("PlantID", jsonObject.getString("PlantID"));
			map.put("PlantName", jsonObject.getString("PlantName"));
			map.put("PlantType", jsonObject.getString("PlantType"));
			map.put("RealUserName", jsonObject.getString("RealUserName"));
			map.put("AccountState", jsonObject.getString("AccountState"));
			map.put("PositionID", jsonObject.getString("PositionID"));
			map.put("PositionName", jsonObject.getString("PositionName"));
		} catch (Exception e) {
			map = null;
		} finally{
			
		}
		return map;
		
	}
}
