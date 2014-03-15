package com.pybeta.daymatter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pybeta.daymatter.db.MatterTable;

import android.content.ContentValues;
import android.database.Cursor;

public class DayMatter implements Serializable {

    private static final long serialVersionUID = -8686549472374751684L;
    
    public static final int MATTER_STATUS_NORMAL = 0;
    public static final int MATTER_STATUS_SECRET = 1;
    
    private int id;
    private String uid;
    private String matter;
    private long date;
    private long nextremindtime;
    private long oldDate;
    private int calendar;
    private int category;
    private int top;
    private int repeat;
    private int remind;
    private int status;
    private String remark;
    private int hour;
    private int min;
    
    

    public DayMatter() {
        this.uid = UUID.randomUUID().toString();
        this.matter = "";
        this.date = System.currentTimeMillis();
        this.calendar = IContants.CALENDAR_GREGORIAN;
    }

    public DayMatter(String matter, long date, int category, int top, int repeat, int remind, int status,long nextRemindTime) {
        this.uid = UUID.randomUUID().toString();
        this.matter = getNoneNullString(matter);
        this.date = date;
        this.nextremindtime = nextRemindTime;
        this.oldDate = date; 
        this.category = category;
        this.top = top;
        this.repeat = repeat;
        this.remind = remind;
        this.status = status;
        this.calendar = IContants.CALENDAR_GREGORIAN;
        
    }

    public DayMatter(Cursor cursor) {
    	if (cursor != null) {
    		id = cursor.getInt(cursor.getColumnIndex(MatterTable.ID));
    		uid = cursor.getString(cursor.getColumnIndex(MatterTable.UID));
    		matter = cursor.getString(cursor.getColumnIndex(MatterTable.MATTER));
    		date = cursor.getLong(cursor.getColumnIndex(MatterTable.DATE));
    		nextremindtime = cursor.getLong(cursor.getColumnIndex(MatterTable.NEXTREMINDTIME));
    		oldDate = cursor.getLong(cursor.getColumnIndex(MatterTable.DATE));
    		calendar = cursor.getInt(cursor.getColumnIndex(MatterTable.CALENDAR));
    		category = cursor.getInt(cursor.getColumnIndex(MatterTable.CATEGORY));
    		top = cursor.getInt(cursor.getColumnIndex(MatterTable.TOP));
    		repeat = cursor.getInt(cursor.getColumnIndex(MatterTable.REPEAT));
    		remind = cursor.getInt(cursor.getColumnIndex(MatterTable.REMIND));
    		status = cursor.getInt(cursor.getColumnIndex(MatterTable.STATUS));
    		remark = cursor.getString(cursor.getColumnIndex(MatterTable.REMARK));
    		hour =  cursor.getInt(cursor.getColumnIndex(MatterTable.HOUR));
    		min =  cursor.getInt(cursor.getColumnIndex(MatterTable.MIN));
    		
    	}
    }
    
    public DayMatter(JSONObject jsonData) {
        if (jsonData != null) {
        	
        	id = jsonData.optInt(IContants.KEY_DATA_ID);
        	
            uid = getNoneNullString(jsonData.optString(IContants.KEY_DATA_UID));
            matter = getNoneNullString(jsonData.optString(IContants.KEY_DATA_MATTER));
            date = jsonData.optLong(IContants.KEY_DATA_DATE);
            nextremindtime = jsonData.optLong(IContants.KEY_DATA_NEXTREMINDTIME);
            oldDate = jsonData.optLong(IContants.KEY_DATA_DATE);
            calendar = jsonData.optInt(IContants.KEY_DATA_CALENDAR, IContants.CALENDAR_GREGORIAN);
            category = jsonData.optInt(IContants.KEY_DATA_CATEGORY);
            top = jsonData.optInt(IContants.KEY_DATA_TOP);
            repeat = jsonData.optInt(IContants.KEY_DATA_REPEAT);
            remind = jsonData.optInt(IContants.KEY_DATA_REMIND);
            status = jsonData.optInt(IContants.KEY_DATA_STATUS, MATTER_STATUS_NORMAL);
            remark = jsonData.optString(IContants.KEY_DATA_REMARK, "");
            
            hour = jsonData.optInt(IContants.KEY_DATA_HOUR);
            min = jsonData.optInt(IContants.KEY_DATA_MIN);
            
        }
    }

    public DayMatter(DayMatter origin) {
        if (origin != null) {
        	this.id = origin.getId();
            this.uid = origin.getUid();
            this.matter = origin.getMatter();
            this.date = origin.getDate();
            this.nextremindtime = origin.getNextRemindTime();
            this.oldDate = origin.getDate();
            this.calendar = origin.getCalendar();
            this.category = origin.getCategory();
            this.top = origin.getTop();
            this.repeat = origin.getRepeat();
            this.remind = origin.getRemind();
            this.status = origin.getStatus();
            this.remark = origin.getRemark();
            this.hour = origin.getHour();
            this.min = origin.getMin();
        } else {
            this.uid = UUID.randomUUID().toString();
            this.matter = "";
            this.date = System.currentTimeMillis();
            this.nextremindtime = 0;
            this.oldDate = System.currentTimeMillis();
            this.calendar = IContants.CALENDAR_GREGORIAN;
        }
    }

    public void copy(DayMatter origin) {
        if (origin != null) {
            this.matter = origin.getMatter();
            this.date = origin.getDate();
            this.nextremindtime = origin.getNextRemindTime();
            this.calendar = origin.getCalendar();
            this.category = origin.getCategory();
            this.top = origin.getTop();
            this.repeat = origin.getRepeat();
            this.remind = origin.getRemind();
            this.status = origin.getStatus();
            this.remark = origin.getRemark();
            this.hour = origin.getHour();
            this.min = origin.getMin();
        }
    }
    public String getUpdateSql(){
    	return "update "+MatterTable.TABLE_NAME+" set matter='"+getNoneNullString(matter)+"',date='"+date+
    			"',nextremindtime='"+nextremindtime+"',calendar='"+calendar+"',category='"+category+"',top='"+top+"',repeat='"+repeat+"',remind='"+remind+"',status='"+status+"',remark='"+remark+"',hour='"+hour+"',min='"+min+"' where uid='"+uid+"'";
    }
    
    public ContentValues getContentValues() {
    	ContentValues values = new ContentValues();
    	
//    	values.put(MatterTable.ID, id);
    	
    	values.put(MatterTable.UID, getNoneNullString(uid));
    	values.put(MatterTable.MATTER, getNoneNullString(matter));
    	values.put(MatterTable.DATE, date);
    	values.put(MatterTable.NEXTREMINDTIME, nextremindtime);
    	values.put(MatterTable.CALENDAR, calendar);
    	values.put(MatterTable.CATEGORY, category);
    	values.put(MatterTable.TOP, top);
    	values.put(MatterTable.REPEAT, repeat);
    	values.put(MatterTable.REMIND, remind);
    	values.put(MatterTable.STATUS, status);
    	values.put(MatterTable.REMARK, remark);
    	
    	values.put(MatterTable.HOUR, hour);
    	values.put(MatterTable.MIN, min);
        
    	return values;
    }
    
    public static String getNoneNullString(String str) {
        return str == null ? "" : str;
    }

    public JSONObject toJsonObject() {
        JSONObject data = new JSONObject();
        try {
        	
        	data.put(IContants.KEY_DATA_ID, id);
            data.put(IContants.KEY_DATA_UID, getNoneNullString(uid));
            data.put(IContants.KEY_DATA_MATTER, getNoneNullString(matter));
            data.put(IContants.KEY_DATA_DATE, date);
            data.put(IContants.KEY_DATA_NEXTREMINDTIME, nextremindtime);
            data.put(IContants.KEY_DATA_CALENDAR, calendar);
            data.put(IContants.KEY_DATA_CATEGORY, category);
            data.put(IContants.KEY_DATA_TOP, top);
            data.put(IContants.KEY_DATA_REPEAT, repeat);
            data.put(IContants.KEY_DATA_REMIND, remind);
            data.put(IContants.KEY_DATA_STATUS, status);
            data.put(IContants.KEY_DATA_REMARK, remark);
            
            data.put(IContants.KEY_DATA_HOUR, hour);
            data.put(IContants.KEY_DATA_MIN, min);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMatter() {
        return getNoneNullString(matter);
    }

    public void setMatter(String matter) {
        this.matter = matter;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getRemind() {
        return remind;
    }

    public void setRemind(int remind) {
        this.remind = remind;
    }

    
    public int getHour() {
        return hour;
    }
    public int getMin() {
        return min;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMin(int min) {
        this.min = min;
    }
    
    
    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCalendar() {
		return calendar;
	}

	public void setCalendar(int calendar) {
		this.calendar = calendar;
	}

	public long getOldDate() {
		return oldDate;
	}

	public void setOldDate(long oldDate) {
		this.oldDate = oldDate;
	}

	public static ArrayList<DayMatter> createFromJsonArray(JSONArray dataArray) {
        if (dataArray == null) {
            throw new IllegalArgumentException("json array is null.");
        }

        ArrayList<DayMatter> dayMatters = new ArrayList<DayMatter>();
        for (int index = 0; index < dataArray.length(); index++) {
            JSONObject data = dataArray.optJSONObject(index);
            if (data != null && data.has(IContants.KEY_DATA_ID) && data.has(IContants.KEY_DATA_MATTER)) {
                DayMatter dayMatter = new DayMatter(data);
                dayMatters.add(dayMatter);
            }
        }

        return dayMatters;
    }

	public String getRemark() {
		return getNoneNullString(remark);
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getNextRemindTime() {
		return nextremindtime;
	}

	public void setNextRemindTime(long nextRemindTime) {
		this.nextremindtime = nextRemindTime;
	}

	public String[] toRecord() {
		return new String[] {
				String.valueOf(id), uid, getNoneNullString(matter), String.valueOf(date),
				String.valueOf(nextremindtime),
				String.valueOf(calendar),
				String.valueOf(category), String.valueOf(top), 
				String.valueOf(repeat), String.valueOf(remind),String.valueOf(status),
				getNoneNullString(remark),
				String.valueOf(hour),String.valueOf(min)
		};
	}
}
