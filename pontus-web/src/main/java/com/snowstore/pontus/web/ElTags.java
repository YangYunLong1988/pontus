package com.snowstore.pontus.web;

import org.apache.commons.lang3.StringUtils;

public class ElTags {
	public static String sub(String str, Integer len) {
		if (StringUtils.isBlank(str) || str.length() <= len) {
			return str;
		}
		return str.substring(0, len) + "...";
	}
}
