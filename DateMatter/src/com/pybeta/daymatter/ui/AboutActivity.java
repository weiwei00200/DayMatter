package com.pybeta.daymatter.ui;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.utils.UtilityHelper;
import com.pybeta.util.ChangeLogDialog;
import com.umeng.fb.UMFeedbackService;

public class AboutActivity extends SherlockPreferenceActivity implements OnPreferenceClickListener {

	private Preference mAboutusPref = null;
	private Preference mFeedbackPref = null;
	private Preference mSharedPref = null;
	private Preference mFollowPref = null;
	
	private Preference mChangelogPref = null;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.about_preferences);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.title_activity_about);

		mSharedPref = findPreference("share_preferences");
		mFeedbackPref = findPreference("feedback_preferences");
		
		mAboutusPref = findPreference("about_preference_version");
		mChangelogPref = findPreference("about_preference_changelog");
		mFollowPref = findPreference("about_preference_follow_content");
		
		mSharedPref.setOnPreferenceClickListener(this);
		mFeedbackPref.setOnPreferenceClickListener(this);
		mChangelogPref.setOnPreferenceClickListener(this);
		mFollowPref.setOnPreferenceClickListener(this);
		
		updateAboutusPreferenceSummary();
	}

	private void updateAboutusPreferenceSummary() {
		String formatSize = getResources().getString(R.string.aboutus_summary_preferences);
		mAboutusPref.setSummary(String.format(formatSize, UtilityHelper.getAppVersionName(this)));
	}

	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			finish();
			break;
		}
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference.getKey().equals(mFeedbackPref.getKey())) {
			UMFeedbackService.openUmengFeedbackSDK(this);
		} else if (preference.getKey().equals(mSharedPref.getKey())) {
			UtilityHelper.share(this, getString(R.string.share_intent_subject), getString(R.string.share_intent_text), null);
		} else if (preference.getKey().equals(mChangelogPref.getKey())) {
			ChangeLogDialog _ChangelogDialog = new ChangeLogDialog(this);
			_ChangelogDialog.show();
		} else if (preference.getKey().equals(mFollowPref.getKey())) {
			UtilityHelper.follow(this, getString(R.string.website));
		} 

		return false;
	}

}
