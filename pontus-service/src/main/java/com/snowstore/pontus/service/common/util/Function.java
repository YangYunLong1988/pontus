package com.snowstore.pontus.service.common.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * 
 * @author: XiaoLei CHENG <br>
 * @date:Aug 28, 2014 <br>
 * @time:17:28:13 PM <br>
 * @version: 1.0
 * @describe 公共函数
 */
public class Function {
	private Function() {
	}

	/*
	 * 循环存值进BiMap
	 */
	public static BiMap<String, String> map(String[] code, String[] codeName) {
		BiMap<String, String> codeMap = HashBiMap.create();
		for (int i = 0; i < code.length; i++) {
			codeMap.put(code[i], codeName[i]);
		}
		return codeMap;
	}
}
