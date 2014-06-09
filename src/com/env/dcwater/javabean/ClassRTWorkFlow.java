package com.env.dcwater.javabean;

import java.util.Date;

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

	public int getPositionID() {
		return PositionID;
	}

	public void setPositionID(int positionID) {
		PositionID = positionID;
	}

	public int getState() {
		return State;
	}

	public void setState(int state) {
		State = state;
	}

	public String getRealUserName() {
		return RealUserName;
	}

	public void setRealUserName(String realUserName) {
		RealUserName = realUserName;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getPositionName() {
		return PositionName;
	}

	public void setPositionName(String positionName) {
		PositionName = positionName;
	}

	public Date getTaskTime() {
		return TaskTime;
	}

	public void setTaskTime(Date taskTime) {
		TaskTime = taskTime;
	}

	public int getTaskID() {
		return TaskID;
	}

	public void setTaskID(int taskID) {
		TaskID = taskID;
	}

}