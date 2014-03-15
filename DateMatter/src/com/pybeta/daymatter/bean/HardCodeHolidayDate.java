package com.pybeta.daymatter.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HardCodeHolidayDate {
	private static HolidayItemBean mItemBean = null;
	
	/**
	 * 元旦
	 * @return
	 */
	public static HolidayItemBean getYuanDanDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		onHolidayList.add("2014-01-01");
		mItemBean = new HolidayItemBean();
		mItemBean.setItemDate("2014-01-01");
		mItemBean.setId("df26e994-3fb7-46da-bc71-9b6580f7d1bf");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	
	/**
	 * 春节
	 * @return
	 */
	public static HolidayItemBean getChunJieDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		onWorkList.add("2014-01-26");
		onWorkList.add("2014-01-27");
		onWorkList.add("2014-01-28");
		onWorkList.add("2014-01-29");
		onWorkList.add("2014-01-30");
		onWorkList.add("2014-02-07");
		onWorkList.add("2014-02-08");
		onHolidayList.add("2014-01-31");
		onHolidayList.add("2014-02-01");
		onHolidayList.add("2014-02-02");
		onHolidayList.add("2014-02-03");
		onHolidayList.add("2014-02-04");
		onHolidayList.add("2014-02-05");
		onHolidayList.add("2014-02-06");
		mItemBean = new HolidayItemBean();
		mItemBean.setId("df26e994-3fb7-46da-bc71-9b6580f7d1b1");
		mItemBean.setItemDate("2014-02-01");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 清明节
	 * @return
	 */
	public static HolidayItemBean getQingMingDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		onHolidayList.add("2014-04-05");
		onHolidayList.add("2014-04-06");
		onHolidayList.add("2014-04-07");
		mItemBean = new HolidayItemBean();
		mItemBean.setId("df26e994-3fb7-46da-bc71-9b6580f7d1b2");
		mItemBean.setItemDate("2014-04-05");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 劳动节
	 * @return
	 */
	public static HolidayItemBean getLaoDongJieDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		onWorkList.add("2014-05-04");
		onWorkList.add("2014-05-05");
		onWorkList.add("2014-05-06");
		onWorkList.add("2014-05-07");
		onWorkList.add("2014-05-08");
		onWorkList.add("2014-05-09");
		onHolidayList.add("2014-05-01");
		onHolidayList.add("2014-05-02");
		onHolidayList.add("2014-05-03");
		mItemBean = new HolidayItemBean();
		mItemBean.setId("df26e994-3fb7-46da-bc71-9b6580f7d1b3");
		mItemBean.setItemDate("2014-05-01");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 端午节
	 * @return
	 */
	public static HolidayItemBean getDuanWuJieDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		onHolidayList.add("2014-05-31");
		onHolidayList.add("2014-06-01");
		onHolidayList.add("2014-06-02");
		mItemBean = new HolidayItemBean();
		mItemBean.setId("df26e994-3fb7-46da-bc71-9b6580f7d1b4");
		mItemBean.setItemDate("2014-06-01");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 中秋节
	 * @return
	 */
	public static HolidayItemBean getZhongQiuJieDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		onHolidayList.add("2014-09-06");
		onHolidayList.add("2014-09-07");
		onHolidayList.add("2014-09-08");
		mItemBean = new HolidayItemBean();
		mItemBean.setId("df26e994-3fb7-46da-bc71-9b6580f7d1b5");
		mItemBean.setItemDate("2014-09-06");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 国庆节
	 * @return
	 */
	public static HolidayItemBean getGuoQingJieDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		onWorkList.add("2014-09-28");
		onWorkList.add("2014-09-29");
		onWorkList.add("2014-09-30");
		onWorkList.add("2014-10-08");
		onWorkList.add("2014-10-09");
		onWorkList.add("2014-10-10");
		onWorkList.add("2014-10-11");
		onHolidayList.add("2014-10-01");
		onHolidayList.add("2014-10-02");
		onHolidayList.add("2014-10-03");
		onHolidayList.add("2014-10-04");
		onHolidayList.add("2014-10-05");
		onHolidayList.add("2014-10-06");
		onHolidayList.add("2014-10-07");
		mItemBean = new HolidayItemBean();
		mItemBean.setId("df26e994-3fb7-46da-bc71-9b6580f7d1b6");
		mItemBean.setItemDate("2014-10-01");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 立春
	 * @return
	 */
	public static HolidayItemBean getLiChunDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("1f26e994-3fb7-46da-bc71-9b6580f7d1b7");
		mItemBean.setItemDate("2014-02-04");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	
	/**
	 * 雨水
	 * @return
	 */
	public static HolidayItemBean getYuShuiDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("2f26e994-3fb7-46da-bc71-9b6580f7d1b7");
		mItemBean.setItemDate("2014-02-18");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 惊蛰
	 * @return
	 */
	public static HolidayItemBean getJingZheDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("3f26e994-3fb7-46da-bc71-9b6580f7d1b7");
		mItemBean.setItemDate("2014-03-05");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 春分
	 * @return
	 */
	public static HolidayItemBean getChunFenDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("4f26e994-3fb7-46da-bc71-9b6580f7d1b7");
		mItemBean.setItemDate("2014-03-20");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 谷雨
	 * @return
	 */
	public static HolidayItemBean getGuYuDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("7f26e994-3fb7-46da-bc71-9b6580f7d1b7");
		mItemBean.setItemDate("2014-04-20");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 立夏
	 * @return
	 */
	public static HolidayItemBean getLiXiaDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8f26e994-3fb7-46da-bc71-9b6580f7d1b7");
		mItemBean.setItemDate("2014-05-05");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 小满
	 * @return
	 */
	public static HolidayItemBean getXiaoManDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("9f26e994-3fb7-46da-bc71-9b6580f7d1b7");
		mItemBean.setItemDate("2014-05-21");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 芒种
	 * @return
	 */
	public static HolidayItemBean getMangZhongDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8f26e914-3fb7-46da-bc71-9b6580f7d1b7");
		mItemBean.setItemDate("2014-06-05");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 夏至
	 * @return
	 */
	public static HolidayItemBean getXiaZhiDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8f26e914-31b7-46da-bc71-9b6580f7d1b7");
		mItemBean.setItemDate("2014-06-21");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 小暑
	 * @return
	 */
	public static HolidayItemBean getXiaoShuDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8f26e914-31b7-46da-1c71-9b6580f7d1b7");
		mItemBean.setItemDate("2014-07-07");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 大暑
	 * @return
	 */
	public static HolidayItemBean getDaShuDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8f16e914-31b7-46da-1c71-9b6580f7d1b7");
		mItemBean.setItemDate("2014-07-22");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 立秋
	 * @return
	 */
	public static HolidayItemBean getLiQiuDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8f26e944-31b7-46da-1c71-9b6580f7d1b7");
		mItemBean.setItemDate("2014-08-07");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 处暑
	 * @return
	 */
	public static HolidayItemBean getChuShuDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8f26e914-31b7-46da-1c71-9b6580f7d1b7");
		mItemBean.setItemDate("2014-08-23");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 白露
	 * @return
	 */
	public static HolidayItemBean getBaiLuDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8f26e914-31b7-12da-1c71-9b6580f7d1b7");
		mItemBean.setItemDate("2014-09-07");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 秋分
	 * @return
	 */
	public static HolidayItemBean getQiuFenDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8f26e914-31b7-122a-1c71-9b6580f7d1b7");
		mItemBean.setItemDate("2014-09-23");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 寒露
	 * @return
	 */
	public static HolidayItemBean getHanLuDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8f111914-31b7-122a-1c71-9b6580f7d1b7");
		mItemBean.setItemDate("2014-10-08");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 霜降
	 * @return
	 */
	public static HolidayItemBean getShuangJiangDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8f111664-31b7-122a-1c71-9b6580f7d1b7");
		mItemBean.setItemDate("2014-10-23");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 立冬
	 * @return
	 */
	public static HolidayItemBean getLiDongDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8f111664-31b7-122a-ac7a-9b6580f7d1b7");
		mItemBean.setItemDate("2014-11-07");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 小雪
	 * @return
	 */
	public static HolidayItemBean getXiaoXueDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8f111664-31b7-122a-ac7a-9b6580f7d1b7");
		mItemBean.setItemDate("2014-11-22");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 大雪
	 * @return
	 */
	public static HolidayItemBean getDaXueDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8f111664-31b7-122a-ac7a-9b658011d1b7");
		mItemBean.setItemDate("2014-12-07");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 冬至
	 * @return
	 */
	public static HolidayItemBean getDongZhiDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8f111664-31b7-122a-ac7a-9b658dd1d1b7");
		mItemBean.setItemDate("2014-12-22");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 小寒
	 * @return
	 */
	public static HolidayItemBean getXiaoHanDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8f1ccc64-31b7-122a-ac7a-9b658dd1d1b7");
		mItemBean.setItemDate("2015-01-05");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 大寒
	 * @return
	 */
	public static HolidayItemBean getDaHanDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8f1cca64-31b7-122a-ac7a-9b658dd1d1b7");
		mItemBean.setItemDate("2015-01-20");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 情人节
	 * @return
	 */
	public static HolidayItemBean getQingRenJieDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8eecca64-31b7-122a-ac7a-9b658dd1d1b7");
		mItemBean.setItemDate("2014-02-14");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 妇女节
	 * @return
	 */
	public static HolidayItemBean getFuNvJieDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8eecca64-a1b7-122a-ac7a-9b658dd1d1b7");
		mItemBean.setItemDate("2014-03-08");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 愚人节
	 * @return
	 */
	public static HolidayItemBean getYuRenJieDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8eeqca64-a1b7-122a-ac7a-9b658dd1d1b7");
		mItemBean.setItemDate("2014-04-01");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 青年节
	 * @return
	 */
	public static HolidayItemBean getQingNianJieDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8eeq1a64-a1b7-122a-ac7a-9b658dd1d1b7");
		mItemBean.setItemDate("2014-05-04");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 世界无烟日
	 * @return
	 */
	public static HolidayItemBean getWuYanDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("8e1q1a24-a1b7-122a-ac7a-9b658dd1d1b7");
		mItemBean.setItemDate("2014-05-31");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 儿童节
	 * @return
	 */
	public static HolidayItemBean getErTongJieDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("1e1q1a24-a1b7-122a-ac7a-9b658dd1d1b7");
		mItemBean.setItemDate("2014-06-01");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 建军节
	 * @return
	 */
	public static HolidayItemBean getJianJunJieDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("1e1q1a44-a1b7-122a-ac7a-9b658dd1d1b7");
		mItemBean.setItemDate("2014-08-01");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 建党节
	 * @return
	 */
	public static HolidayItemBean getJianDangJieDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("1e1q1a44-a1b7-1q2a-ac7a-9b657dd1d1b7");
		mItemBean.setItemDate("2014-07-01");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 教师节
	 * @return
	 */
	public static HolidayItemBean getJiaoShiJieDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("1e1q1a44-a1ba-1q2a-ac7a-9b657dd1d1b7");
		mItemBean.setItemDate("2014-09-10");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 圣诞节
	 * @return
	 */
	public static HolidayItemBean getShengDanJieDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("1e1q1a44-a1ba-1q2a-ac7a-9b65aad1d1b7");
		mItemBean.setItemDate("2014-12-25");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 元宵节
	 * @return
	 */
	public static HolidayItemBean getYuanXiaoJieDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("1e1era44-a1ba-1q2a-ac7a-9b65aad1d1b7");
		mItemBean.setItemDate("2014-02-14");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 腊八节
	 * @return
	 */
	public static HolidayItemBean getLaBaJieDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("aa1era44-a1ba-1q2a-ac7a-9b65aad1d1b7");
		mItemBean.setItemDate("2014-01-08");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 消费者权益日
	 * @return
	 */
	public static HolidayItemBean getXiaoFeiZheQuanYiDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("cccera44-a1ba-1q2a-ac7a-9b65aad1d1b7");
		mItemBean.setItemDate("2014-03-15");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
	/**
	 * 复活节
	 * @return
	 */
	public static HolidayItemBean getFuHuoJieDefault(){
		List<String> onWorkList = new ArrayList<String>();
		List<String> onHolidayList = new ArrayList<String>();
		mItemBean = new HolidayItemBean();
		mItemBean.setId("cccera14-a1ba-1q2a-ac7a-9b65aad1d1b7");
		mItemBean.setItemDate("2014-04-20");
		mItemBean.setOnHolidayList(onHolidayList);
		mItemBean.setOnWorkList(onWorkList);
		return mItemBean;
	}
}
