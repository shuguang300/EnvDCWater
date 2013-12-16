package com.env.dcwater.component;

public class SystemParams {
	// 创建SystemParams单例模式，用以存储系统变量
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
