package com.pybeta.daymatter.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.actionbarsherlock.app.SherlockFragment;
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
import com.pybeta.daymatter.core.DataManager;
import com.pybeta.daymatter.utils.UtilityHelper;
import com.pybeta.ui.utils.ItemListAdapter;
import com.pybeta.ui.utils.ItemView;
import com.pybeta.util.AsyncLoader;

public class HistoryTodayFragement extends SherlockFragment implements LoaderCallbacks<List<HistoryToday>> {

	protected View mMatterListView;
	protected ListView mListView;
    protected TextView mEmptyView;
    protected ProgressBar mProgressBar;
    
    private HistoryTodayAdapter mListAdapter;
    private List<HistoryToday> mHistoryList = Collections.emptyList();
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	if (mHistoryList != null && mHistoryList.size() > 0) {
    		setListShown(true);
    	}
    	getLoaderManager().initLoader(0, null, this);
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getSherlockActivity().getSupportActionBar().setTitle(R.string.title_activity_history_today);
		return inflater.inflate(R.layout.fragment_history_today, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mMatterListView = view.findViewById(R.id.matter_list);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				HistoryToday content = mListAdapter.getItem(position);
				
				String mouth = content.getMmdd().substring(0, 2);
				String day = content.getMmdd().substring(2);
		    	UtilityHelper.share(getSherlockActivity(), getString(R.string.share_intent_subject), 
		    			String.format(getString(R.string.history_today_share_format), mouth, day, content.getContent()), null);
				return false;
			}
		});
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        mEmptyView = (TextView) view.findViewById(android.R.id.empty);
        
        mListAdapter = new HistoryTodayAdapter(R.layout.history_item_view, LayoutInflater.from(getSherlockActivity()));
        mListAdapter.setItems(mHistoryList.toArray());
        mListView.setAdapter(mListAdapter);
	}
	
	public static class HistoryTodayLoader extends AsyncLoader<List<HistoryToday>> {

		private Context mContext;

		public HistoryTodayLoader(Context context) {
			super(context);
			mContext = context;
		}

		@Override
		public List<HistoryToday> loadInBackground() {
			Date today = new Date();
			SimpleDateFormat format = new SimpleDateFormat("MMdd");
			String todayStr = format.format(today);
			
			String lang = IContants.COMMOM_LANG_EN;
			Locale locale = UtilityHelper.getCurrentLocale(mContext);
			if (locale != null && locale.equals(Locale.TRADITIONAL_CHINESE)) {
				lang = IContants.COMMOM_LANG_TW;
			} else if (locale != null && locale.equals(Locale.SIMPLIFIED_CHINESE)) {
				lang = IContants.COMMOM_LANG_CN;
			}
			
			DataManager dataMgr = DataManager.getInstance(mContext);
			List<HistoryToday> list = dataMgr.getHistoryDataByDate(todayStr + lang);
			if (list == null || list.size() == 0) {
				GenericUrl url = new GenericUrl(String.format(IContants.BASE_HISTORY_TODAY_URL, todayStr, lang));
				HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
				try {
					HttpRequest request = httpTransport.createRequestFactory().buildGetRequest(url);
					request.setHeaders(new HttpHeaders().setAccept("utf-8")
							.setBasicAuthentication(IContants.REQUEST_PARAM_1, IContants.REQUEST_PARAM_2));
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
	}
	
	class HistoryTodayAdapter extends ItemListAdapter<HistoryToday, HistoryTodayView> {

		public HistoryTodayAdapter(int viewId, LayoutInflater inflater) {
			super(viewId, inflater);
		}

		@Override
		protected void update(int position, HistoryTodayView view, HistoryToday item) {
			view.mContent.setText(item.getContent());
		}

		@Override
		protected HistoryTodayView createView(View view) {
			return new HistoryTodayView(view);
		}
	}
	
	class HistoryTodayView extends ItemView {

		TextView mContent;
		
		public HistoryTodayView(View view) {
			super(view);
			mContent = textView(view, R.id.tv_history_content);
		}
		
	}

	@Override
	public Loader<List<HistoryToday>> onCreateLoader(int id, Bundle args) {
		return new HistoryTodayLoader(getSherlockActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<HistoryToday>> loader, List<HistoryToday> data) {
		if (data == null) {
			mHistoryList = Collections.emptyList();
		} else {
			mHistoryList = data;
		}
		mListAdapter.setItems(mHistoryList.toArray());
		setListShown(true);
	}

	@Override
	public void onLoaderReset(Loader<List<HistoryToday>> arg0) {
	}
	
    @Override
    public void onDestroyView() {
        mEmptyView = null;
        mProgressBar = null;
        mListView = null;

        super.onDestroyView();
    }

	public void setListShown(boolean shown) {
		if (shown) {
			mProgressBar.setVisibility(View.GONE);
			mMatterListView.setVisibility(View.VISIBLE);
			
			if (mHistoryList != null && !mHistoryList.isEmpty()) {
				mEmptyView.setVisibility(View.GONE);
			} else {
				mEmptyView.setVisibility(View.VISIBLE);
			}
		} else {
			mProgressBar.setVisibility(View.VISIBLE);
			mMatterListView.setVisibility(View.GONE);
		}
	}
	
}
