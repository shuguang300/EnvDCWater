package com.env.dcwater.javabean;

public class EnumList {
	/**
	 * @author Administrator
	 * 用户权限枚举
	 */
	public enum EnumUserRight{
		/**
		 * 设备信息枚举对象
		 */
		MACHINEINFO(0,"设备信息查看"),
		/**
		 * 报修枚举对象
		 */
		REPAIRMANAGE(1,"报修管理"),
		/**
		 * 维修记录枚举对象
		 */
		MAINTAINHISTORY(2,"维修历史记录"),
		/**
		 * 保养记录枚举对象
		 */
		UPKEEPHISTORY(3,"保养历史记录");
		
		/**
		 * 权限名称
		 */
		public static final String RightName = "UserRightName";
		
		/**
		 *  权限代码
		 */
		public static final String RightCode = "UserRightCode";
		private int mRightCode;
		private String mRightName;
		
		/**
		 * 权限枚举对象的构造函数
		 * @param code
		 * @param name
		 */
		private EnumUserRight(int code,String name){
			this.mRightCode = code;
			this.mRightName = name;
		}
		
		
		/**
		 * 根据权限代码获取该权限的 枚举对象
		 * @param code
		 * @return
		 */
		public static EnumUserRight getEnumUserRight(int code) {
			switch (code) {
			case 0:
				return EnumUserRight.MACHINEINFO;
			case 1:
				return EnumUserRight.REPAIRMANAGE;
			case 2:
				return EnumUserRight.MAINTAINHISTORY;
			case 3:
				return EnumUserRight.UPKEEPHISTORY;
			}
			return null;
		}
		
		/**
		 * 返回权限代码
		 * @return
		 */
		public int getCode() {
	        return this.mRightCode;
	    }
		/**返回权限名称
		 * @return
		 */
		public String getName() {
			return this.mRightName;
		}
	}
}
