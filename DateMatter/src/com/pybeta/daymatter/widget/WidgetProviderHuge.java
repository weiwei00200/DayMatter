package com.pybeta.daymatter.widget;

import com.pybeta.daymatter.BuildConfig;
import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.WidgetInfo;
import com.pybeta.daymatter.core.DataManager;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.daymatter.ui.HomeActivity;
import com.pybeta.daymatter.utils.DateUtils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetProviderHuge extends AppWidgetProvider {

    private static final String ACTION_DATE_CHANGED = Intent.ACTION_DATE_CHANGED;
    private static final String ACTION_TIME_SET     = Intent.ACTION_TIME_CHANGED;
    public static final String ACTION_DATE_SET     = "com.pybeta.daymatter.DATE_CHANGED";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (BuildConfig.DEBUG) {
            System.out.println("Receiver intent: " + intent);
        }
        
        String action = intent.getAction();
        if (ACTION_DATE_CHANGED.equals(action) || ACTION_DATE_SET.equals(action)) {
            updateWidgetView(context);
            return;
        } else if (ACTION_TIME_SET.equals(action)) {
            DateUtils.setDateChangedListener(context);
            updateWidgetView(context);
            return;
        }
        super.onReceive(context, intent);
    }

    private void updateWidgetView(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[]  appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WidgetProviderHuge.class));
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
        }
    }
    
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            String dayMatterId = null;
            WidgetInfo info = DataManager.getInstance(context).getWidgetInfoByWidgetId(appWidgetId);
            if (info != null)
                dayMatterId = info.dayMatterId;
            updateAppWidget(context, appWidgetManager, appWidgetId, dayMatterId);
        }
    }
    
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            DataManager.getInstance(context).deleteWidgetInfoByWidgetId(appWidgetIds[i]);
        }
    }
    
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId, String dayMatterId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.daymatter_appwidget_huge);

        if (dayMatterId == null) {
            showDayMatterDelTip(context, views);
        } else {
            DayMatter dayMatter = DatabaseManager.getInstance(context).query(dayMatterId);
            if (dayMatter == null) {
                showDayMatterDelTip(context, views);
            } else {
                Intent intent = new Intent(context, HomeActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                views.setOnClickPendingIntent(R.id.view_top_remind, pendingIntent);
                
                showDayMatter(context, views, dayMatter);
            }
        }
        
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void showDayMatterDelTip(Context context, RemoteViews views) {
        views.setTextViewText(R.id.tv_matter_title, context.getString(R.string.app_name));
        views.setTextViewText(R.id.tv_matter_date, context.getString(R.string.mattery_widget_matter_be_delete));
        views.setTextViewText(R.id.tv_matter_days, "");
        views.setImageViewResource(R.id.iv_matter_unit, 0);
    }
    
    private static void showDayMatter(Context context, RemoteViews views, DayMatter dayMatter) {
    	
        int duration = DateUtils.getDaysBetween(dayMatter).get("Days");
        
        String format = null;
        if (duration >= 0) {
            format = context.getString(R.string.matter_future_format);
        } else {
            format = context.getString(R.string.matter_past_format);
        }
        views.setTextViewText(R.id.tv_matter_title, String.format(format, dayMatter.getMatter()));
        
        views.setTextViewText(R.id.tv_matter_days, "" + Math.abs(duration));
        
        String dataStr = DateUtils.getMatterDateString(context, dayMatter);
        views.setTextViewText(R.id.tv_matter_date, dataStr);
        
        if (Math.abs(duration) > 1) {
            views.setImageViewResource(R.id.iv_matter_unit, R.drawable.days);
        } else {
            views.setImageViewResource(R.id.iv_matter_unit, R.drawable.day);
        }
    }
    
}
