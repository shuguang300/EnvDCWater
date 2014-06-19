package com.env.dcwater.fragment;

import java.util.List;
/**
 * 添加自定义adapter的数据接口
 * @author Administrator
 *
 */
public interface PullToRefreshAdapterInterface <T> {
	public  void datasetNotification(List<T> data);

}
