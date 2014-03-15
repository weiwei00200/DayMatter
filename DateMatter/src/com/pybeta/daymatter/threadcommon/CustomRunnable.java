package com.pybeta.daymatter.threadcommon;

import android.os.Bundle;
import android.os.Message;


/**
 *@Project Name:	DateMatter
 *@Copyright:		FanYue
 *@Version:			1.0.0.1
 *@File_Name:		CustomerRunnable.java	
 *@AuthorName		Sammie.Zhang
 *@CreateDate:		2014-1-22
 *@ModifyHistory:
 */
public class CustomRunnable implements Runnable {
	public final static String RESULT_KEY="result";
	/**
	 * 有执行的Action和执行完之后要调用的Action
	 */
	IDataAction action;
	IDataAction mCompleteAction;
	CustomHandler handler;
	int handlerCode = -1;
	Object mReturnObj;//用于两个Action的数据传递
	
	Thread mThread;
	/**
	 * 
	 * @param runAction
	 * @param completeAction 这个会用UI线程来执行
	 */
	public CustomRunnable(IDataAction runAction,IDataAction completeAction){
		action = runAction;
		mCompleteAction = completeAction;
	}
	 
	@Override
	public void run() {
		android.os.Message msg = new android.os.Message();
		msg.what = handlerCode;
		Bundle data = new Bundle();
		try{
			mReturnObj = action.actionExecute(null);
			msg.setData(data);
			sendMessage(msg);
		}catch(Exception ex){
			ex.printStackTrace();
			sendMessage(msg);			
		}
	}
	
	void sendMessage(Message msg){
		msg.what=0;
		myHandler.sendMessage(msg);
	}
	
	
	
	CustomHandler myHandler = new CustomHandler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case 0:
				mCompleteAction.actionExecute(mReturnObj);
				break;
			}
		}
	};
	
	/**
	 * 执行
	 */
	public void startAction(){
		mThread = new Thread(this);
		mThread.start();
	}

}
