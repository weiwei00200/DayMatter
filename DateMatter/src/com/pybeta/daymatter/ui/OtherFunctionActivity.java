package com.pybeta.daymatter.ui;



import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.pybeta.daymatter.ApkInfo;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.core.DLAService;
import com.pybeta.daymatter.loader.ImageLoader;
import com.pybeta.daymatter.utils.JsonUtil;
import com.pybeta.daymatter.utils.LogUtil;
import com.pybeta.daymatter.utils.NetUtil;
import com.pybeta.daymatter.utils.ToastUtil;


public class OtherFunctionActivity extends SherlockFragment {

	View mainView = null;
	ListView mGameListView,mAppListView;
	View view_nodata;
//	 View view_refresh;

	ApkListAdapter mAppAdapter,mGameAdapter;
	ArrayList<ApkInfo> mApkList,mAppList,mGameList;
//	private CustomLoading fragment_adsfanyue_loading;
	private ProgressBar fragment_adsfanyue_loading;
	private Context mContext;
	private static final String SIZETIPS = "大小：";

	private static final String ADDR = "http://068api.icodestar.com/index.php?m=getWorksPush&type=???&worksType=???";
	private static final String HTTPPIC = "http://client.icodestar.com/upload/";
	private static final String ADDR2 = "http://068api.icodestar.com/index.php?m=setUploadCount&type=???&worksid=???&isGuest=???";

	public static final String LOADDATA_ACTION = "com.aohe.icodestar.filemanager.fileexplorer.fragmentadsfanyue.loaddata_action";
	static final String tag = "AppActivity";
	OnDownloadClickListener mOnDownloadClickListener;
	private ImageLoader mImageLoader;
	private AdsReceiver mAdsReceiver;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = getActivity();
		getSherlockActivity().getSupportActionBar().setTitle(R.string.otherfunction);
		mImageLoader = new ImageLoader();
		mOnDownloadClickListener = new OnDownloadClickListener();
		mAppList = null;
		mGameList = null;

		mainView = inflater.inflate(R.layout.fragment_adsfanyue, null);
		view_nodata = mainView.findViewById(R.id.fragment_adsfanyue__noapklistdata);
		
		mAppListView = (ListView) mainView.findViewById(R.id.fragment_adsfanyue_applist);
		mAppListView.setDividerHeight(0);
		mAppListView.setCacheColorHint(Color.TRANSPARENT);
		mAppListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				ViewHolder mViewHolder = null;
				if (view != null) {
					mViewHolder = (ViewHolder) view.getTag();
					if (mViewHolder != null) {
						ApkInfo mApkInfo = mApkList.get(position);
						int wordsId = mApkInfo.getWorksId();
						if(wordsId != -1 && wordsId != -2){
							boolean isDescShowing = mApkInfo.getDescShowing();
							if (isDescShowing)
								mViewHolder.ll_desc.setVisibility(View.GONE);
							else
								mViewHolder.ll_desc.setVisibility(View.VISIBLE);
//							mViewHolder.isDescShowing = !mViewHolder.isDescShowing;
							mApkInfo.setDescShowing(!isDescShowing);
						}
					}
				}
			}

		});


//		pb =  mainView.findViewById(R.id.fragment_adsfanyue_pb);
//		fragment_adsfanyue_loading = (CustomLoading) mainView.findViewById(R.id.fragment_adsfanyue_loading);
		fragment_adsfanyue_loading = (ProgressBar) mainView.findViewById(R.id.fragment_adsfanyue_loading);
//		fragment_adsfanyue_loading.setVisibility(View.VISIBLE);
		
		
		doDataPostAction(3);
		
		registerReceiver();
		
		return mainView;
	}
	
	private void registerReceiver(){
		if(mAdsReceiver == null){
			mAdsReceiver = new AdsReceiver();
			IntentFilter filter = new IntentFilter(LOADDATA_ACTION);
			mContext.registerReceiver(mAdsReceiver, filter);
		}
	}
	
	private void unregisterReceiver(){
		if(mAdsReceiver != null)
			mContext.unregisterReceiver(mAdsReceiver);
		mAdsReceiver = null;
	}

	class AdsReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if(action == null)
				return;
			if(LOADDATA_ACTION.equals(action)){
				doDataPostAction(3);
			}
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LogUtil.Sysout("onPause cainixihuan");
//		fragment_adsfanyue_loading.hide();
		fragment_adsfanyue_loading.setVisibility(View.GONE);
	}
	
	boolean isShowLoading = true;
	
	private void showLoading(boolean show) {
		LogUtil.Sysout("showLoading");
		if (show) {
//			fragment_adsfanyue_loading.showLoading();
			fragment_adsfanyue_loading.setVisibility(View.VISIBLE);
		} else {
			fragment_adsfanyue_loading.setVisibility(View.GONE);
		}
	}
	
	private void showErrorMsg(String msg) {
//		if (mDataList == null || mDataList.isEmpty()) {
//			fragment_adsfanyue_loading.showMsg(msg);
//		} else {
//			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//		}
	}
	
	private void doDataPostAction(final int worksType) {
		new AsyncTask<Void, Void, String>() {
			private AsyncTask<Void, Void, String> asyTask;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				this.asyTask = this;
				isShowLoading = true;
				showLoading(isShowLoading);
			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String json = null;
				try {
					ArrayList<String> param = new ArrayList<String>();
					param.add("type");
					param.add("worksType");
					ArrayList<String> values = new ArrayList<String>();
					values.add("android");
					values.add("" + worksType);
					json = NetUtil.dopost(mContext, ADDR, param, values);
					LogUtil.d("返回网络数据 response json = " + json);
				} catch (Exception e) {
					json = null;
					e.printStackTrace();
				}
				return json;
			}

			@Override
			protected void onPostExecute(String result) {
				LogUtil.Sysout("doDataPostAction   result: " + result);
//				pb.setVisibility(View.GONE);
				isShowLoading = false;
				showLoading(isShowLoading);
				
				try {
					if (result != null) {
						ArrayList<ApkInfo> mApkInfos = JsonUtil
								.getApkInfoPojo(result);
						if (mApkInfos != null) {
							if (mAppList != null) {
								mAppList.clear();
							} else
								mAppList = new ArrayList<ApkInfo>();
							if (mGameList != null) {
								mGameList.clear();
							} else
								mGameList = new ArrayList<ApkInfo>();
							if(mApkList != null)
								mApkList.clear();
							else
								mApkList = new ArrayList<ApkInfo>();
//							mArrayList.addAll(mApkInfos);
							
							String packageName = getActivity().getPackageName()
									.trim();
							for (ApkInfo t : mApkInfos) {
								if (packageName.equalsIgnoreCase(t
										.getPackageName().trim())) {
									mApkInfos.remove(t);
									break;
								}
							}
							for (ApkInfo t : mApkInfos) {
								if(t.getWorksType() == 1)
									mAppList.add(t);
								else if(t.getWorksType() == 2)
									mGameList.add(t);
							}
							LogUtil.Sysout("====size(): "+mAppList.size());
							
							
							if(mAppList != null && mAppList.size() > 0){
								ApkInfo apkInfo = new ApkInfo();
								apkInfo.setWorksId(-1);
								mAppList.add(0,apkInfo);
								mApkList.addAll(mAppList);
							}
							if(mGameList != null && mGameList.size() > 0){
								ApkInfo apkInfo = new ApkInfo();
								apkInfo.setWorksId(-2);
								mGameList.add(0,apkInfo);
								mApkList.addAll(mGameList);
							}
							
							if(mApkInfos != null && mApkInfos.size() >0){
								mAppAdapter = new ApkListAdapter(mApkList);
								mAppListView.setAdapter(mAppAdapter);
								mAppAdapter.notifyDataSetChanged();
								mAppListView.setVisibility(View.VISIBLE);
							}
							else{
								mAppListView.setVisibility(View.GONE);
							}
							
							if(mApkList == null || mApkList.size() == 0){
								view_nodata.setVisibility(View.GONE);
							}else{
								LogUtil.Sysout("view_nodata");
								view_nodata.setVisibility(View.VISIBLE);
							}
							
						} else {
							long errorcode = JsonUtil.errorPojo(result);
							LogUtil.Sysout("errorcode:" + errorcode);
							if (errorcode == 1000) {
								LogUtil.Sysout(
										mContext.getApplicationContext(),
										"数据更新失败！");
								ToastUtil.TextToast(
										mContext.getApplicationContext(),
										"数据更新失败!", Toast.LENGTH_SHORT);
							} else if (errorcode == 1001) {
								LogUtil.Sysout(
										mContext.getApplicationContext(),
										"无更新数据!");
								ToastUtil.TextToast(
										mContext.getApplicationContext(),
										"无更新数据!", Toast.LENGTH_SHORT);
							} else if (errorcode == 1001) {// 请求页数超过总分页数
								ToastUtil.TextToast(
										mContext.getApplicationContext(),
										"网络请求异常", Toast.LENGTH_SHORT);
								LogUtil.Sysout(
										mContext.getApplicationContext(),
										"网络请求异常");
							}
						}
					}
					
					if((mAppList == null || mAppList.size() == 0) && (mGameList == null  || mGameList.size() == 0)){
						mAppListView.setVisibility(View.GONE);
						view_nodata.setVisibility(View.VISIBLE);
						LogUtil.Sysout("view_nodata0");
					}else{
						view_nodata.setVisibility(View.INVISIBLE);
						mAppListView.setVisibility(View.VISIBLE);
					}
					
				} catch (Exception e) {
					view_nodata.setVisibility(View.VISIBLE);
					e.printStackTrace();
				}
			}
		}.execute((Void) null);

	}

	private void doUploadCountAction(final int worksid) {
		new AsyncTask<Void, Void, String>() {
			private AsyncTask<Void, Void, String> asyTask;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				this.asyTask = this;
			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String json = null;
				try {
					ArrayList<String> param = new ArrayList<String>();
					param.add("type");
					param.add("worksid");
					param.add("isGuest");
					ArrayList<String> values = new ArrayList<String>();
					values.add("android");
					values.add("" + worksid);
					values.add("" + 0);
					json = NetUtil.dopost(mContext, ADDR2, param, values);
					LogUtil.d("返回网络数据 response json = " + json);
				} catch (Exception e) {
					json = null;
					e.printStackTrace();
				}
				return json;
			}

			@Override
			protected void onPostExecute(String result) {
				LogUtil.Sysout("doUploadCountAction   result: " + result);
				if (result != null) {
					long ret = JsonUtil.getResultPojo(result);
					if (ret == 0)
						LogUtil.Sysout("uploadcount ok");
					else
						LogUtil.Sysout("uploadcount error0");
				} else
					LogUtil.Sysout("uploadcount error1");

			}
		}.execute((Void) null);

	}

	boolean isPbShowed = false;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showLoading(isShowLoading);
	}

//	@Override
//	public void onSelected() {
//		// TODO Auto-generated method stub
//		super.onSelected();
//		if (!isPbShowed) {
//			isPbShowed = true;
//			pb.setVisibility(View.VISIBLE);
//			mHandler.sendEmptyMessageDelayed(100, 1500);
//		}
//	}

	class OnDownloadClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(DLAService.APKDOWNLOAD_ACTION);
			ApkInfo mApkInfo = (ApkInfo) v.getTag();
			intent.putExtra("ApkInfo", mApkInfo);
			mContext.sendBroadcast(intent);
			doUploadCountAction(mApkInfo.getWorksId());
		}

	}

	class ApkListAdapter extends BaseAdapter {

		ArrayList<ApkInfo> mList = null;
		
		public ApkListAdapter(ArrayList<ApkInfo> mList){
			this.mList = new ArrayList<ApkInfo>(mList);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LogUtil.Sysout("position: "+position);
			return doGetView(mList,position, convertView);
		}

	}

	private View doGetView(ArrayList<ApkInfo> mList,int position, View convertView) {
		ViewHolder mViewHolder = null;
		ApkInfo mApkInfo = mList.get(position);
		if(mApkInfo.getWorksId() == -1){
			convertView = LayoutInflater.from(getActivity()).inflate(
					R.layout.fragment_adsfanyue_apptitle, null);
			TextView tv = (TextView)convertView.findViewById(R.id.fragment_adsfanyue_app);
			tv.setText(getResources().getString(R.string.user_ads_app));
		}else if(mApkInfo.getWorksId() == -2){
			convertView = LayoutInflater.from(getActivity()).inflate(
					R.layout.fragment_adsfanyue_apptitle, null);
			TextView tv = (TextView)convertView.findViewById(R.id.fragment_adsfanyue_app);
			tv.setText(getResources().getString(R.string.user_ads_game));
		}
		else{
			if (convertView == null) {
					convertView = LayoutInflater.from(getActivity()).inflate(
							R.layout.layout_apklistitem, null);
					mViewHolder = new ViewHolder();
					mViewHolder.apk_pic = (ImageView) convertView
							.findViewById(R.id.apk_pic);
					mViewHolder.tv_apkname = (TextView) convertView
							.findViewById(R.id.tv_apkname);
					mViewHolder.tv_apksize = (TextView) convertView
							.findViewById(R.id.tv_apksize);
					mViewHolder.tv_keyword = (TextView) convertView
							.findViewById(R.id.tv_keyword);
					mViewHolder.btn_download =  convertView
							.findViewById(R.id.btn_download);
					mViewHolder.ll_desc = convertView.findViewById(R.id.ll_desc);
					mViewHolder.tv_desc = (TextView) convertView
							.findViewById(R.id.tv_desc);
//					mViewHolder.ll_desc.setVisibility(View.GONE);
//					mViewHolder.isDescShowing = false;
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
				if(mViewHolder == null){
					convertView = LayoutInflater.from(getActivity()).inflate(
							R.layout.layout_apklistitem, null);
					mViewHolder = new ViewHolder();
					mViewHolder.apk_pic = (ImageView) convertView
							.findViewById(R.id.apk_pic);
					mViewHolder.tv_apkname = (TextView) convertView
							.findViewById(R.id.tv_apkname);
					mViewHolder.tv_apksize = (TextView) convertView
							.findViewById(R.id.tv_apksize);
					mViewHolder.tv_keyword = (TextView) convertView
							.findViewById(R.id.tv_keyword);
					mViewHolder.btn_download =  convertView
							.findViewById(R.id.btn_download);
					mViewHolder.ll_desc = convertView.findViewById(R.id.ll_desc);
					mViewHolder.tv_desc = (TextView) convertView
							.findViewById(R.id.tv_desc);
//					mViewHolder.ll_desc.setVisibility(View.GONE);
//					mViewHolder.isDescShowing = false;
				}
			}
		}
		
		
		if(mApkInfo.getWorksId() != -1 && mApkInfo.getWorksId() != -2){
			
			if(mApkInfo.getDescShowing())
				mViewHolder.ll_desc.setVisibility(View.VISIBLE);
			else
				mViewHolder.ll_desc.setVisibility(View.GONE);
		
			mViewHolder.btn_download.setTag(mList.get(position));
			mViewHolder.btn_download.setOnClickListener(mOnDownloadClickListener);
	
			mViewHolder.tv_apkname.setText(mList.get(position).getName());
			mViewHolder.tv_apksize.setText(SIZETIPS
					+ mList.get(position).getSize());
			mViewHolder.tv_keyword.setText(mList.get(position).getKeyWord());
			mViewHolder.tv_desc.setText(mList.get(position).getDesc());
	
			String photo = mList.get(position).getPhoto();
			if (!TextUtils.isEmpty(photo)) {
	//			mViewHolder.apk_pic.setImageUrl(HTTPPIC + photo);
				mImageLoader.bind(mViewHolder.apk_pic, photo, null);
			}
		
		}
		convertView.setTag(mViewHolder);

		LogUtil.Sysout("doGetViewname :position:  "+position + mList.get(position).getName()+" ,mList.get(position): "+mList.get(position).toString());

		return convertView;
	}

	class ViewHolder {
//		SmartImageView apk_pic;
		ImageView apk_pic;
		TextView tv_apkname;
		TextView tv_apksize;
		TextView tv_keyword;
		TextView tv_desc;
		View ll_desc;
		View btn_download;
//		boolean isDescShowing;
	}

}

