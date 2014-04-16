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
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected abstract void onPostExecute(ArrayList<HashMap<String, String>> result);
		
	}
}
