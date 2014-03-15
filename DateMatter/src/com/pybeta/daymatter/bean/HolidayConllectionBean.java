package com.pybeta.daymatter.bean;

import java.util.List;

import android.os.Environment;

import com.pybeta.daymatter.tool.SerializeManager;


public class HolidayConllectionBean {
	public final static String Serialize_PATH = "/data/data/com.pybeta.daymatter/files/HolidayBean.ini";
	public static HolidayConllectionBean mHolidayConllectionBean = null;
	private HolidaySerializeBean mHolidaySerializeBean = null;
	
	public static HolidayConllectionBean getInstance(){
//		if(null == mHolidayConllectionBean){
//			mHolidayConllectionBean = new HolidayConllectionBean();
//		}
		mHolidayConllectionBean = new HolidayConllectionBean();
		return mHolidayConllectionBean;
	}
	public static HolidayConllectionBean getInstanceStatic(){
		if(null == mHolidayConllectionBean){
			mHolidayConllectionBean = new HolidayConllectionBean();
		}
		return mHolidayConllectionBean;
	}
	
	public HolidayConllectionBean(){
		Object obj = SerializeManager.loadFile(Serialize_PATH);
		mHolidaySerializeBean = null;
		if(obj == null){
			//在这里加入默认数据
			mHolidaySerializeBean = new HolidaySerializeBean();
			mHolidaySerializeBean.getMap().put("df26e994-3fb7-46da-bc71-9b6580f7d1bf",HardCodeHolidayDate.getYuanDanDefault());
			mHolidaySerializeBean.getMap().put("df26e994-3fb7-46da-bc71-9b6580f7d1b1", HardCodeHolidayDate.getChunJieDefault());
			mHolidaySerializeBean.getMap().put("df26e994-3fb7-46da-bc71-9b6580f7d1b2", HardCodeHolidayDate.getQingMingDefault());
			mHolidaySerializeBean.getMap().put("df26e994-3fb7-46da-bc71-9b6580f7d1b3", HardCodeHolidayDate.getLaoDongJieDefault());
			mHolidaySerializeBean.getMap().put("df26e994-3fb7-46da-bc71-9b6580f7d1b4", HardCodeHolidayDate.getDuanWuJieDefault());
			mHolidaySerializeBean.getMap().put("df26e994-3fb7-46da-bc71-9b6580f7d1b5", HardCodeHolidayDate.getZhongQiuJieDefault());
			mHolidaySerializeBean.getMap().put("df26e994-3fb7-46da-bc71-9b6580f7d1b6", HardCodeHolidayDate.getGuoQingJieDefault());
			mHolidaySerializeBean.getMap().put("1f26e994-3fb7-46da-bc71-9b6580f7d1b7", HardCodeHolidayDate.getLiChunDefault());
			mHolidaySerializeBean.getMap().put("2f26e994-3fb7-46da-bc71-9b6580f7d1b7", HardCodeHolidayDate.getYuShuiDefault());
			mHolidaySerializeBean.getMap().put("3f26e994-3fb7-46da-bc71-9b6580f7d1b7", HardCodeHolidayDate.getJingZheDefault());
			mHolidaySerializeBean.getMap().put("4f26e994-3fb7-46da-bc71-9b6580f7d1b7", HardCodeHolidayDate.getChunFenDefault());
			mHolidaySerializeBean.getMap().put("3f26e994-3fb7-46da-bc71-9b6580f7d1b7", HardCodeHolidayDate.getJingZheDefault());
			mHolidaySerializeBean.getMap().put("7f26e994-3fb7-46da-bc71-9b6580f7d1b7", HardCodeHolidayDate.getGuYuDefault());
			mHolidaySerializeBean.getMap().put("8f26e994-3fb7-46da-bc71-9b6580f7d1b7", HardCodeHolidayDate.getLiXiaDefault());
			mHolidaySerializeBean.getMap().put("9f26e994-3fb7-46da-bc71-9b6580f7d1b7", HardCodeHolidayDate.getXiaoManDefault());
			mHolidaySerializeBean.getMap().put("8f26e914-3fb7-46da-bc71-9b6580f7d1b7", HardCodeHolidayDate.getMangZhongDefault());
			mHolidaySerializeBean.getMap().put("8f26e914-31b7-46da-bc71-9b6580f7d1b7", HardCodeHolidayDate.getXiaZhiDefault());
			mHolidaySerializeBean.getMap().put("8f26e914-31b7-46da-1c71-9b6580f7d1b7", HardCodeHolidayDate.getXiaoShuDefault());
			mHolidaySerializeBean.getMap().put("8f16e914-31b7-46da-1c71-9b6580f7d1b7", HardCodeHolidayDate.getDaShuDefault());
			mHolidaySerializeBean.getMap().put("8f26e944-31b7-46da-1c71-9b6580f7d1b7", HardCodeHolidayDate.getLiQiuDefault());
			mHolidaySerializeBean.getMap().put("8f26e914-31b7-46da-1c71-9b6580f7d1b7", HardCodeHolidayDate.getChuShuDefault());
			mHolidaySerializeBean.getMap().put("8f26e914-31b7-12da-1c71-9b6580f7d1b7", HardCodeHolidayDate.getBaiLuDefault());
			mHolidaySerializeBean.getMap().put("8f26e914-31b7-122a-1c71-9b6580f7d1b7", HardCodeHolidayDate.getQiuFenDefault());
			mHolidaySerializeBean.getMap().put("8f111914-31b7-122a-1c71-9b6580f7d1b7", HardCodeHolidayDate.getHanLuDefault());
			mHolidaySerializeBean.getMap().put("8f111664-31b7-122a-1c71-9b6580f7d1b7", HardCodeHolidayDate.getShuangJiangDefault());
			mHolidaySerializeBean.getMap().put("8f111664-31b7-122a-ac7a-9b6580f7d1b7", HardCodeHolidayDate.getLiDongDefault());
			mHolidaySerializeBean.getMap().put("8f111664-31b7-122a-ac7a-9b6580f7d1b7", HardCodeHolidayDate.getXiaoXueDefault());
			mHolidaySerializeBean.getMap().put("8f111664-31b7-122a-ac7a-9b658011d1b7", HardCodeHolidayDate.getDaXueDefault());
			mHolidaySerializeBean.getMap().put("8f111664-31b7-122a-ac7a-9b658dd1d1b7", HardCodeHolidayDate.getDongZhiDefault());
			mHolidaySerializeBean.getMap().put("8f1ccc64-31b7-122a-ac7a-9b658dd1d1b7", HardCodeHolidayDate.getXiaoHanDefault());
			mHolidaySerializeBean.getMap().put("8f1cca64-31b7-122a-ac7a-9b658dd1d1b7", HardCodeHolidayDate.getDaHanDefault());
			mHolidaySerializeBean.getMap().put("8eecca64-31b7-122a-ac7a-9b658dd1d1b7", HardCodeHolidayDate.getQingRenJieDefault());
			mHolidaySerializeBean.getMap().put("8eecca64-a1b7-122a-ac7a-9b658dd1d1b7", HardCodeHolidayDate.getFuNvJieDefault());
			mHolidaySerializeBean.getMap().put("8eeqca64-a1b7-122a-ac7a-9b658dd1d1b7", HardCodeHolidayDate.getYuRenJieDefault());
			mHolidaySerializeBean.getMap().put("8eeq1a64-a1b7-122a-ac7a-9b658dd1d1b7", HardCodeHolidayDate.getQingNianJieDefault());
			mHolidaySerializeBean.getMap().put("8e1q1a24-a1b7-122a-ac7a-9b658dd1d1b7", HardCodeHolidayDate.getWuYanDefault());
			mHolidaySerializeBean.getMap().put("1e1q1a24-a1b7-122a-ac7a-9b658dd1d1b7", HardCodeHolidayDate.getErTongJieDefault());
			mHolidaySerializeBean.getMap().put("1e1q1a44-a1b7-122a-ac7a-9b658dd1d1b7", HardCodeHolidayDate.getJianJunJieDefault());
			mHolidaySerializeBean.getMap().put("1e1q1a44-a1b7-1q2a-ac7a-9b657dd1d1b7", HardCodeHolidayDate.getJianDangJieDefault());
			mHolidaySerializeBean.getMap().put("1e1q1a44-a1ba-1q2a-ac7a-9b657dd1d1b7", HardCodeHolidayDate.getJiaoShiJieDefault());
			mHolidaySerializeBean.getMap().put("1e1q1a44-a1ba-1q2a-ac7a-9b65aad1d1b7", HardCodeHolidayDate.getShengDanJieDefault());
			mHolidaySerializeBean.getMap().put("1e1era44-a1ba-1q2a-ac7a-9b65aad1d1b7", HardCodeHolidayDate.getYuanXiaoJieDefault());
			mHolidaySerializeBean.getMap().put("aa1era44-a1ba-1q2a-ac7a-9b65aad1d1b7", HardCodeHolidayDate.getLaBaJieDefault());
			mHolidaySerializeBean.getMap().put("cccera44-a1ba-1q2a-ac7a-9b65aad1d1b7", HardCodeHolidayDate.getXiaoFeiZheQuanYiDefault());
			mHolidaySerializeBean.getMap().put("cccera14-a1ba-1q2a-ac7a-9b65aad1d1b7", HardCodeHolidayDate.getFuHuoJieDefault());
			SerializeManager.saveFile(mHolidaySerializeBean, Serialize_PATH);
		}else{
			mHolidaySerializeBean = (HolidaySerializeBean)obj;
		}
	}
	
	//获取指定id的HolidayItemBean对象
	public HolidayItemBean getHolidayById(String id){
		return mHolidaySerializeBean.getMap().get(id);
	}
	//根据Id修改HolidayItemBean对象再保存
	public void modifyHolidayById(String id, HolidayItemBean itemBean){
		mHolidaySerializeBean.getMap().remove(id);
		mHolidaySerializeBean.getMap().put(id, itemBean);
		SerializeManager.saveFile(mHolidaySerializeBean, Serialize_PATH);
	}
	
	
}
