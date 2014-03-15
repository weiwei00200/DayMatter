package com.pybeta.daymatter.threadcommon;

import java.io.Serializable;

 


/**
 *@Project Name:	DateMatter
 *@Copyright:		FanYue
 *@Version:			1.0.0.1
 *@File_Name:		IDataAction.java	
 *@AuthorName		Sammie.Zhang
 *@CreateDate:		2014-1-22
 *@ModifyHistory:
 */
public interface IDataAction extends Serializable{
	Object actionExecute(Object obj);
}
