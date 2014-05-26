package com.env.dcwater.javabean;
import com.google.gson.Gson;

/**
 * 设备运行参数
 * @author sk
 */
public class ClassDeviceStatus {
	private int DeviceOperatingParameterID;
	private int DeviceSmallClassID;
	private String DeviceOperatingParameterName;
	private String DeviceOperatingParameterValue;
	/**
	 * @return the deviceOperatingParameterID
	 */
	public int getDeviceOperatingParameterID() {
		return DeviceOperatingParameterID;
	}
	/**
	 * @return the deviceSmallClassID
	 */
	public int getDeviceSmallClassID() {
		return DeviceSmallClassID;
	}
	/**
	 * @return the deviceOperatingParameterName
	 */
	public String getDeviceOperatingParameterName() {
		return DeviceOperatingParameterName;
	}
	/**
	 * @return the deviceOperatingParameterValue
	 */
	public String getDeviceOperatingParameterValue() {
		return DeviceOperatingParameterValue;
	}
	/**
	 * @param deviceOperatingParameterID the deviceOperatingParameterID to set
	 */
	public void setDeviceOperatingParameterID(int deviceOperatingParameterID) {
		DeviceOperatingParameterID = deviceOperatingParameterID;
	}
	/**
	 * @param deviceSmallClassID the deviceSmallClassID to set
	 */
	public void setDeviceSmallClassID(int deviceSmallClassID) {
		DeviceSmallClassID = deviceSmallClassID;
	}
	/**
	 * @param deviceOperatingParameterName the deviceOperatingParameterName to set
	 */
	public void setDeviceOperatingParameterName(String deviceOperatingParameterName) {
		DeviceOperatingParameterName = deviceOperatingParameterName;
	}
	/**
	 * @param deviceOperatingParameterValue the deviceOperatingParameterValue to set
	 */
	public void setDeviceOperatingParameterValue(String deviceOperatingParameterValue) {
		DeviceOperatingParameterValue = deviceOperatingParameterValue;
	}
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
		
	}
}
