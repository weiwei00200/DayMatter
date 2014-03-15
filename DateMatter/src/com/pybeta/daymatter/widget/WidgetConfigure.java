package com.pybeta.daymatter.widget;

import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.R;
import com.pybeta.daymatter.WidgetInfo;
import com.pybeta.daymatter.core.DataManager;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.daymatter.ui.LockBaseActivity;
import com.pybeta.daymatter.ui.MatterListAdapter;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class WidgetConfigure extends LockBaseActivity implements OnItemClickListener{

    private ListView list;
    private MatterListAdapter adapter;
    
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_appwidget_configure);
        
        list = (ListView) findViewById(R.id.view_list);
        adapter = new MatterListAdapter(this);
        adapter.setItems(DatabaseManager.getInstance(this).queryAll(0).toArray());
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Context context = WidgetConfigure.this;

        DataManager dataMgr = DataManager.getInstance(context);
        DayMatter dayMatter = adapter.getItem(position);
        WidgetInfo info = dataMgr.getWidgetInfoByWidgetId(mAppWidgetId);
        if (info == null) {
            WidgetInfo widgetInfo = new WidgetInfo();
            widgetInfo.dayMatterId = dayMatter.getUid();
            widgetInfo.type = WidgetInfo.WIDGET_TYPE_NORMAL;
            widgetInfo.widgetId = mAppWidgetId;
            dataMgr.addWidgetInfo(widgetInfo);
        } else {
            info.dayMatterId = dayMatter.getUid();
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        WidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId, dayMatter.getUid());

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
    
}
