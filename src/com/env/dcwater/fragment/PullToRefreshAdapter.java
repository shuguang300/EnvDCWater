package com.env.dcwater.fragment;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * 添加自定义adapter的数据接口
 * @author Administrator
 *
 */
public interface PullToRefreshAdapter {
	
	public void datasetNotification(ArrayList<HashMap<String, String>> data);

}
