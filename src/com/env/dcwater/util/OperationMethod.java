package com.env.dcwater.util;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.env.dcwater.R;
import com.env.dcwater.javabean.EnumList;
import com.env.dcwater.javabean.EnumList.UpkeepHistoryPlanState;
import com.env.dcwater.javabean.EnumList.UpkeepHistoryState;
import com.env.dcwater.javabean.EnumList.UserRight;
import com.env.dcwater.javabean.EnumList.UserRole;

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
		
		map.put(UserRight.RightName, UserRight.USERINFORMATION.getName());
		map.put(UserRight.RightCode, UserRight.USERINFORMATION.getCode()+"");
		map.put(UserRight.RightAction, UserRight.USERINFORMATION.getAction());		
		map.put(UserRight.RightResourceID, UserRight.USERINFORMATION.getResourceID()+"");		
		map.put(UserRight.RightTaskCount, "");		
		data.add(map);
		
		
		
		if(PositionID == UserRole.EQUIPMENTOPERATION.getState()){
			
			
		}else {
			//设备信息
			map = new HashMap<String, String>();
			map.put(UserRight.RightName, UserRight.MACHINEINFO.getName());
			map.put(UserRight.RightCode, UserRight.MACHINEINFO.getCode()+"");
			map.put(UserRight.RightAction, UserRight.MACHINEINFO.getAction());
			map.put(UserRight.RightResourceID, UserRight.MACHINEINFO.getResourceID()+"");
			map.put(UserRight.RightTaskCount, "");
			data.add(map);
			
			if (PositionID == UserRole.PRODUCTIONOPERATION.getState()) {
				
			}else if (PositionID == UserRole.EQUIPMENTCHIEF.getState()){
				//发送保养工单
				map = new HashMap<String, String>();
				map.put(UserRight.RightName, UserRight.UPKEEPSEND.getName());
				map.put(UserRight.RightCode, UserRight.UPKEEPSEND.getCode()+"");
				map.put(UserRight.RightAction, UserRight.UPKEEPSEND.getAction());
				map.put(UserRight.RightResourceID, UserRight.UPKEEPSEND.getResourceID()+"");
				map.put(UserRight.RightTaskCount, "");
				data.add(map);
				//审核保养工单
				map = new HashMap<String, String>();
				map.put(UserRight.RightName, UserRight.UPKEEPAPPROVE.getName());
				map.put(UserRight.RightCode, UserRight.UPKEEPAPPROVE.getCode()+"");
				map.put(UserRight.RightAction, UserRight.UPKEEPAPPROVE.getAction());
				map.put(UserRight.RightResourceID, UserRight.UPKEEPAPPROVE.getResourceID()+"");
				map.put(UserRight.RightTaskCount, "");
				data.add(map);
				
			}else if (PositionID == UserRole.PRODUCTIONCHIEF.getState()) {
				
			}else if (PositionID == UserRole.REPAIRMAN.getState()) {
				//填写保养工单
				map = new HashMap<String, String>();
				map.put(UserRight.RightName, UserRight.UPKEEPREPORT.getName());
				map.put(UserRight.RightCode, UserRight.UPKEEPREPORT.getCode()+"");
				map.put(UserRight.RightAction, UserRight.UPKEEPREPORT.getAction());
				map.put(UserRight.RightResourceID, UserRight.UPKEEPREPORT.getResourceID()+"");
				map.put(UserRight.RightTaskCount, "");
				data.add(map);
				
			}else if (PositionID == UserRole.PLANTER.getState()) {
				
			}
		}
		
		//维修管理
		map = new HashMap<String, String>();
		map.put(UserRight.RightName, UserRight.REPAIRMANAGE.getName());
		map.put(UserRight.RightCode, UserRight.REPAIRMANAGE.getCode()+"");
		map.put(UserRight.RightAction, UserRight.REPAIRMANAGE.getAction());
		map.put(UserRight.RightResourceID, UserRight.REPAIRMANAGE.getResourceID()+"");
		map.put(UserRight.RightTaskCount, "");
		data.add(map);
		//维修记录
		map = new HashMap<String, String>();
		map.put(UserRight.RightName, UserRight.MAINTAINHISTORY.getName());
		map.put(UserRight.RightCode, UserRight.MAINTAINHISTORY.getCode()+"");
		map.put(UserRight.RightAction, UserRight.MAINTAINHISTORY.getAction());
		map.put(UserRight.RightResourceID, UserRight.MAINTAINHISTORY.getResourceID()+"");
		map.put(UserRight.RightTaskCount, "");
		data.add(map);
		//养护记录
		map = new HashMap<String, String>();
		map.put(UserRight.RightName, UserRight.UPKEEPHISTORY.getName());
		map.put(UserRight.RightCode, UserRight.UPKEEPHISTORY.getCode()+"");
		map.put(UserRight.RightAction, UserRight.UPKEEPHISTORY.getAction());
		map.put(UserRight.RightResourceID, UserRight.UPKEEPHISTORY.getResourceID()+"");
		map.put(UserRight.RightTaskCount, "");
		data.add(map);
		//用户设置
		map = new HashMap<String, String>();
		map.put(UserRight.RightName, UserRight.USERCONFIG.getName());
		map.put(UserRight.RightCode, UserRight.USERCONFIG.getCode()+"");
		map.put(UserRight.RightAction, UserRight.USERCONFIG.getAction());
		map.put(UserRight.RightResourceID, UserRight.USERCONFIG.getResourceID()+"");
		map.put(UserRight.RightTaskCount, "");
		data.add(map);
		//注销
		map = new HashMap<String, String>();
		map.put(UserRight.RightName, "注销");
		map.put(UserRight.RightCode, "");
		map.put(UserRight.RightAction, "");
		map.put(UserRight.RightResourceID, R.drawable.ic_act_logout+"");
		map.put(UserRight.RightTaskCount, "");
		data.add(map);
		return data;
		
	}
	
	/**
	 * 添加用户任务数量
	 * @param data
	 * @param taskcountObject
	 * @return
	 * @throws JSONException 
	 */
	public static ArrayList<HashMap<String, String>> addUserTaskCountInfor(ArrayList<HashMap<String, String>> data, JSONObject taskcountObject) {
		try {
			HashMap<String, String> map = null;
			int repair = LogicMethod.getRightInt(taskcountObject.getString("RepairTaskCount"));
			int send = LogicMethod.getRightInt(taskcountObject.getString("MaintainTaskNeedSendCount"));
			int approve = LogicMethod.getRightInt(taskcountObject.getString("MaintainTaskApproveCount"));
			int report = LogicMethod.getRightInt(taskcountObject.getString("MaintainTaskReceiptCount"));
			for(int i =0;i<data.size();i++){
				map = data.get(i);
				if(map.get(UserRight.RightName).equals(UserRight.REPAIRMANAGE.getName())){
					map.put(UserRight.RightTaskCount, repair>0?repair+"":"");
					continue;
				}else if(map.get(UserRight.RightName).equals(UserRight.UPKEEPAPPROVE.getName())){
					map.put(UserRight.RightTaskCount, approve>0?approve+"":"");
					continue;
				}else if (map.get(UserRight.RightName).equals(UserRight.UPKEEPREPORT.getName())) {
					map.put(UserRight.RightTaskCount, report>0?report+"":"");
					continue;
				}else if (map.get(UserRight.RightName).equals(UserRight.UPKEEPSEND.getName())) {
					map.put(UserRight.RightTaskCount, send>0?send+"":"");
					continue;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
	 * 根据保养单计划状态的名称获得状态代码
	 * @param stateName
	 * @return
	 */
	public static int getUpkeepPlanStateByStateName(String stateName){
		if(stateName.equals("已派发工单")){
			return 1;
		}else if (stateName.equals("已回单")) {
			return 2;
		}else if (stateName.equals("审核通过")) {
			return 3;
		}else if (stateName.equals("审核未通过")) {
			return 4;
		}else if (stateName.equals("已填写待上报")) {
			return 5;
		}else {
			return -1;
		}
	}
	
	/**
	 * 根据保养单状态的名称获得状态代码
	 * @param stateName
	 * @return
	 */
	public static int getUpkeepStateByStateName(String stateName){
		if(stateName.equals("已列入计划")){
			return 1;
		}else if (stateName.equals("养护中")) {
			return 2;
		}else if (stateName.equals("审核未通过")) {
			return 3;
		}else if (stateName.equals("完成")) {
			return 4;
		}else if (stateName.equals("回单待审核")) {
			return 5;
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
	public static boolean canTaskUpdated(int rolePositionID,int taskState,int taskType){
		boolean arg = false;
		switch (rolePositionID) {
		case EnumList.UserRole.USERROLEEQUIPMENTOPERATION:
//			if (taskState==EnumList.RepairState.HASBEENREPORTED.getState()) {
//				arg = true;
//			}
			break;
		case EnumList.UserRole.USERROLEPRODUCTIONOPERATION:
//			if(taskState==EnumList.RepairState.HASBEENREPORTED.getState()){
//				arg = true;
//			}
			break;
		case EnumList.UserRole.USERROLEEQUIPMENTCHIEF:
			switch (taskType) {
			case EnumList.RepairTaskType.TASKTYPE_EQUIPMENT:
				if(taskState==EnumList.RepairState.STATEHASBEENREPORTED||
//				taskState==EnumList.RepairState.STATEHASBEENDISTRIBUTED||
//				taskState==EnumList.RepairState.STATEDEVICETHROUGH||
				taskState==EnumList.RepairState.STATEHASBEENREPAIRED){
					arg = true;
				}
				break;
			case EnumList.RepairTaskType.TASKTYPE_PRODUCTION:
				if(taskState==EnumList.RepairState.STATEHASBEENCONFIRMED||
//				taskState==EnumList.RepairState.STATEHASBEENDISTRIBUTED||
//				taskState==EnumList.RepairState.STATEDEVICETHROUGH||
				taskState==EnumList.RepairState.STATEHASBEENREPAIRED){
					arg = true;
				}
				break;
			}
			break;
		case EnumList.UserRole.USERROLEPRODUCTIONCHIEF:
			switch (taskType) {
			case EnumList.RepairTaskType.TASKTYPE_PRODUCTION:
				if(taskState==EnumList.RepairState.STATEHASBEENREPORTED
				||taskState==EnumList.RepairState.STATEDEVICETHROUGH
//				taskState==EnumList.RepairState.STATEPRODUCTIONTHROUGH
				){
					arg=true;
				}
				break;
			}
			break;
		case EnumList.UserRole.USERROLEREPAIRMAN:
			if(taskState==EnumList.RepairState.STATEHASBEENDISTRIBUTED||
			taskState==EnumList.RepairState.STATEBEENINGREPAIRED||
			taskState==EnumList.RepairState.STATEFORCORRECTION){
				arg = true;
			}
			break;
		case EnumList.UserRole.USERROLEPLANTER:
			switch (taskType) {
			case EnumList.RepairTaskType.TASKTYPE_EQUIPMENT:
				if(taskState==EnumList.RepairState.STATEDEVICETHROUGH)arg=true;
				break;
			case EnumList.RepairTaskType.TASKTYPE_PRODUCTION:
				if(taskState==EnumList.RepairState.STATEPRODUCTIONTHROUGH)arg=true;
				break;
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
	
	/**
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	public static ArrayList<HashMap<String, String>> parseConsDataToList(JSONObject jsonObject) throws JSONException {
		JSONArray jsonArray = new JSONArray(jsonObject.getString("d").toString());
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		JSONObject cons = null;
		HashMap<String, String> map = null;
		for(int i =-1;i<jsonArray.length();i++){
			if(i==-1){
				map = new HashMap<String, String>();
				map.put("StructureName", "全部");
				map.put("StructureID", "全部");
			}else {
				cons = jsonArray.getJSONObject(i);
				map = new HashMap<String, String>();
				map.put("StructureName", cons.getString("StructureName"));
				map.put("StructureID", cons.getInt("StructureID")+"");
			}
			data.add(map);
		}
		return data;
	}
	
	public static HashMap<String, String> parseDeviceDataToHashMap(JSONObject jsonObject) throws JSONException{
		JSONObject device = new JSONObject(jsonObject.getString("d"));
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("DeviceID", device.get("DeviceID").toString());
		
		map.put("DeviceSN", device.get("DeviceSN").toString());
		
		map.put("DeviceName", device.get("DeviceName").toString());
		
		
		map.put("PlantID", device.get("PlantID").toString());
		
		map.put("Department", LogicMethod.getRightString(device.get("Department").toString()));
		
		map.put("FixedAssets", LogicMethod.getRightString(device.get("FixedAssets").toString()));
		
		map.put("InstallPosition", LogicMethod.getRightString(device.get("InstallPosition").toString()));
		
		map.put("InstallPositionforMobile", LogicMethod.getRightString(device.get("InstallPositionforMobile").toString()));
		
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
		
		return map;
	}
	
	/**
	 * @param jsonObject
	 * @param deviceName
	 * @param consName
	 * @return
	 * @throws JSONException
	 */
	public static ArrayList<HashMap<String, String>> parseDeviceDataToList(JSONObject jsonObject ,String deviceName, String consName) throws JSONException{
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		String result = jsonObject.getString("d").toString();
		JSONArray jsonArray;
		if(result.equals("")||result.equals("null")){
			jsonArray = null;
		}else {
			jsonArray = new JSONArray(result);
			JSONObject device = null;
			HashMap<String, String> map = null;
			for(int i =0;i<jsonArray.length();i++){
				device = jsonArray.getJSONObject(i);
				map = new HashMap<String, String>();
				
				//添加过滤条件
				if(!deviceName.equals("")&&!device.get("DeviceName").toString().contains(deviceName)){
					continue;
				}
				if(!consName.equals("全部")&&!consName.equals("")&&!LogicMethod.getRightString(device.get("InstallPosition").toString()).equals(consName)){
					continue;
				}
				
				if(consName.equals("全部")||consName.equals("")){
					//go on
				}else {
					if(consName.equals(LogicMethod.getRightString(device.get("InstallPosition").toString()))){
						//go on
					}else {
						continue;
					}
				}
				
				map.put("DeviceID", device.get("DeviceID").toString());
				
				map.put("DeviceSN", device.get("DeviceSN").toString());
				
				map.put("DeviceName", device.get("DeviceName").toString());
				
				
				map.put("PlantID", device.get("PlantID").toString());
				
				map.put("Department", LogicMethod.getRightString(device.get("Department").toString()));
				
				map.put("FixedAssets", LogicMethod.getRightString(device.get("FixedAssets").toString()));
				
				map.put("InstallPosition", LogicMethod.getRightString(device.get("InstallPosition").toString()));
				
				map.put("InstallPositionforMobile", LogicMethod.getRightString(device.get("InstallPositionforMobile").toString()));
				
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
	public static ArrayList<HashMap<String, String>> parseRepairTaskDataToList(int rolePosition,JSONObject jsonObject,int taskState,String consName,boolean isFilter) throws JSONException{
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		String result = jsonObject.getString("d").toString();
		JSONArray jsonArray;
		if(result.equals("")||result.equals("null")){
			jsonArray = null;
		}else {
			jsonArray = new JSONArray(result);
			JSONObject report = null;
			HashMap<String, String> map = null;
			boolean canUpdate = false;
			int taskType = 0;
			for(int i =0;i<jsonArray.length();i++){
				report = jsonArray.getJSONObject(i);
				map = new HashMap<String, String>();
				try {
					taskType = Integer.valueOf(report.get("RepairTaskType").toString());
				} catch (Exception e) {
					taskType = 0;
				}
				canUpdate = canTaskUpdated(rolePosition, Integer.valueOf(report.get("State").toString()),taskType);
				if(isFilter&&!canUpdate){
					continue;
				}
				if(taskState!=-1&&taskState!=Integer.valueOf(report.get("State").toString())){
					continue;
				}
				if(consName.equals("全部")||consName.equals("")){
					//go on
				}else {
					if(consName.equals(LogicMethod.getRightString(report.get("InstallPosition").toString()))){
						//go on
					}else {
						continue;
					}
				}
				//是否能够编辑
				map.put("CanUpdate", canUpdate?"true":"false");
				//保修单的ID
				map.put("RepairTaskID", report.get("RepairTaskID").toString());
				//维修单类型
				map.put("RepairTaskType", LogicMethod.getRightString(report.get("RepairTaskType").toString()));
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
				map.put("ApprovePersonRealName", LogicMethod.getRightString(report.get("ApprovePersonRealName").toString()));
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
		}
		return data;
	}
	
	
	/**
	 * 将得到的维修 的历史任务json字符串解析为arraylist
	 * @param jsonObject
	 * @param consName
	 * @param rolePosition 
	 * @return
	 * @throws JSONException
	 */
	public static ArrayList<HashMap<String, String>> parseRepairHistoryDataToList(JSONObject jsonObject,String consName) throws JSONException{
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		String result = jsonObject.getString("d").toString();
		JSONArray jsonArray;
		if(result.equals("")||result.equals("null")){
			jsonArray = null;
		}else {
			jsonArray = new JSONArray(result);
			JSONObject report = null;
			HashMap<String, String> map = null;
			for(int i =0;i<jsonArray.length();i++){
				report = jsonArray.getJSONObject(i);
				map = new HashMap<String, String>();
//				try {
//					taskType = Integer.valueOf(report.get("RepairTaskType").toString());
//				} catch (Exception e) {
//					taskType = 0;
//				}
//				
				if(consName.equals("全部")||consName.equals("")){
					//go on
				}else {
					if(consName.equals(LogicMethod.getRightString(report.get("InstallPosition").toString()))){
						//go on
					}else {
						continue;
					}
				}
				//是否能够编辑
				map.put("CanUpdate","false");
				//保修单的ID
				map.put("RepairTaskID", report.get("RepairTaskID").toString());
				//维修单类型
				map.put("RepairTaskType", LogicMethod.getRightString(report.get("RepairTaskType").toString()));
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
				map.put("ApprovePersonRealName", LogicMethod.getRightString(report.get("ApprovePersonRealName").toString()));
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
		}
		return data;
	}
	
	
	/**
	 * 将得到的的设备养护的历史任务json字符串解析为arraylist
	 * @param jsonObject
	 * @param consName
	 * @param rolePosition 
	 * @return
	 * @throws JSONException
	 */
	public static ArrayList<HashMap<String, String>> parseUpkeepHistoryDataToList(JSONObject jsonObject,String consName) throws JSONException{
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		String result = jsonObject.getString("d").toString();
		JSONArray jsonArray;
		if(result.equals("")||result.equals("null")){
			jsonArray = null;
		}else {
			jsonArray = new JSONArray(result);
			JSONObject report = null;
			HashMap<String, String> map = null;
			for(int i =0;i<jsonArray.length();i++){
				report = jsonArray.getJSONObject(i);
				map = new HashMap<String, String>();
				
				if(consName.equals("全部")||consName.equals("")){
					//go on
				}else {
					if(consName.equals(LogicMethod.getRightString(report.get("StructureName").toString()))){
						//go on
					}else {
						continue;
					}
				}
				
				//实际使用工时
				map.put("ActualManHours", LogicMethod.getRightString(report.get("ActualManHours").toString()));
				//要求完成时间
				map.put("NeedComplete", LogicMethod.getRightString(report.get("NeedComplete").toString().replace("T", " ")));
				//设备ID
				map.put("DeviceID", report.get("DeviceID").toString());
				//完成情况及处理措施
				map.put("MaintainDetail",LogicMethod.getRightString(report.get("MaintainDetail").toString()));
				//构筑物
				map.put("StructureName", LogicMethod.getRightString(report.get("StructureName").toString()));
				//设备科长意见
				map.put("DDOpinion", LogicMethod.getRightString(report.get("DDOpinion").toString()));
				//
				map.put("Specification",LogicMethod.getRightString(report.get("Specification").toString()));
				// 养护开始时间
				map.put("MaintainStartTime", LogicMethod.getRightString(report.get("MaintainStartTime").toString().replace("T", " ")));
				//保养计划状态
				map.put("MaintainState", LogicMethod.getRightString(report.get("MaintainState").toString()));
				//保养计划状态描述信息
				map.put("MaintainStateDescription", EnumList.UpkeepHistoryPlanState.getHistoryStateEnum(Integer.valueOf(report.get("MaintainState").toString())).getCodeName());
				//养护要求
				map.put("TaskDetail", LogicMethod.getRightString(report.get("TaskDetail").toString()));
				//审核时间
				map.put("ApproveTime", LogicMethod.getRightString(report.get("ApproveTime").toString()).replace("T", " "));
				//回单时间
				map.put("CheckTime", LogicMethod.getRightString(report.get("CheckTime").toString()).replace("T", " "));
				//
				map.put("MaintainTaskID", LogicMethod.getRightString(report.get("MaintainTaskID").toString()));
				//
				map.put("MaintainPlanID", LogicMethod.getRightString(report.get("MaintainPlanID").toString()));
				//
				map.put("StructureID", LogicMethod.getRightString(report.get("StructureID").toString()));
				//
				map.put("PlantID", LogicMethod.getRightString(report.get("PlantID").toString()));
				//养护周期
				map.put("MaintainPeriod",LogicMethod.getRightString(report.get("MaintainPeriod").toString()));
				//
				map.put("DeviceName", LogicMethod.getRightString(report.get("DeviceName").toString()));
				//派发人
				map.put("CreatePerson", LogicMethod.getRightString(report.get("CreatePerson").toString()));
				//回单人
				map.put("CheckPerson", LogicMethod.getRightString(report.get("CheckPerson").toString()));
				//保养内容：
				map.put("MaintainSpecification", LogicMethod.getRightString(report.get("MaintainSpecification").toString()));
				//
//				map.put("InstallPosition", LogicMethod.getRightString(report.get("InstallPosition").toString()));
				//所需工时
				map.put("RequiredManHours", LogicMethod.getRightString(report.get("RequiredManHours").toString()));
				//养护部位
				map.put("MaintainPosition", LogicMethod.getRightString(report.get("MaintainPosition").toString()));
				//
				map.put("State", LogicMethod.getRightString(report.get("State").toString()));
				//
				map.put("StateDescription", EnumList.UpkeepHistoryState.getHistoryStateEnum(Integer.valueOf(report.get("State").toString())).getCodeName());
				//实际完成时间
//				map.put("RealCompleteTime", LogicMethod.getRightString(report.get("realcompleteTime").toString().replace("T", " ")));
				//审核人
				map.put("ApprovePerson", LogicMethod.getRightString(report.get("ApprovePerson").toString()));
				//派发时间
				map.put("CreateTime", LogicMethod.getRightString(report.get("CreateTime").toString().replace("T", " ")));
				//养护类型
				map.put("MaintainType", LogicMethod.getRightString(report.get("MaintainType").toString()));
				//
				map.put("IsDeleted", LogicMethod.getRightString(report.get("IsDeleted").toString()));
				//
				map.put("MaintainTaskSN", LogicMethod.getRightString(report.get("MaintainTaskSN").toString()));
				//
//				map.put("Capacity", LogicMethod.getRightString(report.get("Capacity").toString()));
				//
//				map.put("Remark", LogicMethod.getRightString(report.get("Remark").toString()));
				//养护人
				map.put("MaintainPerson",LogicMethod.getRightString(report.get("MaintainPerson").toString().toString()));
				//
				map.put("NeedCompleteTotaDay", LogicMethod.getRightString(report.get("NeedCompleteTotaDay").toString()));
				
				data.add(map);
			}
		}
		
		return data;
	}
	
	/**
	 * @param jsonObject
	 * @param type 是全部的还是计划内的
	 * @param consName
	 * @param stateName
	 * @return
	 * @throws JSONException 
	 */
	public static ArrayList<HashMap<String, String>> parseUpkeepSendDataList(JSONObject jsonObject, boolean type,String consName,String stateName) throws JSONException{
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		JSONObject json1 = new JSONObject(jsonObject.getString("d"));
		JSONArray jsonArray = null;
		HashMap<String, String> map = null;
		JSONObject json2 = null;
		int state = 0;
		int taskState = OperationMethod.getUpkeepPlanStateByStateName(stateName);
		if(!type){
			if(json1.get("AllPlanList").equals("")||json1.get("AllPlanList").equals("null")){
				jsonArray = null;
			}else {
				jsonArray = new JSONArray(json1.get("AllPlanList").toString());
			}
		}else if (type) {
			if(json1.get("AllPlanList").equals("")||json1.get("AllPlanList").equals("null")){
				jsonArray = null;
			}else {
				jsonArray = new JSONArray(json1.get("ToTermData").toString());
			}
		}else {
			
		}
		if(jsonArray!=null){
			for(int i = 0; i < jsonArray.length();i++){
				map = new HashMap<String, String>();
				json2 = jsonArray.getJSONObject(i);
				
				if(taskState!=-1&&taskState!=Integer.valueOf(json2.get("MaintainState").toString())){
					continue;
				}
				
				if(consName.equals("全部")||consName.equals("")){
					//go on
				}else {
					if(consName.equals(LogicMethod.getRightString(json2.get("StructureName").toString()))){
						//go on
					}else {
						continue;
					}
				}
				state = Integer.valueOf(json2.get("MaintainState").toString());
				map.put("MaintainSpecification", LogicMethod.getRightString(json2.get("MaintainSpecification").toString()));
				map.put("MaintainState", LogicMethod.getRightString(json2.get("MaintainState").toString()));
				if(state==UpkeepHistoryPlanState.STATE_DONE_INT||state==UpkeepHistoryPlanState.STATE_HASBEENPLAN_INT){
					map.put("CanUpdate", "true");
				}else {
					map.put("CanUpdate", "false");
				}
				map.put("MaintainStateDescription", EnumList.UpkeepHistoryPlanState.getHistoryStateEnum(Integer.valueOf(json2.get("MaintainState").toString())).getCodeName());
				map.put("DeviceID", LogicMethod.getRightString(json2.get("DeviceID").toString()));
				map.put("IsDeleted", LogicMethod.getRightString(json2.get("IsDeleted").toString()));
				map.put("MaintainPeriod", LogicMethod.getRightString(json2.get("MaintainPeriod").toString()));
				map.put("MaintainPlanID", LogicMethod.getRightString(json2.get("MaintainPlanID").toString()));
				map.put("MaintainPosition", LogicMethod.getRightString(json2.get("MaintainPosition").toString()));
				map.put("MaintainStartTime", LogicMethod.getRightString(json2.get("MaintainStartTime").toString().replace("T", " ")));
				map.put("MaintainType", LogicMethod.getRightString(json2.get("MaintainType").toString()));
				map.put("PlantID", LogicMethod.getRightString(json2.get("PlantID").toString()));
				map.put("Maintaintimenext", LogicMethod.getRightString(json2.get("Maintaintimenext").toString().replace("T", " ")));
				map.put("dataToday", LogicMethod.getRightString(json2.get("dataToday").toString()));
				map.put("DeviceName", LogicMethod.getRightString(json2.get("DeviceName").toString()));
				map.put("InstallPosition", LogicMethod.getRightString(json2.get("InstallPosition").toString()));
				map.put("StructureID", LogicMethod.getRightString(json2.get("StructureID").toString()));
				
				data.add(map);
			}
		}
		return  data;
	}
	
	
	/**
	 * @param jsonObject
	 * @param type
	 * @param consName
	 * @param stateName
	 * @return
	 * @throws JSONException
	 */
	public static ArrayList<HashMap<String, String>> parseUpkeepReportDataList(JSONObject jsonObject, boolean filter,String consName,String stateName) throws JSONException{
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		String result = jsonObject.getString("d").toString();
		JSONArray jsonArray;
		if(result.equals("")||result.equals("null")){
			jsonArray = null;
		}else {
			jsonArray = new JSONArray(jsonObject.getString("d").toString());
			HashMap<String, String> map = null;
			JSONObject json2 = null;
			boolean canUpdate = false;
			int state = 0;
			int taskState = OperationMethod.getUpkeepStateByStateName(stateName);
			for(int i = 0; i < jsonArray.length();i++){
				map = new HashMap<String, String>();
				json2 = jsonArray.getJSONObject(i);
				
				if(taskState!=-1&&taskState!=Integer.valueOf(json2.get("State").toString())){
					continue;
				}
				if(consName.equals("全部")||consName.equals("")){
					//go on
				}else {
					if(consName.equals(LogicMethod.getRightString(json2.get("StructureName").toString()))){
						//go on
					}else {
						continue;
					}
				}
				state = Integer.valueOf(LogicMethod.getRightString(json2.get("State").toString()));
				if(state==UpkeepHistoryState.STATE_HASBEENSEND_INT||state==UpkeepHistoryState.STATE_NOTAPPROVE_INT||state==UpkeepHistoryState.STATE_WAITFORSUBMIT_INT){
					canUpdate = true;
				}else {
					canUpdate = false;
				}
				map.put("CanUpdate", canUpdate?"true":"false");
				map.put("PlantID", LogicMethod.getRightString(json2.get("PlantID").toString()));
				map.put("MaintainTaskID", LogicMethod.getRightString(json2.get("MaintainTaskID").toString()));
				map.put("StructureID", LogicMethod.getRightString(json2.get("StructureID").toString()));
				map.put("StructureName", LogicMethod.getRightString(json2.get("StructureName").toString()));
				map.put("NeedComplete", LogicMethod.getRightString(json2.get("NeedComplete").toString().replace("T", " ")));
				map.put("NeedCompleteTotaDay", LogicMethod.getRightString(json2.get("NeedCompleteTotaDay").toString()));
				map.put("MaintainTaskSN", LogicMethod.getRightString(json2.get("MaintainTaskSN").toString()));
				map.put("State", LogicMethod.getRightString(json2.get("State").toString()));
				map.put("StateDescription", EnumList.UpkeepHistoryState.getHistoryStateEnum(Integer.valueOf(json2.get("State").toString())).getCodeName());
				map.put("RequiredManHours", LogicMethod.getRightString(json2.get("RequiredManHours").toString()));
				map.put("ActualManHours", LogicMethod.getRightString(json2.get("ActualManHours").toString()));
				map.put("CreateTime", LogicMethod.getRightString(json2.get("CreateTime").toString().replace("T", " ")));
				map.put("CreatePerson", LogicMethod.getRightString(json2.get("CreatePerson").toString().replace("T", " ")));
				map.put("CreatePersonID", LogicMethod.getRightString(json2.get("CreatePersonID").toString()));
				map.put("CheckTime", LogicMethod.getRightString(json2.get("CheckTime").toString().replace("T", " ")));
				map.put("CheckPerson", LogicMethod.getRightString(json2.get("CheckPerson").toString()));
				map.put("CheckPersonID", LogicMethod.getRightString(json2.get("CheckPersonID").toString()));
				map.put("ApproveTime", LogicMethod.getRightString(json2.get("ApproveTime").toString().replace("T", " ")));
				map.put("ApprovePerson", LogicMethod.getRightString(json2.get("ApprovePerson").toString()));
				map.put("ApprovePersonID", LogicMethod.getRightString(json2.get("ApprovePersonID").toString()));
				map.put("MaintainPerson", LogicMethod.getRightString(json2.get("MaintainPerson").toString()));
				map.put("MaintainPersonID", LogicMethod.getRightString(json2.get("MaintainPersonID").toString()));
				map.put("TaskDetail", LogicMethod.getRightString(json2.get("TaskDetail").toString()));
				map.put("MaintainDetail", LogicMethod.getRightString(json2.get("MaintainDetail").toString()));
				map.put("DDOpinion", LogicMethod.getRightString(json2.get("DDOpinion").toString()));
				map.put("MaintainSpecification", LogicMethod.getRightString(json2.get("MaintainSpecification").toString()));
				map.put("MaintainState", LogicMethod.getRightString(json2.get("MaintainState").toString()));
				map.put("IsDeleted", LogicMethod.getRightString(json2.get("IsDeleted").toString()));
				map.put("MaintainPeriod", LogicMethod.getRightString(json2.get("MaintainPeriod").toString()));
				map.put("Specification", LogicMethod.getRightString(json2.get("Specification").toString()));
				map.put("MaintainPlanID", LogicMethod.getRightString(json2.get("MaintainPlanID").toString()));
				map.put("MaintainPosition", LogicMethod.getRightString(json2.get("MaintainPosition").toString()));
				map.put("MaintainStartTime", LogicMethod.getRightString(json2.get("MaintainStartTime").toString().replace("T", " ")));
				map.put("MaintainType", LogicMethod.getRightString(json2.get("MaintainType").toString()));
				map.put("DeviceName", LogicMethod.getRightString(json2.get("DeviceName").toString()));
				map.put("DeviceID", LogicMethod.getRightString(json2.get("DeviceID").toString()));
				
				data.add(map);
			}
		}
		return  data;
	}
	
	
	/**
	 * @param jsonObject
	 * @param type
	 * @param consName
	 * @param stateName
	 * @return
	 * @throws JSONException
	 */
	public static ArrayList<HashMap<String, String>> parseUpkeepApproveDataList(JSONObject jsonObject, boolean filter,String consName,String stateName) throws JSONException{
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		String result = jsonObject.getString("d").toString();
		JSONArray jsonArray;
		if(result.equals("")||result.equals("null")){
			jsonArray = null;
		}else {
			jsonArray = new JSONArray(result);
			HashMap<String, String> map = null;
			JSONObject json2 = null;
			boolean canUpdate = false;
			int state = 0;
			int taskState = OperationMethod.getUpkeepStateByStateName(stateName);

			for(int i = 0; i < jsonArray.length();i++){
				map = new HashMap<String, String>();
				json2 = jsonArray.getJSONObject(i);
				
				if(taskState!=-1&&taskState!=Integer.valueOf(json2.get("State").toString())){
					continue;
				}
				
				if(consName.equals("全部")||consName.equals("")){
					//go on
				}else {
					if(consName.equals(LogicMethod.getRightString(json2.get("StructureName").toString()))){
						//go on
					}else {
						continue;
					}
				}
				state = Integer.valueOf(LogicMethod.getRightString(json2.get("State").toString()));
				if(state == UpkeepHistoryState.STATE_HASBEENBACK_INT){
					canUpdate = true;
				}else {
					canUpdate = false;
				}
				map.put("CanUpdate", canUpdate?"true":"false");
				map.put("PlantID", LogicMethod.getRightString(json2.get("PlantID").toString()));
				map.put("MaintainTaskID", LogicMethod.getRightString(json2.get("MaintainTaskID").toString()));
				map.put("StructureID", LogicMethod.getRightString(json2.get("StructureID").toString()));
				map.put("StructureName", LogicMethod.getRightString(json2.get("StructureName").toString()));
				map.put("NeedComplete", LogicMethod.getRightString(json2.get("NeedComplete").toString().replace("T", " ")));
				map.put("NeedCompleteTotaDay", LogicMethod.getRightString(json2.get("NeedCompleteTotaDay").toString()));
				map.put("MaintainTaskSN", LogicMethod.getRightString(json2.get("MaintainTaskSN").toString()));
				map.put("State", LogicMethod.getRightString(json2.get("State").toString()));
				map.put("StateDescription", EnumList.UpkeepHistoryState.getHistoryStateEnum(Integer.valueOf(json2.get("State").toString())).getCodeName());
				map.put("RequiredManHours", LogicMethod.getRightString(json2.get("RequiredManHours").toString()));
				map.put("ActualManHours", LogicMethod.getRightString(json2.get("ActualManHours").toString()));
				map.put("CreateTime", LogicMethod.getRightString(json2.get("CreateTime").toString().replace("T", " ")));
				map.put("CreatePerson", LogicMethod.getRightString(json2.get("CreatePerson").toString().replace("T", " ")));
				map.put("CreatePersonID", LogicMethod.getRightString(json2.get("CreatePersonID").toString()));
				map.put("CheckTime", LogicMethod.getRightString(json2.get("CheckTime").toString().replace("T", " ")));
				map.put("CheckPerson", LogicMethod.getRightString(json2.get("CheckPerson").toString()));
				map.put("CheckPersonID", LogicMethod.getRightString(json2.get("CheckPersonID").toString()));
				map.put("ApproveTime", LogicMethod.getRightString(json2.get("ApproveTime").toString().replace("T", " ")));
				map.put("ApprovePerson", LogicMethod.getRightString(json2.get("ApprovePerson").toString()));
				map.put("ApprovePersonID", LogicMethod.getRightString(json2.get("ApprovePersonID").toString()));
				map.put("MaintainPerson", LogicMethod.getRightString(json2.get("MaintainPerson").toString()));
				map.put("MaintainPersonID", LogicMethod.getRightString(json2.get("MaintainPersonID").toString()));
				map.put("TaskDetail", LogicMethod.getRightString(json2.get("TaskDetail").toString()));
				map.put("MaintainDetail", LogicMethod.getRightString(json2.get("MaintainDetail").toString()));
				map.put("DDOpinion", LogicMethod.getRightString(json2.get("DDOpinion").toString()));
				map.put("MaintainSpecification", LogicMethod.getRightString(json2.get("MaintainSpecification").toString()));
				map.put("MaintainState", LogicMethod.getRightString(json2.get("MaintainState").toString()));
				map.put("IsDeleted", LogicMethod.getRightString(json2.get("IsDeleted").toString()));
				map.put("MaintainPeriod", LogicMethod.getRightString(json2.get("MaintainPeriod").toString()));
				map.put("Specification", LogicMethod.getRightString(json2.get("Specification").toString()));
				map.put("MaintainPlanID", LogicMethod.getRightString(json2.get("MaintainPlanID").toString()));
				map.put("MaintainPosition", LogicMethod.getRightString(json2.get("MaintainPosition").toString()));
				map.put("MaintainStartTime", LogicMethod.getRightString(json2.get("MaintainStartTime").toString().replace("T", " ")));
				map.put("MaintainType", LogicMethod.getRightString(json2.get("MaintainType").toString()));
				map.put("DeviceName", LogicMethod.getRightString(json2.get("DeviceName").toString()));
				map.put("DeviceID", LogicMethod.getRightString(json2.get("DeviceID").toString()));
				
				data.add(map);
			}
		}
		return  data;
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
