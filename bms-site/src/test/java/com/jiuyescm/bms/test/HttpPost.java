package com.jiuyescm.bms.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpPost implements Runnable {
	private BomData bomData;
	private static String cookie;
	private static SimpleDateFormat format = new SimpleDateFormat(
			"HHmmss.SSS\t消耗");
	private boolean isJson;

	public HttpPost(BomData bomData) {
		super();
		this.bomData = bomData;
	}

	public boolean isJson() {
		return isJson;
	}

	public void setJson(boolean isJson) {
		this.isJson = isJson;
	}

	@Override
	public void run() {
		long start = new Date().getTime();
		try {
			HttpURLConnection con = getConnection(bomData.getUrl());
			if (isJson) {
				con.setRequestProperty("Content-Type", "application/json");
			}
			try (OutputStreamWriter out = new OutputStreamWriter(
					con.getOutputStream());) {
				out.write(new String(bomData.getBody().getBytes("UTF-8")));
				out.flush();
			}
			con = redirects(con);
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream()))) {
				for (String line = br.readLine(); line != null; line = br
						.readLine()) {
					System.out.println(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Date temp = new Date();
		System.out.println(format.format(temp) + (temp.getTime() - start));
	}

	public static HttpURLConnection redirects(HttpURLConnection urlConnection)
			throws Exception {
		while (urlConnection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
			setCookie(urlConnection);
			String location = urlConnection.getHeaderField("Location");
			if (!location.startsWith("http://")) {
				URL url = urlConnection.getURL();
				location = "http://" + url.getHost() + ":" + url.getPort()
						+ "/" + location;
			}
			urlConnection.disconnect();
			urlConnection = getConnection(location);
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
		}
		setCookie(urlConnection);
		return urlConnection;
	}

	public static HttpURLConnection getConnection(String address) {
		HttpURLConnection httpURLConnection = null;
		try {
			URL url = new URL(address);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			if (cookie != null) {
				httpURLConnection.setRequestProperty("Cookie", cookie);
			}
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setInstanceFollowRedirects(false);
			httpURLConnection.setRequestProperty("Pragma", "no-cache");
			httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
			httpURLConnection.setRequestMethod("POST");
		} catch (Exception e) {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
			e.printStackTrace();
		}
		return httpURLConnection;
	}

	private static void setCookie(HttpURLConnection urlConnection) {
		List<String> cookies;
		if ((cookies = urlConnection.getHeaderFields().get("Set-Cookie")) != null) {
			Pattern pattern = Pattern.compile("^[\\w-]+=[\\w-]+[;]?");
			StringBuilder buffer = new StringBuilder();
			for (String string : cookies) {
				Matcher matcher = pattern.matcher(string.trim());
				if (matcher != null && matcher.groupCount() > 0) {
					string = matcher.group(0);
				}
				buffer.append(string);
				if (!string.endsWith(";")) {
					buffer.append(";");
				}
			}
			if (buffer.length() > 0) {
				cookie = buffer.toString();
			}
		}
	}
}