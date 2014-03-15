package com.pybeta.daymatter.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.core.DataManager;
import com.pybeta.daymatter.utils.DateUtils;
import com.pybeta.ui.utils.ItemListAdapter;
import com.pybeta.ui.utils.ItemView;
import com.pybeta.util.AsyncLoader;
import com.umeng.fb.UMFeedbackService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class HolidayActivity extends BaseActivity implements LoaderCallbacks<List<DayMatter>> {

	private ListView mListView;
	private HolidayMatterAdapter mDateAdapter;
	protected List<DayMatter> mMatterList = Collections.emptyList();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_holiday);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.title_activity_holiday);

		mListView = (ListView) findViewById(R.id.view_list);
		mDateAdapter = new HolidayMatterAdapter(this);
		mListView.setAdapter(mDateAdapter);

		getSupportLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<List<DayMatter>> onCreateLoader(int id, Bundle args) {
		return new MatterListLoader(this);
	}

	@Override
	public void onLoadFinished(Loader<List<DayMatter>> loader, List<DayMatter> data) {
		if (data == null) {
			mMatterList = Collections.emptyList();
		} else {
			mMatterList = data;
		}
		mDateAdapter.setItems(mMatterList.toArray());
	}

	@Override
	public void onLoaderReset(Loader<List<DayMatter>> loader) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_count_down, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			finish();
			break;
		}
		case R.id.menu_item_action_setting: {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.menu_item_action_feedback: {
			UMFeedbackService.openUmengFeedbackSDK(this);
			break;
		}
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public static class MatterListLoader extends AsyncLoader<List<DayMatter>> {

		private Context context;

		public MatterListLoader(Context context) {
			super(context);
			this.context = context;
		}

		@Override
		public List<DayMatter> loadInBackground() {
			ArrayList<DayMatter> matterList = DataManager.getInstance(context).getHolidayList();
			return DataManager.getInstance(context).sortMatters(matterList, IContants.SORT_BY_DATE_AESC);
		}
	}

	public class HolidayMatterAdapter extends ItemListAdapter<DayMatter, HolidayMatterView> {

		private Context context;

		public HolidayMatterAdapter(Context context) {
			super(R.layout.holiday_list_item, LayoutInflater.from(context));
			this.context = context;
		}

		@Override
		protected void update(int position, HolidayMatterView view, DayMatter item) {
			DateUtils.showDayMatter(context, item, null, null, view.date, view.nums, null);
			view.title.setText(item.getMatter());
		}

		@Override
		protected HolidayMatterView createView(View view) {
			return new HolidayMatterView(view);
		}

	}

	public class HolidayMatterView extends ItemView {

		protected TextView title;
		protected TextView date;
		protected TextView nums;

		public HolidayMatterView(View view) {
			super(view);

			title = (TextView) view.findViewById(R.id.tv_matter_title);
			date = (TextView) view.findViewById(R.id.tv_matter_date);
			nums = (TextView) view.findViewById(R.id.tv_matter_days);
		}

	}

}
