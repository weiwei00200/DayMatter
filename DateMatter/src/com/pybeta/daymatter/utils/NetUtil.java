package com.pybeta.daymatter.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;



public class NetUtil {
	private static final int TIME_OUT = 30000; 
	public static boolean checkNet(Context context) {// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 
     * 
     * 判断是否使用移动电话网络
 
     * 
     * @return
 
     */
 
    public static boolean isMobileActive(Context ctx) {
 
        ConnectivityManager cm = 
            (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
 
        NetworkInfo mobileNet = cm.getActiveNetworkInfo();
 
        if (mobileNet != null
 
                && mobileNet.getType() == ConnectivityManager.TYPE_MOBILE) {
 
            return true;
 
        } else {
 
            return false;
 
        }
 
    }
	
	// 从一个URL取得一个图片
	public static BitmapDrawable getImageFromUrl(URL url) {
		BitmapDrawable icon = null;
		try {
			HttpURLConnection hc = (HttpURLConnection) url.openConnection();
			icon = new BitmapDrawable(hc.getInputStream());
			hc.disconnect();
		} catch (Exception e) {
		}
		return icon;
	}
	
	
	/*
	 *post请求 
	 */
/*	public static String postConnect(String url,MultipartEntity multipart){
		try {
			HttpPost post = new HttpPost(url);
			HttpClient client = new DefaultHttpClient();
			post.setEntity(multipart);
			HttpResponse response = client.execute(post);
			return EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	/*
	 * get请求
	 */
	public static String getConnect(String url) {
		if (url == null)
			return null;
		StringBuilder sb = new StringBuilder();
		try {
			HttpClient client = new DefaultHttpClient();
			HttpParams httpParams = client.getParams();
			// 设置网络超时参数
			HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
			HttpConnectionParams.setSoTimeout(httpParams, 10000);
			HttpResponse response = client.execute(new HttpGet(url
					.toString().trim()));

			if ((response.getStatusLine().getStatusCode() >= 200)
					&& (response.getStatusLine().getStatusCode() <= 300)) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String charset = EntityUtils.getContentCharSet(entity);
					if (charset == null) {
						charset = "UTF-8";
					}
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(entity.getContent(),
									charset), 8192);
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					reader.close();
				}
			}
		} catch (Exception e) {
		}
		return sb.toString();
	}
	
	// 工具查询相关使用的读流方法 //支持1.6以上
	public static String readStream(String url, String enc) throws Exception {
		URL myURL = new URL(url);
		URLConnection ucon = myURL.openConnection();
		// 使用InputStream，从URLConnection读取数据
		InputStream is = ucon.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(is);
		// 用ByteArrayBuffer缓存
		ByteArrayBuffer baf = new ByteArrayBuffer(500);
		int current = 0;
		while ((current = bis.read()) != -1) {
			baf.append((byte) current);
		}
		// 将缓存的内容转化为String,用UTF-8编码
		String myString = null;
		if (enc == null) {
			myString = EncodingUtils.getString(baf.toByteArray(), "UTF-8");
		} else {
			myString = EncodingUtils.getString(baf.toByteArray(), enc);
		}
		return myString;
	}

	public static String read(InputStream in, String enc) throws IOException {
		StringBuilder sb = new StringBuilder();
		InputStreamReader isr = null;
		BufferedReader r = null;
		if (enc != null) {
			// 按指定的编码读入流
			r = new BufferedReader(new InputStreamReader(in, enc), 1000);
		} else {
			// 按默认的编码读入
			r = new BufferedReader(new InputStreamReader(in), 1000);
		}

		for (String line = r.readLine(); line != null; line = r.readLine()) {
			sb.append(line);
		}
		in.close();
		return sb.toString();
	}
	
	/**
	 * 把输入流转换成字符数组
	 * 
	 * @param inputStream
	 *            输入流
	 * @return 字符数组
	 * @throws Exception
	 */

	public static byte[] readStream(InputStream inputStream) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
		}
		baos.close();
		inputStream.close();
		return baos.toByteArray();
	}
	
	
	/**
	 * 地图模块专用
	 * @param path
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ProtocolException
	 * @throws Exception
	 */
	public static InputStream getInStreamForMap(String path)
			throws UnsupportedEncodingException, MalformedURLException,
			IOException, ProtocolException, Exception {
		LogUtil.Sysout("完整的请求路径 ："  + "http://59.37.173.45/map_api.php?url=" + path);
		path = URLEncoder.encode(path,"GBK");
		//建立一个URL对象   59.37.173.45
		URL url = new URL("http://59.37.173.45/map_api.php?url=" + path);  
		LogUtil.Sysout("完整的请求路径 BY GBK：" + "map_api.php?url=" + path);
		//得到打开的链接对象   
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
		//设置请求超时与请求方式   
		conn.setReadTimeout(5*1000);  
		conn.setRequestMethod("GET");  
		//从链接中获取一个输入流对象   
		InputStream inStream = conn.getInputStream();  
//		ArrayList<PoiPoJo> pois = XmlUtil.ReadXmlByPull(inStream);
		return inStream;
	}
	public static InputStream getInStreamByPath(String path)
			throws UnsupportedEncodingException, MalformedURLException,
			IOException, ProtocolException, Exception {
		
		//建立一个URL对象   
		URL url = new URL(path);  
		//得到打开的链接对象   
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
		//设置请求超时与请求方式   
		conn.setReadTimeout(5*1000);  
		conn.setRequestMethod("GET");  
		//从链接中获取一个输入流对象   
		InputStream inStream = conn.getInputStream();  
//		ArrayList<PoiPoJo> pois = XmlUtil.ReadXmlByPull(inStream);
		return inStream;
	}
	
//	public static void AlertNetError(final Context con) {
//		AlertDialog.Builder ab = new AlertDialog.Builder(con);
//		ab.setTitle(R.string.NoRouteToHostException);
//		ab.setMessage(R.string.NoSignalException);
//		ab.setNegativeButton(R.string.apn_is_wrong_exit,
//				new OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
////						dialog.cancel();
//////						exitApp(con);
////						System.exit(0);
//						Intent intent=new Intent(Intent.ACTION_MAIN);
//
//						intent.addCategory(Intent.CATEGORY_HOME);
//
//						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//						con.startActivity(intent);
//
//						System.exit(0);
//
//
//					}
//
//				});
//		ab.setPositiveButton(R.string.apn_is_wrong_setnet,
//				new OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						dialog.dismiss();
//						con.startActivity(new Intent(
//								android.provider.Settings.ACTION_WIRELESS_SETTINGS));
//					}
//				});
//			ab.create().show();
//	}
//	
	public static String dopost(Context ctx, String uri,ArrayList<String>param ,ArrayList<String>values){
		
		LogUtil.d("dopost uri = " + uri);
		
		 //URLַ
//	     String uriAPI = "http://www.dubblogs.cc:8751/Android/Test/API/Post/index.php";
	 //   建立HTTP Post连线
	    HttpPost httpRequest =new HttpPost(uri);
	    //Post运作传送变数必须用NameValuePair[]阵列储存
	    //传参数 服务端获取的方法为request.getParameter("name")
//	    param.add("mark");
//	    values.add(App.MDEVICEIDSTR);
//	    if(App.user!=null){
//	    	param.add("markid");
//	    	values.add(App.user.getUserid());
//	    }
	    List <NameValuePair> params=new ArrayList<NameValuePair>();
//	    params.add(new BasicNameValuePair("name","this is post"));
	    for(int i = 0 ; i < param.size() ; i ++){
	    	params.add(new BasicNameValuePair(param.get(i),values.get(i)));
	    	LogUtil.d("dopost param.get(i) = " + param.get(i) + "&values.get(i) = "+ values.get(i));
	    }
	    try{
	     
	     
	     //发出HTTP request
	     httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
	     //取得HTTP response
	     
	     DefaultHttpClient client = new DefaultHttpClient();
	     if(isMobileActive(ctx)){
			HttpHost proxy = new HttpHost("10.0.0.172", 80);
			client.getParams().setIntParameter(proxy.getHostName(), proxy.getPort());
		}else{
			client.getParams().setIntParameter("http.socket.timeout", TIME_OUT);
		}
	     HttpResponse httpResponse=client.execute(httpRequest);
	     //若状态码为200 ok 
	     if(httpResponse.getStatusLine().getStatusCode()==200){
	      //取出回应字串
			     String strResult = getJsonStringFromGZIP(httpResponse);
  			      LogUtil.d("登陆"+strResult);
  			      return strResult;
//	      return getJsonStringFromGZIP(httpResponse);
//	      textView1.setText(strResult);
	     }else{
//	      textView1.setText("Error Response"+httpResponse.getStatusLine().toString());
	     }
	    }catch(ClientProtocolException e){
//	     textView1.setText(e.getMessage().toString());
	     e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
//	     textView1.setText(e.getMessage().toString());
	     e.printStackTrace();
	    } catch (IOException e) {
//	     textView1.setText(e.getMessage().toString());
	     e.printStackTrace();
	    }
		return null;
		
	}
	
public static String dopost2(Context ctx,DefaultHttpClient client, String uri,ArrayList<String>param ,ArrayList<String>values){
		
		LogUtil.d("dopost uri = " + uri);
		
		 //URLַ
//	     String uriAPI = "http://www.dubblogs.cc:8751/Android/Test/API/Post/index.php";
	 //   建立HTTP Post连线
	    HttpPost httpRequest =new HttpPost(uri);
	    //Post运作传送变数必须用NameValuePair[]阵列储存
	    //传参数 服务端获取的方法为request.getParameter("name")
	    param.add("mark");
//	    values.add(App.MDEVICEIDSTR);
//	    if(App.user!=null){
//	    	param.add("markid");
//	    	values.add(App.user.getUserid());
//	    }
	    List <NameValuePair> params=new ArrayList<NameValuePair>();
//	    params.add(new BasicNameValuePair("name","this is post"));
	    for(int i = 0 ; i < param.size() ; i ++){
	    	params.add(new BasicNameValuePair(param.get(i),values.get(i)));
	    	LogUtil.d("dopost param.get(i) = " + param.get(i) + "&values.get(i) = "+ values.get(i));
	    }
	    try{
	     
	     
	     //发出HTTP request
	     httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
	     //取得HTTP response
	     
//	     DefaultHttpClient client = new DefaultHttpClient();
	     if(isMobileActive(ctx)){
			HttpHost proxy = new HttpHost("10.0.0.172", 80);
			client.getParams().setIntParameter(proxy.getHostName(), proxy.getPort());
		}else{
			client.getParams().setIntParameter("http.socket.timeout", TIME_OUT);
		}
	     HttpResponse httpResponse=client.execute(httpRequest);
	     //若状态码为200 ok 
	     if(httpResponse.getStatusLine().getStatusCode()==200){
	      //取出回应字串
			     String strResult = getJsonStringFromGZIP(httpResponse);
  			      LogUtil.d("登陆"+strResult);
  			      return strResult;
//	      return getJsonStringFromGZIP(httpResponse);
//	      textView1.setText(strResult);
	     }else{
//	      textView1.setText("Error Response"+httpResponse.getStatusLine().toString());
	     }
	    }catch(ClientProtocolException e){
//	     textView1.setText(e.getMessage().toString());
	     e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
//	     textView1.setText(e.getMessage().toString());
	     e.printStackTrace();
	    } catch (IOException e) {
//	     textView1.setText(e.getMessage().toString());
	     e.printStackTrace();
	    }
		return null;
		
	}
	
	
	//解除绑定不需要传mark参数，传了第二次解除会解除失败，提示no this mark。由于dopost会传mark参考，而且不知道有多少个地方用这个方法，所以
	//再写一个方法用来解除绑定
	public static String dodelAccredit(Context ctx, String uri,ArrayList<String>param ,ArrayList<String>values){
		
		LogUtil.d("delAccredit uri = " + uri);
		
		 //URLַ
//	     String uriAPI = "http://www.dubblogs.cc:8751/Android/Test/API/Post/index.php";
	 //   建立HTTP Post连线
	    HttpPost httpRequest =new HttpPost(uri);
	    //Post运作传送变数必须用NameValuePair[]阵列储存
	    //传参数 服务端获取的方法为request.getParameter("name")
//	    param.add("mark");
//	    values.add(App.MDEVICEIDSTR);
//	    if(App.user!=null){
//	    	param.add("markid");
//	    	values.add(App.user.getUserid());
//	    }
	    List <NameValuePair> params=new ArrayList<NameValuePair>();
//	    params.add(new BasicNameValuePair("name","this is post"));
	    for(int i = 0 ; i < param.size() ; i ++){
	    	params.add(new BasicNameValuePair(param.get(i),values.get(i)));
	    	LogUtil.d("dopost param.get(i) = " + param.get(i) + "&values.get(i) = "+ values.get(i));
	    }
	    try{
	     
	     
	     //发出HTTP request
	     httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
	     //取得HTTP response
	     
	     DefaultHttpClient client = new DefaultHttpClient();
	     if(isMobileActive(ctx)){
			HttpHost proxy = new HttpHost("10.0.0.172", 80);
			client.getParams().setIntParameter(proxy.getHostName(), proxy.getPort());
		}else{
			client.getParams().setIntParameter("http.socket.timeout", TIME_OUT);
		}
	     HttpResponse httpResponse=client.execute(httpRequest);
	     //若状态码为200 ok 
	     if(httpResponse.getStatusLine().getStatusCode()==200){
	      //取出回应字串
			     String strResult = getJsonStringFromGZIP(httpResponse);
  			      LogUtil.d("登陆"+strResult);
  			      return strResult;
//	      return getJsonStringFromGZIP(httpResponse);
//	      textView1.setText(strResult);
	     }else{
//	      textView1.setText("Error Response"+httpResponse.getStatusLine().toString());
	     }
	    }catch(ClientProtocolException e){
//	     textView1.setText(e.getMessage().toString());
	     e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
//	     textView1.setText(e.getMessage().toString());
	     e.printStackTrace();
	    } catch (IOException e) {
//	     textView1.setText(e.getMessage().toString());
	     e.printStackTrace();
	    }
		return null;
		
	}
	
	
	   public static String getJsonStringFromGZIP(HttpResponse response) {
	        String jsonString = null;
	        try {
	            InputStream is = response.getEntity().getContent();
	            BufferedInputStream bis = new BufferedInputStream(is);
	            bis.mark(2);
	            // 取前两个字节
	            byte[] header = new byte[2];
	            int result = bis.read(header);
	            // reset输入流到开始位置
	            bis.reset();
	            // 判断是否是GZIP格式
	            int headerData = getShort(header);
	            // Gzip 流 的前两个字节是 0x1f8b
	            if (result != -1 && headerData == 0x1f8b) {
//	                LogUtil.d("HttpTask", " use GZIPInputStream  ");
	                is = new GZIPInputStream(bis);
	            } else {
//	                LogUtil.d("HttpTask", " not use GZIPInputStream");
	                is = bis;
	            }
	            InputStreamReader reader = new InputStreamReader(is, "utf-8");
	            char[] data = new char[100];
	            int readSize;
	            StringBuffer sb = new StringBuffer();
	            while ((readSize = reader.read(data)) > 0) {
	                sb.append(data, 0, readSize);
	            }
	            jsonString = sb.toString();
	            bis.close();
	            reader.close();
	        } catch (Exception e) {
//	            LogUtil.e("HttpTask", e.toString(),e);
	        }
	 
//	        LogUtil.d("HttpTask", "getJsonStringFromGZIP net output : " + jsonString );
	        return jsonString;
	    }
	 
	    public static int getShort(byte[] data) {
	        return (int)((data[0]<<8) | data[1]&0xFF);
	    }
	    /**
	     * 实时监控网络状况
	     * @param context
	     * @return
	     */
		public static boolean isNetworkAvailable(Context context) {
			ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity == null) {
				Log.i("NetWorkState", "Unavailabel");
				return false;
			} else {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null) {
					for (int i = 0; i < info.length; i++) {
						if (info[i].getState() == NetworkInfo.State.CONNECTED) {
							Log.i("NetWorkState", "Availabel");
							return true;
						}
					}
				}
			}
			return false;
		}
	
}
