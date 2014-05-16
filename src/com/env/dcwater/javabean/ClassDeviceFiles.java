package com.env.dcwater.javabean;

import com.google.gson.Gson;

/**
 * 技术文档
 * @author sk
 *
 */
public class ClassDeviceFiles {
	private String TechnicalData;
	private boolean WhetherDownload;
	private int TechnicalDataID;
	private int DeviceID;
	/**
	 * @return the technicalData
	 */
	public String getTechnicalData() {
		return TechnicalData;
	}
	/**
	 * @return the whetherDownload
	 */
	public boolean getWhetherDownload() {
		return WhetherDownload;
	}
	/**
	 * @param technicalData the technicalData to set
	 */
	public void setTechnicalData(String technicalData) {
		TechnicalData = technicalData;
	}
	/**
	 * @param whetherDownload the whetherDownload to set
	 */
	public void setWhetherDownload(boolean whetherDownload) {
		WhetherDownload = whetherDownload;
	}
	
	/**
	 * @return the technicalDataID
	 */
	public int getTechnicalDataID() {
		return TechnicalDataID;
	}
	/**
	 * @return the deviceID
	 */
	public int getDeviceID() {
		return DeviceID;
	}
	/**
	 * @param technicalDataID the technicalDataID to set
	 */
	public void setTechnicalDataID(int technicalDataID) {
		TechnicalDataID = technicalDataID;
	}
	/**
	 * @param deviceID the deviceID to set
	 */
	public void setDeviceID(int deviceID) {
		DeviceID = deviceID;
	}
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
