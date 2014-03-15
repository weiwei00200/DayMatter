package com.pybeta.daymatter.ui;

import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.pybeta.daymatter.BuildConfig;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.utils.UtilityHelper;
import com.pybeta.util.ChangeLogDialog;
import com.pybeta.util.DMToast;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends LockBaseActivity implements ActionBar.TabListener {

	private ViewPager mViewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		getSupportActionBar().setTitle(R.string.app_name);
		
		checkCurrentLocale();

		initControlViews();
		showChangeLogDialog();
		
//		AppRater.app_launched(this);
		
		UmengUpdateAgent.update(this);
		UMFeedbackService.enableNewReplyNotification(this, NotificationType.NotificationBar);
	}

	private void showChangeLogDialog() {
		if (UtilityHelper.getChangeLogShow(this, IContants.CURRENT_VERSION)) {
			ChangeLogDialog _ChangelogDialog = new ChangeLogDialog(this);
			_ChangelogDialog.show();

			UtilityHelper.setChangeLogShow(this, IContants.CURRENT_VERSION);
		}
	}

	private void checkCurrentLocale() {
		Resources resources = getResources();
		Configuration config = resources.getConfiguration();
		Locale locale = UtilityHelper.getCurrentLocale(this);
		if (!config.locale.equals(locale)) {
			UtilityHelper.switchLanguage(this);
		}
	}

	private void initControlViews() {
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_action_add: {
			Intent intent = new Intent(this, AddMatterActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.menu_item_action_setting: {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.menu_item_action_count_down: {
			Intent intent = new Intent(this, CountdownActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.menu_item_action_holiday: {
			Intent intent = new Intent(this, HolidayActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.menu_item_action_share: {
			takeScreenshotAndShare();
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

	private void takeScreenshotAndShare() {
		new ScreenshotShare().execute();
	}

	class ScreenshotShare extends AsyncTask<String, String, String> {

		private ProgressDialog progressDlg = null;

		@Override
		protected void onPreExecute() {
			Resources res = MainActivity.this.getResources();
			progressDlg = ProgressDialog.show(MainActivity.this, res.getString(R.string.app_name),
					res.getString(R.string.screen_shot_progress), false, false);

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			String pic = UtilityHelper.takeScreenshot(MainActivity.this);
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

			String tips = MainActivity.this.getString(R.string.screen_shot_save_path);
			DMToast.makeText(MainActivity.this, tips + result, Toast.LENGTH_SHORT).show();

			UtilityHelper.share(MainActivity.this, getString(R.string.share_intent_subject), "#"
					+ getString(R.string.app_name) + "#", result);

			super.onPostExecute(result);
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private String[] mTabTitles;

		public SectionsPagerAdapter(Context ctx, FragmentManager fm) {
			super(fm);
			mTabTitles = ctx.getResources().getStringArray(R.array.matter_category);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new MatterListFragment();
			Bundle args = new Bundle();
			args.putInt(MatterListFragment.ARG_SECTION_NUMBER, position);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return mTabTitles == null ? 0 : mTabTitles.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mTabTitles[position];
		}
	}
}
