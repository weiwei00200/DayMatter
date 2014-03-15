package com.pybeta.daymatter.utils;

import java.io.File;
import java.util.List;
import java.util.Locale;

import com.pybeta.daymatter.BuildConfig;
import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.WidgetInfo;
import com.pybeta.daymatter.core.DataManager;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.daymatter.ui.HomeRecActivity;
import com.pybeta.daymatter.widget.WidgetProvider;
import com.pybeta.daymatter.widget.WidgetProviderHuge;
import com.pybeta.daymatter.widget.WidgetProviderWide;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;

public class UtilityHelper {

	public static void switchLanguage(Activity activity) {
		Locale locale = getCurrentLocale(activity);

		Resources resources = activity.getResources();
		Configuration config = resources.getConfiguration();
		DisplayMetrics dm = resources.getDisplayMetrics();
		config.locale = locale;
		resources.updateConfiguration(config, dm);

		String[] defaultCategory = activity.getResources().getStringArray(R.array.matter_category);
		DatabaseManager.getInstance(activity).updateCatWhenSwithcLang(defaultCategory);
		
		Intent it = new Intent(activity, HomeRecActivity.class);
		it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(it);
		activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "0.0.9";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}

	private static final int LOCALE_DEFAULT = 0;
	private static final int LOCALE_SIMPLIFIED_CHINESE = 1;
	private static final int LOCALE_TRADITIONAL_CHINESE = 2;
	private static final int LOCALE_ENGLISH = 3;

	public static boolean getBarNotifySettings(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(IContants.KEY_PREF_STATUS_BAR_NOTIFY, false);
	}
	public static void setBarNotifySettings(Context context,boolean isShowing) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(IContants.KEY_PREF_STATUS_BAR_NOTIFY, isShowing);
		editor.commit();
	}
	public static boolean getSecretSettings(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(IContants.KEY_PREF_SECRET, false);
	}

	public static void setSecretSettings(Context context, boolean secret, String passwd) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(IContants.KEY_PREF_SECRET_PWD, passwd);
		editor.putBoolean(IContants.KEY_PREF_SECRET, secret);
		editor.commit();
	}

	public static boolean checkSecretInfo(Context context, String passwd) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String savePasswd = preferences.getString(IContants.KEY_PREF_SECRET_PWD, "");
		return savePasswd.equals(passwd);
	}

	public static int getSortTypeIndex(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String type = preferences.getString(IContants.KEY_PREF_SORT_TYPE, String.valueOf(IContants.SORT_BY_DEFAULT));
		return Integer.parseInt(type);
	}

	public static void setSortTypeIndex(Context context, int index) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(IContants.KEY_PREF_SORT_TYPE, String.valueOf(index));
		editor.commit();
	}

	public static boolean getChangeLogShow(Context context, String version) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(version, true);
	}

	public static void setChangeLogShow(Context context, String version) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(version, false);
		editor.commit();
	}
	
	public static int getDatabaseVersion(Context context, String version) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getInt(version, 0);
	}

	public static void setDatabaseVersion(Context context, String version,int code) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(version, code);
		editor.commit();
	}
	

	public static Locale getCurrentLocale(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String type = preferences.getString(IContants.KEY_PREF_LOCALE_TYPE, String.valueOf(LOCALE_DEFAULT));
		int localeType = Integer.parseInt(type);

		if (BuildConfig.DEBUG) {
			System.out.println("getCurrentLocale(), localeType=" + localeType);
		}

		switch (localeType) {
		case LOCALE_SIMPLIFIED_CHINESE:
			return Locale.SIMPLIFIED_CHINESE;
		case LOCALE_TRADITIONAL_CHINESE:
			return Locale.TRADITIONAL_CHINESE;
		case LOCALE_ENGLISH:
			return Locale.ENGLISH;
		default:
			return Locale.getDefault();
		}
	}

	public static void setLocale(Context context, Locale locale) {
		int type = LOCALE_DEFAULT;
		if (locale == null) {
			type = LOCALE_DEFAULT;
		} else if (locale.equals(Locale.ENGLISH)) {
			type = LOCALE_ENGLISH;
		} else if (locale.equals(Locale.TRADITIONAL_CHINESE)) {
			type = LOCALE_TRADITIONAL_CHINESE;
		} else if (locale.equals(Locale.SIMPLIFIED_CHINESE)) {
			type = LOCALE_SIMPLIFIED_CHINESE;
		}

		if (BuildConfig.DEBUG) {
			System.out.println("setLocale(), localeType=" + type);
		}

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(IContants.KEY_PREF_LOCALE_TYPE, String.valueOf(type));
		editor.commit();
	}

	public static String takeScreenshot(Activity activity) {
		String filaName = null;

		try {
			View decorView = activity.getWindow().getDecorView();
			decorView.setDrawingCacheEnabled(true);
			Bitmap bmp = decorView.getDrawingCache();

			Rect rect = new Rect();
			decorView.getWindowVisibleDisplayFrame(rect);
			int statusBarHeight = rect.top;

			int width = bmp.getWidth();
			int height = bmp.getHeight();

			Bitmap saveBmp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight, null, false);
			filaName = FileUtils.savePicture(activity, saveBmp);

			decorView.setDrawingCacheEnabled(false);

			if (saveBmp != null) {
				saveBmp.recycle();
			}

			if (bmp != null) {
				bmp.recycle();
			}

			Uri uri = Uri.fromFile(new File(filaName));
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
			activity.sendBroadcast(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filaName;
	}

	public static void share(Context ctx, String subJect, String content, String pic) {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		if (!TextUtils.isEmpty(pic)) {
			intent.setType("image/*");
			Uri uri = Uri.parse("file:///" + pic);
			intent.putExtra(Intent.EXTRA_STREAM, uri);
		} else {
			intent.setType("text/plain");
		}
		intent.putExtra(Intent.EXTRA_SUBJECT, subJect);
		intent.putExtra(Intent.EXTRA_TEXT, content);
		ctx.startActivity(Intent.createChooser(intent, ctx.getString(R.string.share_intent_title)));
	}
	
	public static void follow(Context ctx, String url) {
		final Intent intent = new Intent("android.intent.action.VIEW");
		if(TextUtils.isEmpty(url))
			return;
		intent.setData(Uri.parse(url));
		ctx.startActivity(intent);
	}

	public static void showStartBarNotification(final Context ctx) {
		Thread workThread = new Thread(new Runnable() {
			@Override
			public void run() {
				NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
				if (getBarNotifySettings(ctx)) {
					List<DayMatter> matterList = DatabaseManager.getInstance(ctx).queryAll(0);
					matterList = DataManager.getInstance(ctx).sortMatters(matterList, IContants.SORT_BY_DATE_AESC);
					if (matterList != null && matterList.size() > 0) {
						DayMatter matter = matterList.get(0);
						
				    	int duration = DateUtils.getDaysBetween(matter).get("Days");
				    	String matterDesc = null;
				    	if (duration == 0) {
				    		matterDesc = String.format(ctx.getString(R.string.notification_matter_today), matter.getMatter());
				    	} else if (duration > 0) {
				    		String format = ctx.getString(R.string.matter_future_format);
				            matterDesc = String.format(format, matter.getMatter()) + Math.abs(duration) + ctx.getString(R.string.matter_unit);
				        } else {
				        	String format = ctx.getString(R.string.matter_past_format);
				            matterDesc = String.format(format, matter.getMatter()) + Math.abs(duration) + ctx.getString(R.string.matter_unit);
				        }
			            String dataStr = DateUtils.getMatterDateString(ctx, matter);
			            
						NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);
						builder.setSmallIcon(R.drawable.ic_launcher);
						builder.setContentTitle(matterDesc);
						builder.setContentText(String.format(ctx.getString(R.string.matter_top_date_format), dataStr));
						builder.setAutoCancel(false);
						builder.setOngoing(true);

						Intent resultIntent = new Intent(ctx, HomeRecActivity.class);
						PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, resultIntent, 0);
						builder.setContentIntent(pendingIntent);

						notificationManager.notify(10000, builder.build());
						return;
					}
				} 
				notificationManager.cancel(10000);
			}
		});
		workThread.start();
	}
	
	public static void showNotification(Context ctx, String contentTitle, String contentText, int size,int notify) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(contentTitle);
		builder.setContentText(contentText);
		builder.setAutoCancel(true);
		builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS);
//
//		if (size > 0) {
//			builder.setContentInfo(String.valueOf(size));
//		}
//
		Intent resultIntent = new Intent(ctx, HomeRecActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pendingIntent);
		builder.setLights(Color.GREEN, 0, 1);
		Notification mNotification = builder.build();
		
//		Notification mNotification = new Notification();
//		mNotification.flags |=Notification.FLAG_SHOW_LIGHTS;
		mNotification.icon = R.drawable.ic_launcher;
		mNotification.tickerText = contentText;
		NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(notify, mNotification);
	}

	public static void updateWidgetView(Context ctx, WidgetInfo info) {
		if (info != null) {
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ctx);
			switch (info.type) {
			case WidgetInfo.WIDGET_TYPE_NORMAL: {
				WidgetProvider.updateAppWidget(ctx, appWidgetManager, info.widgetId, info.dayMatterId);
				break;
			}
			case WidgetInfo.WIDGET_TYPE_WIDE: {
				WidgetProviderWide.updateAppWidget(ctx, appWidgetManager, info.widgetId, info.dayMatterId);
				break;
			}
			case WidgetInfo.WIDGET_TYPE_HUGE: {
				WidgetProviderHuge.updateAppWidget(ctx, appWidgetManager, info.widgetId, info.dayMatterId);
				break;
			}
			}
		}
	}
}
