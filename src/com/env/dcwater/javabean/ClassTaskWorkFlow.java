package com.env.dcwater.javabean;
import com.google.gson.Gson;

/**
 * 维修工单工作流
 * @author Administrator
 *
 */
public class ClassTaskWorkFlow {
	

	private int PositionID;
	private int State;
	private String RealUserName;
	private String UserID;
	private String PositionName;
	private String TaskTime;
	private int TaskID;
	private int TaskType;
	
	public ClassTaskWorkFlow (){
	}
	
	/**
	 * @return the taskType
	 */
	public int getTaskType() {
		return TaskType;
	}

	/**
	 * @param taskType the taskType to set
	 */
	public void setTaskType(int taskType) {
		TaskType = taskType;
	}

	/**
	 * @return the positionID
	 */
	public int getPositionID() {
		return PositionID;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return State;
	}

	/**
	 * @return the realUserName
	 */
	public String getRealUserName() {
		return RealUserName;
	}

	/**
	 * @return the userID
	 */
	public String getUserID() {
		return UserID;
	}

	/**
	 * @return the positionName
	 */
	public String getPositionName() {
		return PositionName;
	}

	/**
	 * @return the taskTime
	 */
	public String getTaskTime() {
		return TaskTime;
	}

	/**
	 * @return the taskID
	 */
	public int getTaskID() {
		return TaskID;
	}

	/**
	 * @param positionID the positionID to set
	 */
	public void setPositionID(int positionID) {
		PositionID = positionID;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		State = state;
	}

	/**
	 * @param realUserName the realUserName to set
	 */
	public void setRealUserName(String realUserName) {
		RealUserName = realUserName;
	}

	/**
	 * @param userID the userID to set
	 */
	public void setUserID(String userID) {
		UserID = userID;
	}

	/**
	 * @param positionName the positionName to set
	 */
	public void setPositionName(String positionName) {
		PositionName = positionName;
	}

	/**
	 * @param taskTime the taskTime to set
	 */
	public void setTaskTime(String taskTime) {
		TaskTime = taskTime.replace("T", " ");
	}

	/**
	 * @param taskID the taskID to set
	 */
	public void setTaskID(int taskID) {
		TaskID = taskID;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}


}