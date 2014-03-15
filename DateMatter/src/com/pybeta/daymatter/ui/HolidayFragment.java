package com.pybeta.daymatter.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.core.DataManager;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.daymatter.utils.DateUtils;
import com.pybeta.ui.utils.ItemListAdapter;
import com.pybeta.ui.utils.ItemView;
import com.pybeta.util.AsyncLoader;
import com.pybeta.util.DMToast;

public class HolidayFragment extends SherlockFragment  implements LoaderCallbacks<List<DayMatter>> {

	private ListView mList;
	private HolidayMatterAdapter mAdapter;
	protected List<DayMatter> mHolidayList = Collections.emptyList();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getSherlockActivity().getSupportActionBar().setTitle(R.string.title_activity_holiday);
		return inflater.inflate(R.layout.activity_holiday, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mList = (ListView) view.findViewById(R.id.view_list);
		mAdapter = new HolidayMatterAdapter(getSherlockActivity());
		mList.setAdapter(mAdapter);
		mList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				DayMatter matter = mAdapter.getItem(position);
				addToLocal(matter);
				return false;
			}
		});
		
	}
	
    public void addToLocal(final DayMatter matter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.holiday_add_local);
        builder.setPositiveButton(R.string.menu_new_daysmatter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	DatabaseManager databaseMgr = DatabaseManager.getInstance(getSherlockActivity());
            	databaseMgr.add(matter);
            	DMToast.makeText(getSherlockActivity(), R.string.matter_save, Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.matter_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.create().show();
    }
    
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<List<DayMatter>> onCreateLoader(int id, Bundle args) {
		return new MatterListLoader(getSherlockActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<DayMatter>> loader, List<DayMatter> data) {
		if (data == null) {
			mHolidayList = Collections.emptyList();
		} else {
			mHolidayList = data;
		}
		mAdapter.setItems(mHolidayList.toArray());
	}

	@Override
	public void onLoaderReset(Loader<List<DayMatter>> loader) {
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
			DateUtils.showDayMatter(context, item,null, null, view.date, view.nums, null);
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
