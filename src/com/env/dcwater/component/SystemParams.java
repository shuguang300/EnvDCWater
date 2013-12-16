package com.env.dcwater.component;

public class SystemParams {
	// ����SystemParams����ģʽ�����Դ洢ϵͳ����
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
