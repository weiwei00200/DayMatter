package com.pybeta.daymatter.ui;

import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.WidgetInfo;
import com.pybeta.daymatter.core.DataManager;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.daymatter.utils.DateUtils;
import com.pybeta.daymatter.utils.LogUtil;
import com.pybeta.daymatter.utils.UtilityHelper;
import com.pybeta.util.DMToast;

public class MatterListFragment extends SherlockFragment implements LoaderCallbacks<List<DayMatter>> {

	private int mMatterCategory = 0;

	protected List<DayMatter> mMatterList = Collections.emptyList();
	private MatterListAdapter mAdapter;
	
	protected ListView mListView;
    protected TextView mEmptyView;
    protected ProgressBar mProgressBar;
    
    protected View mMatterListView;
    protected View mTopMatterView;
    protected TextView mTopMatterTitle;
    protected TextView mTopMatterDate;
    protected TextView mTopMatterNums;
    protected ImageView mTopMatterUnit;
    
	public static final String ARG_SECTION_NUMBER = "section_number";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMatterCategory = getArguments().getInt(ARG_SECTION_NUMBER);
		getSherlockActivity().getSupportActionBar().setTitle(R.string.title_activity_home);
		return inflater.inflate(R.layout.fragment_item_list, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mMatterListView = view.findViewById(R.id.matter_list);
		mTopMatterView = view.findViewById(R.id.view_top_remind);
		mTopMatterTitle = (TextView) view.findViewById(R.id.tv_matter_title);
		mTopMatterDate = (TextView) view.findViewById(R.id.tv_matter_date);
		mTopMatterNums = (TextView) view.findViewById(R.id.tv_matter_days);
		mTopMatterUnit = (ImageView) view.findViewById(R.id.iv_matter_unit);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DayMatter matter = mAdapter.getItem(position);
                enterDetailMatterActivity(matter, position);
            }
        });
        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				DayMatter matter = mAdapter.getItem(position);
				showActionDialog(matter, position);
				return false;
			}
		});
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        mEmptyView = (TextView) view.findViewById(android.R.id.empty);
        
        mAdapter = new MatterListAdapter(getSherlockActivity());
        mListView.setAdapter(mAdapter);
	}

	protected void showActionDialog(final DayMatter matter, final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
		builder.setTitle(R.string.matter_list_action_title);
		builder.setItems(R.array.matter_list_action_array, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 1) {
					enterModifyActivity(matter);
				} else if (which == 2) {
					delete(matter);
				} else {
					enterDetailMatterActivity(matter, position);
				}
			}
		});
		builder.create().show();
	}

    public void delete(final DayMatter matter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.matter_delete_confirm);
        builder.setPositiveButton(R.string.matter_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	
//            	if(matter.getRemind() > IContants.mat)
            	
                DataManager dataMgr = DataManager.getInstance(getSherlockActivity());
                WidgetInfo info = dataMgr.getWidgetInfoByMatterUid(matter.getUid());
                
                DatabaseManager databaseMgr = DatabaseManager.getInstance(getSherlockActivity());
                databaseMgr.delete(matter);

                if (info != null) {
                	UtilityHelper.updateWidgetView(getSherlockActivity(), info);
                    dataMgr.deleteWidgetInfoByMatterId(info.dayMatterId);
                }
                
                UtilityHelper.showStartBarNotification(getSherlockActivity());
                DMToast.makeText(getSherlockActivity(), R.string.matter_delete_success,Toast.LENGTH_LONG).show();
                
                Bundle args = new Bundle();
        		args.putInt(ARG_SECTION_NUMBER, mMatterCategory);
                getLoaderManager().restartLoader(0, args, MatterListFragment.this);
            }
        });
        builder.setNegativeButton(R.string.matter_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.create().show();
    }
    
	@Override
	public void onResume() {
		super.onResume();
		
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, mMatterCategory);
		getLoaderManager().restartLoader(0, args, this);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if (!mMatterList.isEmpty()) {
			setListShown(true);
		}
	}
	
	public void refresh() {
        refresh(null);
    }

    private void refresh(Bundle args) {
    	if (args == null) {
    		args = new Bundle();
    		args.putInt(ARG_SECTION_NUMBER, mMatterCategory);
    	} else {
    		args.putInt(ARG_SECTION_NUMBER, mMatterCategory);
    	}
    	
        getLoaderManager().restartLoader(0, args, this);
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
			
			if (!mMatterList.isEmpty()) {
				mEmptyView.setVisibility(View.GONE);
			} else {
				mEmptyView.setVisibility(View.VISIBLE);
			}
		} else {
			mProgressBar.setVisibility(View.VISIBLE);
			mMatterListView.setVisibility(View.GONE);
		}
	}
	
	@Override
	public Loader<List<DayMatter>> onCreateLoader(int id, Bundle args) {
		return new MatterListLoader(getSherlockActivity(), args.getInt(ARG_SECTION_NUMBER, 0));
	}

	@Override
	public void onLoadFinished(Loader<List<DayMatter>> loader, List<DayMatter> data) {
		if (data == null) {
			mMatterList = Collections.emptyList();
		} else {
			mMatterList = data;
		}
		
		mAdapter.setItems(mMatterList.toArray());
		setListShown(true);
		
		DayMatter matter = DatabaseManager.getInstance(getSherlockActivity()).queryTop();
		if (matter == null) {
			mTopMatterView.setVisibility(View.GONE);
		} else {
			mTopMatterView.setVisibility(View.VISIBLE);
			DateUtils.showDayMatter(getSherlockActivity(), matter, null, mTopMatterTitle, mTopMatterDate, mTopMatterNums, mTopMatterUnit);
		}
	}

	@Override
	public void onLoaderReset(Loader<List<DayMatter>> loader) {
	}

	private void enterDetailMatterActivity(final DayMatter matter, final int position) {
		Intent intent = new Intent(getSherlockActivity(), ViewMatterActivity.class);
		intent.putExtra(IContants.KEY_MATTER_DATA, matter);
		intent.putExtra(IContants.kEY_MATTER_INDEX, position);
		startActivity(intent);
	}

	private void enterModifyActivity(final DayMatter matter) {
		Intent intent = new Intent(getSherlockActivity(), AddMatterActivity.class);
		intent.putExtra(IContants.KEY_MATTER_TYPE, true);
		intent.putExtra(IContants.KEY_MATTER_DATA, matter);
		startActivity(intent);
	}
}
