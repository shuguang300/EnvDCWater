package com.env.dcwater.javabean;

import java.util.Date;

/**
 * 维修工单工作流
 * @author Administrator
 *
 */
public class ClassRTWorkFlow {
	
	private int positionID;
	private int state;
	private String realUserName;
	private String userID;
	private String positionName;
	private Date taskTime;
	private int taskID;
	
	public ClassRTWorkFlow (){
	}

	public int getPositionID() {
		return positionID;
	}

	public int getState() {
		return state;
	}

	public String getRealUserName() {
		return realUserName;
	}

	public String getUserID() {
		return userID;
	}

	public String getPositionName() {
		return positionName;
	}

	public Date getTaskTime() {
		return taskTime;
	}

	public int getTaskID() {
		return taskID;
	}

	public void setPositionID(int positionID) {
		this.positionID = positionID;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setRealUserName(String realUserName) {
		this.realUserName = realUserName;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public void setTaskTime(Date taskTime) {
		this.taskTime = taskTime;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}
}
