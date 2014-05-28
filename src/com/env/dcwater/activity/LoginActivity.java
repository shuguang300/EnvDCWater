package com.env.dcwater.activity;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.transport.HttpResponseException;
import org.xmlpull.v1.XmlPullParserException;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.env.dcwater.R;
import com.env.dcwater.component.DCWaterApp;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.util.DataCenterHelper;
import com.env.dcwater.util.OperationMethod;

/**
 * 登录窗口
 * @author sk
 */
public class LoginActivity extends NfcActivity implements OnClickListener{
	
	public static final String TAG_STRING = "LoginActivity";
	public static final String ACTION_STRING = "com.env.dcwater.activity.LoginActivity";
	
	private Button loginButton,resetButton;
	private Intent userRightIntent;
	private EditText accountView,passwordView;
	private String mAccount,mPassword;
	private HashMap<String, String> user;
	private ProgressDialog mLoginProgressDialog;
	private LoginAsyncTask mLoginAsyncTask;
	private SharedPreferences sp;
	private Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		ini();
		
		if(sp.getBoolean(DCWaterApp.PREFERENCE_ISLOGIN_STRING, false)){
			user = OperationMethod.getLocalUserInfo(sp);
			accountView.setText(user.get("UserName"));
			passwordView.setText(user.get("UserPassword"));
			onClick(loginButton);
		}
		
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}
	
	/**
	 * 初始化控件，设置控件的点击事件和数据
	 */
	private void ini(){
		sp = getSharedPreferences(DCWaterApp.PREFERENCE_STRING, Context.MODE_PRIVATE);
		editor = sp.edit();
		
		loginButton = (Button)findViewById(R.id.activity_login_submit);
		loginButton.setOnClickListener(this);
		resetButton = (Button)findViewById(R.id.activity_login_reset);
		resetButton.setOnClickListener(this);
		
		accountView = (EditText)findViewById(R.id.activity_login_account);
		passwordView = (EditText)findViewById(R.id.activity_login_password);
	}
	
	/**
	 * 登录成功，进入到MainActivity界面
	 */
	private void entranceMainActivity(){
		userRightIntent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(userRightIntent);
		overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out); 
		finish();
	}
	
	/**
	 * 本地验证账号密码不为空后，使用异步方法开始登录
	 */
	private void startLogin(){
		if(getInputUserInfo()){
			mLoginAsyncTask = new LoginAsyncTask();
			mLoginAsyncTask.execute("");	
		}else {
			Toast.makeText(LoginActivity.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 获取用户输入的账号密码
	 */
	private boolean getInputUserInfo(){
		mAccount = accountView.getText().toString().toLowerCase(Locale.CHINA).trim();
		mPassword = passwordView.getText().toString().toLowerCase(Locale.CHINA).trim();
		if(mAccount.equals("")||mPassword.equals("")){
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * 登录验证时，弹出等待窗口s
	 */
	private void showProgressDialog() {
		if(mLoginProgressDialog == null){
			mLoginProgressDialog = new ProgressDialog(LoginActivity.this);
			mLoginProgressDialog.setTitle("请稍后");
			mLoginProgressDialog.setMessage("正在验证用户名和密码");
			mLoginProgressDialog.setIndeterminate(true);
			mLoginProgressDialog.setCancelable(true);
			mLoginProgressDialog.setCanceledOnTouchOutside(false);
		}
		if(!mLoginProgressDialog.isShowing()){
			mLoginProgressDialog.show();
		}
	}
	/**
	 * 取消时，退出对话框
	 */
	private void hideProgressDialog(){
		if(mLoginProgressDialog!=null){
			mLoginProgressDialog.cancel();
		}
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_login_submit:
			startLogin();
			break;
		case R.id.activity_login_reset:
			accountView.setText("");
			passwordView.setText("");
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			onBackPressed();
			return true;
		}else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	@Override
	public void onBackPressed() {
		if(mLoginAsyncTask!=null&&mLoginAsyncTask.getStatus()==AsyncTask.Status.RUNNING){
			mLoginAsyncTask.cancel(true);
		}else {
			this.finish();
		}
	}
	
	/**
	 * 登录的异步方法
	 * @author sk
	 */
	class LoginAsyncTask extends AsyncTask<String, ProgressDialog, Integer>{
		@Override
		protected Integer doInBackground(String... params) {
			int status = 0;
			HashMap<String, String> param = new HashMap<String, String>();
			SoapObject soapObject = null;
			param.put("UserName", mAccount);
			param.put("Pwd", mPassword);
			try {
				soapObject = DataCenterHelper.SoapRequest("GetUserByNamePwd", param);
				if(soapObject==null){
					status = 0;
				}else if(soapObject.getPropertyAsString(0).equals("1")){
					status = 1;
				}else if (soapObject.getPropertyAsString(0).equals("2")) {
					status = 2;
				}else {
					 HashMap<String, String> map = OperationMethod.parseSoapObject(new JSONObject(soapObject.getPropertyAsString(0)),mAccount,mPassword);
					 if(map.get("AccountState").toString().equals("0")){
						 status = 3;
					 }else {
						 SystemParams.getInstance().setLoggedUserInfo(map);
						 OperationMethod.setLocalUserInfo(editor, map);
						 status = 4;
					}
				}
			} catch (HttpResponseException e) {
				e.printStackTrace();
				status = 0;
			} catch (IOException e) {
				e.printStackTrace();
				status = 2;
			} catch (XmlPullParserException e) {
				e.printStackTrace();
				status = 2;
			} catch (JSONException e) {
				e.printStackTrace();
				status = 2;
			}finally{
				
			}
			return status;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog();
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if(mLoginProgressDialog.isShowing()){
				hideProgressDialog();
				switch (result) {
				case 0:
					Toast.makeText(LoginActivity.this, "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
					break;
				case 1:
					Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(LoginActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
					break;
				case 3:
					Toast.makeText(LoginActivity.this, "您的账号未启用", Toast.LENGTH_SHORT).show();
					break;
				case 4:
					entranceMainActivity();
					break;
				}
			}
		}
	}
	
	
}
