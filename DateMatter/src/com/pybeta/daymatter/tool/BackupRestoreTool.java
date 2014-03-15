package com.pybeta.daymatter.tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.drive.DriveScopes;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.bean.Codes;
import com.pybeta.daymatter.core.DataManager;
import com.pybeta.daymatter.ui.FileManagerActivity;
import com.pybeta.util.DMToast;

public class BackupRestoreTool {
	//备份到sdcard
    public static void backupToSdcard(Context context) {
		String path = DataManager.getInstance(context).exportDataCSV();
		if (path != null) {
			String tipsFotmat = context.getString(R.string.settings_backup_success);
			DMToast.makeText(context, String.format(tipsFotmat, path), Toast.LENGTH_LONG).show();
		} else {
			DMToast.makeText(context, R.string.settings_backup_failed, Toast.LENGTH_LONG).show();
		}
	}
    //从sdcard恢复
    public static void restoreFromSdcard(Context context) {
		DMToast.makeText(context, R.string.settings_restore_picker_file, Toast.LENGTH_LONG).show();
		Intent intent = new Intent(context, FileManagerActivity.class);
		((Activity)context).startActivityForResult(intent, Codes.REQUEST_SDCARD_RESTORE);
	}
    //备份到google
    public static GoogleAccountCredential backupToGoogleDrive(Context context, GoogleAccountCredential driveCredential) {
		try {
			driveCredential = GoogleAccountCredential.usingOAuth2(context, DriveScopes.DRIVE);
			((Activity)context).startActivityForResult(driveCredential.newChooseAccountIntent(), Codes.REQUEST_GOOGLE_BACKUP);
		} catch (Exception e) {
			DMToast.makeText(context, R.string.settings_backup_no_google_play_services, Toast.LENGTH_LONG).show();
		}
		return driveCredential;
	}
    //从google恢复
    public static GoogleAccountCredential restoreFromGoogleDrive(Context context, GoogleAccountCredential driveCredential) {
		try {
			driveCredential = GoogleAccountCredential.usingOAuth2(context, DriveScopes.DRIVE);
			((Activity)context). startActivityForResult(driveCredential.newChooseAccountIntent(), Codes.REQUEST_GOOGLE_RESTORE);
		} catch (Exception e) {
			DMToast.makeText(context, R.string.settings_restore_no_google_play_services, Toast.LENGTH_LONG).show();
		}
		return driveCredential;
	}
}
