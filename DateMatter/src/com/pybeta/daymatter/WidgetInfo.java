package com.pybeta.daymatter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WidgetInfo {

	public static final int WIDGET_TYPE_NORMAL	= 1;
	public static final int WIDGET_TYPE_MIDDLE	= 2;
	public static final int WIDGET_TYPE_WIDE	= 3;
	public static final int WIDGET_TYPE_HUGE	= 4;
	
    public int widgetId;
    public String dayMatterId;
    public int type = WIDGET_TYPE_HUGE;
    
    public WidgetInfo() {
    }
    
    public WidgetInfo(JSONObject data) {
        widgetId = data.optInt(IContants.KEY_WIDGET_ID);
        type = data.optInt(IContants.KEY_WIDGET_TYPE, WIDGET_TYPE_HUGE);
        dayMatterId = getNoneNullString(data.optString(IContants.KEY_WIDGET_MATTER));
    }
    
    public JSONObject toJsonObject() {
        JSONObject data = new JSONObject();

        try {
            data.put(IContants.KEY_WIDGET_ID, widgetId);
            data.put(IContants.KEY_WIDGET_TYPE, type);
            data.put(IContants.KEY_WIDGET_MATTER, getNoneNullString(dayMatterId));
        } catch (JSONException e) {
        }
        return data;
    }
    
    public static String getNoneNullString(String str) {
        return str == null ? "" : str;
    }
    
    public static ArrayList<WidgetInfo> createFromJsonArray(JSONArray dataArray) {
        if (dataArray == null)
            throw new IllegalArgumentException("json array is null.");

        ArrayList<WidgetInfo> widgetInfos = new ArrayList<WidgetInfo>();
        for (int index = 0; index < dataArray.length(); index++) {
            JSONObject data = dataArray.optJSONObject(index);
            if (data != null) {
                WidgetInfo info = new WidgetInfo(data);
                widgetInfos.add(info);
            }
        }

        return widgetInfos;
    }
}
