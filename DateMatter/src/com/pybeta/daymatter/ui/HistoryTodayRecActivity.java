package com.pybeta.daymatter.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pybeta.daymatter.BuildConfig;
import com.pybeta.daymatter.HistoryToday;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.R.id;
import com.pybeta.daymatter.R.layout;
import com.pybeta.daymatter.R.string;
import com.pybeta.daymatter.adapter.HistoryTodayAdapter;
import com.pybeta.daymatter.core.DataManager;
import com.pybeta.daymatter.utils.UtilityHelper;
import com.pybeta.ui.widget.UcTitleBar;
import com.sammie.common.thread.CustomRunnable;
import com.sammie.common.thread.IDataAction;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

public class HistoryTodayRecActivity extends Activity {
	protected View mMatterListView;
	protected ListView mListView;
    protected TextView mEmptyView;
    protected ProgressBar mProgressBar;
    private HistoryTodayAdapter mListAdapter = null;
    private UcTitleBar mTitleBar = null;
    
    private List<HistoryToday> mHistoryList = Collections.emptyList();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_today_rec);
		mTitleBar = (UcTitleBar)this.findViewById(R.id.uc_titlebar);
		mMatterListView = this.findViewById(R.id.matter_list);
        mListView = (ListView) this.findViewById(android.R.id.list);
        mProgressBar = (ProgressBar) this.findViewById(R.id.pb_loading);
        mEmptyView = (TextView) this.findViewById(android.R.id.empty);
        
        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				HistoryToday content = mListAdapter.getItem(position);
				
				String mouth = content.getMmdd().substring(0, 2);
				String day = content.getMmdd().substring(2);
		    	UtilityHelper.share(HistoryTodayRecActivity.this, getString(R.string.share_intent_subject), 
		    			String.format(getString(R.string.history_today_share_format), mouth, day, content.getContent()), null);
				return false;
			}
		});
        loadListData();
        initTitleBar();
	}
	private void initTitleBar(){
		mTitleBar.setTitleText(getResources().getString(R.string.today_history));
    	mTitleBar.setViewVisible(false, true, false, false, false, false, false, false);
    	mTitleBar.setListener(new UcTitleBar.ITitleBarListener() {
			@Override
			public void shareClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void menuClick(Object obj) {
				// TODO Auto-generated method stub
			}
			@Override
			public void feedBackClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void editClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void completeClick(Object obj) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void backClick(Object obj) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
			@Override
			public void addClick(Object obj) {
				// TODO Auto-generated method stub
			}
			@Override
			public void closeClick(Object obj) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	private void loadListData(){
		IDataAction runAction = new IDataAction() {
			@Override
			public Object actionExecute(Object arg0) {
				// TODO Auto-generated method stub
				Date today = new Date();
				SimpleDateFormat format = new SimpleDateFormat("MMdd");
				String todayStr = format.format(today);
				
				String lang = IContants.COMMOM_LANG_EN;
				Locale locale = UtilityHelper.getCurrentLocale(HistoryTodayRecActivity.this);
				if (locale != null && locale.equals(Locale.TRADITIONAL_CHINESE)) {
					lang = IContants.COMMOM_LANG_TW;
				} else if (locale != null && locale.equals(Locale.SIMPLIFIED_CHINESE)) {
					lang = IContants.COMMOM_LANG_CN;
				}
				
				DataManager dataMgr = DataManager.getInstance(HistoryTodayRecActivity.this);
				List<HistoryToday> list = dataMgr.getHistoryDataByDate(todayStr + lang);
				if (list == null || list.size() == 0) {
					GenericUrl url = new GenericUrl(String.format(IContants.BASE_HISTORY_TODAY_URL, todayStr, lang));
					HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
					try {
						HttpRequest request = httpTransport.createRequestFactory().buildGetRequest(url);
						request.setHeaders(new HttpHeaders().setAccept("utf-8").setBasicAuthentication(IContants.REQUEST_PARAM_1, IContants.REQUEST_PARAM_2));
						HttpResponse response = request.execute();
						list = new Gson().fromJson(response.parseAsString(), new TypeToken<List<HistoryToday>>(){}.getType());
						dataMgr.putHistoryData(todayStr + lang, list);
						
						if (BuildConfig.DEBUG)  System.out.println(list);
					} catch (HttpResponseException e) {
						if (BuildConfig.DEBUG)  System.err.println("http status code: " + e.getStatusCode());
					} catch (IOException e) {
						if (BuildConfig.DEBUG) e.printStackTrace();
					} catch (Exception e) {
						if (BuildConfig.DEBUG) e.printStackTrace();
					}
				}
				return list;
			}
		};
		IDataAction completeAction = new IDataAction() {
			@Override
			public Object actionExecute(Object arg0) {
				// TODO Auto-generated method stub
				if(arg0!= null){
					mHistoryList = (List<HistoryToday>)arg0;
					mHistoryList.remove(0);
					mListAdapter = new HistoryTodayAdapter(R.layout.history_item_view, LayoutInflater.from(HistoryTodayRecActivity.this));
			        mListAdapter.setItems(mHistoryList.toArray());
			        mListView.setAdapter(mListAdapter);
			        showLoadView(false,true,false);
				}else{
					showLoadView(false,false,true);
				}
				return null;
			}
		};
		CustomRunnable run = new CustomRunnable(runAction, completeAction);
		run.startAction();
		showLoadView(true,false,false);
	}
	
	private void showLoadView(boolean isShowLoading,boolean isShowList,boolean isShowEmpty){
		mMatterListView.setVisibility(isShowList ? View.VISIBLE:View.GONE);
		mProgressBar.setVisibility(isShowLoading ? View.VISIBLE:View.GONE);
		mEmptyView.setVisibility(isShowEmpty ? View.VISIBLE:View.GONE);
	}
	
}
