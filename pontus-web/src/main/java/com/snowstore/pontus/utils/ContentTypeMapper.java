package com.snowstore.pontus.utils;

import java.util.HashMap;
import java.util.Map;

public class ContentTypeMapper {
	private static Map<String, String> mapper = new HashMap<String, String>();
	static {
		mapper.put("bmp", "application/x-bmp");
		mapper.put("png", "image/png");
		mapper.put("jpg", "image/jpeg");
	}

	public static String getContentType(String fileName) {
		int index = fileName.indexOf(".");
		return index < 0 ? "application/octet-stream" : mapper.get(fileName.substring(index + 1).toLowerCase());
	}

	public static String getType(String fileName) {
		int index = fileName.indexOf(".");
		String fileType = (index < 0 ? "default" : fileName.substring(index + 1));
		switch (fileType.toLowerCase()) {
		case "bmp":
			return mapper.get("bmp");
		case "png":
			return mapper.get("png");
		case "jpg":
			return mapper.get("jpg");
		default:
			return "image/jpeg";
		}
	}
}
