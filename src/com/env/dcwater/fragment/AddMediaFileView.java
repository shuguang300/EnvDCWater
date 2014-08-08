package com.env.dcwater.fragment;
import java.io.File;
import java.util.Calendar;
import com.env.dcwater.R;
import com.env.dcwater.component.WaterApplication;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * 在数据录入界面中，可以输入文本，语音，图像
 * @author sk
 */
public class AddMediaFileView extends PopupWindow implements OnClickListener{
	
	private View mView;
	private Context mContext;
	private Button btVoice,btPhoto,btLocation,btTakePhoto,btFile,btVideo;
	private String folderPath;
	
	/**
	 * 构造函数
	 * @param context
	 */
	public AddMediaFileView(Context context){
		super(context);
		mContext = context;
		mView = LayoutInflater.from(context).inflate(R.layout.view_addmediafile, null);
		iniView();
	}
	
	public AddMediaFileView(Context context,AttributeSet attr){
		super(context, attr);
		mContext = context;
		mView = LayoutInflater.from(context).inflate(R.layout.view_addmediafile, null);
		iniView();
	}
	
	/**
	 * 初始化控件
	 */
	private void iniView(){
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setAnimationStyle(R.style.PopupwindowAnim);
		btVoice = (Button)mView.findViewById(R.id.view_addmediafile_voice);
		btPhoto = (Button)mView.findViewById(R.id.view_addmediafile_photo);
		btLocation = (Button)mView.findViewById(R.id.view_addmediafile_location);
		btTakePhoto = (Button)mView.findViewById(R.id.view_addmediafile_takephoto);
		btFile = (Button)mView.findViewById(R.id.view_addmediafile_file);
		btVideo = (Button)mView.findViewById(R.id.view_addmediafile_video);
		btVoice.setOnClickListener(this);
		btPhoto.setOnClickListener(this);
		btTakePhoto.setOnClickListener(this);
		btFile.setOnClickListener(this);
		btVideo.setOnClickListener(this);
		btLocation.setOnClickListener(this);
		setBackgroundDrawable(new ColorDrawable(Color.argb(50, 52, 53, 55)));
		setOutsideTouchable(true);
		setContentView(mView);
		iniPath();
	}
	
	/**
	 * 
	 */
	private void iniPath(){
		String pathLevel1 = Environment.getExternalStorageDirectory().getAbsolutePath();
		String pathLevel2 = pathLevel1+File.separator+WaterApplication.ROOT_PATH_STRING+File.separator+WaterApplication.PICTURE_PATH_STRING;
		File folder = new File(pathLevel2);
		if(!folder.exists()){
			folder.mkdirs();
		}
		folderPath = folder.getAbsolutePath();
	}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.view_addmediafile_voice) {
			Toast.makeText(mContext, "添加录音", Toast.LENGTH_SHORT).show();
		} else if (id == R.id.view_addmediafile_takephoto) {
			String newPicturePath = folderPath+File.separator+Calendar.getInstance().getTimeInMillis()+".jpg";
			File newPictureFile = new File(newPicturePath);
			Intent capture = new Intent();
			capture.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			capture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newPictureFile));
			((Activity)mContext).startActivityForResult(capture,0);
		} else if (id == R.id.view_addmediafile_photo) {
			Intent photo = new Intent();
			photo.setAction(Intent.ACTION_GET_CONTENT);
			photo.addCategory(Intent.CATEGORY_OPENABLE);
			photo.setType("image/*");
			((Activity)mContext).startActivityForResult(photo, 0);
		} else if (id == R.id.view_addmediafile_video) {
			Toast.makeText(mContext, "添加视频", Toast.LENGTH_SHORT).show();
		} else if (id == R.id.view_addmediafile_file) {
			Intent fileIntent = new Intent();
			fileIntent.setAction(android.content.Intent.ACTION_GET_CONTENT);
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator);
			fileIntent.setDataAndType(Uri.fromFile(file), "file/*");
			((Activity)mContext).startActivityForResult(fileIntent,0);
		} else if (id == R.id.view_addmediafile_location) {
			Toast.makeText(mContext, "添加位置", Toast.LENGTH_SHORT).show();
		}
	}

}
