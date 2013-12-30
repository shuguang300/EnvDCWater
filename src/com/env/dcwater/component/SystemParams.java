package com.env.dcwater.component;

public class SystemParams {
	// 将系统常用变量放在该类当中
	private static SystemParams Instance = new SystemParams();

	public static SystemParams getInstance() {
		if (Instance == null) {
			Instance = new SystemParams();
		}
		return Instance;
	}

	private SystemParams() {

	}
}
