package com.env.dcwater.javabean;

import com.google.gson.Gson;

/**
 * 参数
 * @author sk
 */
public class ClassDeviceProperty {
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
