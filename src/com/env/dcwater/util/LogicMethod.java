package com.env.dcwater.util;

/**
 * 一个用于存储 算法，一般逻辑方法的类
 * @author sk
 */
public class LogicMethod {
	/**
	 * 判断是否是闰年
	 * 能够被4整出并且不被100整除或者能够被400整除的是闰年
	 * @param year
	 * @return
	 */
	public static boolean isLeapYear(int year){
		if((year % 4 ==0 && year % 100 !=0)|| year % 400 == 0){
			return true;
		}else {
			return false;
		}
	}
}
