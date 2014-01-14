package com.env.dcwater.javabean;

public class EnumList {
	/**
	 * @author Administrator 用户权限枚举
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
			this.mRightCode = code;
			this.mRightName = name;
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
			return this.mRightCode;
		}

		/**
		 * 返回权限名称
		 * 
		 * @return
		 */
		public String getName() {
			return this.mRightName;
		}
	}
	
	
	/**
	 * 一个描述流程进度的enum集合
	 * @author sk
	 *
	 */
	public enum RepairState {
		/**
		 * 已上报
		 */
		HASBEENREPORTED(0,"已上报"),
		/**
		 * 已确认
		 */
		HASBEENCONFIRMED(1,"已确认"),
		/**
		 * 已派发
		 */
		HASBEENDISTRIBUTED(2,"已派发"),
		/**
		 * 正在维修
		 */
		BEENINGREPAIRED(3,"正在维修"),
		/**
		 * 维修完成
		 */
		HASBEENREPAIRED(4,"维修完成"),
		/**
		 * 返回修改
		 */
		FORCORRECTION(5,"返回修改"),
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
}
