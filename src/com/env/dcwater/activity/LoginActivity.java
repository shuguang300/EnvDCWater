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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.env.dcwater.R;
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
	
	private Button loginButton;
	private Intent userRightIntent;
	private EditText accountView,passwordView;
	private String mAccount,mPassword;
	private ProgressDialog mLoginProgressDialog;
	private LoginAsyncTask mLoginAsyncTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ini();
		
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
		loginButton = (Button)findViewById(R.id.activity_login_submit);
		loginButton.setOnClickListener(this);
		
		accountView = (EditText)findViewById(R.id.activity_login_account);
		passwordView = (EditText)findViewById(R.id.activity_login_password);
	}
	
	/**
	 * 登录成功，进入到MainActivity界面
	 */
	private void entranceMainActivity(){
		userRightIntent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(userRightIntent);
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
//			SystemMethod.hideSoftInput(LoginActivity.this);
			startLogin();
//			entranceMainActivity();
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
	class LoginAsyncTask extends AsyncTask<String, ProgressDialog, SoapObject>{
		@Override
		protected SoapObject doInBackground(String... params) {
			HashMap<String, String> param = new HashMap<String, String>();
			SoapObject soapObject = null;
			param.put("UserName", mAccount);
			param.put("Pwd", mPassword);
			try {
				soapObject = DataCenterHelper.SoapRequest("GetUserByNamePwd", param);
			} catch (HttpResponseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			}finally{
				
			}
			return soapObject;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog();
		}
		
		@Override
		protected void onPostExecute(SoapObject result) {
			super.onPostExecute(result);
			if(mLoginProgressDialog.isShowing()){
				if(result==null){
					Toast.makeText(LoginActivity.this, "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
				}else {
					if(result.getPropertyAsString(0).equals("1")){
						Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
					}else if(result.getPropertyAsString(0).equals("2")){
						Toast.makeText(LoginActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
					}else {
						HashMap<String, String> map = null;
						try {
							map = OperationMethod.parseSoapObject(new JSONObject(result.getPropertyAsString(0)),mAccount,mPassword);
						} catch (JSONException e) {
							map = null;
							e.printStackTrace();
						}
						if(map==null){
							Toast.makeText(LoginActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
						}else {
							if(map.get("AccountState").toString().equals("0")){
								Toast.makeText(LoginActivity.this, "您的账号未启用", Toast.LENGTH_SHORT).show();
							}else {
								SystemParams.getInstance().setLoggedUserInfo(map);
								entranceMainActivity();
							}
						}
					}
				}
			}
			hideProgressDialog();
		}
	}
	
	
}
