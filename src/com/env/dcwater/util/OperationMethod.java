package com.env.dcwater.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.env.dcwater.R;
import com.env.dcwater.component.DCWaterApp;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.javabean.ClassTaskWorkFlow;
import com.env.dcwater.javabean.EnumList;
import com.env.dcwater.javabean.EnumList.RepairState;
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
		
		
		map = new HashMap<String, String>();
		map.put(UserRight.RightName, UserRight.DEVICEROUTING.getName());
		map.put(UserRight.RightCode, UserRight.DEVICEROUTING.getCode()+"");
		map.put(UserRight.RightAction, UserRight.DEVICEROUTING.getAction());
		map.put(UserRight.RightResourceID, UserRight.DEVICEROUTING.getResourceID()+"");
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
//		map = new HashMap<String, String>();
//		map.put(UserRight.RightName, UserRight.USERCONFIG.getName());
//		map.put(UserRight.RightCode, UserRight.USERCONFIG.getCode()+"");
//		map.put(UserRight.RightAction, UserRight.USERCONFIG.getAction());
//		map.put(UserRight.RightResourceID, UserRight.USERCONFIG.getResourceID()+"");
//		map.put(UserRight.RightTaskCount, "");
//		data.add(map);
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
	
	/**
	 * 获取设备基本参数
	 * @param device
	 * @return
	 * @throws JSONException
	 */
	public static HashMap<String, String> parseDevicePropertyToHashMap(JSONObject device) throws JSONException{
		
		HashMap<String, String> map = new HashMap<String, String>();
		//设备id
		map.put("DeviceID", device.get("DeviceID").toString());
		//设备编号：
		map.put("DeviceSN", device.get("DeviceSN").toString());
		//设备名称
		map.put("DeviceName", device.get("DeviceName").toString());
		//厂id
		map.put("PlantID", device.get("PlantID").toString());
		//使用部门
		map.put("Department", LogicMethod.getRightString(device.get("Department").toString()));
		//设备型号
		map.put("Specification", LogicMethod.getRightString(device.get("Specification").toString()));
		//固定资产编号
		map.put("FixedAssets", LogicMethod.getRightString(device.get("FixedAssets").toString()));
		//安装位置的名称
		map.put("InstallPosition", LogicMethod.getRightString(device.get("InstallPosition").toString()));
		//安装位置
		map.put("InstallPositionforMobile", LogicMethod.getRightString(device.get("InstallPositionforMobile").toString()));
		
		map.put("TechnicalParameter", LogicMethod.getRightString(device.get("TechnicalParameter").toString()));
		//生产厂家
		map.put("Manufacturer", LogicMethod.getRightString(device.get("Manufacturer").toString()));
		//设备价格
		map.put("Price", LogicMethod.getRightString(device.get("Price").toString()));
		//建档时间：
		map.put("FilingTime", LogicMethod.getRightString(device.get("FilingTime").toString().replace("T", " ")));
		//安装试车时间
		map.put("InstallTime", LogicMethod.getRightString(device.get("InstallTime").toString().replace("T", " ")));
		//开始使用时间
		map.put("StartUseTime", LogicMethod.getRightString(device.get("StartUseTime").toString().replace("T", " ")));
		//开始停用时间
		map.put("StopUseTime", LogicMethod.getRightString(device.get("StopUseTime").toString().replace("T", " ")));
		//设备报废时间
		map.put("ScrapTime", LogicMethod.getRightString(device.get("ScrapTime").toString().replace("T", " ")));
		//设备折旧年限
		map.put("DepreciationPeriod", LogicMethod.getRightString(device.get("DepreciationPeriod").toString()));
		//设备质量
		map.put("Quality", LogicMethod.getRightString(device.get("Quality").toString()));
		
		map.put("ReMark", LogicMethod.getRightString(device.get("ReMark").toString()));
		//正常运行的标准
		map.put("StandardNorOperation", LogicMethod.getRightString(device.get("StandardNorOperation").toString().trim()));
		//运行管理及操作要点:
		map.put("OperatManagAndOperatPoint", LogicMethod.getRightString(device.get("OperatManagAndOperatPoint").toString().trim()));
		//常见问题及对策:
		map.put("ComProbAndSolutions", LogicMethod.getRightString(device.get("ComProbAndSolutions").toString().trim()));
		//设备类型
		map.put("DeviceClassType", LogicMethod.getRightString(device.get("DeviceClassType").toString()));
		//随机附件及数量
		map.put("AccessoryInfo", LogicMethod.getRightString(device.get("AccessoryInfo").toString()));
		//图片地址
		map.put("PicURL", LogicMethod.getRightString(device.get("PicURL").toString()));
		//所属小类:
		map.put("DeviceSmallClassName", LogicMethod.getRightString(device.get("DeviceSmallClassName").toString()));
		//所属大类：
		map.put("DeviceLargeClassName", LogicMethod.getRightString(device.get("DeviceLargeClassName").toString()));
		//数量
		map.put("Amount", LogicMethod.getRightString(device.get("Amount").toString()));
		//设备最近一次维修时间
		map.put("LastRepairTime", LogicMethod.getRightString(device.get("LastRepairTime").toString().replace("T", " ")));
		//设备最近一次养护时间
		map.put("LastMaintainTime", LogicMethod.getRightString(device.get("LastMaintainTime").toString().replace("T", " ")));
		
		return map;
	}
	
	public static ArrayList<HashMap<String, String>> parseDeviceManageToList(HashMap<String, String> maps){
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("DeviceOperatingParameterName", "最近一次维修时间");
		map.put("DeviceOperatingParameterValue", maps.get("LastRepairTime"));
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("DeviceOperatingParameterName", "最近一次养护时间");
		map.put("DeviceOperatingParameterValue", maps.get("LastMaintainTime"));
		data.add(map);
		
		return data;
	}
	
	/**
	 * @param map
	 * @return
	 */
	public static ArrayList<HashMap<String, String>> parseDevicePropertyToList(HashMap<String, String> maps){
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("Name", "设备名称");
		map.put("Value", maps.get("DeviceName"));
		map.put("Key", "DeviceName");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "设备类型");
		map.put("Value", maps.get("DeviceClassType"));
		map.put("Key", "DeviceClassType");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "建档时间");
		map.put("Value", maps.get("FilingTime"));
		map.put("Key", "FilingTime");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "设备编号");
		map.put("Value", maps.get("DeviceSN"));
		map.put("Key", "DeviceSN");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "固定资产编号");
		map.put("Value", maps.get("FixedAssets"));
		map.put("Key", "FixedAssets");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "型号（规格）");
		map.put("Value", maps.get("Specification"));
		map.put("Key", "Specification");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "安装位置");
		map.put("Value", maps.get("InstallPosition"));
		map.put("Key", "InstallPosition");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "使用部门");
		map.put("Value", maps.get("Department"));
		map.put("Key", "Department");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "生产厂家");
		map.put("Value", maps.get("Manufacturer"));
		map.put("Key", "Manufacturer");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "安装试车时间");
		map.put("Value", maps.get("InstallTime"));
		map.put("Key", "InstallTime");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "开始使用时间");
		map.put("Value", maps.get("StartUseTime"));
		map.put("Key", "StartUseTime");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "停用时间");
		map.put("Value", maps.get("StopUseTime"));
		map.put("Key", "StopUseTime");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "设备报废时间");
		map.put("Value", maps.get("ScrapTime"));
		map.put("Key", "ScrapTime");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "设备折旧年限");
		map.put("Value", maps.get("DepreciationPeriod"));
		map.put("Key", "DepreciationPeriod");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "设备质量");
		map.put("Value", maps.get("Quality"));
		map.put("Key", "Quality");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "设备价格");
		map.put("Value", maps.get("Price"));
		map.put("Key", "Price");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "同型号台数");
		map.put("Value", maps.get("Amount"));
		map.put("Key", "Amount");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "随机附件及数量");
		map.put("Value", maps.get("AccessoryInfo"));
		map.put("Key", "AccessoryInfo");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "所属大类");
		map.put("Value", maps.get("DeviceLargeClassName"));
		map.put("Key", "DeviceLargeClassName");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "所属小类");
		map.put("Value", maps.get("DeviceSmallClassName"));
		map.put("Key", "DeviceSmallClassName");
		data.add(map);
		
		map = new HashMap<String, String>();
		map.put("Name", "设备图片名");
		map.put("Value", maps.get("PicURL"));
		map.put("Key", "PicURL");
		data.add(map);
		
		return data;
	}
	/**
	 * 获取设备技术参数
	 * @return
	 * @throws JSONException 
	 */
	public static ArrayList<HashMap<String, String>> parseDeviceParamsToList(JSONArray devices) throws JSONException{
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		JSONObject device ;
		HashMap<String, String> map ;
		for (int i =0;i<devices.length();i++) {
			device = devices.getJSONObject(i);
			map = new HashMap<String, String>();
			map.put("ParameterName", LogicMethod.getRightString(device.get("ParameterName").toString()));
			map.put("ParameterValue", LogicMethod.getRightString(device.get("ParameterValue").toString()));
			map.put("Remark", LogicMethod.getRightString(device.get("Remark").toString()));
			map.put("DeviceParameterID", LogicMethod.getRightString(device.get("Remark").toString()));
			map.put("DeviceID", LogicMethod.getRightString(device.get("Remark").toString()));
			data.add(map);
		}
		return data;
	}
	
	/**
	 * 获取设备技术文档
	 * @return
	 * @throws JSONException 
	 */
	public static ArrayList<HashMap<String, String>> parseDeviceFilesToList(JSONArray devices) throws JSONException{
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		JSONObject device ;
		HashMap<String, String> map ;
		for (int i =0;i<devices.length();i++) {
			device = devices.getJSONObject(i);
			map = new HashMap<String, String>();
			map.put("TechnicalDataID", LogicMethod.getRightString(device.getString("TechnicalDataID").toString()));
			map.put("TechnicalData", LogicMethod.getRightString(device.getString("TechnicalData").toString()));
			map.put("WhetherDownload", LogicMethod.getRightString(device.getString("WhetherDownload").toString()));
			map.put("DeviceID", LogicMethod.getRightString(device.getString("DeviceID").toString()));
			data.add(map);
		}
		return data;
	}
	
	/**
	 * 获取设备运行参数
	 * @return
	 * @throws JSONException 
	 */
	public static ArrayList<HashMap<String, String>> parseDeviceStatuToList(JSONArray devices) throws JSONException{
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		JSONObject device ;
		HashMap<String, String> map ;
		for (int i =0;i<devices.length();i++) {
			device = devices.getJSONObject(i);
			map = new HashMap<String, String>();
			map.put("DeviceOperatingParameterID", LogicMethod.getRightString(device.getString("DeviceOperatingParameterID").toString()));
			map.put("DeviceSmallClassID", LogicMethod.getRightString(device.getString("DeviceSmallClassID").toString()));
			map.put("DeviceOperatingParameterName", LogicMethod.getRightString(device.getString("DeviceOperatingParameterName").toString()));
			map.put("DeviceOperatingParameterValue", LogicMethod.getRightString(device.getString("DeviceOperatingParameterValue").toString()));
			data.add(map);
		}
		return data;
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
				map = parseDevicePropertyToHashMap(device);
				
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
				
				map.put("PicURL", LogicMethod.getRightString(report.get("PicURL").toString()));
				
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
				//设备类型
				map.put("PicURL", LogicMethod.getRightString(report.get("PicURL").toString()));
				
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
				
				map.put("PicURL", LogicMethod.getRightString(report.get("PicURL").toString()));
				
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
	public static ArrayList<HashMap<String, String>> parseUpkeepSendDataList(JSONObject jsonObject, boolean type,String consName,String stateName) throws JSONException,Exception{
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
				map.put("StructureName", LogicMethod.getRightString(json2.get("StructureName").toString()));
				map.put("PicURL", LogicMethod.getRightString(json2.get("PicURL").toString()));
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
				map.put("PicURL", LogicMethod.getRightString(json2.get("PicURL").toString()));
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
				map.put("PicURL", LogicMethod.getRightString(json2.get("PicURL").toString()));
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
			System.out.println(e.getMessage());
		} finally{
			
		}
		return map;
	}
	
	/**
	 * 从本地获取用户信息
	 * @param sp
	 * @return
	 */
	public static HashMap<String, String> getLocalUserInfo(SharedPreferences sp){
		HashMap<String, String> map = null;
		if(sp!=null){
			try {
				map = new HashMap<String, String>();
				map.put("UserName", sp.getString(DCWaterApp.PREFERENCE_USERNAME_STRING, ""));
				map.put("UserPassword", sp.getString(DCWaterApp.PREFERENCE_USERPSW_STRING, ""));
				map.put("UserRole", sp.getString(DCWaterApp.PREFERENCE_USERROLE_STRING, ""));
				map.put("UserID", sp.getString(DCWaterApp.PREFERENCE_USERID_STRING, ""));
				map.put("PlantID", sp.getString(DCWaterApp.PREFERENCE_PLANTID_STRING, ""));
				map.put("PlantName", sp.getString(DCWaterApp.PREFERENCE_PLANTNAME_STRING, ""));
				map.put("PlantType", sp.getString(DCWaterApp.PREFERENCE_PLANTTYPE_STRING, ""));
				map.put("RealUserName", sp.getString(DCWaterApp.PREFERENCE_REALNAME_STRING, ""));
				map.put("AccountState", sp.getString(DCWaterApp.PREFERENCE_ACTSTATE_STRING, ""));
				map.put("PositionID", sp.getString(DCWaterApp.PREFERENCE_POSITIONID_STRING, ""));
				map.put("PositionName", sp.getString(DCWaterApp.PREFERENCE_POSITIONNAME_STRING, ""));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return map;
	}
	
	/**
	 * 记录用户信息到本地
	 * @param et
	 * @param map
	 * @return
	 */
	public static boolean setLocalUserInfo(Editor et,HashMap<String, String> map){
		boolean ok = false;
		if(et!=null){
			try{
				et.putString(DCWaterApp.PREFERENCE_USERNAME_STRING, map.get("UserName"));
				et.putString(DCWaterApp.PREFERENCE_USERPSW_STRING, map.get("UserPassword"));
				et.putString(DCWaterApp.PREFERENCE_USERROLE_STRING, map.get("UserRole"));
				et.putString(DCWaterApp.PREFERENCE_USERID_STRING, map.get("UserID"));
				et.putString(DCWaterApp.PREFERENCE_PLANTID_STRING, map.get("PlantID"));
				et.putString(DCWaterApp.PREFERENCE_PLANTNAME_STRING, map.get("PlantName"));
				et.putString(DCWaterApp.PREFERENCE_PLANTTYPE_STRING, map.get("PlantType"));
				et.putString(DCWaterApp.PREFERENCE_REALNAME_STRING, map.get("RealUserName"));
				et.putString(DCWaterApp.PREFERENCE_ACTSTATE_STRING, map.get("AccountState"));
				et.putString(DCWaterApp.PREFERENCE_POSITIONID_STRING, map.get("PositionID"));
				et.putString(DCWaterApp.PREFERENCE_POSITIONNAME_STRING, map.get("PositionName"));
				et.putBoolean(DCWaterApp.PREFERENCE_ISLOGIN_STRING, true);
				ok = et.commit();
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		return ok;
	}
	
	/**
	 * 获取维修历史的描述信息
	 * @param map
	 * @return
	 */
	public static String getMaintainHistoryContent(HashMap<String, String> map){
		StringBuilder sb = new StringBuilder();
		try {
			Date date1 = new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).parse(map.get("AccidentOccurTime"));
			Date date2 = new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).parse(map.get("RepairedTime"));;
			sb.append("故障持续时间：").append(LogicMethod.getTimeSpan(date1, date2));
			sb.append("\n").append("故障现象：").append(map.get("AccidentDetail")).append("。");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return sb.toString();
	}
	
	/**
	 * 获取维修任务的描述信息
	 * @param map
	 * @return
	 */
	public static String getRepairTaskContent(HashMap<String, String> map){
		StringBuilder sb = new StringBuilder();
		try {
			Date date1 = new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).parse(map.get("AccidentOccurTime"));
			Date date2 ;
			if(map.get("RepairedTime").equals("")){
				date2 = new Date();
			}else {
				date2 = new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).parse(map.get("RepairedTime"));
			}
			sb.append("故障持续时间：").append(LogicMethod.getTimeSpan(date1, date2));
			sb.append("\n").append("故障现象：").append(map.get("AccidentDetail")).append("。");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return sb.toString();
	} 
	
	/**
	 * 获取保养历史的描述信息
	 * @param map
	 * @return
	 */
	public static String getUpkeepHistoryContent(HashMap<String, String> map){
		StringBuilder sb = new StringBuilder();
		sb.append("保养部位：").append(map.get("MaintainPosition")).append("，耗时").append(LogicMethod.getHoursDescrible(map.get("ActualManHours")));
		sb.append("\n").append("保养内容：").append(map.get("MaintainSpecification")).append("。");
		return sb.toString();
	}
	
	/**
	 * 获取保养工单派发的描述信息
	 * @param map
	 * @return
	 */
	public static String getUpkeepSendContent(HashMap<String, String> map){
		StringBuilder sb = new StringBuilder();
		int state = Integer.parseInt(map.get("MaintainState"));
		sb.append("保养部位：").append(map.get("MaintainPosition")).append("，");
		if(state == EnumList.UpkeepHistoryPlanState.STATE_DONE_INT||state == EnumList.UpkeepHistoryPlanState.STATE_HASBEENPLAN_INT){
			try {
				int min =  Integer.parseInt(map.get("dataToday"));
				if(min<0){
					sb.append("<font color=\"red\">超时:").append(LogicMethod.getMinsDescrible(-min));
				}else if (min>=0) {
					sb.append("<font color=\"blue\">还剩:").append(LogicMethod.getMinsDescrible(min));
				}
				sb.append("</font>");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}else {
			sb.append("保养中。");
		}
		sb.append("<br>").append("保养内容：").append(map.get("MaintainSpecification")).append("。</br>");
		return sb.toString();
	}
	/**
	 * 获取保养工单填写的描述信息
	 * @param map
	 * @return
	 */
	public static String getUpkeepReportContent(HashMap<String, String> map){
		StringBuilder sb = new StringBuilder();
		int state = Integer.valueOf(map.get("State"));
		sb.append("保养部位：").append(map.get("MaintainPosition")).append("\n");
		try {
			if(state == EnumList.UpkeepHistoryState.STATE_HASBEENSEND_INT||state == EnumList.UpkeepHistoryState.STATE_NOTAPPROVE_INT||state==EnumList.UpkeepHistoryState.STATE_WAITFORSUBMIT_INT){
				Date date=new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).parse(map.get("CreateTime"));
				sb.append("已派发：").append(LogicMethod.getTimeSpan(date,new Date()));
			}else if (state == EnumList.UpkeepHistoryState.STATE_HASBEENBACK_INT) {
				Date date= new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).parse(map.get("CheckTime"));
				sb.append("已回单：").append(LogicMethod.getTimeSpan(date,new Date()));
			}
		} catch (ParseException e) {}
		return sb.toString();
	}
	/**
	 * 获取保养工单审核的描述信息
	 * @param map
	 * @return
	 * @throws ParseException 
	 */
	public static String getUpkeepApproveContent(HashMap<String, String> map) {
		StringBuilder sb = new StringBuilder();
		int state = Integer.valueOf(map.get("State"));
		sb.append("保养部位：").append(map.get("MaintainPosition")).append("\n");
		try {
			if(state == EnumList.UpkeepHistoryState.STATE_HASBEENSEND_INT||state == EnumList.UpkeepHistoryState.STATE_NOTAPPROVE_INT||state==EnumList.UpkeepHistoryState.STATE_WAITFORSUBMIT_INT){
				Date date=new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).parse(map.get("CreateTime"));
				sb.append("已派发：").append(LogicMethod.getTimeSpan(date,new Date()));
			}else if (state == EnumList.UpkeepHistoryState.STATE_HASBEENBACK_INT) {
				Date date= new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).parse(map.get("CheckTime"));
				sb.append("已回单：").append(LogicMethod.getTimeSpan(date,new Date()));
			}
		} catch (ParseException e) {}
		return sb.toString();
	}
	
	/**
	 * 验证dataa时间否比datab提前
	 * @param dataA
	 * @param DataB
	 * @return
	 */
	public static boolean isDataABeforeDataB(String dataA,String DataB){
		boolean temp ;
		try {
			Date a = new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).parse(dataA);
			Date b = new SimpleDateFormat(SystemParams.STANDARDTIME_PATTERN_STRING,Locale.CHINA).parse(DataB);
			if(a.before(b)){
				temp = true;
			}else {
				temp = false;
			}
		} catch (Exception e) {
			temp = false;
		}
		return temp;
	}
	
	/**
	 * 返回当前状态的代办状态描述
	 * @param state
	 * @return
	 */
	public static String getNextStateDesc(int state){
		String temp = "";
		switch (state) {
		case EnumList.RepairState.STATEBEENINGREPAIRED:
			temp = "维修工维修";
			break;
		case EnumList.RepairState.STATEDEVICETHROUGH:
			
			break;
		case EnumList.RepairState.STATEDIRECTORTHROUGH:
			
			break;
		case EnumList.RepairState.STATEFORCORRECTION:
			
			break;
		case EnumList.RepairState.STATEHASBEENCONFIRMED:
			
			break;
		case EnumList.RepairState.STATEHASBEENDISTRIBUTED:
			
			break;
		case EnumList.RepairState.STATEHASBEENREPAIRED:
			temp = "设备科长审核";
			break;
		case EnumList.RepairState.STATEHASBEENREPORTED:
			
			break;
		case EnumList.RepairState.STATEPRODUCTIONTHROUGH:
			break;
		}
		return temp;
	}
	
	/**
	 * 返回当前状态代办状态的代码
	 * @param desc
	 * @return
	 */
	public static int getNextStateCode(String desc){
		int code = 0;
		if(desc.equals("维修工维修")){
			code = EnumList.RepairState.STATEBEENINGREPAIRED;
		}else if (desc.equals("设备科长审核")) {
			code = EnumList.RepairState.STATEHASBEENREPAIRED;
		}
		return code;
	}
	
	/**
	 * 根据维修工单状态 角色 以及工单类型来显示状态描述
	 * @param state
	 * @param postionID
	 * @param taskType
	 * @return
	 */
	public static String getProperStateDesc(String state,String taskType,int PositionID){
		int mState = Integer.valueOf(state);
		int mTaskType = Integer.valueOf(taskType);
		String desc = RepairState.getEnumRepairState(mState).getStateDescription();
		switch (mState) {
		case RepairState.STATEBEENINGREPAIRED:
			if(PositionID == UserRole.USERROLEREPAIRMAN){
				desc = "工单待填写";
			}
			break;
		case RepairState.STATEDEVICETHROUGH:
			if(mTaskType==EnumList.RepairTaskType.TASKTYPE_EQUIPMENT&&PositionID==UserRole.USERROLEPLANTER){
				desc = "待审核";
			}else if(mTaskType==EnumList.RepairTaskType.TASKTYPE_PRODUCTION && PositionID == UserRole.USERROLEPRODUCTIONCHIEF) {
				desc = "待审核";
			}
			break;
		case RepairState.STATEDIRECTORTHROUGH:
			break;
		case RepairState.STATEFORCORRECTION:
			if(PositionID == UserRole.USERROLEREPAIRMAN){
				desc = "工单待填写";
			}
			break;
		case RepairState.STATEHASBEENCONFIRMED:
			if(PositionID == UserRole.USERROLEEQUIPMENTCHIEF){
				desc = "维修单待派发";
			}
			break;
		case RepairState.STATEHASBEENDISTRIBUTED:
			if(PositionID == UserRole.USERROLEREPAIRMAN){
				desc = "确认维修";
			}
			break;
		case RepairState.STATEHASBEENREPAIRED:
			if(PositionID == UserRole.USERROLEEQUIPMENTCHIEF){
				desc = "待审核";
			}
			break;
		case RepairState.STATEHASBEENREPORTED:
			if(mTaskType==EnumList.RepairTaskType.TASKTYPE_EQUIPMENT&&PositionID==UserRole.USERROLEEQUIPMENTCHIEF){
				desc = "维修单待派发";
			}else if(mTaskType==EnumList.RepairTaskType.TASKTYPE_PRODUCTION&&PositionID==UserRole.USERROLEPRODUCTIONCHIEF){
				desc = "报修单待确认";
			}
			break;
		case RepairState.STATEPRODUCTIONTHROUGH:
			if(PositionID == UserRole.USERROLEPLANTER){
				desc = "待审核";
			}
			break;
		}
		return desc;
	}
	
	/**
	 * 获得workflow的描述信息
	 * @param data
	 * @return
	 */
	public static String getWorkFlowInfor(ClassTaskWorkFlow data){
		StringBuilder sb = new StringBuilder();
		sb.append("姓名：").append(data.getRealUserName()).append("\n")
		.append("岗位：").append(data.getPositionName()).append("\n")
		.append("时间：").append(data.getTaskTime().replace("T", " ")).append("\n");
		if(data.getTaskType()==EnumList.TaskType.TYPE_MAINTAIN_INT){
			sb.append("状态：").append(EnumList.UpkeepHistoryState.getHistoryStateEnum(data.getState()).getCodeName());
		}else {
			sb.append("状态：").append(EnumList.RepairState.getEnumRepairState(data.getState()).getStateDescription());
		}
		return sb.toString();
	}
}
