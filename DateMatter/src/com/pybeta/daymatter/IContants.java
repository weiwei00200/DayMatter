package com.pybeta.daymatter;

import com.pybeta.widget.DatePicker;

public interface IContants {
    
    String COMMOM_FILE_MATTER_NAME = "matter_data";
    String COMMOM_FILE_WIDGET_NAME = "widget_data";
    String COMMOM_FILE_CN_HOLIDAY_NAME = "holiday_cn.dat";
    String COMMOM_FILE_TW_HOLIDAY_NAME = "holiday_tw.dat";
    String COMMOM_FILE_EN_HOLIDAY_NAME = "holiday_en.dat";
    
    String COMMOM_LANG_CN = "cn";
    String COMMOM_LANG_TW = "tw";
    String COMMOM_LANG_EN = "en";
    
    String BASE_HISTORY_TODAY_URL = "http://pybeta.sinaapp.com/today?date=%s&lang=%s";
    
    String KEY_CHANGELOG_V_1_2_0 = "key_changelog_v_1_2_0";
    String KEY_CHANGELOG_V_1_2_1 = "key_changelog_v_1_2_1";
    String KEY_CHANGELOG_V_1_3_0 = "key_changelog_v_1_3_0";
    String KEY_CHANGELOG_V_1_3_1 = "key_changelog_v_1_3_1";
    String KEY_CHANGELOG_V_1_3_2 = "key_changelog_v_1_3_2";
    String KEY_CHANGELOG_V_1_3_5 = "key_changelog_v_1_3_5";
    String CURRENT_VERSION		 = KEY_CHANGELOG_V_1_3_5;
    
    String KEY_DATABASE_VERSION = "key_database_version";
    
    
    //matter repeat
    public static final int MATTER_REPEAT_NONE = 0;
    public static final int MATTER_REPEAT_WEEK = 1;
    public static final int MATTER_REPEAT_MONTH = 2;
    public static final int MATTER_REPEAT_YEAR = 3;
    
    //matter remind
    public static final int MATTER_REMIND_NONE = 0;
    public static final int MATTER_REMIND_THATDAY = 1;
    public static final int MATTER_REMIND_THEDAYBEFORE = 2;
    
    public static final int DAY_MILLS = 1000 * 60 * 60 * 24;
    
    
    // intent key
    String KEY_MATTER_TYPE      = "key_matter_type";
    String KEY_MATTER_DATA      = "key_matter_data";
    String kEY_MATTER_INDEX     = "key_matter_index";
    
    // Preference key
    String KEY_PREF_NAME        = "com.pybeta.daymatter_preferences";
    String KEY_PREF_SHOWN       = "key_pref_shown";
    String KEY_PREF_SECRET      = "settings_secret_preference";
    String KEY_PREF_SECRET_PWD  = "key_pref_secret_pwd";
    String KEY_PREF_SORT_TYPE   = "key_pref_sort_type";
    String KEY_PREF_LOCALE_TYPE   = "key_pref_locale_type";
    String KEY_PREF_STATUS_BAR_NOTIFY   = "key_pref_status_bar_notify";
    
    int MSG_SORT_COMPLETED      = 1;
    
    // date type
    int CALENDAR_GREGORIAN = DatePicker.CALENDAR_GREGORIAN;
    int CALENDAR_LUNAR = DatePicker.CALENDAR_LUNAR;
    
    // sort type
    int SORT_BY_DEFAULT         = 0;
    int SORT_BY_DATE_AESC       = 1;
    int SORT_BY_DATE_DESC       = 2;
    
    // data key.
    String KEY_DATA_ID          = "id";
    String KEY_DATA_UID          = "uid";
    String KEY_DATA_MATTER      = "matter";
    String KEY_DATA_DATE        = "date";
    String KEY_DATA_CREATE_DATE = "create_date";
    String KEY_DATA_CALENDAR    = "calendar";
    String KEY_DATA_CATEGORY    = "category";
    String KEY_DATA_TOP         = "top";
    String KEY_DATA_REPEAT      = "repeat";
    String KEY_DATA_REMIND      = "remind";
    String KEY_DATA_STATUS      = "status";
    String KEY_DATA_SECRET      = "secret";
    String KEY_DATA_REMARK      = "remark";
    String KEY_DATA_HOUR      = "hour";
    String KEY_DATA_MIN      = "min";
    String KEY_DATA_ALARMCODE      = "alarmcode";
    String KEY_DATA_NEXTREMINDTIME = "next_remind_time";
    
    // date matter
    String REQUEST_PARAM_1 = "dateninth";
    String REQUEST_PARAM_2 = "matterclan";
    
    // widget info key
    String KEY_WIDGET_ID        = "widget_id";
    String KEY_WIDGET_MATTER    = "widget_matter";
    String KEY_WIDGET_TYPE    = "widget_type";
    
    // lock
	String LOCK_PREFERENCES = "lock_pattern";
	
	// google drive folder
	String BACKUP_FILE_MIMETYPE = "text/plain";
	String GOOGLE_DRIVE_MIMETYPE = "application/vnd.google-apps.folder";
	String GOOGLE_DRIVE_SEARCH_FOLDER = "title = 'DateMatter' and mimeType = 'application/vnd.google-apps.folder'";
	String GOOGLE_DRIVE_SEARCH_DATA_FILE = "title contains 'DateMatter' and mimeType = 'text/plain'";
}
