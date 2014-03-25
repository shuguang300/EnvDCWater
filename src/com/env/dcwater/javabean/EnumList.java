package com.env.dcwater.javabean;

import android.location.GpsStatus.NmeaListener;

public class EnumList {
	/**
	 * @author sk 用户权限枚举
	 */
	public enum UserRight {

		/**
		 * 设备信息枚举对象
		 */
		MACHINEINFO(0, "设备信息查看"),
		/**
		 * 报修枚举对象
		 */
		REPAIRMANAGE(1, "设备维修管理"),
		/**
		 * 维修记录枚举对象
		 */
		MAINTAINHISTORY(2, "维修历史记录"),
		/**
		 * 保养记录枚举对象
		 */
		UPKEEPHISTORY(3, "保养历史记录");

		/**
		 * 权限名称
		 */
		public static final String RightName = "UserRightName";

		/**
		 * 权限代码
		 */
		public static final String RightCode = "UserRightCode";
		
		private int mRightCode;
		private String mRightName;

		/**
		 * 权限枚举对象的构造函数
		 * 
		 * @param code
		 * @param name
		 */
		private UserRight(int code, String name) {
			mRightCode = code;
			mRightName = name;
		}

		/**
		 * 根据权限代码获取该权限的 枚举对象
		 * 
		 * @param code
		 * @return
		 */
		public static UserRight getEnumUserRight(int code) {
			switch (code) {
			case 0:
				return UserRight.MACHINEINFO;
			case 1:
				return UserRight.REPAIRMANAGE;
			case 2:
				return UserRight.MAINTAINHISTORY;
			case 3:
				return UserRight.UPKEEPHISTORY;
			}
			return null;
		}

		/**
		 * 返回权限代码
		 * 
		 * @return
		 */
		public int getCode() {
			return mRightCode;
		}

		/**
		 * 返回权限名称
		 * 
		 * @return
		 */
		public String getName() {
			return mRightName;
		}
	}
	
	
	/**
	 * 一个描述流程进度的enum集合
	 * @author sk
	 *
	 */
	public enum RepairState {
		/**
		 * 报修单已上报
		 */
		HASBEENREPORTED(0,"报修单已上报"),
		/**
		 * 报修单已确认
		 */
		HASBEENCONFIRMED(1,"报修单已确认"),
		/**
		 * 维修单已派发
		 */
		HASBEENDISTRIBUTED(2,"维修单已派发"),
		/**
		 * 维修单填写中
		 */
		BEENINGREPAIRED(3,"维修单填写中"),
		/**
		 * 维修单已上报
		 */
		HASBEENREPAIRED(4,"维修单已上报"),
		/**
		 * 维修单返回修改
		 */
		FORCORRECTION(5,"维修单返回修改"),
		/**
		 * 设备科长审核通过
		 */
		DEVICETHROUGH(6,"设备科长审核通过"),
		/**
		 * 生产科长已确认
		 */
		PRODUCTIONTHROUGH(7,"生产科长已确认"),
		/**
		 * 厂长已确认
		 */
		DIRECTORTHROUGH(8,"厂长已确认");
		
		private int mState;
		private String mStateDescription;
		
		/**
		 * 任务状态的枚举类型的构造函数
		 * @param state
		 * @param stateDescription
		 */
		private RepairState(int state,String stateDescription){
			mState = state;
			mStateDescription = stateDescription;
		}
		
		/**
		 * @return the mStateDescription
		 */
		public String getStateDescription() {
			return mStateDescription;
		}
		
		/**
		 * @return 获取当前状态
		 */
		public int getState(){
			return mState;
		}
		/**
		 * @param code
		 * @return 根据state获取任务状态的枚举类型
		 */
		public static RepairState getEnumRepairState(int code) {
			switch (code) {
			case 0:
				return RepairState.HASBEENREPORTED;
			case 1:
				return RepairState.HASBEENCONFIRMED;
			case 2:
				return RepairState.HASBEENDISTRIBUTED;
			case 3:
				return RepairState.BEENINGREPAIRED;
			case 4:
				return RepairState.HASBEENREPAIRED;
			case 5:
				return RepairState.FORCORRECTION;
			case 6:
				return RepairState.DEVICETHROUGH;
			case 7:
				return RepairState.PRODUCTIONTHROUGH;
			case 8:
				return RepairState.DIRECTORTHROUGH;
			}
			return null;
		}
	}
	
	public enum UserRole {

		/**
		 * 生产科操作工
		 */
		PRODUCTIONOPERATION(7,"生产科操作工"),
		
		/**
		 * 设备科长
		 */
		EQUIPMENTCHIEF(8,"设备科长"),

		/**
		 * 设备科操作工
		 */
		EQUIPMENTOPERATION(9,"设备科操作工");
		
		public static final int USERROLEPRODUCTIONOPERATION = 7;
		
		public static final int USERROLEEQUIPMENTCHIEF = 8;
		
		public static final int USERROLEEQUIPMENTOPERATION = 9;
		
		
		
		private int mPositionID;
		private String mPostionName;
		
		/**
		 * 
		 * @param positionID
		 */
		private UserRole(int positionID,String postionName){
			mPositionID = positionID;
			mPostionName = postionName;
		}
		
		/**
		 * @return 获取角色代码
		 */
		public int getState(){
			return mPositionID;
		}
		/**
		 * @return 获取角色代码所代表的名称
		 */
		public String getStateName(){
			return mPostionName;
		}
		
		/**
		 * 根据postionid获取该角色的枚举类型
		 * @param positionID
		 * @return
		 */
		public static UserRole getUserRole(int positionID){
			switch (positionID) {
			case USERROLEEQUIPMENTOPERATION:
				return UserRole.EQUIPMENTOPERATION;
			case USERROLEEQUIPMENTCHIEF:
				return UserRole.EQUIPMENTCHIEF;
			case USERROLEPRODUCTIONOPERATION:
				return UserRole.PRODUCTIONOPERATION;
			}
			return null;
		}
	}
	
	/**
	 * 维修单种类
	 * @author sk
	 */
	public enum RepairTaskType{ 
		
		/**
		 * 生产科类型
		 */
		PRODUCTIONSECTION(1,"生产科"),
		/**
		 * 设备科类型
		 */
		EQUIPMENTSECTION(2,"设备科");
		
		private int mType;
		private String mTypeName;
		
		private RepairTaskType(int type,String typeName){
			mType = type;
			mTypeName = typeName;
		}
		
		public static RepairTaskType getRepairTaskType(int type){
			switch (type) {
			case 1:
				return PRODUCTIONSECTION;

			case 2:
				return EQUIPMENTSECTION;
			}
			return null;
		}
		
		public int getType(){
			return mType;
		}
		
		public String getTypeName(){
			return mTypeName;
		}
	}
	
	/**
	 * 设备类型
	 * @author sk
	 */
	public enum DeviceClassType{
		Type1(1,"一类"),
		Type2(2,"二类"),
		Type3(3,"三类");
		private int mCode;
		private String mName;
		private DeviceClassType(int code,String name){
			mCode = code;
			mName = name;
		}
		public static DeviceClassType getDeviceClassType(int code){
			switch (code) {
			case 1:
				return Type1;
			case 2:
				return Type2;
			case 3:
				return Type3;
			}
			return null;
		}
		public String getCodeName(){
			return mName;
		}
		public int getCode(){
			return mCode;
		}
	}
}
