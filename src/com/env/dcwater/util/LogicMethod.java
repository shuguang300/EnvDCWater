package com.env.dcwater.util;

import java.util.Date;

/**
 * 一个用于存储 算法，一般逻辑方法的类
 * @author sk
 */
public class LogicMethod {
	public static final String TAG_STRING = "LogicMethod";
	public static final double EARTH_RADIUS = 6378137.0;
	
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
	
	
	private static double rad(double d) {
		  return d * Math.PI / 180.0;
	}
	/**
	 * 返回2个经纬度之间的距离,单位是米
	 * @param longitude1 位置1的精度
	 * @param latitude1 位置1的纬度
	 * @param longitude2 位置2的精度
	 * @param latitude2 位置2的纬度
	 * @return 
	 */
	public static double getDistance(double longitude1, double latitude1,double longitude2, double latitude2){
		double Lat1 = rad(latitude1);
		double Lat2 = rad(latitude2);
		double a = Lat1 - Lat2;
		double b = rad(longitude1) - rad(longitude2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)+ Math.cos(Lat1) * Math.cos(Lat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
	
	/**
	 * 接收一个字符串，如果字符串为null则返回""，否则返回原值
	 * @param arg0
	 * @return
	 */
	public static String getRightString(String arg0){
		if (arg0.toLowerCase().equals("null")) {
			return "";
		}else {
			return arg0;
		}
	}
	
	/**
	 * 接收一个字符串，将字符串转换为int型数据
	 * @param arg0
	 * @return
	 */
	public static int getRightInt(String arg0) {
		int count = 0;
		if(arg0.toLowerCase().equals("null")||arg0.length()==0){
			count = 0;
		}else {
			try {
				count = Integer.valueOf(arg0);
			} catch (Exception e) {
				count = 0;
			}
		}
		return count;
	}
	
	/**
	 * 获取2个时间间隔的描述信息
	 * @return
	 */
	public static String getTimeSpan(Date date1,Date date2){
		long timespan = Math.abs(date2.getTime()-date1.getTime())/3600000;
		String result = "";
		timespan = Math.abs(timespan);
		if(timespan<1&&timespan>=0){
			result = ((date2.getTime()-date1.getTime())/60000) +"分钟";
		}else if (timespan>=1&&timespan<24) {
			result = timespan + "小时";
		}else if (timespan>=24) {
			result = (timespan/24)+"天"+(timespan%24==0?"":timespan%24+"小时");
		}else {
			result = "";
		}
		return result;
	}
	
	/**
	 * 获取小时数的描述
	 * @param hours
	 * @return
	 */
	public static String getHoursDescrible(String hours){
		int hour = 0;
		String result ="";
		try{
			hour = Integer.parseInt(hours);
			if(hour<24){
				result = hour+"小时";
			}else {
				result = (hour/24)+"天"+(hour%24==0?"":hour%24+"小时");
			}
		}catch(Exception e){
			hour = 0;
		}
		return result;
	}
	
	/**
	 * 获取分钟的描述
	 * @param mins
	 * @return
	 */
	public static String getMinsDescrible(int mins){
		String result ="";
		if(mins>=0&&mins<60){
			result = mins + "分钟";
		}else if(mins>=60&&mins<24*60){
			result = (mins/60)+"小时"+(mins%60==0?"":mins%60+"分钟");
		}else {
			result = (mins/(60*24))+"天";
		}
		return result;
	}
	
	/**
	 * 返回文件的后缀名
	 * @param fileName
	 * @return
	 */
	public static String getFileExtensions(String fileName){
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos, fileName.length());
	}
}
