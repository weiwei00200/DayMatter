package com.pybeta.daymatter.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pybeta.daymatter.BuildConfig;
import com.pybeta.daymatter.DayMatter;
import com.pybeta.daymatter.HistoryToday;
import com.pybeta.daymatter.IContants;
import com.pybeta.daymatter.WidgetInfo;
import com.pybeta.daymatter.db.DatabaseManager;
import com.pybeta.daymatter.db.MatterTable;
import com.pybeta.daymatter.utils.DateUtils;
import com.pybeta.daymatter.utils.FileUtils;
import com.pybeta.daymatter.utils.LogUtil;
import com.pybeta.daymatter.utils.UtilityHelper;
import com.pybeta.util.csv.CsvReader;
import com.pybeta.util.csv.CsvWriter;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.text.TextUtils;

public class DataManager {

    private Context mContext;
    private ArrayList<DayMatter> mHolidayList;
    private ArrayList<WidgetInfo> mWidgetInfoList;
    private HashMap<String, List<HistoryToday>> mHistoryData;

    private static DataManager sInstance;

    public static DataManager getInstance(Context ctx) {
        if (sInstance == null)
            sInstance = new DataManager(ctx);
        return sInstance;
    }

    private DataManager(Context ctx) {
        mContext = ctx;
    }

	public void startup() {
    	updateData();
        loadWidgetInfo();
//        generateHolidayInfo();
    }
    
	public void putHistoryData(String date, List<HistoryToday> list) {
		if (mHistoryData == null) {
			mHistoryData = new HashMap<String, List<HistoryToday>>();
		}
		mHistoryData.put(date, list);
	}
	
	public List<HistoryToday> getHistoryDataByDate(String date) {
		if (mHistoryData == null) {
			return null;
		}
		return mHistoryData.get(date);
	}
	
    private void updateData() {
    	String data = FileUtils.loadInternalFile(mContext, IContants.COMMOM_FILE_MATTER_NAME);

    	ArrayList<DayMatter> dayMatterList = null;
        if (data != null) {
            try {
                JSONArray dataArray = new JSONArray(data);
                dayMatterList = DayMatter.createFromJsonArray(dataArray);
            } catch (JSONException e) {
            }
        }
        
        if (dayMatterList != null && dayMatterList.size() > 0) {
        	DatabaseManager databaseMgr = DatabaseManager.getInstance(mContext);
        	for (DayMatter matter : dayMatterList) {
        		databaseMgr.add(matter);
        	}
        }
        
        FileUtils.saveInternalFile(mContext, "", IContants.COMMOM_FILE_MATTER_NAME);
	}

    public ArrayList<DayMatter> getHolidayList() {
    	loadHolidayInfo();
		return mHolidayList;
	}

	public void setHolidayList(ArrayList<DayMatter> holidayList) {
		this.mHolidayList = holidayList;
	}

	private void loadHolidayInfo() {
		String holidayFile = IContants.COMMOM_FILE_CN_HOLIDAY_NAME;
		
		Locale locale = UtilityHelper.getCurrentLocale(mContext);
		if (locale != null && locale.equals(Locale.TRADITIONAL_CHINESE)) {
			holidayFile = IContants.COMMOM_FILE_TW_HOLIDAY_NAME;
		} else {
			holidayFile = IContants.COMMOM_FILE_CN_HOLIDAY_NAME;
		}
		
		byte[] data = FileUtils.getAssertsFile(mContext, holidayFile);
		try {
			String strData = new String(data, "UTF-8");
			JSONArray dataArray = new JSONArray(strData);
			mHolidayList = DayMatter.createFromJsonArray(dataArray);
		} catch (Exception e) {
		}
	}
	
	public void generateHolidayInfo() {
		try {
			JSONArray holiday = new JSONArray();
			
			InputStream inputStream = null;
			AssetManager assetManager = mContext.getAssets();
			inputStream = assetManager.open("gregorian_tw.dat");
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (!TextUtils.isEmpty(line)) {
					String[] info = line.split(" ");
					SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy年MM月dd日");
					Date date = simpleDF.parse(info[0]);
					
					DayMatter matter = new DayMatter();
					matter.setDate(date.getTime());
					matter.setMatter(info[1]);
					matter.setRepeat(3);
					
					holiday.put(matter.toJsonObject());
				}
			}
			
			line = null;
			inputStream = assetManager.open("lunar_tw.dat");
			br = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = br.readLine()) != null) {
				if (!TextUtils.isEmpty(line)) {
					String[] info = line.split(" ");
					SimpleDateFormat simpleDF = new SimpleDateFormat("yyyy年MM月dd日");
					Date date = simpleDF.parse(info[0]);
					
					DayMatter matter = new DayMatter();
					matter.setDate(date.getTime());
					matter.setMatter(info[1]);
					matter.setRepeat(3);
					matter.setCalendar(IContants.CALENDAR_LUNAR);
					
					holiday.put(matter.toJsonObject());
				}
			}
			
			FileUtils.saveInternalFile(mContext, holiday.toString(), IContants.COMMOM_FILE_TW_HOLIDAY_NAME);
		} catch (Exception e) {
		}
	}
	
    private void loadWidgetInfo() {
        String widgetData = FileUtils.loadInternalFile(mContext, IContants.COMMOM_FILE_WIDGET_NAME);
        if (widgetData != null) {
            try {
                JSONArray dataArray = new JSONArray(widgetData);
                mWidgetInfoList = WidgetInfo.createFromJsonArray(dataArray);
            } catch (JSONException e) {
            }
        }
        
        if (mWidgetInfoList == null) {
            mWidgetInfoList = new ArrayList<WidgetInfo>();
        }
    }
    
    private void saveWidgetInfoData() {
        if (mWidgetInfoList != null) {
            JSONArray dataArray = new JSONArray();
            for (WidgetInfo info : mWidgetInfoList) {
                if (info != null) {
                    JSONObject data = info.toJsonObject();
                    dataArray.put(data);
                }
            }
            FileUtils.saveInternalFile(mContext, dataArray.toString(), IContants.COMMOM_FILE_WIDGET_NAME);
        }
    }
    
    public String exportDataCSV() {
    	String filePath = null;
    	
    	DatabaseManager databaseMgr = DatabaseManager.getInstance(mContext);
    	List<DayMatter>  matterList = databaseMgr.queryAll(0);
        if (matterList != null) {
        	String cacheDir = Environment.getExternalStorageDirectory().getPath();
        	
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String dataStr = dateFormat.format(new Date());
            String outputFile = "DateMatter_" + dataStr + ".csv";
            
            filePath = cacheDir + File.separator + outputFile;
            
            try {
				CsvWriter csvOutput = new CsvWriter(new FileWriter(filePath, true), ',');
				
				String[] tabColumn = {
						MatterTable.ID, MatterTable.UID, MatterTable.MATTER, MatterTable.DATE, 
					MatterTable.CALENDAR, MatterTable.CATEGORY, MatterTable.TOP,
					MatterTable.REPEAT, MatterTable.REMIND, MatterTable.STATUS,MatterTable.REMARK,MatterTable.NEXTREMINDTIME,
					MatterTable.HOUR,MatterTable.MIN
				};
				csvOutput.writeRecord(tabColumn);
				
	            for (DayMatter dayMatter : matterList) {
	                if (dayMatter != null) {
	                	csvOutput.writeRecord(dayMatter.toRecord());
	                }
	            }
	            
	            csvOutput.close();
			} catch (IOException e) {
				if (BuildConfig.DEBUG) e.printStackTrace();
			}
        }
        return filePath;
    }
    
    public boolean importDataCSV(InputStream input) {
    	boolean success = false;
    	CsvReader reader = null;
    	
    	try {
    		DatabaseManager databaseMgr = DatabaseManager.getInstance(mContext);
    		DayMatter matter = new DayMatter();
    		
			reader = new CsvReader(input, Charset.forName("UTF-8"));
			reader.readHeaders();
			while (reader.readRecord()) {
				success = true;
				extraMatterInfo(reader, matter);
				databaseMgr.add(matter);
			}
			
			UtilityHelper.showStartBarNotification(mContext);
		} catch (FileNotFoundException e) {
			if (BuildConfig.DEBUG) e.printStackTrace();
		} catch (IOException e) {
			if (BuildConfig.DEBUG) e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
        return success;
    }
    
    public boolean importDataCSV(String filePath) {
    	boolean success = false;
    	CsvReader reader = null;
    	
    	try {
    		DatabaseManager databaseMgr = DatabaseManager.getInstance(mContext);
    		DayMatter matter = new DayMatter();
    		
			reader = new CsvReader(new FileReader(filePath));
			reader.readHeaders();
			while (reader.readRecord()) {
				success = true;
				extraMatterInfo(reader, matter);
				databaseMgr.add(matter);
			}
			DateUtils.setDateChangedListener(mContext);//add by vk
			UtilityHelper.showStartBarNotification(mContext);
		} catch (FileNotFoundException e) {
			if (BuildConfig.DEBUG) e.printStackTrace();
		} catch (IOException e) {
			if (BuildConfig.DEBUG) e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
        return success;
    }

	private void extraMatterInfo(CsvReader reader, DayMatter matter) throws IOException {
//		int currentDataBaseVersion = UtilityHelper.getDatabaseVersion(mContext, IContants.KEY_DATABASE_VERSION);
//		LogUtil.Sysout("currentDataBaseVersion : "+currentDataBaseVersion);
//		if(currentDataBaseVersion)
		
		if(!extraMatterInfo139(reader, matter))
			if(!extraMatterInfo138(reader, matter))
				extraMatterInfo136(reader, matter);
		
	}
	
	private boolean extraMatterInfo139(CsvReader reader,DayMatter matter) throws IOException{
		
		//1.3.9
		boolean ok = true;
		try{
			matter.setUid(reader.get(MatterTable.UID));
			matter.setMatter(reader.get(MatterTable.MATTER));
			matter.setDate(Long.parseLong(reader.get(MatterTable.DATE)));
			matter.setCalendar(Integer.parseInt(reader.get(MatterTable.CALENDAR)));
			matter.setCategory(Integer.parseInt(reader.get(MatterTable.CATEGORY)));
			matter.setTop(Integer.parseInt(reader.get(MatterTable.TOP)));
			matter.setRepeat(Integer.parseInt(reader.get(MatterTable.REPEAT)));
			matter.setRemind(Integer.parseInt(reader.get(MatterTable.REMIND)));
			try{
			matter.setRemark(reader.get(MatterTable.REMARK));
			}catch(Exception e){
				e.printStackTrace();
			}
			try {
				matter.setNextRemindTime(Long.parseLong(reader.get(MatterTable.NEXTREMINDTIME)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			try{
				matter.setHour((Integer.parseInt(reader.get(MatterTable.HOUR))));
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				matter.setMin((Integer.parseInt(reader.get(MatterTable.MIN))));
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			ok = false;
		}
		
		return ok;
	}
	
	private boolean extraMatterInfo138(CsvReader reader,DayMatter matter) throws IOException{
		
		//1.3.8
		boolean ok = true;
		try{
			matter.setUid(reader.get(MatterTable.MATTER));
			matter.setMatter(reader.get(MatterTable.DATE));
			matter.setDate(Long.parseLong(reader.get(MatterTable.CALENDAR)));
			matter.setCalendar(Integer.parseInt(reader.get(MatterTable.REPEAT)));
			matter.setCategory(Integer.parseInt(reader.get(MatterTable.REMIND)));
			matter.setTop(Integer.parseInt(reader.get(MatterTable.REMARK)));
			matter.setRepeat(Integer.parseInt(reader.get(10)));
			matter.setRemind(Integer.parseInt(reader.get(11)));
			try{
			matter.setRemark(reader.get(12));
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				matter.setHour((Integer.parseInt(reader.get(MatterTable.CATEGORY))));
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				matter.setMin((Integer.parseInt(reader.get(MatterTable.TOP))));
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			ok = false;
		}
		
		return ok;
	}
	
	private boolean extraMatterInfo136(CsvReader reader,DayMatter matter) throws IOException{
		
		//1.3.6
		boolean ok = true;
		try{
			matter.setUid(reader.get(MatterTable.UID));
			matter.setMatter(reader.get(MatterTable.MATTER));
			matter.setDate(Long.parseLong(reader.get(MatterTable.DATE)));
			matter.setCalendar(Integer.parseInt(reader.get(MatterTable.CALENDAR)));
			matter.setCategory(Integer.parseInt(reader.get(MatterTable.CATEGORY)));
			matter.setTop(Integer.parseInt(reader.get(MatterTable.TOP)));
			matter.setRepeat(Integer.parseInt(reader.get(MatterTable.REPEAT)));
			matter.setRemind(Integer.parseInt(reader.get(MatterTable.REMIND)));
			try{
			matter.setRemark(reader.get(MatterTable.REMARK));
			}catch(Exception e){
				e.printStackTrace();
			}
			matter.setHour(8);
			matter.setMin(0);
		}catch(Exception e){
			ok = false;
		}
		
		return ok;
	}
    
    public boolean restoreData(String filePath) {
        boolean success = false;
        String data = FileUtils.loadExternalFile(mContext, filePath);

        ArrayList<DayMatter> matterList = null;
        if (data != null) {
            try {
                JSONArray dataArray = new JSONArray(data);
                matterList = DayMatter.createFromJsonArray(dataArray);
            } catch (JSONException e) {
            }
            
            if (matterList != null && matterList.size() > 0) {
            	success = true;
            	DatabaseManager databaseMgr = DatabaseManager.getInstance(mContext);
            	for (DayMatter matter : matterList) {
            		databaseMgr.add(matter);
            	}
            	
            	UtilityHelper.showStartBarNotification(mContext);
            }
        }
        return success;
    }

    public void addWidgetInfo(WidgetInfo info) {
        if (mWidgetInfoList == null) {
            loadWidgetInfo();
        }

        mWidgetInfoList.add(info);
        saveWidgetInfoData();
    }
    
    public WidgetInfo getWidgetInfoByWidgetId(int widgetId) {
        if (mWidgetInfoList == null) {
            loadWidgetInfo();
        }
        
        for (WidgetInfo info : mWidgetInfoList) {
            if (info.widgetId == widgetId)
                return info;
        }
        
        return null;
    }
    
    public WidgetInfo getWidgetInfoByMatterUid(String dayMatterUid) {
        if (mWidgetInfoList == null) {
            loadWidgetInfo();
        }
        
        if (dayMatterUid == null)
            throw new IllegalArgumentException("day matter id is null.");
        
        for (WidgetInfo info : mWidgetInfoList) {
            if (info.dayMatterId.equals(dayMatterUid))
                return info;
        }
        
        return null;
    }

    public void deleteWidgetInfoByWidgetId(int widgetId) {
        if (mWidgetInfoList == null) {
            loadWidgetInfo();
        }
        
        for (WidgetInfo info : mWidgetInfoList) {
            if (info.widgetId == widgetId) {
                mWidgetInfoList.remove(info);
                saveWidgetInfoData();
                return;
            }
        }
    }
    
    public void deleteWidgetInfoByMatterId(String matterId) {
        if (mWidgetInfoList == null) {
            loadWidgetInfo();
        }
        
        for (WidgetInfo info : mWidgetInfoList) {
            if (info.dayMatterId.equals(matterId)) {
                mWidgetInfoList.remove(info);
                saveWidgetInfoData();
                return;
            }
        }
    }
    
    public List<DayMatter> sortMatters(List<DayMatter> dayMatterList, int sortType) {
        if (sortType == IContants.SORT_BY_DEFAULT) {
            return dayMatterList;
        }
        
        ArrayList<DayMatter> matterPastList = new ArrayList<DayMatter>();
        ArrayList<DayMatter> matterFutureList = new ArrayList<DayMatter>();
        
        for (int index = 0; index < dayMatterList.size(); index++) {
            DayMatter matter = dayMatterList.get(index);
            if (matter == null) {
                continue;
            }

            int duration = DateUtils.getDaysBetween(matter).get("Days");
            
            if (duration >= 0) {
                addMatter2List(matter, matterFutureList, false);
            } else {
                addMatter2List(matter, matterPastList, true);
            }
        }
        
        dayMatterList.clear();
        switch (sortType) {
            case IContants.SORT_BY_DATE_AESC: {
                dayMatterList.addAll(matterFutureList);
                dayMatterList.addAll(matterPastList);
                break;
            }
            case IContants.SORT_BY_DATE_DESC: {
                sortByDateDesc(dayMatterList, matterPastList, matterFutureList);
                break;
            }
            default:
                break;
        }
        return dayMatterList;
    }
    
    public List<DayMatter> sortMatters(List<DayMatter> dayMatterList) {
    	int sortType = UtilityHelper.getSortTypeIndex(mContext);
    	return sortMatters(dayMatterList, sortType);
    }

    private void sortByDateDesc(List<DayMatter> dayMatterList, ArrayList<DayMatter> matterPastList, ArrayList<DayMatter> matterFutureList) {
        int i = 0, j = 0;
        while(i < matterFutureList.size() && j < matterPastList.size()) {
            int duration1 = Math.abs(DateUtils.getDaysBetween(matterFutureList.get(i)).get("Days"));
            int duration2 = Math.abs(DateUtils.getDaysBetween(matterPastList.get(j)).get("Days"));
            if (duration1 <= duration2) {
                dayMatterList.add(matterFutureList.get(i));
                i++;
            } else {
                dayMatterList.add(matterPastList.get(j));
                j++;
            }
        }
        
        for (; i < matterFutureList.size(); i++) {
            dayMatterList.add(matterFutureList.get(i));
        }

        for (; j < matterPastList.size(); j++) {
            dayMatterList.add(matterPastList.get(j));
        }
    }

    private void addMatter2List(DayMatter matter, ArrayList<DayMatter> matterList, boolean past) {
        int index = 0;
        for (index = 0; index < matterList.size(); index++) {
            DayMatter dayMatter = matterList.get(index);
            
            int duration1 = Math.abs(DateUtils.getDaysBetween(dayMatter).get("Days"));
            int duration2 = Math.abs(DateUtils.getDaysBetween(matter).get("Days"));

            if (duration1 >= duration2) {
                break;
            }
        }
        
        if (index < matterList.size()) {
            matterList.add(index, matter);
        } else {
            matterList.add(matter);
        }
    }
    
}
