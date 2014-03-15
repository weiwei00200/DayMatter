package com.pybeta.daymatter.ui;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuInflater;
import com.pybeta.daymatter.BuildConfig;
import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.utils.DateUtils;
import com.pybeta.daymatter.utils.UtilityHelper;
import com.pybeta.util.ChineseCalendarGB;
import com.pybeta.util.DMToast;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ViewMatterActivity extends BaseActivity implements OnPageChangeListener, LoaderCallbacks<List<DayMatter>>{

    protected List<DayMatter> mMatterList = Collections.emptyList();
    
    private ViewPager mViewPager;
    private PageIndicator mPageIndicator;
    private ViewMatterAdapter mAdapter;
    private int mPosition = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_matter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);
        
        mAdapter = new ViewMatterAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);

        mPageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mPageIndicator.setViewPager(mViewPager);
        mPageIndicator.setOnPageChangeListener(this);

        mViewPager.setCurrentItem(mPosition);
        mPageIndicator.setCurrentItem(mPosition);
        
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }
    
    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.activity_view, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
            case R.id.menu_item_action_modify: { 
                gotoModifyActivity();
                break;
            }
            case R.id.menu_item_action_share: {
            	takeScreenshotAndShare();
            	break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void takeScreenshotAndShare() {
    	new ScreenshotShare().execute();
    }

    class ScreenshotShare extends AsyncTask<String, String, String> {

    	private ProgressDialog progressDlg = null;
    	
    	@Override
    	protected void onPreExecute() {
    		Resources res = ViewMatterActivity.this.getResources();
    		progressDlg = ProgressDialog.show(ViewMatterActivity.this, res.getString(R.string.app_name),
    				res.getString(R.string.screen_shot_progress), false, false);
    		
//    		mAdView.setVisibility(View.GONE);
    		super.onPreExecute();
    	}
    	
		@Override
		protected String doInBackground(String... params) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			String pic = UtilityHelper.takeScreenshot(ViewMatterActivity.this);
	    	if (BuildConfig.DEBUG) {
	    		System.out.println("screen shot: " + pic);
	    	}
			return pic;
		}
		
		@Override
		protected void onPostExecute(String result) {
	    	if (progressDlg != null) {
	    		progressDlg.dismiss();
	    	}
	    	
	    	String tips = ViewMatterActivity.this.getString(R.string.screen_shot_save_path);
	    	DMToast.makeText(ViewMatterActivity.this, tips + result, Toast.LENGTH_SHORT).show();
	    	
	    	DayMatter dayMatter = mMatterList.get(mPosition);
	    	UtilityHelper.share(ViewMatterActivity.this, getString(R.string.share_intent_subject), 
	    			DateUtils.getDayMatterDesc(ViewMatterActivity.this, dayMatter), result);
	    	
//	    	mAdView.setVisibility(View.VISIBLE);
			super.onPostExecute(result);
		}
		
    }
    
	@Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {
        this.mPosition = position;
    }

    public void gotoModifyActivity() {
        DayMatter dayMatter = mMatterList.get(mPosition);
        
        Intent intent = new Intent(this, AddMatterActivity.class);
        intent.putExtra(IContants.KEY_MATTER_TYPE, true);
        intent.putExtra(IContants.KEY_MATTER_DATA, dayMatter);
        startActivity(intent);
        
        finish();
    }
    
    class ViewMatterAdapter extends FragmentPagerAdapter {

        private List<DayMatter> mMatterList;
        
        public ViewMatterAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setItems(List<DayMatter> data) {
            mMatterList = data;
            notifyDataSetChanged();
        }
        
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new ViewMatterFragment();
            Bundle args = new Bundle();
            args.putSerializable(IContants.KEY_MATTER_DATA, mMatterList.get(position));
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return mMatterList == null ? 0 : mMatterList.size();
        }
    }
    
    class ViewMatterFragment extends SherlockFragment {
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.view_pager_item, null);
        }
        
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            TextView title = (TextView) view.findViewById(R.id.day_matter_title);
            TextView days = (TextView) view.findViewById(R.id.day_matter_days);
            TextView date = (TextView) view.findViewById(R.id.day_matter_date);
            TextView today = (TextView) view.findViewById(R.id.day_matter_today);
            TextView remark = (TextView) view.findViewById(R.id.day_matter_remark);
            
            DayMatter dayMatter = (DayMatter) getArguments().getSerializable(IContants.KEY_MATTER_DATA);
            DateUtils.showDayMatter(getActivity(), dayMatter,null, title, date, days, null);
            
            String todayFormat = getString(R.string.matter_today_format);
            
            Calendar todayCal = Calendar.getInstance();
            if (dayMatter.getCalendar() == IContants.CALENDAR_LUNAR) {
                ChineseCalendarGB lunar = new ChineseCalendarGB();
        		lunar.setGregorian(todayCal.get(Calendar.YEAR), todayCal.get(Calendar.MONTH) + 1, todayCal.get(Calendar.DAY_OF_MONTH));
        		lunar.computeChineseFields();
        		todayCal.set(lunar.getChineseYear(), lunar.getChineseMonth() - 1, lunar.getChineseDate());
            }
            today.setText(String.format(todayFormat, DateUtils.getMatterDateString(getActivity(), todayCal.getTimeInMillis(), dayMatter)));
            
            remark.setText(dayMatter.getRemark());
        }
    }

    @Override
    public Loader<List<DayMatter>> onCreateLoader(int id, Bundle args) {
        return new MatterListLoader(this, 0);
    }

    @Override
    public void onLoadFinished(Loader<List<DayMatter>> loader, List<DayMatter> data) {
        mMatterList = data;
        mAdapter.setItems(data);
        
        DayMatter matter = (DayMatter) getIntent().getSerializableExtra(IContants.KEY_MATTER_DATA);
        int position = getIntent().getIntExtra(IContants.kEY_MATTER_INDEX, 0);
        if (data != null && matter != null) {
		    for (int index = position; index < data.size(); index++) {
		    	DayMatter item = data.get(index);
		    	if (item.getUid().equals(matter.getUid())) {
		    		mPosition = index;
		            mViewPager.setCurrentItem(mPosition);
		            mPageIndicator.setCurrentItem(mPosition);
		            return;
		    	}
		    }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<DayMatter>> loader) {
    }
    
}
