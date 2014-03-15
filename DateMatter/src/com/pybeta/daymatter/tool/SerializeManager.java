package com.pybeta.daymatter.tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/***
 * 序列化
 *@Project Name:	com.or.commond
 *@Copyright:		Copyright (c) 2010-2012 By POSBIT Technology Co.,Ltd
 *@Version:			1.0.0.1
 *@File_Name:		SerializeManager.java	
 *@AuthorName		Vincent.Wen
 *@CreateDate:		2012-5-11
 *@ModifyHistory:	2012-5-31  在SaveFile方法里加入一个新建目录方法
 *					2012-8-26 createFloer小修改
 */
public class SerializeManager {

	
	/**
	 * 序列化文件
	 * @param obj
	 * @param path
	 */
	public static void saveFile(Object obj,String path)  {  
		FileOutputStream fos = null;
		 try{			  
		   createFloer(path);
		   fos = new FileOutputStream(path);  	 
	       ObjectOutputStream oos = new ObjectOutputStream(fos);   
	       oos.writeObject(obj);  	 
	       oos.flush(); 
		 }catch(Exception ex){		
			 ex.printStackTrace();
		 }finally{
			 if(null != fos){
				 try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		 }
	}
	
	
	/**
	 * 反序列化文件
	 * @param filepath
	 * @return
	 */
	public static Object loadFile(String filepath) {  
		File f = null;
		FileInputStream fis = null;
		ObjectInputStream oin = null;
		try{
			f = new File(filepath);
			if(f.exists()){
			  fis = new FileInputStream(filepath); 
              oin = new ObjectInputStream(fis); 
              Object obj = oin.readObject(); 
              return obj;
			} 
		}catch(Exception ex){	
			ex.printStackTrace();
		}finally{
			try {
				if(null != oin){
					oin.close();
				}
				if(null != fis){
					fis.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}


	static void createFloer(String fileName){
		File f = new File(fileName);
		File parent = f.getParentFile();
		if(!parent.exists()){
			parent.mkdirs();
		}
	}
	
	
	public static Object clone(Object obj){
		try{
			byte [] bs = serialize(obj);			
			return deserialize(bs);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}		
	}
	
	public static byte[] serialize(Object obj) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(obj);
	    os.close();
	    out.close();
	    return out.toByteArray();
	}
	
	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
	    ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    is.close();
	    return is.readObject();
	}
}
