package com.env.dcwater.fragment;

import com.env.dcwater.R;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class PullToRefreshView extends ListView implements OnScrollListener{

	private OnScrollListener mScrollListener; //���listview��onscroll�¼�����
	private OnRefreshListener mOnRefreshListener; // ����ˢ�µ�ִ���¼�
	private Context mContext;
	private RelativeLayout mHeader;
	private TextView mHeaderTime,mHeaderStatu;
	private ImageView mHeaderArrow;
	private ProgressBar mHeaderProgressBar;
	private Animation mRotateUpAnim; //�ص���Ч
	private Animation mRotateDownAnim; //������Ч
	private Scroller mScroller;
	private int mHeaderHeight;
	private int mState = STATE_NORMAL;
	private boolean mEnablePullRefresh = true; //�Ƿ�����pull refresh
	private boolean mPullRefreshing = false; // �Ƿ�����pullrefresh
	private int mScrollBack;
	
	/**
	 * �ص��˶���
	 */
	private final int SCROLLBACK_HEADER = 0;
	/**
	 * �ص��˵ײ�
	 */
	private final int SCROLLBACK_FOOTER = 1;
	/**
	 * scroll back duration
	 */
	private final int SCROLL_DURATION = 500; 
	/**
	 * when pull up >= 50px
	 */
	private final int PULL_LOAD_MORE_DELTA = 50; 
	/**
	 * support iOS like pull
	 */
	private final float OFFSET_RADIO = 1.8f; 
	/**
	 * ��Чʱ��
	 */
	private final int ROTATE_ANIM_DURATION = 500; 
	
	/**
	 * ��ͨ״̬����ʾ����ˢ��
	 */
	public final static int STATE_NORMAL = 0;
	/**
	 * ����״̬���ɿ�����ˢ��
	 */
	public final static int STATE_READY = 1;
	/**
	 * ˢ��״̬����ʾ����ˢ��
	 */
	public final static int STATE_REFRESHING = 2;
	
	
	
	
	public PullToRefreshView(Context context) {
		super(context);
		mContext = context;
		iniHeader();
		addHeaderView(mHeader);
	}
	public PullToRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		iniHeader();
		addHeaderView(mHeader);
	}
	
	
	/**
	 * ��ʼ��lisrview��ui
	 */
	private void iniListView(){
		mScroller = new Scroller(mContext,new DecelerateInterpolator());
		super.setOnScrollListener(this);
		
	
	}
	/**
	 * ��ʼ������ˢ�µ�ͷ��ui
	 */
	private void iniHeader(){
		mHeader = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.view_pulltorefresh_header, null);
		mHeaderTime = (TextView)mHeader.findViewById(R.id.view_pulltorefresh_header_time);
		mHeaderStatu = (TextView)mHeader.findViewById(R.id.view_pulltorefresh_header_statu);
		mHeaderProgressBar = (ProgressBar)mHeader.findViewById(R.id.view_pulltorefresh_header_progressbar);
		mHeaderArrow = (ImageView)mHeader.findViewById(R.id.view_pulltorefresh_header_arrow);
		//���ö�����Ч����ת������ͬ״̬��ʱ����ͷ��λ�ã���Ȼ����Ҳ����ֱ��ʹ���滻ͼ��ķ���
		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
		
		mHeader.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				mHeaderHeight = mHeader.getHeight();
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
		
	}
	
	/**
	 * enable or disable pull down refresh feature.
	 * 
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // disable, hide the content
			mHeader.setVisibility(View.INVISIBLE);
		} else {
			mHeader.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void setOnScrollListener(OnScrollListener l) {
		super.setOnScrollListener(l);
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
		
	}
	
	
	/**
	 * �����ϴ�ˢ�µ�ʱ��
	 * @param arg0
	 */
	public void setRefreshTime(String arg0){
		mHeaderTime.setText(arg0);
	}
	/**
	 * ֹͣˢ��
	 */
	public void stopRefresh(){
		
	}
	
	/**
	 * ����header����ʾ״��
	 * @param statu
	 */
	public void setStatu(int state){
		if (state == mState) return ;
		if (state == STATE_REFRESHING) {	// ��ʾ����
			mHeaderArrow.clearAnimation();
			mHeaderArrow.setVisibility(View.INVISIBLE);
			mHeaderProgressBar.setVisibility(View.VISIBLE);
		} else {	// ��ʾ��ͷͼƬ
			mHeaderArrow.setVisibility(View.VISIBLE);
			mHeaderProgressBar.setVisibility(View.INVISIBLE);
		}
		switch(state){
		case STATE_NORMAL:
			if (mState == STATE_READY) {
				mHeaderArrow.startAnimation(mRotateDownAnim);
			}
			if (mState == STATE_REFRESHING) {
				mHeaderArrow.clearAnimation();
			}
			mHeaderStatu.setText(R.string.view_pulltorefresh_header_normal);
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				mHeaderArrow.clearAnimation();
				mHeaderArrow.startAnimation(mRotateUpAnim);
				mHeaderStatu.setText(R.string.view_pulltorefresh_header_ready);
			}
			break;
		case STATE_REFRESHING:
			mHeaderStatu.setText(R.string.view_pulltorefresh_header_loading);
			break;
		}
		mState = state;
	}
	
	
	/**
	 * @param height ����headerview�Ŀɼ��߶�
	 */
	public void setVisiableHeight(int height) {
		if (height < 0)height = 0;
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mHeader.getLayoutParams();
		lp.height = height;
		mHeader.setLayoutParams(lp);
	}
	
	
	/**
	 * @return headerview�ĸ߶�
	 */
	public int getVisiableHeight(){
		return mHeader.getHeight();
	}
	
	/**
	 * ���ˢ���¼�
	 * @author Administrator
	 *
	 */
	public interface OnRefreshListener{
		public void OnRefresh();
	}	

}

