package com.env.dcwater.component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.env.dcwater.R;
import com.env.dcwater.javabean.ClassRTWorkFlow;
import com.env.dcwater.util.DataCenterHelper;
import com.env.dcwater.util.LogicMethod;
import com.env.dcwater.util.OperationMethod;
import com.env.dcwater.util.SystemMethod;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
				object.put("PlantID", SystemParams.PLANTID_INT);
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
	public static abstract class GetDeviceDetailData extends AsyncTask<String, String, HashMap<String, ArrayList<HashMap<String, String>>>>{
		@Override
		protected HashMap<String, ArrayList<HashMap<String, String>>> doInBackground(String... params) {
			HashMap<String, ArrayList<HashMap<String, String>>> data = null;
			String result = DataCenterHelper.RESPONSE_FALSE_STRING;
			JSONObject param = new JSONObject();
			try {
				param.put("DeviceID", params[0]);
				param.put("PlantID", SystemParams.PLANTID_INT+"");
				result = DataCenterHelper.HttpPostData("GetDeviceInfo", param);
				if(!result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					JSONObject jsonObject = new JSONObject(result);
					JSONObject deviceJsonObject = new JSONObject(jsonObject.getString("d"));
					JSONObject devicePropertyJsonObject = new JSONObject(deviceJsonObject.getString("Decivice"));
					data = new HashMap<String, ArrayList<HashMap<String,String>>>();
					ArrayList<HashMap<String, String>> deviceproperty= new ArrayList<HashMap<String,String>>();
					ArrayList<HashMap<String, String>> devicemanage= new ArrayList<HashMap<String,String>>();
					ArrayList<HashMap<String, String>> deviceparam = new ArrayList<HashMap<String,String>>();
					ArrayList<HashMap<String, String>> devicefiles = new ArrayList<HashMap<String,String>>();
					ArrayList<HashMap<String, String>> devicestatus = new ArrayList<HashMap<String,String>>();
					
					HashMap<String, String> temp = OperationMethod.parseDevicePropertyToHashMap(devicePropertyJsonObject);
					
					deviceproperty = OperationMethod.parseDevicePropertyToList(temp);
					devicemanage = OperationMethod.parseDevicePropertyToList(temp);
					
					if(!LogicMethod.getRightString(deviceJsonObject.getString("DecivicePara")).equals("")){
						JSONArray deviceParams = new JSONArray(deviceJsonObject.getString("DecivicePara"));
						deviceparam = OperationMethod.parseDeviceParamsToList(deviceParams);
					}
					if(!LogicMethod.getRightString(deviceJsonObject.getString("AnnualReportk")).equals("")){
						JSONArray deviceFiles = new JSONArray(deviceJsonObject.getString("AnnualReportk"));
						devicefiles = OperationMethod.parseDeviceFilesToList(deviceFiles);
					}
					if(!LogicMethod.getRightString(deviceJsonObject.getString("DeviceOperatingParameter")).equals("")){
						JSONArray deviceStatu = new JSONArray(deviceJsonObject.getString("DeviceOperatingParameter"));
						devicestatus = OperationMethod.parseDeviceStatuToList(deviceStatu);
					}
					data.put("DeviceProperty", deviceproperty);
					data.put("DeviceManage", devicemanage);
					data.put("DeviceParam", deviceparam);
					data.put("DeviceFile", devicefiles);
					data.put("DeviceStatu", devicestatus);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				data = null;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				data = null;
			} catch (IOException e) {
				e.printStackTrace();
				data = null;
			}
			return data;
		}
		
		@Override
		protected abstract void onPreExecute();
		
		@Override
		protected abstract void onPostExecute(HashMap<String, ArrayList<HashMap<String, String>>> result);
		
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
	
	
	/**
	 * @author sk
	 * @param  params[0] 本地路径
	 * @param  params[1] 文件名
	 * @param  params[2] 远程路径(路径全称，包括文件名)
	 */
	public static class GetDevicePic extends AsyncTask<String, Integer, File>{
		private ImageView imageView;
		private float size ;
		public GetDevicePic (ImageView iv,int dpi,Context context){
			imageView = iv;
			size = context.getResources().getDimension(R.dimen.ic_adapter_pic)*dpi/160;
		}
		
		@Override
		protected File doInBackground(String... params) {
			String path = params[0]+File.separator+params[1];
			File file = new File(path);
			if(file.exists()){
			}else {
				try {
					file = DataCenterHelper.HttpGetDownloadPng(params[1]);
				} catch (IOException e) {
					e.printStackTrace();
					file = null;
				}
			}
			return file;
		}
		
		@Override
		protected void onPostExecute(File result) {
			super.onPostExecute(result);
			if(result!=null){
				try {
					imageView.setImageBitmap(SystemMethod.compressBitmap(result,size ,size));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 下载技术文档
	 * @author sk
	 *
	 */
	public static class GetDeviceFile extends AsyncTask<String, Integer, String>{
		
		private ProgressBar mPb;
		private Button mBtn;
		
		public GetDeviceFile (ProgressBar pb,Button btn){
			mPb = pb;
			mBtn = btn;
		}

		@Override
		protected String doInBackground(String... params) {
			String result = DataCenterHelper.RESPONSE_FALSE_STRING;
			String fileUrl = null;
			try {
				fileUrl = DataCenterHelper.FILE_URL_STRING+"/"+URLEncoder.encode(params[0],HTTP.UTF_8);
			} catch (UnsupportedEncodingException e2) {
				e2.printStackTrace();
			}
			File folder = new File(SystemMethod.getDownloadFileFolderPath());
			if(!folder.exists()){
				folder.mkdirs();
			}
			File file = new File(SystemMethod.getDownloadFilePath(params[0]));
			URL url;
			HttpURLConnection conn = null;
			try {
				url = new URL(fileUrl.replace("+", "%20"));
				conn = (HttpURLConnection)url.openConnection();
				conn.setConnectTimeout(DataCenterHelper.CONNECTION_TIMEOUT_INTEGER);
				conn.setReadTimeout(DataCenterHelper.SO_TIMEOUT_INTEGER);
				conn.setRequestProperty(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded");
				InputStream is = conn.getInputStream();
				if(is.available()>0){
					FileOutputStream fos = new FileOutputStream(file);
					byte [] buffer = new byte[8192];
					int len = 0, hasRead=0 , max = is.available();
					while ((len = is.read(buffer))!=-1) {
						fos.write(buffer, 0, len);
						hasRead += len;
						mPb.setProgress(hasRead/max);
					}
					fos.flush();
					fos.close();
					result = DataCenterHelper.RESPONSE_SUCCESS_STRING;
				}
				is.close();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
					file.delete();
				}
			}
			return result;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mPb.setVisibility(View.VISIBLE);
			mBtn.setVisibility(View.GONE);
			mPb.setProgress(0);
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			mPb.setProgress(values[0]);
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mPb.setVisibility(View.GONE);
			mBtn.setVisibility(View.VISIBLE);
			if(result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)){
				mBtn.setText("下载失败");
			}else {
				mBtn.setText("打开");
			}
		}
	}
	
	/**
	 * 获得维修单的工作流
	 * 执行时，请提供一个RepairTaskID
	 * @author Administrator
	 */
	public abstract static class GetRepairTaskWorkFlow extends AsyncTask<String, Integer, ArrayList<ClassRTWorkFlow>>{
		@Override
		protected ArrayList<ClassRTWorkFlow> doInBackground(String... params) {
			JSONObject param = new JSONObject();
			String result = DataCenterHelper.RESPONSE_FALSE_STRING;
			ArrayList<ClassRTWorkFlow> data = null;
			try {
				param.put("RepairTaskID", params[0]);
				result = DataCenterHelper.HttpPostData("getWorkFlowForRepairTaskID", param);
				if(result.equalsIgnoreCase(DataCenterHelper.RESPONSE_FALSE_STRING)){
					Gson gson = new Gson();
					JSONObject jsonObject = new JSONObject(result);
					data = gson.fromJson(jsonObject.getString("d"), new TypeToken<ArrayList<ClassRTWorkFlow>>(){}.getType());
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return data;
		}
		@Override
		public abstract void onPreExecute();
		
		@Override
		public abstract void onPostExecute(ArrayList<ClassRTWorkFlow> result);
	}
}
