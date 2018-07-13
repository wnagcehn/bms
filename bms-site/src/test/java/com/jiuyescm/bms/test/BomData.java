package com.jiuyescm.bms.test;

public class BomData {
	private String url;
	private String body;

	public BomData(String url, String body) {
		super();
		this.url = url;
		this.body = body;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
