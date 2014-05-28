package com.env.dcwater.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.env.dcwater.R;
import com.env.dcwater.component.NfcActivity;
import com.env.dcwater.component.SystemParams;
import com.env.dcwater.component.ThreadPool;
import com.env.dcwater.component.ThreadPool.GetDevicePic;
import com.env.dcwater.fragment.DataFilterView;
import com.env.dcwater.fragment.ListviewItemAdapter;
import com.env.dcwater.fragment.PullToRefreshView;
import com.env.dcwater.fragment.PullToRefreshView.IXListViewListener;
import com.env.dcwater.javabean.EnumList;
import com.env.dcwater.util.DataCenterHelper;
import com.env.dcwater.util.OperationMethod;
import com.env.dcwater.util.SystemMethod;

/**
 * 设备维修管理
 * 
 * @author sk
 */
public class RepairManageActivity extends NfcActivity implements IXListViewListener, OnItemClickListener {

	public static final String TAG_STRING = "RepairManageActivity";

	/**
	 * 操作工新增工单
	 */
	public static final String METHOD_ADD_STRING = "Add";

	/**
	 * 操作工删除工单
	 */
	// public static final String METHOD_DELETE_STRING = "Delete";

	/**
	 * 操作工更新工单
	 */
	public static final String METHOD_UPDATE_STRING = "Update";

	/**
	 * 查看工单详情
	 */
	public static final String METHOD_DETAIL_STRING = "Detail";

	/**
	 * 设备科长发送维修单
	 */
	public static final String METHOD_SENDTASK_STRING = "SendTask";

	/**
	 * 机修工确认接收报修单
	 */
	public static final String METHOD_RECEIVE_STRING = "ReceiveTask";

	/**
	 * 机修工填写维修单
	 */
	public static final String METHOD_REPAIRTASK_STRING = "RepairTask";

	/**
	 * 机修工返回修改维修单
	 */
	public static final String METHOD_REREPAIRTASK_STRING = "ReRepairTask";

	/**
	 * 设备科长审核
	 */
	public static final String METHOD_DDAPPROVE_STRING = "DDApprove";

	/**
	 * 生产科长确认报修单
	 */
	public static final String METHOD_PDCONFIRM_STRING = "PDConfirm";

	/**
	 * 生产科长审核
	 */
	public static final String METHOD_PDAPPROVE_STRING = "PDApprove";

	/**
	 * 厂长确认
	 */
	public static final String METHOD_PMAPPROVE_STRING = "PMApprove";

	/**
	 * 历史浏览
	 */
	public static final String METHOD_HISTORY_STRING = "History";

	/**
	 * 新增模式
	 */
	public static final int REPAIRMANAGE_ADD_INTEGER = 0;
	/**
	 * 一般模式
	 */
	public static final int REPAIRMANAGE_NORMAL_INTEGER = 1;
	/**
	 * 编辑模式
	 */
	public static final int REPAIRMANAGE_UPDATE_INTEGER = 2;
	/**
	 * 历史浏览模式
	 */
	public static final int REPAIRMANAGE_HISTORY_INTEGER = 3;

	private TextView menuMessage;
	private ActionBar mActionBar;
	private ThreadPool.GetServerConsData getServerConsData;
	private PullToRefreshView mListView;
	private DrawerLayout mDrawerLayout;
	private DataFilterView mDataFilterView;
	private RepairManageAdapter mListViewAdapter;
	private ArrayList<HashMap<String, String>> mData;
	private Intent sendedIntent;
	private GetServerTaskData getServerTaskData;
	private boolean isFilter = false, actionChooseIsShow = false;;
	private ProgressDialog mProgressDialog;
	private int userPositionID, dpi;
	private String[] dateFilters, nfcCardAction = { "查看设备信息", "设备故障上报" };
	private AlertDialog.Builder actionChoose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repairmanage);
		iniData();
		iniActionBar();
		iniView();
		getServerData();
	}

	/**
	 * 初始化actionbar
	 */
	private void iniActionBar() {
		mActionBar = getActionBar();
		SystemMethod.setActionBarHomeButton(true, mActionBar);
	}

	/**
	 * 初始化数据
	 */
	private void iniData() {
		dpi = SystemMethod.getDpi(getWindowManager());
		mData = new ArrayList<HashMap<String, String>>();
		userPositionID = Integer.valueOf(SystemParams.getInstance().getLoggedUserInfo().get("PositionID"));
		if (userPositionID == EnumList.UserRole.USERROLEEQUIPMENTOPERATION || userPositionID == EnumList.UserRole.USERROLEPRODUCTIONOPERATION) {
			isFilter = false;
		}
	}

	/**
	 * 从服务器上获取数据
	 */
	private void getServerData() {
		if (SystemParams.getInstance().getConstructionList() == null) {
			getServerConsData = new ThreadPool.GetServerConsData() {
				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					if (result != null) {
						String[] posList = new String[result.size()];
						for (int i = 0; i < result.size(); i++) {
							posList[i] = result.get(i).get("StructureName");
						}
						mDataFilterView.setPosList(posList, 0);
					}
				}

				@Override
				protected void onPreExecute() {

				}
			};
			getServerConsData.execute("");
		} else {
			ArrayList<HashMap<String, String>> result = SystemParams.getInstance().getConstructionList();
			String[] posList = new String[result.size()];
			for (int i = 0; i < result.size(); i++) {
				posList[i] = result.get(i).get("StructureName");
			}
			mDataFilterView.setPosList(posList, 0);
		}
		getServerTaskData();
	}

	/**
	 * 
	 */
	private void getServerTaskData() {
		getServerTaskData = new GetServerTaskData();
		getServerTaskData.execute("");
	}

	// /**
	// * 更新服务器端数据
	// * @param methodName
	// */
	// private void updateServerData(String methodName){
	// updateServerData = new UpdateServerData();
	// updateServerData.execute(methodName);
	// }

	/**
	 * 获取数据时，弹出进度对话框
	 * 
	 * @param cancelable
	 *            是否能被取消的操作
	 */
	private void showProgressDialog(boolean cancelable) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(RepairManageActivity.this);
			mProgressDialog.setTitle("获取数据中");
			mProgressDialog.setMessage("正在努力加载数据，请稍后");
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setCancelable(cancelable);
		}
		mProgressDialog.show();
	}

	/**
	 * 取消时，退出对话框
	 */
	private void hideProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.cancel();
		}
	}

	/**
	 * 弹出刷卡时的功能选择框
	 */
	private void showNfcActionChoose() {
		if (actionChoose == null) {
			actionChoose = new AlertDialog.Builder(this);
			actionChoose.setCancelable(true);
			actionChoose.setTitle("请选择").setSingleChoiceItems(nfcCardAction, -1, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:

						break;
					case 1:
						break;
					}
					dialog.dismiss();
					actionChooseIsShow = false;
					Toast.makeText(RepairManageActivity.this, nfcCardAction[which], Toast.LENGTH_SHORT).show();
				}
			});
			actionChoose.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					actionChooseIsShow = false;
				}
			});
			actionChoose.create();
		}
		actionChoose.show();
		actionChooseIsShow = true;
	}

	/**
	 * 初始化view
	 */
	private void iniView() {
		mListView = (PullToRefreshView) findViewById(R.id.activity_repairmanage_info);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_repairmanage_drawlayout);
		mDataFilterView = (DataFilterView) findViewById(R.id.activity_repairmanage_datafilter);

		mDataFilterView.setSubmitEvent(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getServerTaskData = new GetServerTaskData();
				getServerTaskData.execute("");

				mDrawerLayout.closeDrawer(Gravity.RIGHT);
			}
		});

		mDataFilterView.hideTimeSelectionPart();

		mListViewAdapter = new RepairManageAdapter(mData);
		mListView.setAdapter(mListViewAdapter);
		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(this);

		mDataFilterView.setStateList(getResources().getStringArray(R.array.view_datafilter_repairstatelist), 0);
		mDrawerLayout.setDrawerListener(new DrawerListener() {
			@Override
			public void onDrawerStateChanged(int arg0) {
			}

			@Override
			public void onDrawerSlide(View arg0, float arg1) {
			}

			@Override
			public void onDrawerOpened(View arg0) {
				mListView.setEnabled(false);
				mListView.setPullRefreshEnable(false);
			}

			@Override
			public void onDrawerClosed(View arg0) {
				mListView.setEnabled(true);
				mListView.setPullRefreshEnable(true);
			}
		});

		// mListView.setOnItemLongClickListener(this); //长按事件与上下文菜单冲突，二者只能选其一
		// registerForContextMenu(mListView);
	}

	/**
	 * 根据code进行activity的跳转，并携带该报修单的信息
	 * 
	 * @param code
	 * @param data
	 * @param methodName
	 */
	private void sendIntent(int code, HashMap<String, String> data) {
		switch (code) {
		case REPAIRMANAGE_ADD_INTEGER:
			sendedIntent = new Intent(RepairManageActivity.this, RepairManageItemActivity.class);
			break;
		case REPAIRMANAGE_NORMAL_INTEGER:
			sendedIntent = new Intent(RepairManageActivity.this, RepairManageItemActivity.class);
			break;
		}
		sendedIntent.putExtra("RequestCode", code);
		sendedIntent.putExtra("Data", data);
		startActivityForResult(sendedIntent, code);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			getServerTaskData();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
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
		if (userPositionID == EnumList.UserRole.USERROLEEQUIPMENTOPERATION || userPositionID == EnumList.UserRole.USERROLEPRODUCTIONOPERATION) {
			if (!actionChooseIsShow) {
				showNfcActionChoose();
			}
		} else {

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_repairmanage, menu);
		menuMessage = (TextView) menu.getItem(2).getActionView();
		menuMessage.setTextColor(getResources().getColor(R.color.white));
		// menuMessage.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,
		// getResources().getDimension(R.dimen.small));
		if (userPositionID == EnumList.UserRole.USERROLEREPAIRMAN || userPositionID == EnumList.UserRole.USERROLEPRODUCTIONOPERATION) {
			menu.getItem(0).setVisible(true);
		} else {
			menu.getItem(0).setVisible(false);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		mDrawerLayout.closeDrawer(Gravity.RIGHT);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.menu_repairmanage_add:
			sendIntent(REPAIRMANAGE_ADD_INTEGER, null);
			break;
		case R.id.menu_repairmanage_filter:
			isFilter = !isFilter;
			item.setChecked(isFilter);
			getServerTaskData();
			break;
		case R.id.menu_repairmanage_drawlayout:
			if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
				mDrawerLayout.closeDrawer(Gravity.RIGHT);
			} else {
				mDrawerLayout.openDrawer(Gravity.RIGHT);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// 不需要为headerview注册上下文菜单，所以进行判断
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		if (info.position != 0 && mData.get(info.position - 1).get("CanUpdate").equals("true")) {
			int positionID = Integer.valueOf(SystemParams.getInstance().getLoggedUserInfo().get("PositionID"));
			int taskState = Integer.valueOf(mData.get(info.position - 1).get("State"));
			// int taskType =
			// Integer.valueOf(mData.get(info.position-1).get("RepairTaskType"));
			switch (positionID) {
			case EnumList.UserRole.USERROLEPLANTER:
				getMenuInflater().inflate(R.menu.cm_rm_pm, menu);
				break;
			case EnumList.UserRole.USERROLEEQUIPMENTOPERATION:
			case EnumList.UserRole.USERROLEPRODUCTIONOPERATION:
				getMenuInflater().inflate(R.menu.cm_rm_op, menu);
				break;
			case EnumList.UserRole.USERROLEEQUIPMENTCHIEF:
				getMenuInflater().inflate(R.menu.cm_rm_dd, menu);
				if (taskState == EnumList.RepairState.STATEHASBEENCONFIRMED || taskState == EnumList.RepairState.STATEHASBEENREPORTED || taskState == EnumList.RepairState.STATEHASBEENDISTRIBUTED) {
					menu.getItem(0).setVisible(true);
					menu.getItem(1).setVisible(false);
				} else {
					menu.getItem(0).setVisible(false);
					menu.getItem(1).setVisible(true);
				}
				break;
			case EnumList.UserRole.USERROLEPRODUCTIONCHIEF:
				getMenuInflater().inflate(R.menu.cm_rm_pd, menu);
				if (taskState == EnumList.RepairState.STATEHASBEENREPORTED) {
					menu.getItem(0).setVisible(true);
					menu.getItem(1).setVisible(false);
				} else {
					menu.getItem(0).setVisible(false);
					menu.getItem(1).setVisible(true);
				}
				break;
			case EnumList.UserRole.USERROLEREPAIRMAN:
				getMenuInflater().inflate(R.menu.cm_rm_rm, menu);
				if (taskState == EnumList.RepairState.STATEHASBEENDISTRIBUTED) {
					menu.getItem(0).setVisible(true);
					menu.getItem(1).setVisible(false);
				} else {
					menu.getItem(0).setVisible(false);
					menu.getItem(1).setVisible(true);
				}
				break;
			}
			menu.setHeaderTitle("更多操作");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// 获得contextmenu的触发控件
		// AdapterContextMenuInfo
		// info=(AdapterContextMenuInfo)item.getMenuInfo();
		// selectedPos = info.position-1;
		return super.onContextItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
			mDrawerLayout.closeDrawer(Gravity.RIGHT);
		} else {
			this.finish();
		}
	}

	@Override
	public void onRefresh() {
		getServerTaskData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position != 0) {
			sendIntent(REPAIRMANAGE_NORMAL_INTEGER, mData.get(position - 1));
		}
	}

	private class RepairManageAdapter extends ListviewItemAdapter {

		public RepairManageAdapter(ArrayList<HashMap<String, String>> data) {
			super(data);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final HashMap<String, String> map = getItem(position);
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(RepairManageActivity.this).inflate(R.layout.item_listview, null);
				viewHolder.lefttop = (TextView) convertView.findViewById(R.id.item_listview_lefttop);
				viewHolder.righttop = (TextView) convertView.findViewById(R.id.item_listview_righttop);
				viewHolder.leftbottom = (TextView) convertView.findViewById(R.id.item_listview_leftbottom);
				viewHolder.pic = (ImageView) convertView.findViewById(R.id.item_listview_pic);
				viewHolder.arrow = (ImageView) convertView.findViewById(R.id.item_listview_rightbottom);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (map.get("PicURL").equals("")) {
				viewHolder.pic.setImageResource(R.drawable.ic_pic_default);
			} else {
				GetDevicePic getDevicePic = new GetDevicePic(viewHolder.pic, dpi, RepairManageActivity.this);
				getDevicePic.execute(SystemMethod.getLocalTempPath(), map.get("PicURL").toString(), DataCenterHelper.PIC_URL_STRING + "/" + map.get("PicURL").toString());
			}
			viewHolder.lefttop.setText(map.get("DeviceName") + "(" + map.get("InstallPosition") + ")");
			viewHolder.leftbottom.setText(OperationMethod.getRepairTaskContent(map));
			viewHolder.righttop.setText(map.get("StateDescription"));
			viewHolder.arrow.setVisibility(map.get("CanUpdate").equalsIgnoreCase("true") ? View.VISIBLE : View.GONE);
			viewHolder.pic.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SystemMethod.startBigImageActivity(RepairManageActivity.this, map.get("PicURL"));
				}
			});
			return convertView;
		}
	}

	/**
	 * 获取服务器端的数据，并将数据放入 arraylist中
	 * 
	 * @author sk
	 */
	private class GetServerTaskData extends AsyncTask<String, String, ArrayList<HashMap<String, String>>> {

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
			JSONObject object = new JSONObject();
			ArrayList<HashMap<String, String>> data = null;
			try {
				object.put("PlantID", SystemParams.PLANTID_INT);
				object.put("UserRole", Integer.valueOf(SystemParams.getInstance().getLoggedUserInfo().get("UserRole")));
				String result = DataCenterHelper.HttpPostData("GetReportInfoList", object);
				if (!result.equals(DataCenterHelper.RESPONSE_FALSE_STRING)) {
					JSONObject jsonObject = new JSONObject(result);
					int rolePositionID = Integer.valueOf(SystemParams.getInstance().getLoggedUserInfo().get("PositionID"));
					dateFilters = mDataFilterView.getSelectCondition();
					int taskState = OperationMethod.getTaskStateByStateName(dateFilters[3]);
					data = OperationMethod.parseRepairTaskDataToList(rolePositionID, jsonObject, taskState, dateFilters[2], isFilter);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				data = null;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				data = null;
			} catch (IOException e) {
				e.printStackTrace();
				data = null;
			}
			return data;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog(true);
		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			super.onPostExecute(result);
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				int count = 0;
				if (result != null) {
					mData = result;
					mListViewAdapter.datasetNotification(mData);
					count = mData.size();
				} else {
					Toast.makeText(RepairManageActivity.this, "获取数据失败，请重试", Toast.LENGTH_SHORT).show();
				}
				mListView.stopRefresh();
				hideProgressDialog();
				menuMessage.setText("当前共有" + count + "条任务");
			}
		}

	}

}
