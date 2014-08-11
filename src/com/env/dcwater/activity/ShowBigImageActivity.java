package com.env.dcwater.activity;
import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.env.dcwater.R;
import com.env.dcwater.component.WaterApplication;
import com.env.dcwater.component.NfcActivity;

public class ShowBigImageActivity extends NfcActivity{
	
	public static final String ACTION_STRING = "com.env.dcwater.activity.ShowBigImageActivity";
	
	private ImageView imageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//      this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_showbigimage);
		imageView = (ImageView)findViewById(R.id.activity_showbigimage);
		String fileName = getIntent().getExtras().getString("file");
		if(fileName.equals("")){
			imageView.setImageResource(R.drawable.ic_pic_default);
		}else {
			File file ;
			if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)){
				StringBuilder sb = new StringBuilder();
				sb.append(Environment.getExternalStorageDirectory().getAbsolutePath()).append(File.separator)
				.append(WaterApplication.ROOT_PATH_STRING).append(File.separator).append(WaterApplication.CACHE_PATH_STRING)
				.append(File.separator).append(fileName);
				file = new File(sb.toString());
				if(file.exists()){
					imageView.setImageURI(Uri.fromFile(file));
				}else {
					imageView.setImageResource(R.drawable.ic_pic_default);
				}
			}else {
				imageView.setImageResource(R.drawable.ic_pic_default);
			}
		}
		
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_UP){
			onBackPressed();
			return true;
		}else {
			return super.onTouchEvent(event);
		}
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out); 
	}
}
