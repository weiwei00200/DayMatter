package com.pybeta.daymatter.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.pybeta.daymatter.ApkInfo;


public class JsonUtil {

	// 错误代码解析
	public static Long errorPojo(String jsonData) {
		LogUtil.d("jsonData = " + jsonData);
		JSONObject personalObject = null;
		long result = 0;
		if (TextUtils.isEmpty(jsonData)) {
			return result = -1000;
		} else {
			try {
				personalObject = new JSONObject(jsonData);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				LogUtil.Sysout("解析出错了");
			}
			if (personalObject != null) {
				try {
					result = personalObject.getLong("errorCode");
					LogUtil.Sysout("errorCode:" + result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					LogUtil.Sysout("获取不到错误的信息");
					result = -1000;
					e.printStackTrace();
				}
			} else {
				result = -1000;
			}
		}

		return result;
	}

	public static String errorPojo2(String jsonData, Context context) {
		LogUtil.d("jsonData = " + jsonData);
		JSONObject personalObject = null;
		String result = "";
		if (TextUtils.isEmpty(jsonData)) {
			return null;
		} else {
			try {
				personalObject = new JSONObject(jsonData);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (personalObject != null) {
				try {
					result = personalObject.getString("errorType");
					LogUtil.Sysout("errorCode:" + result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					LogUtil.Sysout("获取不到错误的信息");
					result = "";
					e.printStackTrace();
				}
			} else {
				result = "";
			}
		}

		return result;
	}

	// 通用结果解析
	public static Long getResultPojo(String jsonData) {

		LogUtil.d("jsonData = " + jsonData);

		JSONObject personalObject = null;
		long result = -1000l;

		if (TextUtils.isEmpty(jsonData) || jsonData == null) {
			result = -1000l;
		} else {
			try {
				personalObject = new JSONObject(jsonData);
				if (personalObject != null) {
					String ss = personalObject.getString("success");
					LogUtil.Sysout("succeed:" + ss);
					if (ss.equals("0")) {
						result = 0;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = -1000l;
			}
		}

		return result;
	}

	private static final String HTTPPIC = "http://client.icodestar.com/upload/";
	public static ArrayList<ApkInfo> getApkInfoPojo(String jsonData){
		ArrayList<ApkInfo> mApkInfos = null;
		
		LogUtil.d("jsonData = " + jsonData);
		JSONArray jsonArray = null;
		if (TextUtils.isEmpty(jsonData) || jsonData == null || jsonData.equals("[]")) {
			mApkInfos = null;
		} else {
			try {
				jsonArray = new JSONObject(jsonData).getJSONArray("data");
				if (jsonArray != null) {
					mApkInfos = new ArrayList<ApkInfo>();
					int len = jsonArray.length();
					ApkInfo apkInfo  = null;
					JSONObject mJsonObject = null;
					for(int i = 0 ; i< len;i++){
						apkInfo = new ApkInfo();
						mJsonObject = jsonArray.getJSONObject(i);
						apkInfo.setDesc(mJsonObject.getString("description"));
						apkInfo.setKeyWord(mJsonObject.getString("general"));
						apkInfo.setName(mJsonObject.getString("name"));
						apkInfo.setPhoto(HTTPPIC+mJsonObject.getString("photo"));
						apkInfo.setSize(mJsonObject.getString("android_size"));
						apkInfo.setUrl(mJsonObject.getString("android_url"));
						apkInfo.setWorksId(mJsonObject.getInt("worksid"));
						apkInfo.setWorksType(mJsonObject.getInt("works_type"));
						apkInfo.setPackageName(mJsonObject.getString("android_packagename"));
						LogUtil.Sysout("apttt: "+apkInfo.getKeyWord());
						mApkInfos.add(apkInfo);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mApkInfos = null;
			}
		}
		return mApkInfos;
		
		
	}
	
}
