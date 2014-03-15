package com.pybeta.util;

import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.pybeta.daymatter.BuildConfig;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.core.DataManager;
import com.sammie.common.log.LogManager;

public class RestoreFromDriveTask extends AsyncTask<String, String, List<File>> {
	protected ProgressDialog mLoadingDialog = null;
	private Context mContext = null;
	private Drive mDriveService = null;
	private IRestoreFromDriveListener mListener = null;

	public RestoreFromDriveTask(Context context, Drive driveService,
			IRestoreFromDriveListener listener) {
		mContext = context;
		mDriveService = driveService;
		mListener = listener;
	}

	@Override
	protected void onPreExecute() {
		try {
			showLoadingDialog(R.string.settings_restore_get_file_list_progress,
					new OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							RestoreFromDriveTask.this.cancel(true);
						}
					});
		} catch (Exception e) {
			LogManager.writeErrorLog(e.getMessage() + "");
		}

	}

	@Override
	protected List<File> doInBackground(String... params) {
		try {
			Files.List request = mDriveService.files().list();
			FileList fileList = request.setQ(
					IContants.GOOGLE_DRIVE_SEARCH_DATA_FILE).execute();
			if (fileList != null) {
				return fileList.getItems();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}

	@Override
	protected void onPostExecute(List<File> result) {
		hideLoadingDialog();

		if (result != null) {
			if (result.size() == 0) {
				DMToast.makeText(mContext,
						R.string.settings_restore_file_empty, Toast.LENGTH_LONG)
						.show();
			} else {
				showDrivePickerFileDialog(result);
			}
		} else {
			DMToast.makeText(mContext,
					R.string.settings_restore_get_file_list_failed,
					Toast.LENGTH_LONG).show();
		}
	}

	private void showDrivePickerFileDialog(final List<File> result) {
		FileListAdapter adapter = new FileListAdapter(mContext,
				R.layout.file_dialog_row, R.id.fdrowtext, result);

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.settings_restore_picker_file);
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				File chooseFile = result.get(which);
				fetchFileContent(chooseFile);
			}
		});
		builder.create().show();
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

	// Adapter
	class FileListAdapter extends ArrayAdapter<File> {

		private int resource;
		private int fieldId = 0;
		private LayoutInflater inflater;

		public FileListAdapter(Context context, int resource,
				int textViewResourceId, List<File> objects) {
			super(context, resource, textViewResourceId, objects);
			this.resource = resource;
			this.fieldId = textViewResourceId;
			this.inflater = LayoutInflater.from(context);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			return createViewFromResource(position, convertView, parent,
					resource);
		}

		private View createViewFromResource(int position, View convertView,
				ViewGroup parent, int resource) {
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

	protected void fetchFileContent(File file) {
		if (file != null) {
			new GetFileContentTask().execute(file.getId());
		}
	}

	class GetFileContentTask extends AsyncTask<String, String, Boolean> {
		@Override
		protected void onPreExecute() {
			showLoadingDialog(R.string.settings_restore_progress,
					new OnCancelListener() {
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
				HttpResponse resp = mDriveService.getRequestFactory()
						.buildGetRequest(new GenericUrl(file.getDownloadUrl()))
						.execute();
				return DataManager.getInstance(mContext).importDataCSV(
						resp.getContent());
			} catch (IOException e) {
				if (BuildConfig.DEBUG)
					e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			hideLoadingDialog();

			if (result) {
				DMToast.makeText(mContext, R.string.settings_restore_success,
						Toast.LENGTH_LONG).show();
			} else {
				DMToast.makeText(mContext, R.string.settings_restore_failed,
						Toast.LENGTH_LONG).show();
			}
			if(null != mListener)
				mListener.restoreFinish();
		}
	}

	public interface IRestoreFromDriveListener {
		void restoreFinish();
	}
}
