package com.env.dcwater.javabean;

import java.util.Date;

import com.google.gson.Gson;

/**
 * 维修工单工作流
 * @author Administrator
 *
 */
public class ClassRTWorkFlow {
	
	private int PositionID;
	private int State;
	private String RealUserName;
	private String UserID;
	private String PositionName;
	private Date TaskTime;
	private int TaskID;
	
	public ClassRTWorkFlow (){
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
	public Date getTaskTime() {
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
	public void setTaskTime(Date taskTime) {
		TaskTime = taskTime;
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