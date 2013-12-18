package com.env.dcwater.fragment;

import com.env.dcwater.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
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
	private float mLastY = -1; // ��¼��һ��y���λ��
	/**
	 * �ص��˶���
	 */
	private final int SCROLLBACK_HEADER = 0;
	/**
	 * �ص��˵ײ�
	 */
	private final int SCROLLBACK_FOOTER = 1;
	/**
	 * �����ӳ�
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
	 * �����Ƿ���������
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
	
	/**
	 * ֹͣ���£�����header�ĸ߶�
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}
	
	/**
	 * ����headerview�Ŀɼ��߶�
	 */
	private void resetHeaderHeight() {
		int height = getVisiableHeight();
		if (height == 0) // ���ɼ�
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderHeight) {
			finalHeight = mHeaderHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height,SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}
	
	
	@Override
	public void setOnScrollListener(OnScrollListener l) {
		super.setOnScrollListener(l);
		mScrollListener = l;
	}
	
	@Override
	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				setVisiableHeight(mScroller.getCurrY());
			} else {
//				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
	}
	

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
		// send to user's listener
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,totalItemCount);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			System.out.println("���ݼ�⣺" + getFirstVisiblePosition() + "---->"
					+ getLastVisiblePosition());
			if (getFirstVisiblePosition() == 0
					&& (getVisiableHeight() > 0 || deltaY > 0)) {
				// the first item is showing, header has shown or pull down.
				updateHeaderHeight(deltaY / OFFSET_RADIO);
				invokeOnScrolling();
			} 
//			else if (getLastVisiblePosition() == mTotalItemCount - 1
//					&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
//				// last item, already pulled up or want to pull up.
//				updateFooterHeight(-deltaY / OFFSET_RADIO);
//			}
			break;
		default:
			mLastY = -1; // reset
			if (getFirstVisiblePosition() == 0) {
				// invoke refresh
				if (mEnablePullRefresh
						&& getVisiableHeight() > mHeaderHeight) {
					mPullRefreshing = true;
					setStatu(STATE_REFRESHING);
					if (mOnRefreshListener != null) {
						mOnRefreshListener.OnRefresh();
					}
				}
				resetHeaderHeight();
			}
//			if (getLastVisiblePosition() == mTotalItemCount - 1) {
//				// invoke load more.
//				if (mEnablePullLoad
//						&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
//					startLoadMore();
//				}
//				resetFooterHeight();
//			}
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}
	
	private void updateHeaderHeight(float delta) {
		setVisiableHeight((int) delta+getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // δ����ˢ��״̬�����¼�ͷ
			if (getVisiableHeight() >mHeaderHeight) {
				setStatu(STATE_READY);
			} else {
				setStatu(STATE_NORMAL);
			}
		}
		setSelection(0); // scroll to top each time
	}
	
	/**
	 * �����ϴ�ˢ�µ�ʱ��
	 * @param arg0
	 */
	public void setRefreshTime(String arg0){
		mHeaderTime.setText(arg0);
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
	 * you can listen ListView.OnScrollListener or this one. it will invoke
	 * onXScrolling when header/footer scroll back.
	 */
	public interface OnXScrollListener extends OnScrollListener {
		public void onXScrolling(View view);
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

