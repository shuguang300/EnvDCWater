package com.env.dcwater.util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
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
	 * 服务器地址
	 */
	public static final String URL_STRING = "http://192.168.200.50/dcwater";
	/**
	 * webservice数据中心地址
	 */
//	public static final String URL_STRING = "http://183.81.180.26:8080/dcwater/MobileDataCenter.asmx";
	public static final String DATA_URL_STRING = "http://192.168.200.50/dcwater/MobileDataCenter.asmx";
	/**
	 * 设备文档的地址
	 */
	public static final String FILE_URL_STRING = "http://192.168.200.50/dcwater/PdfFiles/";
	/**
	 * 设备图片的地址
	 */
	public static final String PIC_URL_STRING = "http://192.168.200.50/dcwater/UploadImages/";
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
	 * 使用HttpClient的post方法获取数据
	 * @param method 请求的WebService的方法
	 * @param params 请求的参数
	 * @return 返回请求字符串
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String HttpPostData(String method,JSONObject params) throws ClientProtocolException, IOException{
		HttpPost request = new HttpPost(DATA_URL_STRING+"/"+method);
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
	 * 使用HttpClient的get方法获取数据
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String HttpGetData(String url) throws ClientProtocolException, IOException{
		HttpGet get = new HttpGet(url);
		get.addHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8");
		get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT_INTEGER);
		get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT_INTEGER);
		HttpResponse response = new DefaultHttpClient().execute(get);
		if(response.getStatusLine().getStatusCode()==200){
			return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		}else {
			return RESPONSE_FALSE_STRING;
		}
	}
	
	/**
	 * 使用HttpUrlConnection get的方法获取 服务器上的文件
	 * @param fileServerPath 服务器上的路径+文件名
	 * @param fileLocalPath 本地的路径
	 * @param fileName 文件名
	 * @throws IOException
	 */
	public static File HttpGetDownloadFile(String fileServerPath,String fileLocalPath,String fileName) throws IOException{
		File file = null ;
		URL url = new URL(fileServerPath);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(CONNECTION_TIMEOUT_INTEGER);
		conn.setReadTimeout(SO_TIMEOUT_INTEGER);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		if(conn.getResponseCode()==200){
			InputStream os = conn.getInputStream();
			if(os.available()>0){
				file = new File(fileLocalPath+File.separator+fileName);
				FileOutputStream fos = new FileOutputStream(file);
				byte [] temp = new byte[8192]; 
				int len = 0;
				while ((len =os.read(temp))!=-1) {
					fos.write(temp, 0, len);
				}
				fos.flush();
				fos.close();
			}
			os.close();
		}
		return file;
		
	} 
	
	/**
	 * 使用HttpUrlConnection post的方法获取 服务器上的文件
	 * @param filePath
	 * @throws IOException
	 */
	public static void HttpPostDownloadFile(String filePath) throws IOException{
		URL url = new URL(filePath);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(CONNECTION_TIMEOUT_INTEGER);
		conn.setReadTimeout(SO_TIMEOUT_INTEGER);
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(false);
	} 
	
	/**
	 * @param method 请求的WebService的方法
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
		HttpTransportSE ht = new HttpTransportSE(DATA_URL_STRING, CONNECTION_TIMEOUT_INTEGER);
		ht.debug = true;
		ht.call(SOAP_NAMESPACE_STRING+method, envelope);
		SoapObject soapObject = (SoapObject)envelope.bodyIn;
		return soapObject;
		
	}
}
