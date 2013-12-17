package com.env.dcwater.javabean;

public class EnumList {
	/**
	 * @author Administrator
	 * �û�Ȩ��ö��
	 */
	public enum EnumUserRight{
		/**
		 * �豸��Ϣö�ٶ���
		 */
		MACHINEINFO(0,"�豸��Ϣ�鿴"),
		/**
		 * ����ö�ٶ���
		 */
		REPAIRMANAGE(1,"���޹���"),
		/**
		 * ά�޼�¼ö�ٶ���
		 */
		MAINTAINHISTORY(2,"ά����ʷ��¼"),
		/**
		 * ������¼ö�ٶ���
		 */
		UPKEEPHISTORY(3,"������ʷ��¼");
		
		/**
		 * Ȩ������
		 */
		public static final String RightName = "UserRightName";
		
		/**
		 *  Ȩ�޴���
		 */
		public static final String RightCode = "UserRightCode";
		private int mRightCode;
		private String mRightName;
		
		/**
		 * Ȩ��ö�ٶ���Ĺ��캯��
		 * @param code
		 * @param name
		 */
		private EnumUserRight(int code,String name){
			this.mRightCode = code;
			this.mRightName = name;
		}
		
		
		/**
		 * ����Ȩ�޴����ȡ��Ȩ�޵� ö�ٶ���
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
		 * ����Ȩ�޴���
		 * @return
		 */
		public int getCode() {
	        return this.mRightCode;
	    }
		/**����Ȩ������
		 * @return
		 */
		public String getName() {
			return this.mRightName;
		}
	}
}
