package com.jiuyescm.bms.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @author zhengyishan
 *
 */
public class APIHttpClient {

	private static final Logger logger = Logger.getLogger(APIHttpClient.class.getName());
	
	// 接口地址
	private String apiURL = "";
	private HttpClient httpClient = null;
	private HttpPost method = null;
	private long startTime = 0L;
	private long endTime = 0L;
	private int status = 0;
	private HttpGet httpGet = null;

	/**
	 * 接口地址
	 * 
	 * @param url
	 */
	@SuppressWarnings("deprecation")
	public APIHttpClient(String url) {

		if (url != null) {
			this.apiURL = url;
		}
		if (apiURL != null) {
			httpClient = new DefaultHttpClient();
			method = new HttpPost(apiURL);
			httpGet=new HttpGet(apiURL);
			
			
		}
	}

	/**
	 * 调用 API
	 * 
	 * @param parameters
	 * @return
	 * @throws Exception 
	 */
	public String post(String parameters) throws Exception  {
		String body = null;
	   
		logger.info("parameters:" + parameters); 
		if (method != null & parameters != null && !"".equals(parameters.trim())) { 
			try { 
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				// 建立一个NameValuePair数组，用于存储欲传送的参数
				params.add(new BasicNameValuePair("Data", parameters));
				// 添加参数
				method.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

				startTime = System.currentTimeMillis();
				httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
				HttpResponse response = httpClient.execute(method); 
				
				endTime = System.currentTimeMillis();
				
				int statusCode = response.getStatusLine().getStatusCode();
				
				logger.info("statusCode:" + statusCode);
				logger.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
				if (statusCode != HttpStatus.SC_OK) {
					//请求失败
					logger.error("Method failed:" + response.getStatusLine()); 
				} 
				// Read the response body
				body = EntityUtils.toString(response.getEntity()); 
			} catch (IOException e) {
				// 网络异常 
				// 发生网络异常
				logger.error("exception occurred!\n" + ExceptionUtils.getFullStackTrace(e));  
				
				throw new Exception(e.getMessage());
			}catch (Exception e) { 
				//其他异常
				logger.error("exception occurred!\n" + ExceptionUtils.getFullStackTrace(e));
				
				throw new Exception(e.getMessage());
			} finally {
				method.releaseConnection();
			}

		}
		return body;
	}

	public String postPara(String parameters) throws Exception { 
		String body = null;  
        logger.info("parameters:" + parameters);  
  
        if (method != null & parameters != null  
                && !"".equals(parameters.trim())) {  
            try {  
                // 建立一个NameValuePair数组，用于存储欲传送的参数  
                method.addHeader("Content-type","application/json; charset=utf-8");  
                method.setHeader("Accept", "application/json");  
                method.setEntity(new StringEntity(parameters, Charset.forName("UTF-8")));  
                startTime = System.currentTimeMillis();  
                //httpClient.getParams().setIntParameter(HttpClient, arg1);
                HttpResponse response = httpClient.execute(method);  
                  
                endTime = System.currentTimeMillis();  
                int statusCode = response.getStatusLine().getStatusCode();  
                  
                logger.info("statusCode:" + statusCode);  
                logger.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));  
                if (statusCode != HttpStatus.SC_OK) {  
                    logger.error("Method failed:" + response.getStatusLine());  
                    status = 1;  
                }  
  
                // Read the response body  
                body = EntityUtils.toString(response.getEntity());  
  
            } catch (IOException e) {  
            	logger.error("请求异常：{0}",e);
                // 网络错误  
                status = 3;  
            } finally {  
                logger.info("调用接口状态：" + status);  
            }  
  
        }  
        return body;  
	}
	
    public  String beanToJson(Object bean) {
    	 
        JSONObject json = JSONObject.fromObject(bean);
         
        return json.toString();
 
    }
	
	public  String sendGet(String url, Class c) {
        String result = "";
        BufferedReader in = null;
        try {  
    		Field[] fieldList = c.getDeclaredFields();
	 
			StringBuffer sb = null;
			StringBuffer params = new StringBuffer();
			for (int i = 0, size = fieldList.length; i < size; i++) {
			    sb = new StringBuffer();
				String field = fieldList[i].getName();
				// 将属性的首字符大写
				sb.append("get").append(field.substring(0, 1).toUpperCase())
						.append(field.substring(1));
				// 获取get方法对象
				Method m = c.getMethod(sb.toString());
                if (i != 0){
                	params.append("&");
				}
				params.append(field).append("=").append(((String) m.invoke(c.newInstance())));  
			} 
        	
            String urlNameString = url + "?" + params.toString();
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"utf-8"));//防止乱码
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("发送GET请求出现异常！", e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
            	logger.error("关闭输入流异常", e2);
                e2.printStackTrace();
            }
        }
        return result;
    }
	
	/**
	 * 调用 API
	 * 
	 * @param parameters
	 * @return
	 * @throws Exception 
	 */
	public String post() throws Exception {
		String body = null;
		if (method != null) {
			try {
				startTime = System.currentTimeMillis();
				HttpResponse response = httpClient.execute(httpGet);
				endTime = System.currentTimeMillis();
				int statusCode = response.getStatusLine().getStatusCode();
				logger.info("statusCode:" + statusCode);
				logger.info("调用API 花费时间(单位：毫秒)：" + (endTime - startTime));
				if (statusCode != HttpStatus.SC_OK) {
					logger.error("Method failed:" + response.getStatusLine());
					status = 1;
				}
				// Read the response body
				body = EntityUtils.toString(response.getEntity(),"UTF-8");
			} catch (IOException e) {
				// 发生网络异常
				logger.error("exception occurred!\n" + ExceptionUtils.getFullStackTrace(e)); 
				// 网络错误
				status = 3; 
				throw new Exception(e.getMessage());
			} finally {
				logger.info("调用接口状态：" + status);
			}

		}
		return body;
	}


	/**
	 * 0.成功 1.执行方法失败 2.协议错误 3.网络错误
	 * 
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}
}