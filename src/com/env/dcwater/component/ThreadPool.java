package com.env.dcwater.component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.env.dcwater.util.DataCenterHelper;
import com.env.dcwater.util.OperationMethod;

/**
 * 线程及异步方法的管理 
 * @author sk
 */
public class ThreadPool {
	
	public static final String TAG_STRING = "ThreadPool";
	
	/**
	 * 获取构筑物列表数据的异步方法
	 * @author sk
	 */
	public static abstract class GetServerConsData extends AsyncTask<String, String, ArrayList<HashMap<String, String>>>{
		ArrayList<HashMap<String, String>> data ;
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
			JSONObject object = new JSONObject();
			try {
				object.put("PlantID", 1);
				String result = DataCenterHelper.HttpPostData("GetStructureByPlantID", object);
				if(!result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					JSONObject jsonObject = new JSONObject(result);
					data = OperationMethod.parseConsDataToList(jsonObject);
					SystemParams.getInstance().setConstructionList(data);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				data = null;
			} catch (IOException e) {
				e.printStackTrace();
				data = null;
			} catch (JSONException e) {
				e.printStackTrace();
				data = null;
			}
			return data;
		}
		
		@Override
		protected abstract void onPreExecute();
		
		
		@Override
		protected abstract void onPostExecute(ArrayList<HashMap<String, String>> result);
		
	}
	
	/**
	 * 获取单个设备的最新信息
	 * 执行时，需要提供2个参数
	 * 0 DeviceID
	 * 1 PlantID
	 * @author sk
	 *
	 */
	public static abstract class GetDeviceDetailData extends AsyncTask<String, String, HashMap<String, String>>{
		@Override
		protected HashMap<String, String> doInBackground(String... params) {
			HashMap<String, String> map = null;
			String result = DataCenterHelper.RESPONSE_FALSE_STRING;
			JSONObject param = new JSONObject();
			try {
				param.put("DeviceID", params[0]);
				param.put("PlantID", SystemParams.PLANTID_INT+"");
				result = DataCenterHelper.HttpPostData("GetDeviceInfo", param);
				if(!result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					JSONObject jsonObject = new JSONObject(result);
					map = OperationMethod.parseDeviceDataToHashMap(jsonObject);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				map = null;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				map = null;
			} catch (IOException e) {
				e.printStackTrace();
				map = null;
			}
			return map;
		}
		
		@Override
		protected abstract void onPreExecute();
		
		@Override
		protected abstract void onPostExecute(HashMap<String, String> result);
	}
	
	/**
	 * 养护流程的 发送，填报，审核工单
	 * @author sk
	 */
	public static abstract class UpkeepTaskUpdate extends AsyncTask<String, String, String>{
		
		public static final String METHOD_SEND_STRING = "InsertMaintainTasklist";
		public static final String METHOD_REPORT_STRING = "ChangePlanAs5ANDTaskAs2andInPeople";
		public static final String METHOD_APPROVE_STRING = "ChangePlanToOK";
		public static final String METHOD_NOTAPPROVE_STRING = "ChangePlanToNotOK";
		
		private JSONObject mParam;
		
		public UpkeepTaskUpdate (JSONObject param){
			mParam = param;
		}
		
		@Override
		protected String doInBackground(String... params) {
			String result = DataCenterHelper.RESPONSE_FALSE_STRING;
			try {
				if(params[0].equals(METHOD_SEND_STRING)){
					result = DataCenterHelper.HttpPostData(METHOD_SEND_STRING, mParam);
				}else if (params[0].equals(METHOD_REPORT_STRING)) {
					result = DataCenterHelper.HttpPostData(METHOD_REPORT_STRING, mParam);
				}else if (params[0].equals(METHOD_APPROVE_STRING)) {
					result = DataCenterHelper.HttpPostData(METHOD_APPROVE_STRING, mParam);
				}else if (params[0].equals(METHOD_NOTAPPROVE_STRING)) {
					result = DataCenterHelper.HttpPostData(METHOD_NOTAPPROVE_STRING, mParam);
				}
			} catch (Exception e) {
				
			}
			
			return result;
		}
		
		@Override
		public abstract void onPreExecute();
		
		@Override
		public abstract void onPostExecute(String result);
		
	}
	
	/**
	 * 获取登录用户的任务个数
	 * @author sk
	 *
	 */
	public static abstract class GetTaskCountByUserPositionID extends AsyncTask<Integer, String, JSONObject>{
		@Override
		protected JSONObject doInBackground(Integer... params) {
			String result = DataCenterHelper.RESPONSE_FALSE_STRING;
			JSONObject jsonObject = null;
			try {
				JSONObject param = new JSONObject();
				param.put("PlantID", SystemParams.PLANTID_INT);
				param.put("PositionID", params[0]);
				result = DataCenterHelper.HttpPostData("GetTaskCountByUserPositionID", param);
				if(!result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					JSONObject rootObject = new JSONObject(result);
					jsonObject = new JSONObject(rootObject.getString("d"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return jsonObject;
		}
		
		@Override
		public abstract void onPostExecute(JSONObject result);
	}

}
