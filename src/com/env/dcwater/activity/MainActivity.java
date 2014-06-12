package com.env.dcwater.activity;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.component.ThreadPool.GetTaskCountByUserPositionID;
import com.env.dcwater.fragment.NaviBarAdapter;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;
import com.env.dcwater.util.DataCenterHelper;
import com.env.dcwater.util.OperationMethod;
import com.env.dcwater.util.SystemMethod;

/**
 * 登录后的主界面，
 * @author sk
 */
public class MainActivity extends NfcActivity implements OnClickListener,IXListViewListener{
	
	public static final String TAG_STRING = "MainActivity";
	public static final String ACTION_STRING = "com.env.dcwater.activity.MainActivity";
	private long lastExitTime;
	private ArrayList<HashMap<String, String>> data;
	private ActionBar mActionBar;
	private TextView titleMessage;
	private DrawerLayout drawerLayout;
	private WebView webView;
	private PullToRefreshView naviListView;
	private NaviBarAdapter naviBarAdapter;
	private Button back,forward,refresh,stop;
	private ProgressBar progressBar;
	private GetTaskCountByUserPositionID getTaskCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iniActionBar();
		iniData();
		iniView();
	}
	
	/**
	 *  初始化actionbar
	 */
	private void iniActionBar(){
		mActionBar = getActionBar();
		mActionBar.setTitle(getResources().getString(R.string.app_name));
		SystemMethod.setActionBarHomeButton(true, mActionBar);
	}
	
	/**
	 *   初始化数据
	 */
	private void iniData(){
		data = OperationMethod.getViewByUserRole(Integer.valueOf(SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("PositionID")));
		SystemParams.getInstance().setUserRightData(data);
	}
	
	/**
	 * 初始化界面
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void iniView(){
		drawerLayout = (DrawerLayout)findViewById(R.id.activity_main_drawlayout);
		naviListView = (PullToRefreshView)findViewById(R.id.activity_main_navibar);
		webView = (WebView)findViewById(R.id.activity_main_showdata);
		back = (Button)findViewById(R.id.activity_main_back);
		forward = (Button)findViewById(R.id.activity_main_forward);
		refresh = (Button)findViewById(R.id.activity_main_refresh);
		stop = (Button)findViewById(R.id.activity_main_stop);
		progressBar = (ProgressBar)findViewById(R.id.activity_main_webviewprogress);
		
		naviBarAdapter = new NaviBarAdapter(MainActivity.this,data,ACTION_STRING){
			@Override
			public void doNothing() {
				drawerLayout.closeDrawer(Gravity.LEFT);
			}
		};
		naviListView.setAdapter(naviBarAdapter);
		naviListView.setOnItemClickListener(naviBarAdapter);
		naviListView.setXListViewListener(this);
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDefaultTextEncodingName("utf-8");
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url); 
				return true;
			}
			@Override
            public void onPageFinished(WebView view, String url) {
            	super.onPageFinished(view, url);
            	progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            	super.onPageStarted(view, url, favicon);
            	progressBar.setVisibility(View.VISIBLE);
            }
		});
		webView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				progressBar.setProgress(newProgress);
			}
		});
		webView.loadUrl(DataCenterHelper.URL_STRING+"/MobileMainPage.htm");
		back.setOnClickListener(this);
		forward.setOnClickListener(this);
		refresh.setOnClickListener(this);
		stop.setOnClickListener(this);
	}
	
	private void setTaskCountInfor(){
		getTaskCount = new GetTaskCountByUserPositionID() {
			@Override
			public void onPostExecute(JSONObject result) {
				if(result!=null){
					data = OperationMethod.addUserTaskCountInfor(data, result);
					SystemParams.getInstance().setUserRightData(data);
					naviBarAdapter.datasetNotification(data);
				}
			}
		};
		getTaskCount.execute(Integer.valueOf(SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("PositionID")));
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setTaskCountInfor();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		titleMessage = (TextView)menu.getItem(0).getActionView();
		titleMessage.setTextColor(getResources().getColor(R.color.white));
		titleMessage.setText(SystemParams.getInstance().getLoggedUserInfo(getApplicationContext()).get("PlantName"));
//		titleMessage.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.i_small));
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
				drawerLayout.closeDrawer(Gravity.LEFT);
			}else{
				drawerLayout.openDrawer(Gravity.LEFT);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
			drawerLayout.closeDrawer(Gravity.LEFT);
		}else {
			if(System.currentTimeMillis()-lastExitTime>2000){
				Toast.makeText(MainActivity.this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
			}else {
				SystemParams.getInstance().setLoggedUserInfo(null);
				finish();
				
			}
			lastExitTime = System.currentTimeMillis();	
		}
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_main_back:
			webView.goBack();
			break;
		case R.id.activity_main_forward:
			webView.goForward();
			break;
		case R.id.activity_main_refresh:
			webView.reload();
			break;
		case R.id.activity_main_stop:
			webView.stopLoading();
			break;
		}
		
	}

	@Override
	public void onRefresh() {
		setTaskCountInfor();
	}
	
}
