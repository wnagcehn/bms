package com.jiuyescm.bs.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpPostUtil {

	private static final int TIMEOUT = 60000;
	
	private static final String UTF_8 = "UTF-8";
	private HttpPostUtil(){
		
	}
	
	public static String doPost(String url, Map<String, String> params) {
		HttpURLConnection conn = null;
		OutputStream out = null;
		String rsp = null;
		
		try {
			URL endpoint = new URL(url);
			
			getConnection(endpoint, "POST", "text/html; charset=UTF-8", null);
			conn = (HttpURLConnection) endpoint.openConnection();
			
			conn.setConnectTimeout(TIMEOUT);
			conn.setReadTimeout(TIMEOUT);
			conn.setDoOutput(true);
			
			out = conn.getOutputStream();
			
			byte[] content = spliceParams(params).getBytes(UTF_8);
			out.write(content);
			
			rsp = getResponseAsString(conn);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		
		return rsp;
	}
	
	private static String getStreamAsString(InputStream stream, String charset) throws IOException {
		try {
			Reader reader = new InputStreamReader(stream, charset);
			StringBuilder response = new StringBuilder();

			final char[] buff = new char[1024];
			int read = 0;
			while ((read = reader.read(buff)) > 0) {
				response.append(buff, 0, read);
			}

			return response.toString();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}
	
	protected static String getResponseAsString(HttpURLConnection conn) throws IOException {
		String charset = getResponseCharset(conn.getContentType());
		InputStream es = conn.getErrorStream();
		if (es == null) {
			return getStreamAsString(conn.getInputStream(), charset);
		} else {
			String msg = getStreamAsString(es, charset);
			if (msg != null && !"".equals(msg.trim())) {
				throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
			} else {
				throw new IOException(msg);
			}
		}
	}
	
	private static String getResponseCharset(String ctype) {
		String charset = UTF_8;

		if (ctype != null && !"".equals(ctype.trim())) {
			String[] params = ctype.split(";");
			for (String param : params) {
				param = param.trim();
				if (param.startsWith("charset")) {
					String[] pair = param.split("=", 2);
					if (pair.length == 2) {
						if (pair[1] != null && !"".equals(pair[1].trim())) {
							charset = pair[1].trim();
						}
					}
					break;
				}
			}
		}

		return charset;
	}
	
	private static HttpURLConnection getConnection(URL url, String method, String ctype, Map<String, String> headerMap) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod(method);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Accept", "text/xml,text/javascript");
		conn.setRequestProperty("Content-Type", ctype);
		if (headerMap != null) {
			for (Map.Entry<String, String> entry : headerMap.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		return conn;
	}
	
	private static String spliceParams(Map<String, String> params) throws UnsupportedEncodingException {
		
		StringBuffer paramsString = new StringBuffer("");
		int i = 0;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (i == 0) {
			    paramsString.append(key + "=");
			    paramsString.append(value != null ? URLEncoder.encode(params.get(key), UTF_8) : "");
			} else {
				 paramsString.append("&" + key + "=");
				 paramsString.append(value != null ? URLEncoder.encode(params.get(key), UTF_8) : "");
			}
			i++;
		}
		
		return paramsString.toString();
	}
}
