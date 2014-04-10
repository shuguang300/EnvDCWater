package com.env.dcwater.util;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;


/**
 * @author sk
 * @see
 */
public class DataCenterHelper {
	public static final String TAG_STRING = "DataCenterHelper";
	
	/**
	 * 使用soap协议请求数据时的namespace
	 */
	public static final String SOAP_NAMESPACE_STRING = "http://tempuri.org/";
	
	/**
	 * webservice数据中心地址
	 */
	public static final String URL_STRING = "http://192.168.0.104/dcwater/MobileDataCenter.asmx";
	
	/**
	 * 连接超时
	 */
	public static final int CONNECTION_TIMEOUT_INTEGER = 60000;
	
	/**
	 * 读取超时
	 */
	public static final int SO_TIMEOUT_INTEGER = 90000;
	
	/**
	 * 请求错误
	 */
	public static final String RESPONSE_FALSE_STRING = "false";
	
	/**
	 * @param method 请求的webservice的方法
	 * @param params 请求的参数
	 * @return 返回请求字符串
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String HttpPostData(String method,JSONObject params) throws ClientProtocolException, IOException{
		HttpPost request = new HttpPost(URL_STRING+"/"+method);
		request.addHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8");
		request.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT_INTEGER);
		request.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT_INTEGER);
		if(params!=null){
			HttpEntity paramEntity = new StringEntity(params.toString(),HTTP.UTF_8);
			request.setEntity(paramEntity);		
		}
		HttpResponse response =  new DefaultHttpClient().execute(request);
		if(response.getStatusLine().getStatusCode()==200){
			return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		}else {
			return RESPONSE_FALSE_STRING;
		}
	}
	
	/**
	 * @param method 请求的webservice的方法
	 * @param params 请求的参数
	 * @return 返回请求的soap对象
	 * @throws HttpResponseException
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public static SoapObject SoapRequest(String method,HashMap<String, String> params) throws HttpResponseException, IOException, XmlPullParserException{
		SoapObject request = new SoapObject(SOAP_NAMESPACE_STRING, method);
		if(params!=null){
			Iterator<?> iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iterator.next();
				request.addProperty(entry.getKey().toString(), entry.getValue());
			}
		}
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		HttpTransportSE ht = new HttpTransportSE(URL_STRING, CONNECTION_TIMEOUT_INTEGER);
		ht.debug = true;
		ht.call(SOAP_NAMESPACE_STRING+method, envelope);
		SoapObject soapObject = (SoapObject)envelope.bodyIn;
		return soapObject;
	}
}
