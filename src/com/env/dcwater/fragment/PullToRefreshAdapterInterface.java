package com.env.dcwater.fragment;

import java.util.List;
/**
 * 添加自定义adapter的数据接口
 * @author Administrator
 *
 */
public interface PullToRefreshAdapterInterface {
	public<T> void datasetNotification(List<T> data);

}
