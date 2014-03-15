package com.pybeta.daymatter.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.pybeta.daymatter.ApkInfo;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.utils.LogUtil;
import com.pybeta.daymatter.utils.ToastUtil;



public class DLAService extends Service {
	private static final String APPDOWNLOADS = "daymatter";
	private NotificationManager nm;
	private boolean cancelUpdate = false;
	private MyHandler myHandler;
	private int[] notifyIds = {1000,1001,1002,1003,1004,1005,1006,1007,1008,1009,1010};
	MyRecevier myRecevier = null;
	public static final String APKDOWNLOAD_ACTION = "daymatter.intent.apkdownload.action";
	private ArrayList<String> mApkNameList = null;
	private ArrayList<NotifyIdList> mNotifyIdLists = null;
	
	class NotifyIdList{
		public NotifyIdList(int notifyId){
			isUsed = false;
			this.notifyId = notifyId;
		}
		boolean isUsed;
		int notifyId;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@SuppressLint("NewApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		myHandler = new MyHandler(Looper.myLooper(), this);
		if(myRecevier == null){
			myRecevier = new MyRecevier();
			IntentFilter filter = new IntentFilter();
			filter.addAction(APKDOWNLOAD_ACTION);
			registerReceiver(myRecevier, filter);
		}
		if(mApkNameList == null)
			mApkNameList = new ArrayList<String>();
		if(mNotificationMap == null)
			mNotificationMap = new HashMap<Integer, Notification>();
		if(mRemoteViewsMap == null)
			mRemoteViewsMap = new HashMap<Integer, RemoteViews>();
		if(mNotifyIdLists == null){
			mNotifyIdLists = new ArrayList<DLAService.NotifyIdList>();
			for(int i = 0;i < notifyIds.length ;i++){
				NotifyIdList m = new NotifyIdList(notifyIds[i]);
				mNotifyIdLists.add(m);
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private Object nameListLock = new Object();
	private Object idListLock = new Object();
	private Object notificationMapLock = new Object();
	class MyRecevier extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			LogUtil.Sysout(context,"onReceive action: "+action);
			if(action == null)
				return ;
			if(APKDOWNLOAD_ACTION.equals(action)){
				ApkInfo mApkInfo = (ApkInfo)intent.getSerializableExtra("ApkInfo");
				if(mApkInfo == null)
					return ;
				String name = mApkInfo.getName();
				synchronized (nameListLock) {
					if(mApkNameList.contains(name))
						return;
					else{
						
						downloadApk(mApkInfo);
					}
				}
					
			}
		}
		
	}
	
	private HashMap<Integer, Notification> mNotificationMap = null;
	private HashMap<Integer, RemoteViews> mRemoteViewsMap = null;
	
	private void downloadApk(ApkInfo mApkInfo){
		if(mApkInfo == null)
			return;
		int notifyId = -1;
		synchronized (idListLock) {
			for(NotifyIdList t: mNotifyIdLists){
				if(!t.isUsed){
					notifyId = t.notifyId;
					t.isUsed = true;
					break;
				}
			}
			if(notifyId == -1){
				ToastUtil.TextToast(getApplicationContext(), "仅最多同时下载10个安装包", Toast.LENGTH_SHORT);
				return;
			}
		}
		String url = mApkInfo.getUrl();
		String name =  mApkInfo.getName();
		Notification notification = new Notification();
		notification.icon = android.R.drawable.stat_sys_download;
		notification.tickerText = "下载"+name + "安装包";
		notification.when = System.currentTimeMillis();
		notification.defaults = Notification.DEFAULT_LIGHTS;

		// 设置任务栏中下载进程显示的views
		RemoteViews view = new RemoteViews(getPackageName(), R.layout.downloadapk);
		view.setTextViewText(R.id.downloadapk_name, name);
		view.setImageViewResource(R.id.downloadapk_ivLogo, R.drawable.downloadapk);
		notification.contentView = view;
		mRemoteViewsMap.put(Integer.valueOf(notifyId), view);
//		notification.setLatestEventInfo(this, "", "", null);
//		synchronized (notificationMapLock) {
//			mNotificationMap.put(Integer.valueOf(notifyId), notification);
//		}
		
		
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			
			PendingIntent contentIntent = PendingIntent.getActivity(this,0,null,0);
			notification.setLatestEventInfo(this, "", "", contentIntent);
		}
		synchronized (notificationMapLock) {
			mNotificationMap.put(Integer.valueOf(notifyId), notification);
		}
		
		// 将下载任务添加到任务栏中
		nm.notify(notifyId, notification);
		// 初始化下载任务内容views
		Bundle mBundle = new Bundle();
		mBundle.putInt("notifyId", notifyId);
		mBundle.putInt("precent", 0);
		mBundle.putString("name", name);
		Message message = myHandler.obtainMessage(3,
				mBundle);
		myHandler.sendMessage(message);
		
		doDownFile(name,url, notifyId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(myRecevier != null)
			unregisterReceiver(myRecevier);
		myRecevier = null;
		if(mNotificationMap != null)
			mNotificationMap.clear();
		mNotificationMap = null;
		if(mApkNameList != null)
			mApkNameList.clear();
		mApkNameList = null;
		if(mNotifyIdLists != null)
			mNotifyIdLists.clear();
		mNotifyIdLists = null;
		if(mRemoteViewsMap != null)
			mRemoteViewsMap.clear();
		mRemoteViewsMap = null;
	}
	
	private void doDownFile(String name,String url,int notifyId){
		new ApkDownloadThread(name,url, notifyId).start();
	}

	class ApkDownloadThread extends Thread{
		private String name;
		private String url;
		private int notifyId;
		private int download_precent;
		private File tempFile = null;
		public ApkDownloadThread(String name,String url,int notifyId){
			this.notifyId = notifyId;
			this.url = url;
			this.name = name;
			download_precent = 0;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Bundle mBundle = new Bundle();
			try {
				HttpClient client = new DefaultHttpClient();
				// params[0]代表连接的url
				HttpGet get = new HttpGet(url);
				HttpResponse response = client.execute(get);
				HttpEntity entity = response.getEntity();
				long length = entity.getContentLength();
				InputStream is = entity.getContent();
				if (is != null) {
					File rootFile = new File(
							Environment.getExternalStorageDirectory(), "/"
									+ APPDOWNLOADS + "/App");
					if (!rootFile.exists() && !rootFile.isDirectory())
						rootFile.mkdirs();

					tempFile = new File(
							Environment.getExternalStorageDirectory(),
							"/"
									+ APPDOWNLOADS
									+ "/App/"
									+ url.substring(url.lastIndexOf("/") + 1));
					if (tempFile.exists())
						tempFile.delete();
					tempFile.createNewFile();

					// 已读出流作为参数创建一个带有缓冲的输出流
					BufferedInputStream bis = new BufferedInputStream(is);

					// 创建一个新的写入流，讲读取到的图像数据写入到文件中
					FileOutputStream fos = new FileOutputStream(tempFile);
					// 已写入流作为参数创建一个带有缓冲的写入流
					BufferedOutputStream bos = new BufferedOutputStream(fos);

					int read;
					long count = 0;
					int precent = 0;
					byte[] buffer = new byte[1024];
					while ((read = bis.read(buffer)) != -1 && !cancelUpdate) {
						bos.write(buffer, 0, read);
						count += read;
						precent = (int) (((double) count / length) * 100);

						// 每下载完成5%就通知任务栏进行修改下载进度
						if (precent - download_precent >= 5) {
							download_precent = precent;
							mBundle.clear();
							mBundle.putInt("notifyId", notifyId);
							mBundle.putInt("precent", precent);
							mBundle.putString("name", name);
							Message message = myHandler.obtainMessage(3,
									mBundle);
							myHandler.sendMessage(message);
						}
					}
					bos.flush();
					bos.close();
					fos.flush();
					fos.close();
					is.close();
					bis.close();
				}

				if (!cancelUpdate) {
					mBundle.clear();
					mBundle.putInt("notifyId", notifyId);
					mBundle.putSerializable("tempFile", tempFile);
					mBundle.putString("name", name);
					Message message = myHandler.obtainMessage(2,
							mBundle);
					myHandler.sendMessage(message);
				} else {
					tempFile.delete();
				}
			} catch (ClientProtocolException e) {
				mBundle.clear();
				mBundle.putString("name", name);
				mBundle.putInt("notifyId", notifyId);
				mBundle.putString("Exception", "下载更新文件失败");
				Message message = myHandler.obtainMessage(4,
						mBundle);
				myHandler.sendMessage(message);
			} catch (IOException e) {
				mBundle.clear();
				mBundle.putString("name", name);
				mBundle.putInt("notifyId", notifyId);
				mBundle.putString("Exception", "下载更新文件失败");
				Message message = myHandler.obtainMessage(4,
						mBundle);
				myHandler.sendMessage(message);
			} catch (Exception e) {
				mBundle.clear();
				mBundle.putString("name", name);
				mBundle.putInt("notifyId", notifyId);
				mBundle.putString("Exception", "下载更新文件失败");
				Message message = myHandler.obtainMessage(4,
						mBundle);
				myHandler.sendMessage(message);
			}
		
		}
	}
	

	// 安装下载后的apk文件
	private void Instanll(File file, Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/* 事件处理类 */
	class MyHandler extends Handler {
		private Context context;

		public MyHandler(Looper looper, Context c) {
			super(looper);
			this.context = c;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			if (msg != null) {
				Bundle mBundle =null;
				int notifyId = -1;
				mBundle = (Bundle)msg.obj;
				notifyId = mBundle.getInt("notifyId");
				String name = mBundle.getString("name");
				switch (msg.what) {
				case 0:
					Toast.makeText(context, msg.obj.toString(),
							Toast.LENGTH_SHORT).show();
					break;
				case 1:
					break;
				case 2:

					// 下载完成后清除所有下载信息，执行安装提示
					
					nm.cancel(notifyId);
					synchronized (notificationMapLock) {
						mNotificationMap.remove(Integer.valueOf(notifyId));
					}
					File tempFile = (File)mBundle.getSerializable("tempFile");
					Instanll(tempFile, context);
					synchronized (nameListLock) {
						if(mApkNameList.contains(name))
							mApkNameList.remove(name);
					}
					synchronized (idListLock) {
						for(NotifyIdList t: mNotifyIdLists){
							if(t.notifyId == notifyId){
								t.isUsed = false;
								break;
							}
						}
					}
					// 停止掉当前的服务
//					stopSelf();
					break;
				case 3:
					int precent = mBundle.getInt("precent");
					// 更新状态栏上的下载进度信息
					Notification mNotification = null;
					synchronized (notificationMapLock) {
						mNotification = mNotificationMap.get(Integer.valueOf(notifyId));
						
						RemoteViews view = mRemoteViewsMap.get(Integer.valueOf(notifyId));
						view.setTextViewText(R.id.downloadapk_tvProcess, "已下载"
								+ precent + "%");
						view.setProgressBar(R.id.downloadapk_pbDownload, 100,
								precent, false);
						mNotification.contentView = view;
						LogUtil.Sysout("notificationMapLock: notifyId: "+notifyId+" , mNotification: "+mNotification+" ,precent:  "+precent);
						nm.notify(notifyId, mNotification);
					}
					break;
				case 4:
					nm.cancel(notifyId);
					synchronized (notificationMapLock) {
						mNotificationMap.remove(Integer.valueOf(notifyId));
					}
					synchronized (nameListLock) {
						if(mApkNameList.contains(name))
							mApkNameList.remove(name);
					}
					synchronized (idListLock) {
						for(NotifyIdList t: mNotifyIdLists){
							if(t.notifyId == notifyId){
								t.isUsed = false;
								break;
							}
						}
					}
					break;
				}
			}
		}
	}

	public static boolean isWorked(final Context context) {
		ActivityManager myManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(30);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString()
					.equals("com.aohe.icodestar.zandouji.upgradeservice")) {
				LogUtil.Sysout("下载更新的服务已启动");
				return true;
			}
		}
		LogUtil.Sysout("下载更新的服务未启动");
		return false;
	}

}
