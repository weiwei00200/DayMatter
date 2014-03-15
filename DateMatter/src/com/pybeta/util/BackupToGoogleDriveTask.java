package com.pybeta.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Children;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.core.DataManager;

public class BackupToGoogleDriveTask extends AsyncTask<String, String, File> {

	protected ProgressDialog mLoadingDialog = null;
	private Context mContext = null;
	private Drive mDriveService = null;
	
	public BackupToGoogleDriveTask(Context context,Drive drive){
		mContext = context;
		mDriveService = drive;
	}
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
			
			String path = DataManager.getInstance(mContext).exportDataCSV();
			fileContent = new java.io.File(path);
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			String dataStr = dateFormat.format(new Date());

			FileContent mediaContent = new FileContent("text/csv", fileContent);
            File body = new File();
			body.setTitle("DateMatter_" + dataStr + ".csv");
			body.setMimeType(IContants.BACKUP_FILE_MIMETYPE);
			body.setParents(Arrays.asList(new ParentReference().setId(folderId)));

			return mDriveService.files().insert(body, mediaContent).execute();
		}catch (Exception e) {
			e.printStackTrace();
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
			DMToast.makeText(mContext, R.string.settings_backup_to_drive_success, Toast.LENGTH_LONG).show();
		} else {
			DMToast.makeText(mContext, R.string.settings_backup_failed, Toast.LENGTH_LONG).show();
		}
	}
	protected void showLoadingDialog(int msgId, OnCancelListener listener) {
		if (mLoadingDialog == null) {
			mLoadingDialog = new ProgressDialog(mContext);
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

		mLoadingDialog.setMessage(mContext.getString(msgId));
		mLoadingDialog.show();
	}

	protected void hideLoadingDialog() {
		if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
		}
	}
}
