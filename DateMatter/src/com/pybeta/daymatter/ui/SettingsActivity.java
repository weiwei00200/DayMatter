package com.pybeta.daymatter.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Children;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;

import com.pybeta.daymatter.BuildConfig;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.core.DataManager;
import com.pybeta.daymatter.utils.UtilityHelper;
import com.pybeta.util.DMToast;

import com.umeng.analytics.MobclickAgent;

public class SettingsActivity extends SherlockPreferenceActivity implements OnPreferenceClickListener,
		OnSharedPreferenceChangeListener {

	private final static int REQUEST_PICKER_FILE = 1000;
	private final static int REQUEST_LOCK_CHANGE = 2000;
	private final static int REQUEST_LOCK_DISABLE = 3000;
	private static final int REQUEST_ACCOUNT_PICKER_BACKUP = 4000;
	private static final int REQUEST_AUTHORIZATION_BACKUP = 5000;
	private static final int REQUEST_ACCOUNT_PICKER_RESTORE = 6000;
	private static final int REQUEST_AUTHORIZATION_RESTORE = 7000;
	
	private ListPreference mLocalePref = null;
	private CheckBoxPreference mBarNotifyPref = null;
	private Preference mLockEnablePref = null;
	private Preference mLockDisablePref = null;
	private Preference mBackupPref = null;
	private Preference mRestorePref = null;

	private static Drive mDriveService;
	private GoogleAccountCredential mDriveCredential;
	
	protected ProgressDialog mLoadingDialog = null;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.title_activity_settings);
		
		mLockEnablePref = findPreference(getString(R.string.settings_user_lock_enable));
		mLockDisablePref = findPreference(getString(R.string.settings_user_lock_disable));
		
		mLockEnablePref.setOnPreferenceClickListener(this);
		mLockDisablePref.setOnPreferenceClickListener(this);
		
		updateLockPref();

		mLocalePref = (ListPreference) findPreference("key_pref_locale_type");
		mBarNotifyPref = (CheckBoxPreference) findPreference("key_pref_status_bar_notify");
		
		mBackupPref = findPreference("settings_backup");
		mRestorePref = findPreference("settings_restore");

		mBackupPref.setOnPreferenceClickListener(this);
		mRestorePref.setOnPreferenceClickListener(this);
	}

	private void updateLockPref() {
		SharedPreferences prefs = getSharedPreferences(IContants.LOCK_PREFERENCES, 0);
		String currentPattern = prefs.getString(LockPatternActivity._Pattern, null);
		if (currentPattern != null) {
			mLockEnablePref.setEnabled(true);
			mLockDisablePref.setEnabled(true);
			mLockEnablePref.setTitle(R.string.change_lock);
		} else {
			mLockEnablePref.setEnabled(true);
			mLockDisablePref.setEnabled(false);
			mLockEnablePref.setTitle(R.string.set_lock);
		}
	}

	public void onResume() {
		super.onResume();
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);

		updateLockPref();

		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.unregisterOnSharedPreferenceChangeListener(this);

		MobclickAgent.onPause(this);
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

	private void backupData() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(R.string.backup_picker_way_title);
	    builder.setItems(R.array.backup_way_list, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   if (which == 0) {
	            		   backupToSdcard();
	            	   } else if (which == 1) {
	            		   backupToGoogleDrive();
	            	   }
	           }
	    });
	    builder.create().show();
	}

	private void backupToSdcard() {
		String path = DataManager.getInstance(getApplicationContext()).exportDataCSV();
		if (path != null) {
			String tipsFotmat = getString(R.string.settings_backup_success);
			DMToast.makeText(SettingsActivity.this, String.format(tipsFotmat, path), Toast.LENGTH_LONG).show();
		} else {
			DMToast.makeText(SettingsActivity.this, R.string.settings_backup_failed, Toast.LENGTH_LONG).show();
		}
	}
	
	private void backupToGoogleDrive() {
		try {
			mDriveCredential = GoogleAccountCredential.usingOAuth2(this, DriveScopes.DRIVE);
		    startActivityForResult(mDriveCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER_BACKUP);
		} catch (Exception e) {
			DMToast.makeText(SettingsActivity.this, R.string.settings_backup_no_google_play_services, Toast.LENGTH_LONG).show();
		}
	}
	
	private void realbackupToGoogleDrive() {
		new BackupToGoogleDriveTask().execute();
	}
	
	class BackupToGoogleDriveTask extends AsyncTask<String, String, File> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog(R.string.settings_backup_progress, new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					BackupToGoogleDriveTask.this.cancel(true);
				}
			});
		}
		
		@Override
		protected File doInBackground(String... params) {
			java.io.File fileContent = null;
			
			try {
				String folderId = null;
				
				Children.List request = mDriveService.children().list("root");
				ChildList children = request.setQ(IContants.GOOGLE_DRIVE_SEARCH_FOLDER).execute();
				if (children != null && children.getItems() != null) {
					if (children.getItems().size() > 0) {
						ChildReference child = children.getItems().get(0);
						if (child != null) {
							folderId = child.getId();
						}
					}
				}

				if (folderId == null) {
					File matterFolder = new File();
					matterFolder.setTitle("DateMatter");
					matterFolder.setMimeType(IContants.GOOGLE_DRIVE_MIMETYPE);
					
					File folder = mDriveService.files().insert(matterFolder).execute();
					if (folder != null) {
						folderId = folder.getId();
					} else {
						throw new IOException("can't crate DateMatter folder.");
					}
				}
				
				String path = DataManager.getInstance(getApplicationContext()).exportDataCSV();
				fileContent = new java.io.File(path);
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
				String dataStr = dateFormat.format(new Date());

				FileContent mediaContent = new FileContent("text/csv", fileContent);
	            File body = new File();
				body.setTitle("DateMatter_" + dataStr + ".csv");
				body.setMimeType(IContants.BACKUP_FILE_MIMETYPE);
				body.setParents(Arrays.asList(new ParentReference().setId(folderId)));

				return mDriveService.files().insert(body, mediaContent).execute();
			} catch (UserRecoverableAuthIOException e) {
				startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION_BACKUP);
			} catch (IOException e) {
				if (BuildConfig.DEBUG) e.printStackTrace();
			} finally {
				if (fileContent != null) {
					fileContent.delete();
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(File result) {
			hideLoadingDialog();
			if (result != null) {
				DMToast.makeText(SettingsActivity.this, R.string.settings_backup_to_drive_success, Toast.LENGTH_LONG).show();
			} else {
				DMToast.makeText(SettingsActivity.this, R.string.settings_backup_failed, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private void restoreData() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.restore_picker_way_title);
		builder.setItems(R.array.restore_way_list,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							restoreFromSdcard();
						} else if (which == 1) {
							restoreFromGoogleDrive();
						}
					}
				});
		builder.create().show();
	}
	
	private void restoreFromSdcard() {
		DMToast.makeText(this, R.string.settings_restore_picker_file, Toast.LENGTH_LONG).show();
		Intent intent = new Intent(getBaseContext(), FileManagerActivity.class);
		startActivityForResult(intent, REQUEST_PICKER_FILE);
	}

	private void restoreFromGoogleDrive() {
		try {
			mDriveCredential = GoogleAccountCredential.usingOAuth2(this, DriveScopes.DRIVE);
		    startActivityForResult(mDriveCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER_RESTORE);
		} catch (Exception e) {
			DMToast.makeText(SettingsActivity.this, R.string.settings_restore_no_google_play_services, Toast.LENGTH_LONG).show();
		}
	}
	
	private void realRestoreFromGoogleDrive() {
		new RestoreFromDriveTask().execute();
	}
	
	class RestoreFromDriveTask extends AsyncTask<String, String, List<File>> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog(R.string.settings_restore_get_file_list_progress, new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					RestoreFromDriveTask.this.cancel(true);
				}
			});
		}
		
		@Override
		protected List<File> doInBackground(String... params) {
			try {
				Files.List request = mDriveService.files().list();
				FileList fileList = request.setQ(IContants.GOOGLE_DRIVE_SEARCH_DATA_FILE).execute();
				if (fileList != null) {
					return fileList.getItems();
				}
			} catch (UserRecoverableAuthIOException e) {
				startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION_RESTORE);
			} catch (IOException e) {
				if (BuildConfig.DEBUG) e.printStackTrace();
			} finally {
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(List<File> result) {
			hideLoadingDialog();
			
			if (result != null) {
				if (result.size() == 0) {
					DMToast.makeText(SettingsActivity.this, R.string.settings_restore_file_empty, Toast.LENGTH_LONG).show();
				} else {
					showDrivePickerFileDialog(result);
				}
			} else {
				DMToast.makeText(SettingsActivity.this, R.string.settings_restore_get_file_list_failed, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private void showDrivePickerFileDialog(final List<File> result) {
		FileListAdapter adapter = new FileListAdapter(this, R.layout.file_dialog_row, R.id.fdrowtext, result);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(R.string.settings_restore_picker_file);
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				File chooseFile = result.get(which);
				fetchFileContent(chooseFile);
			}
		});
		builder.create().show();
	}
	
	protected void fetchFileContent(File file) {
		if (file != null) {
			new GetFileContentTask().execute(file.getId());
		}
	}

	class GetFileContentTask extends AsyncTask<String, String, Boolean> {
		@Override
		protected void onPreExecute() {
			showLoadingDialog(R.string.settings_restore_progress, new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					GetFileContentTask.this.cancel(true);
				}
			});
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			String fileId = params[0];
			try {
				File file = mDriveService.files().get(fileId).execute();
				HttpResponse resp = mDriveService.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl())).execute();
			    return DataManager.getInstance(SettingsActivity.this).importDataCSV(resp.getContent());
			} catch (IOException e) {
				if (BuildConfig.DEBUG) e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			hideLoadingDialog();
			
			if (result) {
				DMToast.makeText(SettingsActivity.this, R.string.settings_restore_success, Toast.LENGTH_LONG).show();
			} else {
				DMToast.makeText(SettingsActivity.this, R.string.settings_restore_failed, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	class FileListAdapter extends ArrayAdapter<File> {
		
		private int resource;
		private int fieldId = 0;
		private LayoutInflater inflater;
		 
		public FileListAdapter(Context context, int resource, int textViewResourceId, List<File> objects) {
			super(context, resource, textViewResourceId, objects);
			this.resource = resource;
			this.fieldId = textViewResourceId;
			this.inflater = LayoutInflater.from(context);
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
	        return createViewFromResource(position, convertView, parent, resource);
	    }

	    private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
	        View view;
	        TextView text;

	        if (convertView == null) {
	            view = inflater.inflate(resource, parent, false);
	        } else {
	            view = convertView;
	        }

	        text = (TextView) view.findViewById(fieldId);

	        File item = getItem(position);
	        text.setText(item.getTitle());

	        return view;
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_PICKER_FILE) {
				String filePath = data.getStringExtra(FileManagerActivity.RESULT_PATH);
				parsePickerFile(filePath);
			} else if (requestCode == REQUEST_LOCK_CHANGE) {
				SharedPreferences prefs = getSharedPreferences(IContants.LOCK_PREFERENCES, 0);
				prefs.edit().clear().commit();
				
				Intent intent = new Intent(this, LockPatternActivity.class);
				intent.putExtra(LockPatternActivity._Mode, LockPatternActivity.LPMode.CreatePattern);
				startActivity(intent);
			} else if (requestCode == REQUEST_LOCK_DISABLE) {
				SharedPreferences prefs = getSharedPreferences(IContants.LOCK_PREFERENCES, 0);
				prefs.edit().clear().commit();
				
				updateLockPref();
			} else if (requestCode == REQUEST_ACCOUNT_PICKER_BACKUP) {
				if (data != null && data.getExtras() != null) {
					String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
					if (accountName != null) {
						mDriveCredential.setSelectedAccountName(accountName);
						mDriveService = getDriveService(mDriveCredential);
						realbackupToGoogleDrive();
					}
				}
			} else if (requestCode == REQUEST_AUTHORIZATION_BACKUP) {
				realbackupToGoogleDrive();
			}  else if (requestCode == REQUEST_ACCOUNT_PICKER_RESTORE) {
				if (data != null && data.getExtras() != null) {
					String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
					if (accountName != null) {
						mDriveCredential.setSelectedAccountName(accountName);
						mDriveService = getDriveService(mDriveCredential);
						realRestoreFromGoogleDrive();
					}
				}
			} else if (requestCode == REQUEST_AUTHORIZATION_RESTORE) {
				realRestoreFromGoogleDrive();
			} 
		} else {
			if (requestCode == REQUEST_AUTHORIZATION_BACKUP) {
				startActivityForResult(mDriveCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER_BACKUP);
			} else if (requestCode == REQUEST_AUTHORIZATION_RESTORE) {
				startActivityForResult(mDriveCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER_RESTORE);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private Drive getDriveService(GoogleAccountCredential credential) {
		return new Drive.Builder(AndroidHttp.newCompatibleTransport(),
				new GsonFactory(), credential).build();
	}

	protected void showLoadingDialog(int msgId, OnCancelListener listener) {
		if (mLoadingDialog == null) {
			mLoadingDialog = new ProgressDialog(this);
			mLoadingDialog.setTitle(R.string.app_name);
			mLoadingDialog.setIndeterminate(false);
			mLoadingDialog.setCancelable(true);
			mLoadingDialog.setCanceledOnTouchOutside(false);
			mLoadingDialog.setOnCancelListener(listener);
			mLoadingDialog.setIcon(0);
		}

		if (mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
		}

		mLoadingDialog.setMessage(getString(msgId));
		mLoadingDialog.show();
	}

	protected void hideLoadingDialog() {
		if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
		}
	}

	private void parsePickerFile(String filePath) {
		boolean success = DataManager.getInstance(this).restoreData(filePath);
		if (!success) {
			success = DataManager.getInstance(this).importDataCSV(filePath);
		}
		
		if (success) {
			DMToast.makeText(this, R.string.settings_restore_success, Toast.LENGTH_LONG).show();
		} else {
			DMToast.makeText(this, R.string.settings_restore_failed, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference.getKey().equals(mBackupPref.getKey())) {
			backupData();
		} else if (preference.getKey().equals(mRestorePref.getKey())) {
			restoreData();
		} else if (preference.getKey().equals(mLockEnablePref.getKey())) {
			SharedPreferences prefs = getSharedPreferences(IContants.LOCK_PREFERENCES, 0);
			String currentPattern = prefs.getString(LockPatternActivity._Pattern, null);
			Intent intent = new Intent(this, LockPatternActivity.class);
			if (currentPattern != null) {
				intent.putExtra(LockPatternActivity._Mode, LockPatternActivity.LPMode.ComparePattern);
				startActivityForResult(intent, REQUEST_LOCK_CHANGE);
			} else {
				intent.putExtra(LockPatternActivity._Mode, LockPatternActivity.LPMode.CreatePattern);
				startActivity(intent);
			}
		} else if (preference.getKey().equals(mLockDisablePref.getKey())) {
			Intent intent = new Intent(this, LockPatternActivity.class);
			intent.putExtra(LockPatternActivity._Mode, LockPatternActivity.LPMode.ComparePattern);
			startActivityForResult(intent, REQUEST_LOCK_DISABLE);
		}
		return false;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(mLocalePref.getKey())) {
			UtilityHelper.switchLanguage(this);
		} 
		if (key.equals(mBarNotifyPref.getKey())) {
			UtilityHelper.showStartBarNotification(this);
		}
	}

}
