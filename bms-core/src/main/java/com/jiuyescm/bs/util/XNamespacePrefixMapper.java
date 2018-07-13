package com.jiuyescm.bs.util;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class XNamespacePrefixMapper extends NamespacePrefixMapper {

	private String pre = "";

	public XNamespacePrefixMapper() {
	}

	public XNamespacePrefixMapper(String pre) {
		this.pre = pre;
	}

	@Override
	public String getPreferredPrefix(String arg0, String arg1, boolean arg2) {

		return pre;
	}
}
