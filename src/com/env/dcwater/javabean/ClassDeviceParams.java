package com.env.dcwater.javabean;
import com.google.gson.Gson;
/**
 * 设备技术参数
 * @author sk
 *
 */
public class ClassDeviceParams {
	private String ParameterName;
	private String ParameterValue;
	private String Remark;
	private int DeviceParameterID;
	private int DeviceID;
	/**
	 * @return the parameterName
	 */
	public String getParameterName() {
		return ParameterName;
	}
	/**
	 * @return the parameterValue
	 */
	public String getParameterValue() {
		return ParameterValue;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return Remark;
	}
	/**
	 * @param parameterName the parameterName to set
	 */
	public void setParameterName(String parameterName) {
		ParameterName = parameterName;
	}
	/**
	 * @param parameterValue the parameterValue to set
	 */
	public void setParameterValue(String parameterValue) {
		ParameterValue = parameterValue;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		Remark = remark;
	}
	
	/**
	 * @return the deviceParameterID
	 */
	public int getDeviceParameterID() {
		return DeviceParameterID;
	}
	/**
	 * @return the deviceID
	 */
	public int getDeviceID() {
		return DeviceID;
	}
	/**
	 * @param deviceParameterID the deviceParameterID to set
	 */
	public void setDeviceParameterID(int deviceParameterID) {
		DeviceParameterID = deviceParameterID;
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
