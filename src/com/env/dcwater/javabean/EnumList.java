package com.env.dcwater.javabean;
/**
 * 常用数据集合
 * @author sk
 */
public class EnumList {
	/**
	 * @author sk 用户权限枚举
	 */
	public enum UserRight {
		/**
		 * 个人信息
		 */
		USERINFORMATION(99, "个人信息","com.env.dcwater.activity.UserInformationAcivity"),
		/**
		 * 设置
		 */
		USERCONFIG(100, "设置","com.env.dcwater.activity.UserConfigActivity"),
		
		/**
		 * 设备信息枚举对象
		 */
		MACHINEINFO(0, "设备信息查看","com.env.dcwater.activity.DeviceInfoListActivity"),
		/**
		 * 报修管理枚举对象
		 */
		REPAIRMANAGE(1, "设备维修管理","com.env.dcwater.activity.RepairManageActivity"),
		
		/**
		 * 保养工单派发
		 */
		UPKEEPSEND(2, "保养工单派发","com.env.dcwater.activity.UpkeepSendActivity"),
		
		/**
		 * 保养工单填写
		 */
		UPKEEPREPORT(3, "保养工单填写","com.env.dcwater.activity.UpkeepReportActivity"),
		
		/**
		 * 保养工单审核
		 */
		UPKEEPAPPROVE(4, "保养工单审核","com.env.dcwater.activity.UpkeepApproveActivity"),
		
		/**
		 * 维修记录枚举对象
		 */
		MAINTAINHISTORY(5, "维修历史记录","com.env.dcwater.activity.MaintainHistoryActivity"),
		/**
		 * 保养记录枚举对象
		 */
		UPKEEPHISTORY(6, "保养历史记录","com.env.dcwater.activity.UpkeepHistoryActivity");

		/**
		 * 权限名称
		 */
		public static final String RightName = "UserRightName";

		/**
		 * 权限代码
		 */
		public static final String RightCode = "UserRightCode";
		/**
		 * 权限路径
		 */
		public static final String RightAction = "UserRightAction";
		
		private int mRightCode;
		private String mRightName;
		private String mAction;

		/**
		 * 权限枚举对象的构造函数
		 * 
		 * @param code
		 * @param name
		 * @param action
		 */
		private UserRight(int code, String name,String action) {
			mRightCode = code;
			mRightName = name;
			mAction = action;
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
				return UserRight.UPKEEPSEND;
			case 3:
				return UserRight.UPKEEPREPORT;
			case 4:
				return UserRight.UPKEEPAPPROVE;
			case 5:
				return UserRight.MAINTAINHISTORY;
			case 6:
				return UserRight.UPKEEPHISTORY;
			case 99:
				return UserRight.USERINFORMATION;
			case 100:
				return UserRight.USERCONFIG;
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
		
		public String getAction(){
			return mAction;
		}
	}
	
	
	/**
	 * 一个描述流程进度的enum集合
	 * @author sk
	 *
	 */
	/**
	 * @author Administrator
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
		
		/**
		 * 报修单已上报
		 */
		public static final int STATEHASBEENREPORTED = 0;
		/**
		 * 报修单已确认
		 */
		public static final int STATEHASBEENCONFIRMED = 1;
		/**
		 * 维修单已派发
		 */
		public static final int STATEHASBEENDISTRIBUTED = 2;
		/**
		 * 维修单填写中
		 */
		public static final int STATEBEENINGREPAIRED = 3;
		/**
		 * 维修单已上报
		 */
		public static final int STATEHASBEENREPAIRED = 4;
		/**
		 * 维修单返回修改
		 */
		public static final int STATEFORCORRECTION = 5;
		/**
		 * 设备科长审核通过
		 */
		public static final int STATEDEVICETHROUGH = 6;
		/**
		 * 生产科长已确认
		 */
		public static final int STATEPRODUCTIONTHROUGH = 7;
		/**
		 * 厂长已确认
		 */
		public static final int STATEDIRECTORTHROUGH = 8;
		
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
		EQUIPMENTOPERATION(9,"设备科操作工"),
		
		/**
		 * 生产科长
		 */
		PRODUCTIONCHIEF(6,"生产科长"),
		
		/**
		 * 厂长
		 */
		PLANTER(5,"厂长"),
		
		/**
		 * 机修工
		 */
		REPAIRMAN(10,"机修工");
		
		/**
		 * 生产科操作工
		 */
		public static final int USERROLEPRODUCTIONOPERATION = 7;
		
		/**
		 * 设备科长
		 */
		public static final int USERROLEEQUIPMENTCHIEF = 8;
		
		/**
		 * 设备科操作工
		 */
		public static final int USERROLEEQUIPMENTOPERATION = 9;
		
		/**
		 * 生产科长
		 */
		public static final int USERROLEPRODUCTIONCHIEF = 6;
		
		/**
		 * 机修工
		 */
		public static final int USERROLEREPAIRMAN = 10;
		
		/**
		 * 厂长
		 */
		public static final int USERROLEPLANTER = 5;
		
		
		
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
			case USERROLEPRODUCTIONCHIEF:
				return UserRole.PRODUCTIONCHIEF;
			case USERROLEREPAIRMAN:
				return UserRole.REPAIRMAN;
			case USERROLEPLANTER:
				return UserRole.PLANTER;
			}
			return null;
		}
	}
	
	
	
	/**
	 * 保养流程的工单状态
	 * @author SK
	 *
	 */
	public enum UpkeepHistoryPlanState{
		
		/**
		 * 已列入计划
		 */
		HASBEENPLAN(1,"已列入计划"),
		/**
		 * 养护中
		 */
		BEENUPKEEP(2,"养护中"),
		/**
		 * 审核未通过
		 */
		NOTAPPROVE(3,"审核未通过"),
		/**
		 * 完成
		 */
		DONE(4,"完成"),
		/**
		 * 回单待审核
		 */
		WAITFORAPPROVE(5,"回单待审核");
		
		/**
		 * 已列入计划
		 */
		public static final int STATE_HASBEENPLAN_INT = 1;
		/**
		 *  养护中
		 */
		public static final int STATE_BEENUPKEEP_INT = 2;
		/**
		 * 审核未通过
		 */
		public static final int STATE_NOTAPPROVE_INT = 3;
		/**
		 * 完成
		 */
		public static final int STATE_DONE_INT = 4;
		/**
		 * 回单待审核
		 */
		public static final int STATE_WAITFORAPPROVE_INT = 5;
		
		private int mCode;
		private String mName;
		
		private UpkeepHistoryPlanState (int code,String codeName){
			mCode = code;
			mName = codeName;
		}
		
		public int getCode(){
			return mCode;
		}
		
		public String getCodeName(){
			return mName;
		}
		
		public static UpkeepHistoryPlanState getHistoryStateEnum(int code){
			switch (code) {
			case STATE_HASBEENPLAN_INT:
				return UpkeepHistoryPlanState.HASBEENPLAN;
			case STATE_BEENUPKEEP_INT:
				return UpkeepHistoryPlanState.BEENUPKEEP;
			case STATE_NOTAPPROVE_INT:
				return UpkeepHistoryPlanState.NOTAPPROVE;
			case STATE_DONE_INT:
				return UpkeepHistoryPlanState.DONE;
			case STATE_WAITFORAPPROVE_INT:
				return UpkeepHistoryPlanState.WAITFORAPPROVE;
			}
			return null;
		}
		
	}
	
	
	/**
	 * 保养流程的工单状态
	 * @author SK
	 *
	 */
	public enum UpkeepHistoryState{
		
		/**
		 * 已派发工单
		 */
		HASBEENSEND(1,"已派发工单"),
		/**
		 * 已回单
		 */
		HASBEENBACK(2,"已回单"),
		/**
		 * 审核通过
		 */
		HASBEENAPPROVE(3,"审核通过"),
		/**
		 * 审核未通过
		 */
		NOTAPPROVE(4,"审核未通过"),
		/**
		 * 已填写待上报
		 */
		WAITFORSUBMIT(5,"已填写待上报");
		
		/**
		 * 已派发工单
		 */
		public static final int STATE_HASBEENSEND_INT = 1;
		/**
		 * 已回单
		 */
		public static final int STATE_HASBEENBACK_INT = 2;
		/**
		 * 审核通过
		 */
		public static final int STATE_HASBEENAPPROVE_INT = 3;
		/**
		 * 审核未通过
		 */
		public static final int STATE_NOTAPPROVE_INT = 4;
		/**
		 * 已填写待上报
		 */
		public static final int STATE_WAITFORSUBMIT_INT = 5;
		
		private int mCode;
		private String mName;
		
		private UpkeepHistoryState (int code,String codeName){
			mCode = code;
			mName = codeName;
		}
		
		public int getCode(){
			return mCode;
		}
		
		public String getCodeName(){
			return mName;
		}
		
		public static UpkeepHistoryState getHistoryStateEnum(int code){
			switch (code) {
			case STATE_HASBEENSEND_INT:
				return UpkeepHistoryState.HASBEENSEND;
			case STATE_HASBEENBACK_INT:
				return UpkeepHistoryState.HASBEENBACK;
			case STATE_HASBEENAPPROVE_INT:
				return UpkeepHistoryState.HASBEENAPPROVE;
			case STATE_NOTAPPROVE_INT:
				return UpkeepHistoryState.NOTAPPROVE;
			case STATE_WAITFORSUBMIT_INT:
				return UpkeepHistoryState.WAITFORSUBMIT;
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
		/**
		 * 生产科工单
		 */
		public static final int TASKTYPE_PRODUCTION = 1;
		/**
		 * 设备科工单
		 */
		public static final int TASKTYPE_EQUIPMENT  = 2;
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
		
		TYPE1(1,"一类"),
		
		TYPE2(2,"二类"),
		
		TYPE3(3,"三类");
		
		private int mCode;
		private String mName;
		
		private DeviceClassType(int code,String name){
			mCode = code;
			mName = name;
		}
		
		public static DeviceClassType getDeviceClassType(int code){
			switch (code) {
			case 1:
				return TYPE1;
			case 2:
				return TYPE2;
			case 3:
				return TYPE3;
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
	
	/**
	 * 请求webservice，返回的结果对应的意义
	 * @author sk
	 */
	public enum DataCenterResult{
		
		SUCCESS(1,"更新成功"),
		
		SERVERERRO(2,"数据库错误"),
		
		OPERATIONERRO(3,"业务逻辑错误"),
		
		OTHERERRO(4,"服务器未知错误");
		
		/**
		 * 更新成功
		 */
		public static final int CODE_SUCCESS = 1;
		
		/**
		 * 数据库错误
		 */
		public static final int CODE_SERVERERRO = 2;
		
		/**
		 * 业务逻辑错误(一般由于工单状态发生改变)
		 */
		public static final int CODE_OPERATIONERRO = 3;
		
		/**
		 * 服务器未知错误
		 */
		public static final int CODE_OTHERERRO = 4;
		
		private int mCode;
		private String mName;
		
		private DataCenterResult (int code,String name){
			mCode = code;
			mName = name;
		}
		
		public static DataCenterResult getDataCenterResult(int code){
			switch (code) {
			case CODE_SUCCESS:
				return SUCCESS;
			case CODE_SERVERERRO:
				return SERVERERRO;
			case CODE_OPERATIONERRO:
				return OPERATIONERRO;
			case CODE_OTHERERRO:
				return OTHERERRO;
			}
			return null;
		}
		
		public String getName(){
			return mName;
		}
		
		public int getCode(){
			return mCode;
		}
		
	}
}
